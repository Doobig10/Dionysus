package network.interactions;

import com.google.gson.Gson;
import network.connection.Completion;
import network.connection.Request;

public class NetworkInteraction implements Request, Completion {

    private final InteractionType header;

    private String json;

    NetworkInteraction(NetworkInteractionBuilder builder) {
        this.header = builder.type;
        this.json = builder.json;
    }

    public InteractionType getHeader() {
        return this.header;
    }

    public String getJson() {
        return this.json;
    }

    public void updateJson(Object object) {
        Gson gson = new Gson();
        this.json = gson.toJson(object);
    }

}
