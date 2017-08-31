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
public class Pacote {
    private String titulo;
    private String inicio;
    private String fim;

    public Pacote() {
    }

    public Pacote(String titulo, String inicio, String fim) {
        this.titulo = titulo;
        this.inicio = inicio;
        this.fim = fim;
    }
    
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    

    
    
    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }
    
    
}
