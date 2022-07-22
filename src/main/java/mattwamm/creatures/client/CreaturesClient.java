package mattwamm.creatures.client;

import mattwamm.creatures.entity.ModEntities;
import mattwamm.creatures.entity.client.RacoonRenderer;
import mattwamm.creatures.entity.client.YetiRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class CreaturesClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.RACOON, RacoonRenderer::new);
        EntityRendererRegistry.register(ModEntities.YETI, YetiRenderer::new);
    }
}
