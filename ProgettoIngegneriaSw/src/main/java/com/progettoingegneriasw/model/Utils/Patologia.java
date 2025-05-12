package com.progettoingegneriasw.model.Utils;

public class Patologia {
    private int id;
    private String nome;
    private String codiceIcd;

    public Patologia(int id, String nome, String codiceIcd){
        this.id = id;
        this.nome = nome;
        this.codiceIcd = codiceIcd;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCodiceIcd() { return codiceIcd; }

    public String toString(){
        return "id: " + getId() + "; nome: " + getNome() + "; codiceIcd: " + getCodiceIcd();
    }

}
