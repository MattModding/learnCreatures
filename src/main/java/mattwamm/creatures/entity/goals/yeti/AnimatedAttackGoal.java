package mattwamm.creatures.entity.goals.yeti;

import java.util.EnumSet;

import mattwamm.creatures.entity.YetiEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;

public class AnimatedAttackGoal extends MeleeAttackGoal {

    public AnimatedAttackGoal(YetiEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob,speed,pauseWhenMobIdle);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void start() {
        if(this.mob instanceof YetiEntity) {
            ((YetiEntity) this.mob).setFrozen(true);
            ((YetiEntity) this.mob).setAnimationState(YetiEntity.animationState.ROAR);

        }
        super.start();
    }


}
