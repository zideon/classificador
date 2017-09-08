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
public class Usuario {
    private String email;

    public Usuario() {
    }

    public Usuario(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
