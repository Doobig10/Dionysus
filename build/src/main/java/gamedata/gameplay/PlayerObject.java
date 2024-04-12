package gamedata.gameplay;

import gamedata.AgentPrecept;
import gamedata.mapdata.AbstractLocation;
import gamedata.mapdata.AbstractRoom;
import gamedata.mapdata.Linkable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PlayerObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerObject.class);

    private final AgentPrecept precepts;

    private final int maxMovement = 5;

    private final ArrayList<Lootable> loot = new ArrayList<>();
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
                if (targetRoom.isRoom()) {
                    boolean success = false;
                    if (targetRoom.hasChallenge()) {
                        int difficulty = targetRoom.getDifficulty();
                        int attempt = GameTools.roll(2, 6);
                        LOGGER.atTrace().log("Challenging Encounter:\t"+difficulty+"\tRoll: "+attempt);
                        if (attempt >= difficulty) {
                            success = true;
                        }
                    }
                    else {success = true;}

                    if (success) {
                        this.loot.addAll(targetRoom.lootAll());
                        targetRoom.clearChallenge();
                    }
                    else {
                        targetRoom.addKnownLoot(this.loot);
                        this.loot.clear();
                    }
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

        double specificAccuracy = PlayerMath.calculateAccuracyPenalty(gameContainer.getRequiredLoot(), this.getCurrentLootValue(), (int) encounterValue);
        double specificDistance = PlayerMath.calculateDistance(this.currentLocation, encounter);

        double lootValueModifier = Math.max(1 - (specificAccuracy * this.precepts.getAccuracyFactor()), 0);
        double difficultyModifier = encounter.hasKnownDifficulty() ? GameTools.getChanceOrHigher(encounter.getDifficulty()) : this.precepts.getDifficultyFactor(encounter.getRoomType());
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
        public static double calculateAccuracyPenalty(int requiredLoot, int currentLoot, int lootValue) {
            double currentFrac = (double) (currentLoot) / requiredLoot;
            double interest = Math.pow(currentFrac, 2);
            double resultFrac = (double) (currentLoot + lootValue) / requiredLoot;
            double accuracy = Math.abs(1 - resultFrac);
            return (interest * accuracy);
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
