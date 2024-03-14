package gamedata.gameplay;

import gamedata.mapdata.AbstractLocation;
import gamedata.mapdata.AbstractRoom;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final AbstractLocation initialLocation;

    private final ArrayList<AbstractRoom> rooms = new ArrayList<>();

    public Board(AbstractLocation initialLocation) {
        this.initialLocation = initialLocation;
    }

    public void addRooms(ArrayList<AbstractRoom> rooms){
        this.rooms.addAll(rooms);
    }

    public AbstractRoom addRoomWithCallback(AbstractRoom room) {this.rooms.add(room); return room;}

    public AbstractLocation getInitialLocation() {
        return this.initialLocation;
    }

    public AbstractRoom[] getEncounters() {
        return rooms.toArray(new AbstractRoom[0]);
    }

    public void serialise(String filename) {
        try
                (
                        FileOutputStream file = new FileOutputStream(filename);
                )
        {
            ObjectOutputStream objectStream = new ObjectOutputStream(file);
            objectStream.writeObject(this);
            objectStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
