package com.progettoingegneriasw.model.Utils;

public class Patologia {
    private final Integer id;
    private final String nome;
    private final String codiceIcd;

    public Patologia(int id){
        this(id, null, null);
    }

    public Patologia(String nome, String codiceIcd){
        this(null, nome, codiceIcd);
    }

    public Patologia(Integer id, String nome, String codiceIcd){
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
