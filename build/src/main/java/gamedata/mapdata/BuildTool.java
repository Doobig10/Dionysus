package gamedata.mapdata;

import gamedata.gameplay.Board;
import gamedata.mapdata.roomTypes.*;

import java.util.ArrayList;
import java.util.HashMap;

public final class BuildTool {

    private final AbstractRoom origin;
    private final HashMap<String, ArrayList<AbstractRoom>> rooms = new HashMap<>();
    private final ArrayList<Tile> junctions = new ArrayList<>();

    public BuildTool(AbstractRoom origin) {
        this.origin = origin;
    }

    public AbstractRoom getOrigin() {
        return origin;
    }

    public AbstractRoom getRoomByType(RoomType type) {
        return switch(type) {
            case STAIRCASE -> throw new RuntimeException();
            case CHAMBER -> new Chamber();
            case RED -> new RedRoom();
            case BLUE -> new BlueRoom();
            case PURPLE -> new PurpleRoom();
            case YELLOW -> new YellowRoom();
            case GREEN -> new GreenRoom();
            case ORANGE -> new OrangeRoom();
        };
    }

    public void generateRoomsWithIdentifier(String identifier, RoomType type, Integer count) {
        ArrayList<AbstractRoom> roomList = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            roomList.add(getRoomByType(type));
        }
        rooms.put(identifier, roomList);
    }

    public HashMap<String, ArrayList<AbstractRoom>> getRooms() {
        return rooms;
    }

    public ArrayList<Tile> getJunctions() {
        return junctions;
    }

    public Board build() {
        Board board = new Board(this.origin);
        for (String key: rooms.keySet()) {
            board.addRooms(this.rooms.get(key));
        }
        return board;
    }

    public void generateJunctions(int count) {
        for (int index = 0; index < count; index++) {
            this.junctions.add(new Tile());
        }
    }
}
