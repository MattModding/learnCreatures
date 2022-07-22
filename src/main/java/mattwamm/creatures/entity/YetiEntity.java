package mattwamm.creatures.entity;

import mattwamm.creatures.entity.goals.yeti.AnimatedAttackGoal;
import mattwamm.creatures.entity.goals.yeti.PickUpGoal;
import mattwamm.creatures.entity.navigation.YetiNavigation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;
import static java.lang.Math.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class YetiEntity extends HostileEntity implements IAnimatable {

    private Entity holdingEntity;
    protected static final TrackedData<Integer> ANIMATION_STATE;
    protected static final TrackedData<Integer> ANIMATION_TIMER;
    protected static final TrackedData<Boolean> FROZEN;
    protected static final TrackedData<Boolean> HOLDING_ENTITY;

    static {
        ANIMATION_STATE = DataTracker.registerData(YetiEntity.class,TrackedDataHandlerRegistry.INTEGER);
        ANIMATION_TIMER = DataTracker.registerData(YetiEntity.class,TrackedDataHandlerRegistry.INTEGER);
        FROZEN = DataTracker.registerData(YetiEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
        HOLDING_ENTITY = DataTracker.registerData(YetiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }


    @Override
    public void move(MovementType movementType, Vec3d movement) {
        if(!getFrozen())
            super.move(movementType, movement);
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new YetiNavigation(this, world);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (getAnimationState()) {
            case ATTACK -> event.getController().setAnimation(animationState.ATTACK.animation);
            case SNIFF -> event.getController().setAnimation(animationState.SNIFF.animation);
            case ROAR -> event.getController().setAnimation(animationState.ROAR.animation);
            case RUN -> event.getController().setAnimation(animationState.RUN.animation);
            case WALK -> event.getController().setAnimation(animationState.WALK.animation);
            default -> {
                if(event.isMoving()) {
                    if(this.isAttacking())
                    {
                        event.getController().setAnimation(animationState.RUN.animation);
                        break;
                    }
                    event.getController().setAnimation(animationState.WALK.animation);
                }
                else{
                    event.getController().setAnimation(animationState.IDLE.animation);
                }
            }
        }
//        if(event.getController().getCurrentAnimation() != null && MinecraftClient.getInstance().player != null)
//            MinecraftClient.getInstance().player.sendMessage(Text.of(event.getController().getCurrentAnimation().animationName));
        return PlayState.CONTINUE;
    }

    protected YetiEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0f;
    }

    @Override
    public void tick() {
        int animationTime = this.getAnimationTimer();
        if(animationTime == 0){
            this.setFrozen(false);
            this.setAnimationState(animationState.IDLE);
        }
        else if(animationTime > 0){
            this.setAnimationTimer(animationTime -1);
        }
        if(this.holdingEntity != null && holdingEntity.isAlive())
        {
//            MinecraftClient.getInstance().player.sendMessage(Text.of(this.holdingEntity.getEntityName()));
            updateHoldingPosition(this.holdingEntity,Entity::setPosition);
        }
        super.tick();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 45.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0f)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20f);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new RevengeGoal(this));
        this.goalSelector.add(1, new PickUpGoal(this, 1.2f, true));
        this.goalSelector.add(1, new AnimatedAttackGoal(this, 1.2f, true));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new LookAtEntityGoal(this, ZombieEntity.class, 8.0f));
        this.goalSelector.add(3, new WanderAroundPointOfInterestGoal(this, 0.75f, false));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this,"controller",
                0,this::predicate));
    }

    //TODO: make it so the yeti can only see in a cone
    @Override
    public boolean canSee(Entity entity) {
        if (entity.world != this.world) {
            return false;
        } else {
            Vec3d vec3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
            Vec3d vec3d2 = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
            if (vec3d2.distanceTo(vec3d) > 128.0) {
                return false;
            } else {
                return this.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
            }
        }
    }

    @Override
    public void swingHand(Hand hand) {
        this.setAnimationState(animationState.ATTACK);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    public void tryPickUp(Entity target) {
        if (!this.world.isClient) {

            PositionUpdater positionUpdater = Entity::setPosition;
            target.setYaw(this.getYaw());
            target.setPitch(this.getPitch());
            Vec3d rotationPosition = new Vec3d(this.getX(),this.getY() + this.getMountedHeightOffset() + target.getHeightOffset(),this.getZ()).add(this.getRotationVec(1f).normalize());
            positionUpdater.accept(target, rotationPosition.x, rotationPosition.y , rotationPosition.z);
            //set initial position
            updateHoldingPosition(target, positionUpdater);
            this.holdingEntity = target;
        }
    }

    private void updateHoldingPosition(Entity target, PositionUpdater positionUpdater) {

        this.getAnimationTimer();
        //gets location of this entity
        Vec3d rotationPosition = new Vec3d(this.getX(),this.getY() + this.getMountedHeightOffset() + target.getHeightOffset(),this.getZ());
        Vec3d rotationVector = this.getRotationVec(1f).normalize();

        Vec3d firingPosition = new Vec3d(0, 2, 3); // change this to put it at different locations

        firingPosition = rotateY(firingPosition, atan2(rotationVector.x,rotationVector.z));

        Vec3d firingDirection = firingPosition.normalize();

        Vec3d setPosition = rotationPosition.add(firingDirection.multiply(2));
        target.setVelocity(0,0,0);
        positionUpdater.accept(target, setPosition.x, setPosition.y , setPosition.z);
//        MinecraftClient.getInstance().player.sendMessage(Text.of("X:" + setPosition.x +"Y:" +setPosition.y + "Z:" + setPosition.z ) );
    }

    Vec3d rotateY(Vec3d pos, double angle) {
        return new Vec3d(
                pos.x * cos(angle) - pos.y * sin(angle),
                pos.x * sin(angle) + pos.y * cos(angle),
                pos.z
        );
    }
    @Override
    public void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANIMATION_STATE,animationState.IDLE.ordinal());
        this.dataTracker.startTracking(ANIMATION_TIMER, 0);
        this.dataTracker.startTracking(FROZEN, false);
        this.dataTracker.startTracking(HOLDING_ENTITY,false);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WARDEN_AGITATED;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WARDEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WARDEN_ANGRY;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1f, 1.0f);
    }

    public animationState getAnimationState() {
        return animationState.valueOf(this.dataTracker.get(ANIMATION_STATE));
    }
    public void setAnimationState(animationState state) {
        this.dataTracker.set(ANIMATION_STATE,state.ordinal());
        this.dataTracker.set(ANIMATION_TIMER, state.animationDuration); //minus 1 for infinite
    }
    public int getAnimationTimer(){
        return this.dataTracker.get(ANIMATION_TIMER);
    }
    public void setAnimationTimer(int i){
        this.dataTracker.set(ANIMATION_TIMER,i);
    }

    public void setFrozen(boolean bool) {
        this.dataTracker.set(FROZEN, bool );
    }
    public boolean getFrozen(){
        return this.dataTracker.get(FROZEN);
    }

    public enum animationState{
        IDLE(0, new AnimationBuilder().addAnimation("animation.yeti.idle",true),-1),
        SNIFF(1, new AnimationBuilder().addAnimation("animation.yeti.sniff",false), 15),
        ROAR(2, new AnimationBuilder().addAnimation("animation.yeti.roar",false),45),
        ATTACK(3, new AnimationBuilder().addAnimation("animation.yeti.attack",false), 8),
        RUN(4, new AnimationBuilder().addAnimation("animation.yeti.run",true),-1),
        WALK(5, new AnimationBuilder().addAnimation("animation.yeti.walk",true),-1),
        THROW(6, new AnimationBuilder().addAnimation("animation.yeti.throw",false),20);
        private final int stateNumber;
        public final AnimationBuilder animation;
        public final int animationDuration;
        private final static Map<Integer, animationState> map =
                stream(animationState.values()).collect(toMap(i -> i.stateNumber, i -> i));
        animationState(int i, AnimationBuilder animationBuilder, int duration) {
            stateNumber = i;
            animation = animationBuilder;
            animationDuration = duration;
        }
        public static animationState valueOf(int index){
            return map.get(index);
        }
    }

}
