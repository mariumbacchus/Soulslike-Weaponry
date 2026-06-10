package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.entitydata.PostureData;

public interface IPostureLossProjectile {

    int getPostureLoss();
    void setPostureLoss(int postureLoss);

    default void applyPostureLoss(LivingEntity target) {
        if (GunConfig.can_projectiles_apply_posture_loss) {
            int posture = this.getPostureLoss();
            if (target instanceof PlayerEntity) {
                posture = MathHelper.floor((float) posture * GunConfig.silver_bullet_posture_loss_on_player_modifier);
            }
            PostureData.addPostureLoss(target, posture);
        }
    }
}
