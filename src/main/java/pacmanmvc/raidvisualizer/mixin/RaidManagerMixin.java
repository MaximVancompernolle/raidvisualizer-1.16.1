package pacmanmvc.raidvisualizer.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.village.raid.Raid;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.GameRules;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pacmanmvc.raidvisualizer.Raidvisualizer;
import pacmanmvc.raidvisualizer.render.Color;
import pacmanmvc.raidvisualizer.render.Cube;
import pacmanmvc.raidvisualizer.render.Cuboid;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(RaidManager.class)
public abstract class RaidManagerMixin extends PersistentState {
    @Final
    @Shadow
    private ServerWorld world;

    public RaidManagerMixin(String key) {
        super(key);
    }

    @Inject(method = "startRaid", at = @At("HEAD"), cancellable = true)
    private void startRaid(ServerPlayerEntity player, CallbackInfoReturnable<Raid> cir) {
        if (player.isSpectator()) {
            cir.setReturnValue(null);
        } else if (this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS)) {
            cir.setReturnValue(null);
        } else {
            DimensionType dimensionType = player.world.getDimension();
            if (!dimensionType.hasRaids()) {
                cir.setReturnValue(null);
            } else {
                BlockPos blockPos = player.getBlockPos();
                List<PointOfInterest> list = this.world
                        .getPointOfInterestStorage()
                        .getInCircle(PointOfInterestType.ALWAYS_TRUE, blockPos, 64, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED)
                        .collect(Collectors.toList());
                BlockPos blockPos3 = getBlockPos(list, blockPos);

                Raidvisualizer.submitCenter(new Cube(blockPos3, Color.RED));
                Raidvisualizer.submitSpawn(new Cuboid(blockPos3, new Vec3i(4, 0, 4), Color.PINK));
            }
        }
    }

    @Unique
    private static BlockPos getBlockPos(List<PointOfInterest> list, BlockPos blockPos) {
        int i = 0;
        Vec3d vec3d = Vec3d.ZERO;

        for (PointOfInterest pointOfInterest : list) {
            BlockPos blockPos2 = pointOfInterest.getPos();
            Raidvisualizer.submitPoi(new Cube(blockPos2, Color.YELLOW));
            vec3d = vec3d.add(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            i++;
        }

        BlockPos blockPos3;
        if (i > 0) {
            vec3d = vec3d.multiply(1.0 / (double)i);
            blockPos3 = new BlockPos(vec3d);
        } else {
            blockPos3 = blockPos;
        }
        return blockPos3;
    }
}
