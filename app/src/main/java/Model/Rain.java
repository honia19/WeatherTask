package Model;

public class Rain
{
    private String h3;

    public String geth3 ()
    {
        return h3;
    }

    public void set3h (String h3)
    {
        this.h3 = h3;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [3h = "+h3+"]";
    }
}
