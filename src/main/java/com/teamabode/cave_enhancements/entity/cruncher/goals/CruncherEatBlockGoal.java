package com.teamabode.cave_enhancements.entity.cruncher.goals;

import com.teamabode.cave_enhancements.entity.cruncher.Cruncher;
import net.minecraft.core.BlockPos;;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import com.teamabode.cave_enhancements.registry.ModTags;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class CruncherEatBlockGoal extends Goal {

    private final Cruncher cruncher;
    public int currentTick = 0;

    public CruncherEatBlockGoal(Cruncher cruncher) {
        this.cruncher = cruncher;
    }

    public boolean canUse() {
        return Objects.equals(cruncher.getEatingState(), "mining");
    }

    public boolean canContinueToUse() {
        if (cruncher.getTargetPos() == null) return false;
        if (cruncher.getBlockY() == cruncher.getOrePosY() + 1) return false;
        if (!cruncher.level.getBlockState(cruncher.blockPosition().below()).is(ModTags.CRUNCHER_CONSUMABLES)) return false;
        if (cruncher.level.getBlockState(cruncher.blockPosition().below()).is(ModTags.CRUNCHER_SEARCHABLES)) return false;
        if (cruncher.getLastHurtByMob() == null) return true;

        return Objects.equals(cruncher.getEatingState(), "mining");
    }

    public void tick() {
        Level level = cruncher.getLevel();
        BlockPos pos = cruncher.blockPosition().below();
        currentTick++;

        cruncher.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());

        if(cruncher.getTargetPos() == null) return;

        Vec3 vec3 = new Vec3(cruncher.getTargetPos().getX() + 0.5D, cruncher.getY(), cruncher.getTargetPos().getZ() + 0.5D);

        if(cruncher.position().distanceTo(vec3) <=  1){
            if(cruncher.position().distanceTo(vec3) > 0.2){
                cruncher.setDeltaMovement(0.0, 0.0, 0.0);
                cruncher.teleportToWithTicket(vec3.x, vec3.y, vec3.z);
            }

            if (currentTick % 40 == 0 && level.getBlockState(pos).is(ModTags.CRUNCHER_CONSUMABLES)) {
                cruncher.setPose(Pose.DIGGING);
                level.destroyBlock(pos, true);
            }
            if (currentTick % 60 == 0) {
                cruncher.setPose(Pose.STANDING);
                currentTick = 0;
            }

        }else {
            cruncher.getNavigation().moveTo(cruncher.getNavigation().createPath(new BlockPos(vec3), 0), 1.5F);
        }
    }

    public void stop() {
        cruncher.setEatingState("none");
        cruncher.setSearchCooldownTime(240);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}