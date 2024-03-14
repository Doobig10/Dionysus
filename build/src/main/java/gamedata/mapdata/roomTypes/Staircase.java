package gamedata.mapdata.roomTypes;

import gamedata.mapdata.AbstractRoom;

public final class Staircase extends AbstractRoom {

    @Override
    public void populate() {
        return; // Special Unpopulated Room Type
    }

    @Override
    public RoomType getRoomType() {
        return RoomType.STAIRCASE;
    }
}
