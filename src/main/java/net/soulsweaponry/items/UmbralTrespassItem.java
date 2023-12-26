package net.soulsweaponry.items;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;

public class UmbralTrespassItem extends SoulHarvestingItem {

    private final int ticksBeforeDismount;
    public static final EntityDataAccessor<Boolean> SHOULD_DAMAGE_RIDING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> TICKS_BEFORE_DISMOUNT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    public UmbralTrespassItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, int ticksBeforeDismount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.ticksBeforeDismount = ticksBeforeDismount;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (user.getLastHurtMob() != null && user.distanceToSqr(user.getLastHurtMob()) < 200D) {
            LivingEntity target = user.getLastHurtMob();
            if (user.startRiding(target, true)) {
                try {//TODO add ability, the fact that you enter targets soul and deal damage on exit and stuff
                    if (!user.getEntityData().get(SHOULD_DAMAGE_RIDING)) {
                        user.getEntityData().set(SHOULD_DAMAGE_RIDING, Boolean.TRUE);
                        user.getEntityData().set(TICKS_BEFORE_DISMOUNT, this.ticksBeforeDismount);
                    } else {
                        return InteractionResultHolder.fail(stack);
                    }
                } catch (Exception e) {
                    user.getEntityData().define(SHOULD_DAMAGE_RIDING, Boolean.TRUE);
                    user.getEntityData().define(TICKS_BEFORE_DISMOUNT, this.ticksBeforeDismount);
                }
                if (!world.isClientSide) {
                    world.playSound(null, user.getOnPos(), SoundRegistry.UMBRAL_TRESPASS_EVENT.get(), SoundSource.PLAYERS, 0.8f, 1f);
                    ParticleHandler.particleOutburstMap(world, 100, user.getX(), user.getEyeY(), user.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 1f);//TODO test
                }
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
