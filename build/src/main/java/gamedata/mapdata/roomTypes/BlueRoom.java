package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class BlueRoom extends SimpleAbstractRoom {
    @Override
    public RoomType getRoomType() {
        return RoomType.BLUE;
    }

    @Override
    public int getAverageLoot() {
        return 6000;
    }

    @Override
    public double getLootVariance() {
        return 0.33;
    }

    @Override
    public int getChallengeCount() {
        return 1;
    }

    @Override
    public int getAverageStrength() {
        return 9;
    }

    @Override
    public int getStrengthRange() {
        return 3;
    }
}
