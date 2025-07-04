package com.progettoingegneriasw.model.Utils;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Farmaco other = (Farmaco) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
