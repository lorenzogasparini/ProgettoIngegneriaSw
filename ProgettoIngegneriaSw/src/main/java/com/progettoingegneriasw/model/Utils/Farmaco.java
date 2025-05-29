package com.progettoingegneriasw.model.Utils;

public class Farmaco {
    private final Integer id;
    private final String codiceAic;
    private final String nome;

    public Farmaco(Integer id){
        this(id, null,null);
    }

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
    public String getNome(){ return codiceAic; }

    public String toString(){
        return "id: " + id + "; codice_aic: " + codiceAic + "; nome: " + nome;
    }
}
