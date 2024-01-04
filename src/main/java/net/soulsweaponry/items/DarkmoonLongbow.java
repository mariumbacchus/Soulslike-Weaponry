package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkmoonLongbow extends ModdedBow implements IKeybindAbility {

    public DarkmoonLongbow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            boolean creativeAndInfinity = playerEntity.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemStack = playerEntity.getProjectile(stack);
            if (!itemStack.isEmpty() || creativeAndInfinity) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }
                int maxUseTime = this.getUseDuration(stack) - remainingUseTicks;
                float pullProgress = this.getModdedPullProgress(maxUseTime);
                if (!((double)pullProgress < 0.1D)) {
                    if (!world.isClientSide) {
                        /*MoonlightArrow projectile = new MoonlightArrow(EntityRegistry.MOONLIGHT_ARROW, world);
                        projectile.setPierceLevel((byte) 4);
                        projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;//TODO add a way to increase damage and/or velocity from config
                        this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, projectile, 0.5f, 3f);*///TODO add projectile
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SLOW_PULL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MOONLIGHT_ARROW, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.ARROW_STORM, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public float getReducedPullTime() {
        return - CommonConfig.DARKMOON_LONGBOW_INCREASED_PULL_TIME.get();
    }

    @Override
    public void useKeybindAbilityServer(ServerLevel world, ItemStack stack, Player player) {
        if (!player.getCooldowns().isOnCooldown(this)) {
            world.playSound(null, player.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1f, 1f);
            /*ArrowStormEntity entity = new ArrowStormEntity(EntityRegistry.ARROW_STORM_ENTITY, world);
            entity.setPos(player.getX(), player.getY() + 4.5F, player.getZ());
            entity.setVelocity(player, 0, player.getYaw(), 0.0F, 1f, 1.0F);
            entity.setOwner(player);
            double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            entity.setDamage(ConfigConstructor.darkmoon_longbow_ability_damage + power * 2f);
            entity.setMaxArrowAge(40);TODO add arrow storm entity
            world.spawnEntity(entity);*/
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(this, CommonConfig.DARKMOON_LONGBOW_ABILITY_COOLDOWN_TICKS.get() - EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * 30);
            }
            stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player) {
    }
}
