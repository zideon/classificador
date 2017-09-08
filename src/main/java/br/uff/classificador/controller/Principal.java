/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.classificador.controller;

import br.uff.classificador.Constantes;
import br.uff.classificador.modelo.Classificacao;
import br.uff.classificador.modelo.Comentario;
import br.uff.classificador.modelo.Noticia;
import br.uff.classificador.modelo.Pacote;
import br.uff.classificador.modelo.Usuario;
import br.uff.classificador.service.EmailService;
import br.uff.classificador.service.EscritorClassificacaoCSV;
import br.uff.classificador.service.LeitorClassificacaoCSV;
import br.uff.classificador.service.LeitorNoticiaJson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zideon
 */
@Controller
public class Principal {

    @Autowired
    private LeitorNoticiaJson leitorNoticiaJson;
    @Autowired
    private LeitorClassificacaoCSV leitorClassificacaoCSV;
    @Autowired
    private EscritorClassificacaoCSV escritorClassificacaoCSV;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String inseirEmailGET(Model model, @CookieValue(value = "email", required = false) String email,
            @CookieValue(value = "start", required = false) String inicio,
            @CookieValue(value = "end", required = false) String fim,HttpServletResponse response) throws IOException {
        if (email == null) {
            model.addAttribute("view", "fragments/inserirEmail");
            model.addAttribute("usuario", new Usuario());
        } else {
            if (inicio == null || fim == null) {
                return selecionarPacoteGET(model,new Usuario(email),response);
            } else {
                return selecionarNoticiaGET(model,inicio,fim,email,response);
            }
        }
        return "template";
    }

    @RequestMapping(value = "/selecionadorPacote", method = RequestMethod.POST)
    String selecionarPacoteGET(Model model, @ModelAttribute Usuario usuario, HttpServletResponse response) {
        Cookie limiteCookie = new Cookie("email", usuario.getEmail()); //bake cookie
        limiteCookie.setPath("/");
        limiteCookie.setMaxAge(10000); //set expire time to 1000 sec
        response.addCookie(limiteCookie);
        model.addAttribute("view", "fragments/selecionarPacote");
        List<Pacote> pacotes = new ArrayList<>();
        int passo = Constantes.SIZE / Constantes.SPLIT;
        Pacote p = null;
        char[] alphabet = "ABCDEFGHIJLMNOPQRSTUVXZ".toCharArray();
        for (int i = 0; i < Constantes.SPLIT; i++) {
            if ((i + 1) == Constantes.SPLIT) {
                p = new Pacote("Noticias pacote " + alphabet[i], (i * passo) + "", Constantes.SIZE + "");
            } else {
                p = new Pacote("Noticias pacote " + alphabet[i], i + "", (i + 1) * passo + "");
            }
            pacotes.add(p);
        }
        model.addAttribute("pacotes", pacotes);
        return "template";
    }

    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    void exit(Model model) {
        System.exit(0);
    }

    @RequestMapping(value = "/selecionarNoticia", method = RequestMethod.GET)
    String selecionarNoticiaGET(Model model, @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim, @CookieValue(value = "email") String email, HttpServletResponse response) throws IOException {

        Cookie limiteCookie = new Cookie("start", inicio); //bake cookie
        limiteCookie.setPath("/");
        limiteCookie.setMaxAge(10000); //set expire time to 1000 sec
        response.addCookie(limiteCookie);

        limiteCookie = new Cookie("end", fim); //bake cookie
        limiteCookie.setPath("/");
        limiteCookie.setMaxAge(10000); //set expire time to 1000 sec
        response.addCookie(limiteCookie);

        model.addAttribute("view", "fragments/selecionarNoticia");
        model.addAttribute("help", "Qualquer ato de comunicação que inferiorize uma pessoa ou incite violência contra a mesma, tendo por base características como raça, gênero, etnia, nacionalidade, religião, orientação sexual ou outro aspecto passível de discriminação.");
        List<Noticia> noticias = leitorNoticiaJson.lerNoticias(Integer.parseInt(inicio), Integer.parseInt(fim));
        List<Noticia> noticiasValidas = new ArrayList<>();
        for (Noticia noticia : noticias) {
            if (!checaComentarios(email, noticia)) {
                noticiasValidas.add(noticia);
            }
        }
        model.addAttribute("noticias", noticiasValidas);
        return "template";
    }

