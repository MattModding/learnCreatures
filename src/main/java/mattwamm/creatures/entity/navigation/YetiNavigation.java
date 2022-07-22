package mattwamm.creatures.entity.navigation;

import mattwamm.creatures.entity.YetiEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class YetiNavigation extends EntityNavigation {


    public YetiNavigation(YetiEntity yeti, World world) {
        super(yeti, world);
    }

    @Override
    protected PathNodeNavigator createPathNodeNavigator(int range) {
        this.nodeMaker = new LandPathNodeMaker();
        this.nodeMaker.setCanEnterOpenDoors(false);
        return new PathNodeNavigator(this.nodeMaker, range);
    }

    @Override
    protected Vec3d getPos() {
        return new Vec3d(this.entity.getX(), (double)this.getPathfindingY(), this.entity.getZ());
    }

//    @Override
//    public boolean startMovingTo(double x, double y, double z, double speed) {
//        ((YetiEntity)this.entity).setAnimationState(YetiEntity.animationState.WALK);
//        return this.startMovingAlong(this.findPathTo(x, y, z, 1), speed);
//    }

    private int getPathfindingY() {
        if (this.entity.isTouchingWater() && this.canSwim()) {
            int i = this.entity.getBlockY();
            BlockState blockState = this.world.getBlockState(new BlockPos(this.entity.getX(), (double)i, this.entity.getZ()));
            int j = 0;
            do {
                if (!blockState.isOf(Blocks.WATER)) {
                    return i;
                }
                ++i;
                blockState = this.world.getBlockState(new BlockPos(this.entity.getX(), (double)i, this.entity.getZ()));
                ++j;
            } while(j <= 16);

            return this.entity.getBlockY();
        } else {
            return MathHelper.floor(this.entity.getY() + 0.5);
        }
    }

    @Override
    public boolean canSwim() {
        return false;
    }

    @Override
    protected boolean isAtValidPosition() {
        return this.entity.isOnGround() || this.isInLiquid() || this.entity.hasVehicle();
    }

}
