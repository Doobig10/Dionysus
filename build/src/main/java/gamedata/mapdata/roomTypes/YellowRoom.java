package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class YellowRoom extends SimpleAbstractRoom {
    @Override
    public RoomType getRoomType() {
        return RoomType.YELLOW;
    }

    @Override
    public int getAverageLoot() {
        return 1750;
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
        return 6;
    }

    @Override
    public int getStrengthRange() {
        return 2;
    }
}
