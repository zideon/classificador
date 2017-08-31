/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.classificador.service;

import br.uff.classificador.Constantes;
import br.uff.classificador.modelo.Comentario;
import br.uff.classificador.modelo.Noticia;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author zideon
 */
@Service
public class LeitorNoticiaJson {

    @Cacheable(value = "noticias", key = "#inicio.toString().concat('-').concat(#fim.toString())")
    public List<Noticia> lerNoticias(Integer inicio, Integer fim) {
        List<Noticia> noticias = new ArrayList<>();
        try (Scanner s = new Scanner(this.getClass().getResourceAsStream(Constantes.PATH_JSON))) {
            int indice = 0;
            while (s.hasNextLine()) {
                String json = s.nextLine();
                if (indice >= inicio && indice < fim) {
                    Noticia noticia = loadNoticiaFromJSON(json);
                    noticias.add(noticia);
                }
                indice++;
            }
        } catch (Exception ex) {
            Logger.getLogger(LeitorNoticiaJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return noticias;
    }

    public Noticia loadNoticiaFromJSON(String json) {
        Noticia noticia = new Noticia();
        JsonParser parser = new JsonParser();
        // The JsonElement is the root node. It can be an object, array, null or
        // java primitive.
        JsonElement element = parser.parse(json);
        // use the isxxx methods to find out the type of jsonelement. In our
        // example we know that the root object is the Albums object and
        // contains an array of dataset objects
        List<Comentario> comentarios = new ArrayList<>();
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            String titulo = jsonObject.get("titulo").getAsString();
            noticia.setTitulo(titulo);
            JsonObject id = jsonObject.get("_id").getAsJsonObject();
            String idString = id.get("$oid").getAsString();
            noticia.setId(idString);
            JsonArray datasets = jsonObject.getAsJsonArray("comentarios");
            for (int i = 0; i < datasets.size(); i++) {
                JsonObject obj = datasets.get(i).getAsJsonObject();
                String idComentario = obj.get("idComentario").getAsString();
                String texto = obj.get("texto").getAsString();
                comentarios.add(new Comentario(idComentario, texto));
            }
            noticia.setComentarios(comentarios);
        }

        return noticia;
    }

}