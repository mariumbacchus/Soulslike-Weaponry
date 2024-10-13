package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.EmpoweredDawnbreakerRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.invisible.FlamePillar;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Also known as "Genesis Fracture"
 */
public class EmpoweredDawnbreaker extends AbstractDawnbreaker implements IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public EmpoweredDawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.empowered_dawnbreaker_damage, ConfigConstructor.empowered_dawnbreaker_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.CHAOS_STORM, WeaponUtil.TooltipAbilities.VEIL_OF_FIRE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            int i = this.getChargeTime(stack, remainingUseTicks);
            if (i >= 10) {
                stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                this.summonFlamePillars(world, stack, user);
                this.applyItemCooldown(player, this.getScaledCooldown(stack));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0));
            }
        }
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.empowered_dawnbreaker_ability_enchant_reduces_cooldown;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return ConfigConstructor.empowered_dawnbreaker_ability_enchant_reduces_cooldown_id;
    }

    protected int getScaledCooldown(ItemStack stack) {
        int base = ConfigConstructor.empowered_dawnbreaker_ability_cooldown;
        return Math.max(ConfigConstructor.empowered_dawnbreaker_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 40);
    }

    private void summonFlamePillars(World world, ItemStack stack, LivingEntity user) {
        if (!world.isClient) {
            int i = 0;
            List<BlockPos> list = new ArrayList<>();
            list.add(new BlockPos(0, 0, 0));
            while (i < 20 + 3 * WeaponUtil.getEnchantDamageBonus(stack)) {
                int x = user.getBlockX() + user.getRandom().nextInt(12) - 6;
                int y = user.getBlockY();
                int z = user.getBlockZ() + user.getRandom().nextInt(12) - 6;
                BlockPos pos = new BlockPos(x, y, z);
                for (BlockPos listPos : list) {
                    if (listPos != pos) {
                        FlamePillar pillar = new FlamePillar(EntityRegistry.FLAME_PILLAR.get(), world);
                        pillar.setDamage(ConfigConstructor.empowered_dawnbreaker_ability_damage + WeaponUtil.getEnchantDamageBonus(stack) * 2);
                        pillar.setPos(x, y, z);
                        pillar.setOwner(user);
                        pillar.setWarmup(i * 2);
                        world.spawnEntity(pillar);
                        i++;
                    }
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private EmpoweredDawnbreakerRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new EmpoweredDawnbreakerRenderer();

                return renderer;
            }
        });
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            AbstractDawnbreaker.dawnbreakerEvent(player, player, stack);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.VEIL_OF_FIRE.get(), 200, MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
            this.applyItemCooldown(player, this.getScaledCooldown(stack));
        }
        /*
        NOTE: Used to summon an orb of fireballs that shoots outwards from the player, but was a little
        too laggy with the particles from the explosion.
        double phi = Math.PI * (3. - Math.sqrt(5.));
        float points = 90;
        for (int i = 0; i < points; i++) {
            double y = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - y*y);
            double theta = phi * i;
            double x = Math.cos(theta) * radius;
            double z = Math.sin(theta) * radius;
            AgingSmallFireball entity = new AgingSmallFireball(world, player, x, y, z);
            entity.setPos(player.getX(), player.getEyeY(), player.getZ());
            world.spawnEntity(entity);
        }
         */
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_empowered_dawnbreaker;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_empowered_dawnbreaker;
    }
}