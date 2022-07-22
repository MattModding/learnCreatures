package mattwamm.creatures.entity.client;

import mattwamm.creatures.Creatures;
import mattwamm.creatures.entity.RacoonEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RacoonModel extends AnimatedGeoModel<RacoonEntity> {
    @Override
    public Identifier getModelResource(RacoonEntity object) {
        return new Identifier(Creatures.MOD_ID, "geo/raccoon.geo.json");
    }

    @Override
    public Identifier getTextureResource(RacoonEntity object) {
        return new Identifier(Creatures.MOD_ID, "textures/entity/raccoon.raccoon.png");
    }

    @Override
    public Identifier getAnimationResource(RacoonEntity animatable) {
        return new Identifier(Creatures.MOD_ID, "animations/raccoon.animation.json");
    }

    @Override
    public void setLivingAnimations(RacoonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

}
