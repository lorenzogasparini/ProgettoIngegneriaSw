package com.progettoingegneriasw.model.Utils;

public class Patologia {
    private int id;
    private String nome;
    private String codiceAic;

    public Patologia(int id, String nome, String codiceAic){
        this.id = id;
        this.nome = nome;
        this.codiceAic = codiceAic;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCodiceAic() { return codiceAic; }

}
