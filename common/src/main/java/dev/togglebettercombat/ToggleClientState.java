package dev.togglebettercombat;

/**
 * Client-side copy of the local player's toggle state.
 */
public final class ToggleClientState {
    private static boolean disabled;

    private ToggleClientState() {
    }

    public static boolean isDisabled() {
        return disabled;
    }

    public static void setDisabled(boolean value) {
        disabled = value;
    }
}
