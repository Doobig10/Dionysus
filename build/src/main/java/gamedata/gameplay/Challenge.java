package gamedata.gameplay;

public interface Challenge {
    int getDifficulty();

    default void setDifficulty(int difficulty){};


}
