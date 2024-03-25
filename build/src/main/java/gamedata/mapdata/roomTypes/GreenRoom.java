package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class GreenRoom extends SimpleAbstractRoom {
    @Override
    public RoomType getRoomType() {
        return RoomType.GREEN;
    }

    @Override
    public int getAverageLoot() {
        return 3000;
    }

    @Override
    public double getLootVariance() {
        return 0.66;
    }

    @Override
    public int getChallengeCount() {
        return 1;
    }

    @Override
    public int getAverageStrength() {
        return 7;
    }

    @Override
    public int getStrengthRange() {
        return 2;
    }
}
