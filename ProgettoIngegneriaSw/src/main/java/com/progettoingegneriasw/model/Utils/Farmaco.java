package com.progettoingegneriasw.model.Utils;

public class Farmaco {
    int id;
    String codiceAic;
    String nome;

    public Farmaco(String codiceAic, String nome){
        this(null, codiceAic, nome);
    }

    public Farmaco(Integer id, String codiceAic, String nome) {
        this.id = id;
        this.codiceAic = codiceAic;
        this.nome = nome;
    }

    public Integer getId(){ return id; }
    public String getCodiceAic() { return codiceAic; }
    public String getNome(){ return nome; }

    public String toString(){
        return "id: " + id + "; codice_aic: " + codiceAic + "; nome: " + nome;
    }
}
