package br.com.imoveisnomapa.imoveisnomapa.model;

/**
 * Created by calixto on 14/02/16.
 */
public class _id {

    private String $oid;

    public String get$oid ()
    {
        return $oid;
    }

    public void set$oid (String $oid)
    {
        this.$oid = $oid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [$oid = "+$oid+"]";
    }
}
