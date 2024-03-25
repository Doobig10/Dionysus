package gamedata.mapdata;

public final class TileChain {

    private final Tile root = new Tile();
    private Tile active = this.root;

    public TileChain(int length) {
        for (int iter = 1; iter < length ; iter ++) {
            Tile newTile = new Tile();
            this.active.link(newTile);
            this.active = newTile;
        }
    }

    public TileChain linkActive(Linkable linkable) {
        this.active.link(linkable);
        return this;
    }

    public Tile getRoot() {
        return this.root;
    }

    public static Tile getDefault(int length, Linkable terminator) {
        return new TileChain(length).linkActive(terminator).getRoot();
    }
}
