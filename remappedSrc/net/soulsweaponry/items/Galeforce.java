package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ChargedArrow;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Galeforce extends ModdedBow implements IKeybindAbility {

    public Galeforce(Settings settings) {
        super(settings);
    }
    
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean creativeAndInfinity = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty() || creativeAndInfinity) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }
                int maxUseTime = this.getMaxUseTime(stack) - remainingUseTicks;
                float pullProgress = BowItem.getPullProgress(maxUseTime);
                if (!((double)pullProgress < 0.1D)) {
                    if (!world.isClient) {
                        this.shootArrow((ServerWorld) world, stack, itemStack, playerEntity, pullProgress, false, null);
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.GALEFORCE, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    private void shootArrow(ServerWorld world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, float pullProgress, boolean scaleDamageHp, @Nullable Vec3d currentTargetPos) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 50, 3));
        ChargedArrow chargedArrow = new ChargedArrow(world, player, arrowStack, scaleDamageHp);
        chargedArrow.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        if (currentTargetPos != null) {
            chargedArrow.setVelocity(currentTargetPos.getX(), currentTargetPos.getY(), currentTargetPos.getZ(), pullProgress * 3f, 1f);
        } else {
            chargedArrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, pullProgress * 3.0F, 1.0F);
        }
        if (pullProgress == 1.0F) {
            chargedArrow.setCritical(true);
        }
        double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack); // Normal bow: 2.5 -> 5 (power V) -> 10 with crit
        chargedArrow.setDamage(chargedArrow.getDamage() + power * 0.6f + ConfigConstructor.galeforce_bonus_damage); //This: 3 -> 6 -> 12 with crit
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punch > 0) {
            chargedArrow.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
            chargedArrow.setOnFireFor(8);
        }
        stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));

        boolean creativeAndInfinity = player.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        boolean bl2 = creativeAndInfinity && arrowStack.isOf(Items.ARROW);
        if (bl2 || player.getAbilities().creativeMode && (arrowStack.isOf(Items.SPECTRAL_ARROW) || arrowStack.isOf(Items.TIPPED_ARROW))) {
            chargedArrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        world.spawnEntity(chargedArrow);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
        if (!bl2 && !player.getAbilities().creativeMode) {
            arrowStack.decrement(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeOne(arrowStack);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            player.getItemCooldownManager().set(this, ConfigConstructor.galeforce_dash_cooldown);
            if (player.getAttacking() != null) {
                LivingEntity target = player.getAttacking();
                double x = target.getX() - player.getX();
                double y = target.getEyeY() - player.getBodyY(1f);
                double z = target.getZ() - player.getZ();
                this.shootArrow(world, stack, new ItemStack(Items.ARROW), player, 1f, true, new Vec3d(x, y, z));
            } else {
                this.shootArrow(world, stack, new ItemStack(Items.ARROW), player, 1f, true, null);
            }
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            float f = player.getYaw();
            float g = player.getPitch();
            float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float k = -MathHelper.sin(g * 0.017453292F);
            float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 3.0F * ((1.0F + 1F) / 4.0F);
            h *= n / m;
            k *= n / m;
            l *= n / m;
            player.addVelocity(h, k, l);
        }
    }

    @Override
    public float getReducedPullTime() {
        return 0;
    }
}
