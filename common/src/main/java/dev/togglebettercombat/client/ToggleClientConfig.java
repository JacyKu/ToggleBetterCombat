package dev.togglebettercombat.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import dev.togglebettercombat.ToggleBetterCombat;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Client-only config. Plain JSON, no config library required.
 * Stored in {@code config/togglebettercombat-client.json}.
 */
public final class ToggleClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Platform.getConfigFolder().resolve("togglebettercombat-client.json");

    private static NotificationStyle notificationStyle = NotificationStyle.ACTION_BAR;

    public enum NotificationStyle {
        ACTION_BAR,
        TOAST
    }

    private ToggleClientConfig() {
    }

    public static void load() {
        if (Files.notExists(PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(PATH)) {
            Data data = GSON.fromJson(reader, Data.class);
            notificationStyle = data == null
                    ? NotificationStyle.ACTION_BAR
                    : data.parseStyle();
        } catch (Exception exception) {
            ToggleBetterCombat.LOGGER.warn(
                    "Could not read client config at {}; using {}",
                    PATH,
                    NotificationStyle.ACTION_BAR,
                    exception
            );
            notificationStyle = NotificationStyle.ACTION_BAR;
        }
    }

    public static void save() {
        Data data = new Data();
        data.notificationStyle = notificationStyle.name();

        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException exception) {
            ToggleBetterCombat.LOGGER.warn("Could not write client config at {}", PATH, exception);
        }
    }

    public static NotificationStyle notificationStyle() {
        return notificationStyle;
    }

    private static final class Data {
        String notificationStyle = NotificationStyle.ACTION_BAR.name();

        NotificationStyle parseStyle() {
            if (notificationStyle == null) {
                return NotificationStyle.ACTION_BAR;
            }
            try {
                return NotificationStyle.valueOf(notificationStyle.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                ToggleBetterCombat.LOGGER.warn(
                        "Unknown notificationStyle '{}' in {}; expected ACTION_BAR or TOAST",
                        notificationStyle,
                        PATH
                );
                return NotificationStyle.ACTION_BAR;
            }
        }
    }
}
