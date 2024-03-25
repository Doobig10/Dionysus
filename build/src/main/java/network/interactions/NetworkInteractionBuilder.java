package network.interactions;

import com.google.gson.Gson;

public final class NetworkInteractionBuilder {

    InteractionType type;
    String json;

    public NetworkInteractionBuilder(InteractionType type) {
        this.type = type;
    }

    public NetworkInteractionBuilder setJson(Object object) {
        Gson gson = new Gson();
        this.json = gson.toJson(object);
        return this;
    }

    public NetworkInteraction build() {
        return new NetworkInteraction(this);
    }
}
