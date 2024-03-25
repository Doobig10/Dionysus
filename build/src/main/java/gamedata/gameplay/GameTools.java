package gamedata.gameplay;

import java.util.concurrent.ThreadLocalRandom;

public final class GameTools {

    public static int roll(Integer count, Integer dice) {
        int value = 0;
        for (int step = 0; step < count; step++) {
            value = value + ThreadLocalRandom.current().nextInt(1, dice);
        }
        return value;
    }

    public static double getChance(Integer roll) {
        return (double) (6 - Math.abs(7 - roll)) / 36;
    }

    public static double getChanceOrHigher(Integer roll) {
        double chance = 0;
        for (int high = roll; high <= 12; high++) {
            chance  = chance + getChance(high);
        }
        return chance;
    }
}