    @RequestMapping(value = "/mostrarComentarios", method = RequestMethod.GET)
    String selecionarComentarioGET(Model model, @RequestParam("id") String id, @CookieValue(value = "email") String email) throws IOException {
        model.addAttribute("view", "fragments/selecionarComentario");
        model.addAttribute("helpOdio", "Qualquer ato de comunicação que inferiorize uma pessoa ou incite violência contra a mesma, tendo por base características como raça, gênero, etnia, nacionalidade, religião, orientação sexual ou outro aspecto passível de discriminação.");
        model.addAttribute("helpOfensa", "Comentários que podem ser considerados impróprios para a discussão em determinados contextos. Geralmente com a intenção de ofender, provocar gratuitamente ou desqualificar o debate.");
        Noticia n = getNoticia(id);
        List<Comentario> saida = getComentarios(email, n);
        if (saida.isEmpty()) {
            try {
                emailService.sendMessageWithAttachment("fabioberlim@id.uff.br", "Classificacao#" + n.getId(), "Arquivo contendo as classificações da noticia " + n.getTitulo() + " do email " + email, email + "/" + id + ".csv");
            } catch (MessagingException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.addAttribute("comentarios", saida);
        model.addAttribute("noticia", n);
        return "template";
    }

    @RequestMapping(value = "/classificarComentario", method = RequestMethod.GET)
    String classificarComentarioGET(Model model, @RequestParam("id") String id,
            @RequestParam("com") String comentarioId, @RequestParam("cla") String classe,
            @CookieValue(value = "email") String email) throws IOException {
        new File(email).mkdir();
        Classificacao c = new Classificacao(id, comentarioId, classe);
        escritorClassificacaoCSV.setClassificacao(email, c);
        return selecionarComentarioGET(model, id, email);
    }

    private List<Comentario> getComentarios(String path, Noticia n) {
        List<Classificacao> classificacoes = leitorClassificacaoCSV.getClassificacoesPorNoticia(path, n.getId());
        List<Comentario> contidos = new ArrayList<>();
        List<Comentario> comentarios = n.getComentarios();
        for (Comentario comentario : comentarios) {
            if (checaComentario(comentario.getIdComentario(), classificacoes)) {
                contidos.add(comentario);
            }
        }
        for (Comentario contido : contidos) {
            comentarios.remove(contido);
        }
        return comentarios;
    }

    private boolean checaComentarios(String path, Noticia noticia) {
        List<Classificacao> classificacoes = leitorClassificacaoCSV.getClassificacoesPorNoticia(path, noticia.getId());
        if (noticia.getComentarios() != null && !noticia.getComentarios().isEmpty()) {
            if (classificacoes != null && !classificacoes.isEmpty()) {
                for (Comentario comentario : noticia.getComentarios()) {
                    String idComentario = comentario.getIdComentario();
                    if (!checaComentario(idComentario, classificacoes)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean checaComentario(String idComentario, List<Classificacao> classificacoes) {
        for (Classificacao classificacoe : classificacoes) {
            if (classificacoe.getIdComentario().equals(idComentario)) {
                return true;
            }
        }
        return false;
    }

    private Noticia getNoticia(String id) {
        List<Noticia> noticias = leitorNoticiaJson.lerNoticias(0, Constantes.SIZE);
        for (Noticia noticia : noticias) {
            if (noticia.getId().equals(id)) {
                return noticia;
            }
        }
        return null;
    }

}
