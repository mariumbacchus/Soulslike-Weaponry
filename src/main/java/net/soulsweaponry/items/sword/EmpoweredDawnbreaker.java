package net.soulsweaponry.items.sword;

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
import net.soulsweaponry.client.renderer.item.EmpoweredDawnbreakerRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.noclip.DamagingWarmupEntityEvents;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Also known as "Genesis Fracture"
 */
public class EmpoweredDawnbreaker extends AbstractDawnbreaker implements IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public EmpoweredDawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.empowered_dawnbreaker_damage, ConfigConstructor.empowered_dawnbreaker_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.CHAOS_STORM, TooltipAbilities.VEIL_OF_FIRE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            int i = WeaponUtil.getChargeTime(stack, remainingUseTicks);
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
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.empowered_dawnbreaker_ability_enchant_reduces_cooldown_ids;
    }

    protected int getScaledCooldown(ItemStack stack) {
        int base = (int) ConfigConstructor.empowered_dawnbreaker_ability_cooldown;
        return (int) Math.max(ConfigConstructor.empowered_dawnbreaker_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 40);
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
                        FlamePillar pillar = new FlamePillar(world, user, 1.5f, i * 2, DamagingWarmupEntityEvents.SPAWN_FIRE);
                        pillar.setDamage(ConfigConstructor.empowered_dawnbreaker_ability_damage + WeaponUtil.getEnchantDamageBonus(stack) * 2);
                        pillar.setPos(x, y, z);
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
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final EmpoweredDawnbreakerRenderer renderer = new EmpoweredDawnbreakerRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            AbstractDawnbreaker.dawnbreakerEvent(player, player, stack);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.VEIL_OF_FIRE, 200, MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
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
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
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