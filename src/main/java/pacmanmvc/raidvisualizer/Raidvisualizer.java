package pacmanmvc.raidvisualizer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pacmanmvc.raidvisualizer.render.*;

public class Raidvisualizer implements ModInitializer {
    public static final String MOD_ID = "raidvisualizer";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final RendererGroup<Cube> CENTER = new RendererGroup<>(1, RendererGroup.RenderOption.RENDER_FRONT);
    private static final RendererGroup<Cuboid> SPAWNS = new RendererGroup<>(100, RendererGroup.RenderOption.RENDER_FRONT);
    private static final RendererGroup<Cube> POI_LIST = new RendererGroup<>(100, RendererGroup.RenderOption.RENDER_FRONT);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");

        RenderQueue.get().add("hand", matrixStack -> {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.peek().getModel());
            GlStateManager.disableTexture();
            GlStateManager.disableDepthTest();

            CENTER.render();
            SPAWNS.render();
            POI_LIST.render();

            GlStateManager.disableDepthTest();

            RenderSystem.popMatrix();
        });
    }

    public static void submitCenter(Cube c) {
        CENTER.addRenderer(c);
    }

    public static void submitPoi(Cube c) {
        POI_LIST.addRenderer(c);
    }

    public static void submitSpawn(Cuboid c) {
        SPAWNS.addRenderer(c);
    }

    public static void clearAll() {
        clearCenter();
        clearSpawns();
        clearPois();
    }

    public static void clearCenter() {
        CENTER.clear();
    }

    public static void clearPois() {
        POI_LIST.clear();
    }

    public static void clearSpawns() {SPAWNS.clear();}
}