package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public final class RedRoom extends SimpleAbstractRoom {

    @Override
    public RoomType getRoomType() {
        return RoomType.RED;
    }


    @Override
    public int getAverageLoot() {
        return 500;
    }

    @Override
    public double getLootVariance() {
        return 0.25;
    }

    @Override
    public int getChallengeCount() {
        return 1;
    }

    @Override
    public int getAverageStrength() {
        return 4;
    }

    @Override
    public int getStrengthRange() {
        return 1;
    }
}
