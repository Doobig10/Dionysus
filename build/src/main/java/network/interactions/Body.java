package network.interactions;

import gamedata.AgentPrecept;

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

}
