package com.springboot.reactor.app.models;

import java.util.ArrayList;

public class UsuarioComentarios {
    private Usuario usuario;
    private Comentarios comentarios;

    public UsuarioComentarios(Usuario usuario, Comentarios comentarios) {
        this.usuario = usuario;
        this.comentarios = comentarios;
    }

    public UsuarioComentarios() {
        this.usuario = new Usuario();
        this.comentarios = new Comentarios();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Comentarios getComentarios() {
        return comentarios;
    }

    public void setComentarios(Comentarios comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "UsuarioComentarios{" +
                "usuario=" + usuario +
                ", comentarios=" + comentarios +
                '}';
    }
}
