package network.interactions;

import gamedata.AgentPrecept;
import gamedata.AgentResult;

import java.util.HashMap;

public class Body {

    public static class PopulateRequest {

        private final int requiredPopulation;

        private AgentPrecept[] population;

        public PopulateRequest(int count) {
            this.requiredPopulation = count;
        }

        public int getRequiredPopulation() {
            return requiredPopulation;
        }

        public void setPopulation(AgentPrecept[] population) {
            this.population = population;
        }

        public AgentPrecept[] getPopulation(){
            return population;
        }

    }

    public static class FinaliseRequest {

        private final HashMap<Integer, AgentResult> results;
        private boolean success = false;

        public FinaliseRequest(HashMap<Integer, AgentResult> results) {
            this.results = results;
        }

        public HashMap<Integer, AgentResult> getResults() {
            return results;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }

}
