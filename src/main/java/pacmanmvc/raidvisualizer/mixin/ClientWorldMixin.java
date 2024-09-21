package pacmanmvc.raidvisualizer.mixin;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pacmanmvc.raidvisualizer.Raidvisualizer;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(CallbackInfo ci) {
        Raidvisualizer.clearAll();
    }
}
