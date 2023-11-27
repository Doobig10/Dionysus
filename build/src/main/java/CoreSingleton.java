public final class CoreSingleton {
    // YOU CAN'T STOP ME DOING THIS IT SHOULD BE SINGLETON!
    // Each Master service should have exactly one and only one core module.
    // Initialised during the static initialiser.

    private final static Core instance = new Core();

    public static Core getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

}
