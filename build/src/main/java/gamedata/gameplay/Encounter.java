package gamedata.gameplay;

public interface Encounter {
    void populate();

    boolean isLooted();
    boolean hasKnownDifficulty();

    int getLootValue();
    int getDifficulty();
}
