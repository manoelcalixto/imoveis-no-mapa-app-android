package br.com.imoveisnomapa.imoveisnomapa.model;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by calixto on 05/03/16.
 */
public class Preco {

    public Preco (String nome) {
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

        if (nome.equals("")){
            return nome;
        } else {
            Locale ptBr = new Locale("pt", "BR");
            NumberFormat format = NumberFormat.getCurrencyInstance(ptBr);
            Double preco = Double.parseDouble(nome);

            return format.format(preco);
        }
    }
}
