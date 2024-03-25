import gamedata.gameplay.Board;
import gamedata.mapdata.AbstractRoom;
import gamedata.mapdata.TileChain;
import gamedata.mapdata.roomTypes.*;
import org.junit.Test;

import java.util.ArrayList;

public class CreateBoardTest {

    @Test
    public void CreateBoard () {

        ArrayList<AbstractRoom> rooms = new ArrayList<>();

        AbstractRoom stairs = new Staircase();
        rooms.add(stairs);

        Board board = new Board(stairs);
        board.addRooms(rooms);

        for (int i = 0 ; i < 9 ; i++) {
            stairs.link(TileChain.getDefault(5+i, board.addRoomWithCallback(new RedRoom())));
            stairs.link(TileChain.getDefault(7+i, board.addRoomWithCallback(new PurpleRoom())));
            stairs.link(TileChain.getDefault(9+i, board.addRoomWithCallback(new YellowRoom())));
            stairs.link(TileChain.getDefault(7+i, board.addRoomWithCallback(new GreenRoom())));
            stairs.link(TileChain.getDefault(9+i, board.addRoomWithCallback(new OrangeRoom())));
            stairs.link(TileChain.getDefault(5+i, board.addRoomWithCallback(new BlueRoom())));
        }

        board.serialise("default_board.ser");

    }

}
