package pacmanmvc.raidvisualizer.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import pacmanmvc.raidvisualizer.Raidvisualizer;
import pacmanmvc.raidvisualizer.render.Color;
import pacmanmvc.raidvisualizer.render.Cube;
import pacmanmvc.raidvisualizer.render.Cuboid;

import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.command.arguments.EntityArgumentType.getPlayer;
import static net.minecraft.command.arguments.EntityArgumentType.player;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RaidCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("raid").then(
                        literal("center").then(
                                argument("player", player()).executes(context -> {
                                    List<PointOfInterest> list = ((ServerWorld)(getPlayer(context, "player").world))
                                            .getPointOfInterestStorage()
                                            .getInCircle(PointOfInterestType.ALWAYS_TRUE, getPlayer(context, "player").getBlockPos(), 64, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED)
                                            .collect(Collectors.toList());
                                    BlockPos blockPos3 = getBlockPos(list, getPlayer(context, "player").getBlockPos());

                                    Raidvisualizer.submitCenter(new Cube(blockPos3, Color.RED));
                                    Raidvisualizer.submitSpawn(new Cuboid(blockPos3, new Vec3i(4, 0, 4), Color.PINK));
                                    return 1;
                                })
                        )
                ).then(
                        literal("clear").executes(context -> {
                            Raidvisualizer.clearAll();
                            return 1;
                        })
                )
        );
    }

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
