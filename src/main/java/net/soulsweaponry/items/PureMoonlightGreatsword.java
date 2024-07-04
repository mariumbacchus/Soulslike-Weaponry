package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.List;

public class PureMoonlightGreatsword extends ChargeToUseItem {
    
    public PureMoonlightGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.PURE_MOONLIGHT_GREATSWORD_DAMAGE.get(), attackSpeed, settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!world.isClient) {
                    stack.damage(5, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                    for (int j = -1; j < 2; j++) {
                        MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE.get(), world, user);
                        entity.setAgeAndPoints(30, 75, 4);
                        entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw() + j*5, 0.0F, 1.5F, 1.0F);
                        entity.setDamage(CommonConfig.MOONLIGHT_GREATSWORD_PROJECTILE_DAMAGE.get() + 1f);
                        entity.setItemStack(stack);
                        world.spawnEntity(entity);
                    }
                    world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.TRIPLE_MOONLIGHT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_PURE_MOONLIGHT_GREATSWORD.get();
    }
}
