package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class MasterSword extends ChargeToUseItem {

    public MasterSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.MASTER_SWORD_DAMAGE.get(), CommonConfig.MASTER_SWORD_ATTACK_SPEED.get(), settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SKYWARD_STRIKES, WeaponUtil.TooltipAbilities.RIGHTEOUS);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(1, playerEntity, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(user.getActiveHand());
                });
                MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE.get(), world, user);
                entity.setAgeAndPoints(30, 150, 4);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
                entity.setDamage(CommonConfig.MASTER_SWORD_PROJECTILE_DAMAGE.get());
                entity.setItemStack(stack);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.getHealth() < user.getMaxHealth()) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_MASTER_SWORD.get();
    }
}