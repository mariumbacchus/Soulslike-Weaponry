package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public abstract class UmbralTrespassItem extends ModdedSword {

    private final int ticksBeforeDismount;

    public UmbralTrespassItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, int ticksBeforeDismount) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.ticksBeforeDismount = ticksBeforeDismount;
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.UMBRAL_TRESPASS);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (user.getAttacking() != null && user.squaredDistanceTo(user.getAttacking()) < 200D) {
            LivingEntity target = user.getAttacking();
            if (user.startRiding(target, true)) {
                if (!UmbralTrespassData.shouldDamageRiding(user)) {
                    UmbralTrespassData.setShouldDamageRiding(user, true);
                    UmbralTrespassData.setTicksBeforeDismount(user, this.ticksBeforeDismount);
                }
                if (!world.isClient) {
                    world.playSound(null, user.getBlockPos(), SoundRegistry.UMBRAL_TRESPASS_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                    ParticleHandler.particleOutburstMap(world, 150, user.getX(), user.getEyeY(), user.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 1f);
                }
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }
}
