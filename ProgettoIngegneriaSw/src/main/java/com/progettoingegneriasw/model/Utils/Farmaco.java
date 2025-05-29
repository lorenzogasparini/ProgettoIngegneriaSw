package com.progettoingegneriasw.model.Utils;

public class Farmaco {
    private int id;
    private String codice_aic;
    private String nome;

    public Farmaco(int id, String codice_aic, String nome) {
        this.id = id;
        this.codice_aic = codice_aic;
        this.nome = nome;
    }

    public String toString(){
        return "id: " + id + "; codice_aic: " + codice_aic + "; nome: " + nome;
    }

    public String getCodice_aic() {
        return codice_aic;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
