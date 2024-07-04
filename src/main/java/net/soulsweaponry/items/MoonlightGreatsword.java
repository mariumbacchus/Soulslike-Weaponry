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
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.List;

public class MoonlightGreatsword extends ChargeToUseItem {
    
    public MoonlightGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.MOONLIGHT_GREATSWORD_DAMAGE.get(), attackSpeed, settings);
    }

    public MoonlightGreatsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_MOONLIGHT_GREATSWORD.get();
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(3, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
        
                MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE.get(), world, user);
                entity.setAgeAndPoints(30, 150, 4);
                //float damage = (float) user.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
                entity.setDamage(CommonConfig.MOONLIGHT_GREATSWORD_PROJECTILE_DAMAGE.get());
                entity.setItemStack(stack);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
                if (this instanceof BluemoonGreatsword) {
                    if (stack.hasNbt() && !playerEntity.isCreative()) {
                        stack.getNbt().putInt(IChargeNeeded.CHARGE, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            if (this instanceof BluemoonGreatsword) {
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.NEED_CHARGE, stack, tooltip);
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHARGE, stack, tooltip);
            }
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MOONLIGHT, stack, tooltip);
            if (!(this instanceof BluemoonGreatsword)) {
                for (int i = 1; i <= 3; i++) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.moonlight_greatsword.part_" + i).formatted(Formatting.DARK_GRAY));
                }
            }
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }
}
