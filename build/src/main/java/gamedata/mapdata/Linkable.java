package gamedata.mapdata;

public interface Linkable {

    default Linkable getLinkable() {return this;}

    default void link(Linkable target) {
        System.out.println("linking: "+this.getLinkable()+ " and " + target.getLinkable());
        this.getLinkable().createLink(target);
        target.getLinkable().createLink(this.getLinkable());
    }

    void createLink(Linkable target);

    Linkable[] getLinked();

}
