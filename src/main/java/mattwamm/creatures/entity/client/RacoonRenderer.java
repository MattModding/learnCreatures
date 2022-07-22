package mattwamm.creatures.entity.client;

import mattwamm.creatures.Creatures;
import mattwamm.creatures.entity.RacoonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RacoonRenderer extends GeoEntityRenderer<RacoonEntity> {
    public RacoonRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RacoonModel());
    }

    @Override
    public Identifier getTextureResource(RacoonEntity instance) {
        return new Identifier(Creatures.MOD_ID, "textures/entity/raccoon/raccoon.png");
    }

}
