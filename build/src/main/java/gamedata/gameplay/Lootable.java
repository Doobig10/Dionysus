package gamedata.gameplay;

public interface Lootable {

    String getName();

    int getValue();

    default void setValue(int value) {};
}
