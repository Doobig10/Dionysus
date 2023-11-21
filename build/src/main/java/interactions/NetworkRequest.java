package interactions;

import connection.Request;

public class NetworkRequest implements Request {

    private final RequestType nature;

    NetworkRequest(RequestType nature) {
        this.nature = nature;
    }

    public RequestType getNature() {
        return this.nature;
    }
}
