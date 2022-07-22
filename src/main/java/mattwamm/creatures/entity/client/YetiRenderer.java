package mattwamm.creatures.entity.client;

import mattwamm.creatures.Creatures;
import mattwamm.creatures.entity.RacoonEntity;
import mattwamm.creatures.entity.YetiEntity;
import mattwamm.creatures.entity.client.RacoonModel;
import mattwamm.creatures.entity.client.YetiModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class YetiRenderer extends GeoEntityRenderer<YetiEntity> {

    public YetiRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new YetiModel());
    }

    @Override
    public Identifier getTextureResource(YetiEntity instance) {
        return new Identifier(Creatures.MOD_ID, "textures/entity/yeti/yeti.png");
    }

}