package pacmanmvc.raidvisualizer.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pacmanmvc.raidvisualizer.Raidvisualizer;
import pacmanmvc.raidvisualizer.render.Color;
import pacmanmvc.raidvisualizer.render.Cube;

import java.util.Optional;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow private Optional<BlockPos> preCalculatedRavagerSpawnLocation;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        BlockPos blockPos = this.preCalculatedRavagerSpawnLocation.orElse(null);

        if (blockPos != null) {
            Cube cube = new Cube(blockPos, Color.PURPLE);
            Raidvisualizer.submitSpawn(cube);
        }
    }
}
