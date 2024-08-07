package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;

public class ForlornScythe extends SoulHarvestingItem implements IAnimatable {

    private static final String CRITICAL = "3rd_shot";
    private static final String PREV_UUID = "prev_projectile_uuid";
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public ForlornScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.forlorn_scythe_damage, ConfigConstructor.forlorn_scythe_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SOUL_RELEASE_WITHER);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (!world.isClient) {
            this.detonatePrevEntity((ServerWorld) world, stack);
        }
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(KILLS)) {
                int power = this.getSouls(stack);
                if (power > 0 || user.isCreative()) {
                    WitherSkullEntity entity = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
                    entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                    entity.setOwner(user);
                    if (this.isCritical(stack)) {
                        entity.setCharged(true);
                        stack.getNbt().putInt(CRITICAL, 1);
                    } else {
                        stack.getNbt().putInt(CRITICAL, stack.getNbt().getInt(CRITICAL) + 1);
                    }
                    entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3f, 1.0F);
                    world.spawnEntity(entity);
                    this.setPrevUuid(stack, entity);
                    if (!user.isCreative()) this.addAmount(stack, -1);
                    user.getItemCooldownManager().set(this, 10);
                    stack.damage(1, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(hand));
                    return TypedActionResult.success(stack, world.isClient());
                }
            }
        }
        return TypedActionResult.fail(stack);
    }

    /**
     * Due to the skulls' drag at 0.95 (and 0.73 for the charged ones), the projectiles WILL get stuck in the air
     * at some point. Therefore, to avoid this, the previous projectile's UUID will be stored into NBT, then
     * checked in the world whether it exists or not, then removed accordingly.
     */
    private void detonatePrevEntity(ServerWorld world, ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(PREV_UUID)) {
            UUID uuid = stack.getNbt().getUuid(PREV_UUID);
            Entity entity = world.getEntity(uuid);
            if (entity instanceof WitherSkullEntity skull) {
                Explosion.DestructionType destructionType = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                world.createExplosion(skull, skull.getX(), skull.getY(), skull.getZ(), skull.isCharged() ? 2f : 1f, false, destructionType);
                skull.discard();
            }
        }
    }

    private void setPrevUuid(ItemStack stack, Entity entityToSet) {
        if (stack.hasNbt()) {
            stack.getNbt().putUuid(PREV_UUID, entityToSet.getUuid());
        }
    }

    private boolean isCritical(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(CRITICAL)) {
            return stack.getNbt().getInt(CRITICAL) >= 3;
        } else {
            stack.getNbt().putInt(CRITICAL, 1);
        }
        return false;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_forlorn_scythe;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_scythe;
    }
}