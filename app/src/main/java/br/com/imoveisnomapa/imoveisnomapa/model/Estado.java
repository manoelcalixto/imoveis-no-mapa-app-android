package br.com.imoveisnomapa.imoveisnomapa.model;

public class Estado
{
    private String estado;

    private _id _id;

    private String longitude;

    private String latitude;

    public String getEstado ()
    {
        return estado;
    }

    public void setEstado (String estado)
    {
        this.estado = estado;
    }

    public _id get_id ()
    {
        return _id;
    }

    public void set_id (_id _id)
    {
        this._id = _id;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return estado;
    }
}