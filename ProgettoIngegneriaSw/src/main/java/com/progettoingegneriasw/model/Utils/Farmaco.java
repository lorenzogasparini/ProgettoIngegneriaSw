package com.progettoingegneriasw.model.Utils;

public class Farmaco {
    int id;
    String codice_aic;
    String nome;

    public Farmaco(int id, String codice_aic, String nome) {
        this.id = id;
        this.codice_aic = codice_aic;
        this.nome = nome;
    }

    public String toString(){
        return "id: " + id + "; codice_aic: " + codice_aic + "; nome: " + nome;
    }
}
