package gamedata.mapdata.roomTypes;

import gamedata.mapdata.SimpleAbstractRoom;

public class PurpleRoom extends SimpleAbstractRoom {
    @Override
    public RoomType getRoomType() {
        return RoomType.PURPLE;
    }

    @Override
    public int getAverageLoot() {
        return 1000;
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
        return 5;
    }

    @Override
    public int getStrengthRange() {
        return 1;
    }
}
