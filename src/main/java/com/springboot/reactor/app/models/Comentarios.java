package com.springboot.reactor.app.models;

import java.util.ArrayList;
import java.util.List;

public class Comentarios {
    private List<String> comentarios;

    public List<String> getComentarios() {
        return comentarios;
    }

    public void addComentario(String comentario){
        this.comentarios.add(comentario);
    }

    public void setComentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }

    public Comentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }

    public Comentarios() {
        this.comentarios = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "Comentarios: ".concat(comentarios.toString());
    }
}
