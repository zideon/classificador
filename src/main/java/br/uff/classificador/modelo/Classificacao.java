/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.classificador.modelo;

/**
 *
 * @author zideon
 */
public class Classificacao {
    private String idNoticia;
    private String idComentario;
    private String classe;

    public Classificacao() {
    }

    public Classificacao(String idNoticia, String idComentario, String classe) {
        this.idNoticia = idNoticia;
        this.idComentario = idComentario;
        this.classe = classe;
    }
    public Classificacao(String[] line) {
        this.idNoticia = line[0];
        this.idComentario = line[1];
        this.classe = line[2];
    }
    public String[] getRecord(){
        return new String[]{idNoticia,idComentario,classe};
    }
    
    
    public String getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(String idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    
}
