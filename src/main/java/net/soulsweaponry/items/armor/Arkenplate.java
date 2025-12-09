package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Arkenplate extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Arkenplate(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.UNBREAKABLE, TooltipAbilities.AFTERSHOCK);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && player.getHealth() <= player.getMaxHealth() / 2f && this.isSlotActive(player, EquipmentSlot.CHEST)) {
            ItemStack chest = player.getInventory().getArmorStack(2);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1, false, false));
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE.get(), 40, 1, false, false));
            if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(chest.getItem()) && player.getAttacker() != null) {
                this.shockwave(world, player);
            }
        }
    }

    private void shockwave(World world, PlayerEntity player) {
        float i = ConfigConstructor.arkenplate_shockwave_knockback;
        ItemStack stack = player.getInventory().getArmorStack(2);
        if (stack == null) return;
        i += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
        ParticleHandler.singleParticle(world, ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getBodyY(0.5D), player.getZ(), 0, 0, 0);
        for (Entity entity : world.getOtherEntities(player, player.getBoundingBox().expand(5D))) {
            if (entity instanceof LivingEntity target && !target.isTeammate(player)) {
                if (this.equals(ArmorRegistry.ENHANCED_ARKENPLATE)) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 2));
                }
                target.damage(player.getDamageSources().mobAttack(player), ConfigConstructor.arkenplate_shockwave_damage);
                double x = player.getX() - target.getX();
                double z = player.getZ() - target.getZ();
                target.takeKnockback(i * 0.5f, x, z);
            }
        }
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
        if (!player.isCreative()) {
            player.getItemCooldownManager().set(stack.getItem(), (int) Math.max(ConfigConstructor.arkenplate_shockwave_min_cooldown, ConfigConstructor.arkenplate_shockwave_cooldown
                    - this.getReduceCooldownEnchantLevel(stack) * 20));
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_arkenplate;
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.getItem() instanceof Arkenplate;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_arkenplate;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.arkenplate_shockwave_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.arkenplate_shockwave_enchant_reduces_cooldown_ids;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosArmorRenderer<>();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    public PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        return PlayState.CONTINUE;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.chaos_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.chaos_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.chaos_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.chaos_armor_bleed_damage_resistances;
    }
}