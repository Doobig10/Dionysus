package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class OrangeRoom extends SimpleAbstractRoom {

    @Override
    public RoomType getRoomType() {
        return RoomType.ORANGE;
    }

    @Override
    public int getAverageLoot() {
        return 4500;
    }

    @Override
    public double getLootVariance() {
        return 0.5;
    }

    @Override
    public int getChallengeCount() {
        return 1;
    }

    @Override
    public int getAverageStrength() {
        return 8;
    }

    @Override
    public int getStrengthRange() {
        return 3;
    }
}
