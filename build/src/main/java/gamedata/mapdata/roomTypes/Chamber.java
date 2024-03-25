package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class Chamber extends SimpleAbstractRoom {
    @Override
    public RoomType getRoomType() {
        return RoomType.CHAMBER;
    }

    @Override
    public int getAverageLoot() {
        return 0;
    }

    @Override
    public double getLootVariance() {
        return 0;
    }

    @Override
    public int getChallengeCount() {
        return 0;
    }

    @Override
    public int getAverageStrength() {
        return 0;
    }

    @Override
    public int getStrengthRange() {
        return 0;
    }
}
