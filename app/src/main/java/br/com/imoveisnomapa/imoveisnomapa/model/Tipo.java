package br.com.imoveisnomapa.imoveisnomapa.model;

public class Tipo
{
    private _id _id;

    private String tipo;

    private String operacao;

    public _id get_id ()
    {
        return _id;
    }

    public void set_id (_id _id)
    {
        this._id = _id;
    }

    public String getTipo ()
    {
        return tipo;
    }

    public void setTipo (String tipo)
    {
        this.tipo = tipo;
    }

    public String getOperacao ()
    {
        return operacao;
    }

    public void setOperacao (String operacao)
    {
        this.operacao = operacao;
    }

    @Override
    public String toString()
    {
        return tipo;
    }
}