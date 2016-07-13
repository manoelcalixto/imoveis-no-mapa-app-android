package br.com.imoveisnomapa.imoveisnomapa.model;

/**
 * Created by calixto on 05/03/16.
 */
public class Quarto {

    public  Quarto (String nome) {
        this.nome = nome;
    }

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

}
