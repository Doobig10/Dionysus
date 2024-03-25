package gamedata;

public class AgentResult {

    private final int score;
    private final int turns;

    public AgentResult(int score, int turns) {
        this.score = score;
        this.turns = turns;
    }

    public int getScore() {
        return score;
    }

    public int getTurns() {
        return turns;
    }
}
