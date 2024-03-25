package gamedata.mapdata;

import gamedata.gameplay.Challenge;
import gamedata.gameplay.Encounter;
import gamedata.gameplay.Lootable;
import gamedata.mapdata.roomTypes.RoomType;

import java.util.ArrayList;

public abstract class AbstractRoom
        extends AbstractLocation
        implements Encounter
{

    private boolean knownDifficulty = false;
    private final ArrayList<Challenge> challenges = new ArrayList<>();

    private boolean looted = false;

    private final ArrayList<Lootable> knownLoot = new ArrayList<>();
    private final ArrayList<Lootable> hiddenLoot = new ArrayList<>();


    public void setKnownDifficulty(boolean state) {
        this.knownDifficulty = state;
    }

    public void addKnownLoot(ArrayList<Lootable> loot) {
        this.knownLoot.addAll(loot);
    }

    public void addHiddenLoot(ArrayList<Lootable> loot) {
        this.hiddenLoot.addAll(loot);
    }

    public void addChallenge(Challenge challenge) {
        this.challenges.add(challenge);
    }

    @Override
    public boolean isRoom() {return true;}

    @Override
    public boolean isLooted() {
        return this.looted;
    }

    public Challenge getNextChallenge() {
        this.knownDifficulty = true;
        return this.challenges.get(0);
    }

    @Override
    public boolean hasKnownDifficulty() {
        return knownDifficulty;
    }

    @Override
    public int getLootValue() {
        int total = 0;
        for (Lootable item : knownLoot) {total = total + item.getValue();}
        return total;
    }

    public ArrayList<Lootable> lootAll() {
        ArrayList<Lootable> allLoot = new ArrayList<>();
        allLoot.addAll(this.knownLoot);
        allLoot.addAll(this.hiddenLoot);
        this.knownLoot.clear();
        this.hiddenLoot.clear();
        this.looted = true;
        return allLoot;
    }

    @Override
    public int getDifficulty() {
        return this.getNextChallenge().getDifficulty();
    }

    public void clearChallenge(){
        this.challenges.remove(0);
        if (this.hasChallenge()) {
            this.knownDifficulty = false;
        }
    }

    public boolean hasChallenge(){
        return !this.challenges.isEmpty();
    }

    public abstract RoomType getRoomType();

}
