package dev.togglebettercombat.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Locale;

/**
 * Small vanilla config screen. No config library required.
 */
public final class ToggleConfigScreen extends Screen {
    private final Screen parent;

    public ToggleConfigScreen(Screen parent) {
        super(Text.translatable("screen.togglebettercombat.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 20;

        this.addDrawableChild(ButtonWidget.builder(styleText(), button -> {
            ToggleClientConfig.cycleNotificationStyle();
            button.setMessage(styleText());
        }).dimensions(x, y, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.done"),
                button -> this.close()
        ).dimensions(x, y + 40, 200, 20).build());
    }

    private static Text styleText() {
        String key = "screen.togglebettercombat.config.style."
                + ToggleClientConfig.notificationStyle().name().toLowerCase(Locale.ROOT);
        return Text.translatable(
                "screen.togglebettercombat.config.style",
                Text.translatable(key)
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
