public final class MasterSingleton {
    // YOU CAN'T STOP ME DOING THIS IT SHOULD BE SINGLETON!
    // Each Master service should have exactly one and only one core module.
    // Initialised during the static initialiser.

    private final static Master instance = new Master();

    public static Master getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

}
