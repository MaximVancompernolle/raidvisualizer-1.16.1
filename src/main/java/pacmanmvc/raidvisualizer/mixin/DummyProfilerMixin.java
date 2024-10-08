package pacmanmvc.raidvisualizer.mixin;

import net.minecraft.util.profiler.DummyProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pacmanmvc.raidvisualizer.render.RenderQueue;

@Mixin(DummyProfiler.class)
public abstract class DummyProfilerMixin {
    @Inject(method = "swap(Ljava/lang/String;)V", at = @At("HEAD"))
    private void swap(String location, CallbackInfo ci) {
        RenderQueue.get().onRender(location);
    }
}
