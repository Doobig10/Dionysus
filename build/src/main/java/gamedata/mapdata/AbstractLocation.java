package gamedata.mapdata;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractLocation
        implements Linkable, Location, Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<Linkable> links = new ArrayList<>();

    public boolean isRoom() {return false;}

    @Override
    public void createLink(Linkable target) {
        links.add(target);
    }

    @Override
    public Linkable[] getLinked() {
        return this.links.toArray(new Linkable[0]);
    }

}
