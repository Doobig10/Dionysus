package integration.tools;

import gamedata.AgentPrecept;
import gamedata.mapdata.roomTypes.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public final class AgentModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentModifier.class);

    private final int mutationChance;
    private final double mutationFactor;

    private final int crossChance;

    public AgentModifier(int mutationChance, double mutationFactor, int crossChance) {
        this.mutationChance = mutationChance;
        this.mutationFactor = mutationFactor;
        this.crossChance = crossChance;
    }

    public AgentPrecept attemptMutation(AgentPrecept baseline) {
        if (ThreadLocalRandom.current().nextInt(0, 100) > this.mutationChance) {return baseline;}
        LOGGER.atTrace().log("Mutating agent with ID: "+baseline.getID());
        AgentPrecept newPrecept = new AgentPrecept(
                -1,
                baseline.getGeneration() + 1,
                baseline.getAccuracyFactor() * ThreadLocalRandom.current()
                        .nextDouble(1-mutationFactor,1+mutationFactor),
                baseline.getDistanceFactor() * ThreadLocalRandom.current()
                        .nextDouble(1-mutationFactor,1+mutationFactor)
        );
        for (RoomType type: RoomType.values()) {
            newPrecept.addLootPrediction(type,
                    (int) Math.round(baseline.getLootPrediction(type) * ThreadLocalRandom.current()
                            .nextDouble(1-mutationFactor,1+mutationFactor))
            );
            newPrecept.addDifficultyFactor(type,
                    baseline.getDifficultyFactor(type) * ThreadLocalRandom.current()
                            .nextDouble(1-mutationFactor,1+mutationFactor)
            );
        }
        return newPrecept;
    }

    public AgentPrecept attemptCross(AgentPrecept primary, AgentPrecept secondary) {
        if (ThreadLocalRandom.current().nextInt(0, 100) > this.crossChance) {return primary;}
        LOGGER.atTrace().log("Crossing agent ID: "+primary.getID()+" with: "+secondary.getID());
        AgentPrecept newPrecept = new AgentPrecept(
                -1,
                Math.max(primary.getGeneration(), secondary.getGeneration()) + 1,
                0.5*(primary.getAccuracyFactor()+ secondary.getAccuracyFactor()),
                0.5*(primary.getDistanceFactor() + secondary.getDistanceFactor())
        );
        for (RoomType type: RoomType.values()) {
            newPrecept.addLootPrediction(type,
                    (int) Math.round(0.5 * (primary.getLootPrediction(type) + secondary.getLootPrediction(type)))
            );
            newPrecept.addDifficultyFactor(type,
                    0.5 * (primary.getDifficultyFactor(type) + secondary.getDifficultyFactor(type))
            );
        }
        return newPrecept;
    }

}
