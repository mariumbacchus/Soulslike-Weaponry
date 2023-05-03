package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HolyMoonlightGreatsword extends TrickWeapon {

    private static final String RUPTURES = "ruptures_amount";
    private static final String SPOT_X = "vector_spot_x";
    private static final String SPOT_Z = "vector_spot_z";
    private static final String CAN_RUPTURE = "can_rupture";
    private static final String MOD = "rupture_modifier";
    private static final String POS = "last_position";

    public HolyMoonlightGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings, int switchWeaponIndex) {
        super(toolMaterial, 3, attackSpeed, settings, switchWeaponIndex, 3, false, true);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int chargeTime = this.getMaxUseTime(stack) - remainingUseTicks;
            if (chargeTime >= 10) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.holy_moonlight_ability_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.damage(5, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
                int ruptures = 5 + WeaponUtil.getEnchantDamageBonus(stack);
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos((int)vecBlocksAway.x, (int)user.getY(), (int)vecBlocksAway.z);
                if (stack.hasNbt()) {
                    Vec3d vec = player.getRotationVector().multiply(3);
                    int[] pos = {targetArea.getX(), targetArea.getY(), targetArea.getZ()};
                    stack.getNbt().putInt(RUPTURES, ruptures);
                    stack.getNbt().putDouble(SPOT_X, vec.getX());
                    stack.getNbt().putDouble(SPOT_Z, vec.getZ());
                    stack.getNbt().putBoolean(CAN_RUPTURE, true);
                    stack.getNbt().putDouble(MOD, 0.5D);
                    stack.getNbt().putIntArray(POS, pos);
                    stack.getNbt().putInt(WeaponUtil.CHARGE, 0);
                }
                float power = ConfigConstructor.holy_moonlight_ability_damage;
                for (Entity entity : world.getOtherEntities(player, new Box(targetArea).expand(3))) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(world.getDamageSources().mobAttack(player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.addVelocity(0, this.getKnockup(stack), 0);
                    }
                }
                player.world.playSound(player, targetArea, SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                player.world.playSound(player, targetArea, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.MOONFALL_ID, targetArea, 100);
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity user && stack.hasNbt() && stack.getNbt().contains(RUPTURES)
                && stack.getNbt().contains(SPOT_X) && stack.getNbt().contains(SPOT_Z) && stack.getNbt().contains(POS)
                && stack.getNbt().contains(CAN_RUPTURE) && stack.getNbt().contains(MOD)) {
            if (stack.getNbt().getBoolean(CAN_RUPTURE) && user.age % 2 == 0) {
                double mod = stack.getNbt().getDouble(MOD);
                int times = stack.getNbt().getInt(RUPTURES);
                Vec3d direction = new Vec3d(stack.getNbt().getDouble(SPOT_X), 0, stack.getNbt().getDouble(SPOT_Z)).multiply(mod);
                BlockPos pos = new BlockPos(stack.getNbt().getIntArray(POS)[0], stack.getNbt().getIntArray(POS)[1], stack.getNbt().getIntArray(POS)[2]);
                Vec3d targetArea = new Vec3d(pos.getX(), user.getY() - 5, pos.getZ()).add(direction);
                BlockPos blockPos = this.getAlteredPos(world, BlockPos.ofFloored(targetArea));
                for (Entity e : world.getOtherEntities(user, new Box(blockPos).expand(2))) {
                    if (e instanceof LivingEntity) {
                        e.damage(world.getDamageSources().mobAttack(user), this.getAbilityDamage((LivingEntity) e, stack));
                        e.addVelocity(0, this.getKnockup(stack), 0);
                    }
                }
                if (!world.isClient) {
                    ParticleNetworking.specificServerParticlePacket((ServerWorld) world, PacketRegistry.SOUL_FLAME_RUPTURE_ID, blockPos, targetArea.getX(), (float)targetArea.getZ());
                }
                world.playSound(null, blockPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                world.playSound(null, blockPos, SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                stack.getNbt().putDouble(MOD, mod + 0.5D);
                stack.getNbt().putInt(RUPTURES, times - 1);
                if (times < 0) {
                    stack.getNbt().putBoolean(CAN_RUPTURE, false);
                }
            }
        }
    }

    private BlockPos getAlteredPos(World world, BlockPos start) {
        return world.getBlockState(start).isAir() ? start : this.getAlteredPos(world, start.add(0, 1, 0));

    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        WeaponUtil.addCharge(stack, 1);
        return super.postHit(stack, target, attacker);
    }

    private float getAbilityDamage(LivingEntity target, ItemStack stack) {
        return ConfigConstructor.holy_moonlight_ability_damage + 2 * EnchantmentHelper.getAttackDamage(stack, target.getGroup());
    }

    private float getKnockup(ItemStack stack) {
        return ConfigConstructor.holy_moonlight_ability_knockup + (float)WeaponUtil.getEnchantDamageBonus(stack)/10;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1 || !WeaponUtil.isCharged(itemStack)) {
            return TypedActionResult.fail(itemStack);
        }
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.TRICK_WEAPON, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.NEED_CHARGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHARGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MOONFALL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RIGHTEOUS, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }
}