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
public class Comentario {
    
    private String idComentario;
    private String texto;

    public Comentario() {
    }

    public Comentario(String idComentario, String texto) {
        this.idComentario = idComentario;
        this.texto = texto;
    }

    
    
    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

   
    
    
}
