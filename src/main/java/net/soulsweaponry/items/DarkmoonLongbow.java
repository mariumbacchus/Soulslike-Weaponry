package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightArrow;
import net.soulsweaponry.entity.projectile.invisible.ArrowStormEntity;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;

public class DarkmoonLongbow extends ModdedBow implements IKeybindAbility {

    public DarkmoonLongbow(Settings settings) {
        super(settings);
        this.addTooltipAbility( WeaponUtil.TooltipAbilities.SLOW_PULL, WeaponUtil.TooltipAbilities.MOONLIGHT_ARROW, WeaponUtil.TooltipAbilities.ARROW_STORM);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean creativeAndInfinity = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getArrowType(stack);
            if (!itemStack.isEmpty() || creativeAndInfinity) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }
                int maxUseTime = this.getMaxUseTime(stack) - remainingUseTicks;
                float pullProgress = this.getModdedPullProgress(maxUseTime);
                if (!((double)pullProgress < 0.1D)) {
                    if (!world.isClient) {
                        MoonlightArrow projectile = new MoonlightArrow(world, playerEntity);
                        projectile.setPierceLevel((byte) 4);
                        projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                        this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, projectile, 0.5f, 3f);
                    }
                }
            }
        }
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public float getReducedPullTime() {
        return - ConfigConstructor.darkmoon_longbow_increased_pull_time;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
            ArrowStormEntity entity = new ArrowStormEntity(EntityRegistry.ARROW_STORM_ENTITY.get(), world);
            entity.setPos(player.getX(), player.getY() + 4.5F, player.getZ());
            entity.setVelocity(player, 0, player.getYaw(), 0.0F, 1f, 1.0F);
            entity.setOwner(player);
            double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            entity.setDamage(ConfigConstructor.darkmoon_longbow_ability_damage + power * 2f);
            entity.setMaxArrowAge(40);
            world.spawnEntity(entity);
            if (!player.isCreative()) {
                player.getItemCooldownManager().set(this, Math.max(ConfigConstructor.darkmoon_longbow_ability_cooldown_ticks - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 30, 60));
            }
            stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkmoon_longbow;
    }
}