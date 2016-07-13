package br.com.imoveisnomapa.imoveisnomapa.model;

/**
 * Created by calixto on 15/02/16.
 */
public class Images
{
    private String path;

    private String checksum;

    private String url;

    public String getPath ()
    {
        return path;
    }

    public void setPath (String path)
    {
        this.path = path;
    }

    public String getChecksum ()
    {
        return checksum;
    }

    public void setChecksum (String checksum)
    {
        this.checksum = checksum;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [path = "+path+", checksum = "+checksum+", url = "+url+"]";
    }
}
