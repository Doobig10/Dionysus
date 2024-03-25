package gamedata.gameplay;

import gamedata.AgentPrecept;
import gamedata.mapdata.AbstractLocation;
import gamedata.mapdata.AbstractRoom;
import gamedata.mapdata.Linkable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerObject.class);

    private final AgentPrecept precepts;

    private int maxMovement = 5;

    private ArrayList<Lootable> loot = new ArrayList<>();
    private AbstractLocation currentLocation = null;

    private int turnsTaken = 0;

    public PlayerObject(AgentPrecept precepts) {
        this.precepts = precepts;
    }

    public void setLocation(AbstractLocation newLocation) {
        this.currentLocation = newLocation;
    }

    public AgentPrecept getPrecepts() {
        return this.precepts;
    }

    public int getTurnsTaken() {
        return this.turnsTaken;
    }

    public void processTurn(GameContainer gameContainer) {
        this.turnsTaken++;
        int turnMoves = 0;

        if (this.getCurrentLootValue() >= gameContainer.getRequiredLoot()) {
            AbstractLocation exit = gameContainer.getBoard().getInitialLocation();

            while ((this.currentLocation != exit) && (turnMoves++ < maxMovement)) {
                this.moveTowardsTarget(exit);
            }

            if (this.currentLocation == exit) {
                LOGGER.atTrace().log("Cleared Game, Attempting win");
                gameContainer.setWinner(this);
                gameContainer.removePlayer(this);
            }
        }

        else {
            AbstractRoom targetRoom = this.getTargetRoom(gameContainer);

            while ((this.currentLocation != targetRoom) && (turnMoves++ < maxMovement)) {
                this.moveTowardsTarget(targetRoom);
            }

            if (this.currentLocation == targetRoom) {
                boolean success = false;
                if (targetRoom.hasChallenge()) {
                    int difficulty = targetRoom.getDifficulty();
                    int attempt =
                            ThreadLocalRandom.current().nextInt(1,6) +
                            ThreadLocalRandom.current().nextInt(1,6)
                            ; // Two Dice rolls
                    LOGGER.atTrace().log("Challenging Encounter:\t"+difficulty+"\tRoll: "+attempt);
                    if (attempt >= difficulty) {
                        success = true;
                    }
                }
                else {success = true;}

                if (success) {
                    this.loot.addAll(targetRoom.lootAll());
                }
                else {
                    targetRoom.addKnownLoot(this.loot);
                    this.loot.clear();
                }
            }
        }
    }

    private AbstractRoom getTargetRoom(GameContainer gameContainer) {
        AbstractRoom bestEncounter = null;
        double bestValue = -1;
        for (AbstractRoom encounter : gameContainer.getBoard().getEncounters()) {
            double value = evaluateEncounter(encounter, gameContainer);
            if (value > bestValue) {
                bestEncounter = encounter;
                bestValue = value;
            }
        }
        return bestEncounter;
    }

    public double evaluateEncounter(AbstractRoom encounter, GameContainer gameContainer) {
        if (encounter == null) {return 0;}
        double encounterValue = 0;

        encounterValue = encounterValue + encounter.getLootValue();

        if (!encounter.isLooted()){
            encounterValue = encounterValue + this.precepts.getLootPrediction(encounter.getRoomType());
        }

        double specificAccuracy = PlayerMath.calculateAccuracy(gameContainer.getRequiredLoot(), this.getCurrentLootValue(), (int) encounterValue);
        double specificDistance = PlayerMath.calculateDistance(this.currentLocation, encounter);

        double lootValueModifier = (1+specificAccuracy) * this.precepts.getAccuracyFactor();
        double difficultyModifier = this.precepts.getDifficultyFactor(encounter.getRoomType());
        double distanceModifier = Math.pow(this.precepts.getDistanceFactor(), Math.ceil(specificDistance/maxMovement));

        return encounterValue*lootValueModifier*difficultyModifier*distanceModifier;
    }

    private void moveTowardsTarget(AbstractLocation target) {
        int currentClosestDistance = PlayerMath.calculateDistance(this.currentLocation, target);
        AbstractLocation currentClosest = null;
        for (Linkable linked : this.currentLocation.getLinked()) {
            int distance = PlayerMath.calculateDistance(linked, target);
            if (distance < currentClosestDistance) {
                currentClosest = (AbstractLocation) linked;
                currentClosestDistance = distance;
            }
        }
        if (currentClosest != null) {
            this.currentLocation = currentClosest;
        }
    }

    public int getCurrentLootValue() {
        int total = 0;
        for (Lootable item : loot) {total = total + item.getValue();}
        return total;
    }

    private static final class PlayerMath {
        public static double calculateAccuracy(int requiredLoot, int currentLoot, int lootValue) {
            double remainingFrac = (1- ((double) (currentLoot) / requiredLoot));
            double resultFrac = Math.abs(1-(((currentLoot) + lootValue) / requiredLoot));
            return remainingFrac-resultFrac;
        }

        public static int calculateDistance(Linkable currentLocation, Linkable targetLocation) {
            if (currentLocation == targetLocation) {return 0;}
            int distance = 0;

            ArrayList<Linkable> seen = new ArrayList<>();
            ArrayList<Linkable> active = new ArrayList<>();
            ArrayList<Linkable> next_active = new ArrayList<>();

            seen.add(currentLocation);
            next_active.add(currentLocation);
            while (!next_active.isEmpty() && distance++ < 100) {
                active.clear(); active.addAll(next_active); next_active.clear();
                for (Linkable linkable : active) {
                    for (Linkable linked : linkable.getLinked()) {
                        if (linked == targetLocation) {
                            return distance;
                        }
                        else if (!seen.contains(linked)) {
                            next_active.add(linked);
                        }
                    }
                    seen.add(linkable);
                }
            }
            return distance;
        }
    }
}
