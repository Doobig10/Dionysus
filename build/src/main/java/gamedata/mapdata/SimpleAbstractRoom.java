package gamedata.mapdata;

import gamedata.gameplay.Challenge;
import gamedata.gameplay.Lootable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SimpleAbstractRoom extends AbstractRoom {

    @Override
    public void populate() {
        Lootable lootable = new Lootable() {

            private int value;

            @Override
            public String getName() {
                return "SimpleLoot";
            }

            @Override
            public int getValue() {
                return this.value;
            }

            @Override
            public void setValue(int value) {
                this.value = value;
            }
        };

        lootable.setValue(ThreadLocalRandom.current().nextInt(
                (int) Math.floor(getAverageLoot()*(1-getLootVariance())),
                (int) Math.ceil(getAverageLoot()*(1+getLootVariance()))
        ));

        this.addHiddenLoot(new ArrayList<Lootable>(List.of(
                lootable
        )));

        for (int count = 0; count <= this.getChallengeCount(); count++) {
            Challenge challenge = new Challenge() {
                private int difficulty;

                @Override
                public int getDifficulty() {
                    return this.difficulty;
                }

                @Override
                public void setDifficulty(int difficulty) {
                    this.difficulty = difficulty;
                }
            };

            challenge.setDifficulty(ThreadLocalRandom.current().nextInt(
                    getAverageStrength()-getStrengthRange(),
                    getAverageStrength()+getStrengthRange()
            ));

            this.addChallenge(challenge);
        }

    }

    public abstract int getAverageLoot();
    public abstract double getLootVariance();

    public abstract int getChallengeCount();
    public abstract int getAverageStrength();
    public abstract int getStrengthRange();

}
