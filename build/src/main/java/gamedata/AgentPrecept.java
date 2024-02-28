package gamedata;

import gamedata.mapdata.roomTypes.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class AgentPrecept {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentPrecept.class);

    private final int id;
    private final int generation;
    private final double accuracyFactor;
    private final double distanceFactor;

    private final HashMap<RoomType, Integer> lootPrediction = new HashMap<>();
    private final HashMap<RoomType, Double> difficultyFactor = new HashMap<>();

    public AgentPrecept(int id, int generation, double accuracyFactor, double distanceFactor) {
        this.id = id;
        this.generation = generation;
        this.accuracyFactor = accuracyFactor;
        this.distanceFactor = distanceFactor;
    }

    public int getID() {
        return this.id;
    }

    public int getGeneration() {
        return this.generation;
    }


    public double getAccuracyFactor() {
        return accuracyFactor;
    }
    public double getDistanceFactor() {
        return distanceFactor;
    }

    public double getDifficultyFactor(RoomType roomType) {
        return this.difficultyFactor.getOrDefault(roomType, 0.0);
    }

    public int getLootPrediction(RoomType roomType) {
        return lootPrediction.getOrDefault(roomType, 0);
    }

    public AgentPrecept addLootPrediction(RoomType roomType, Integer value) {
        this.lootPrediction.putIfAbsent(roomType, value);
        return this;
    }

    public AgentPrecept addDifficultyFactor(RoomType roomType, Double value) {
        this.difficultyFactor.putIfAbsent(roomType, value);
        return this;
    }
}
