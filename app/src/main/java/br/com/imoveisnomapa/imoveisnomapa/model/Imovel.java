package br.com.imoveisnomapa.imoveisnomapa.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by calixto on 15/02/16.
 */
public class Imovel implements ClusterItem, Serializable
{
    private String preco;

    private String enderecoSomenteComNumero;

    private Loc loc;

    private String[] image_urls;

    private String url;

    private String operacao;

    private String bairro;

    private String cidade;

    private String quarto;

    private String estado;

    private String area;

    private _id _id;

    private String tipo;

    private Images[] images;

    private String imobiliaria;

    private String endereco;

    public LatLng getmPosition() {
        LatLng mPosition = new LatLng(Double.parseDouble(getLoc().getCoordinates()[0]), Double.parseDouble(getLoc().getCoordinates()[1]));
        return mPosition;
    }

    private LatLng mPosition;

    public String getPreco ()
    {
        return preco;
    }

    public void setPreco (String preco)
    {
        this.preco = preco;
    }

    public String getEnderecoSomenteComNumero ()
    {
        return enderecoSomenteComNumero;
    }

    public void setEnderecoSomenteComNumero (String enderecoSomenteComNumero)
    {
        this.enderecoSomenteComNumero = enderecoSomenteComNumero;
    }

    public Loc getLoc ()
    {
        return loc;
    }

    public void setLoc (Loc loc)
    {
        this.loc = loc;
    }

    public String[] getImage_urls ()
    {
        return image_urls;
    }

    public void setImage_urls (String[] image_urls)
    {
        this.image_urls = image_urls;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getOperacao ()
    {
        return operacao;
    }

    public void setOperacao (String operacao)
    {
        this.operacao = operacao;
    }

    public String getBairro ()
    {
        return bairro;
    }

    public void setBairro (String bairro)
    {
        this.bairro = bairro;
    }

    public String getCidade ()
    {
        return cidade;
    }

    public void setCidade (String cidade)
    {
        this.cidade = cidade;
    }

    public String getQuarto ()
    {
        return quarto;
    }

    public void setQuarto (String quarto)
    {
        this.quarto = quarto;
    }

    public String getEstado ()
    {
        return estado;
    }

    public void setEstado (String estado)
    {
        this.estado = estado;
    }

    public String getArea ()
    {
        return area;
    }

    public void setArea (String area)
    {
        this.area = area;
    }

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

    public Images[] getImages ()
    {
        return images;
    }

    public void setImages (Images[] images)
    {
        this.images = images;
    }

    public String getImobiliaria ()
    {
        return imobiliaria;
    }

    public void setImobiliaria (String imobiliaria)
    {
        this.imobiliaria = imobiliaria;
    }

    public String getEndereco ()
    {
        return endereco;
    }

    public void setEndereco (String endereco)
    {
        this.endereco = endereco;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [preco = "+preco+", enderecoSomenteComNumero = "+enderecoSomenteComNumero+", loc = "+loc+", image_urls = "+image_urls+", url = "+url+", operacao = "+operacao+", bairro = "+bairro+", cidade = "+cidade+", quarto = "+quarto+", estado = "+estado+", area = "+area+", _id = "+_id+", tipo = "+tipo+", images = "+images+", imobiliaria = "+imobiliaria+", endereco = "+endereco+"]";
    }

    @Override
    public LatLng getPosition() {
        return getmPosition();
    }
}
