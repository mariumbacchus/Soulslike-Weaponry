package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.posthit.DawnbreakerExplosion;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VeilOfFireAbility extends DawnbreakerExplosion implements IKeybindAbility {

    private final int veilOfFireDuration;
    private final float veilOfFireBonusDurationPerLvl;
    private final int veilOfFireAmp;
    private final float veilOfFireBonusAmpPerLvl;
    private final int minCooldown;
    private final int cooldown;
    private final int reducedCooldownPerLvl;

    /**
     * Apply {@link EffectRegistry#RETRIBUTION} effect post hit. Each amp
     * increases the chance for the Dawnbreaker explosion, mainly targeting
     * undead mobs, unless boolean allows it.
     * <p>
     * Pressing ability keybind triggers the Dawnbreaker explosion and grants
     * the user {@link EffectRegistry#VEIL_OF_FIRE} which damages and sets
     * nearby mobs on fire.
     *
     * @param affectAllEntities        whether the explosion should target all entities or only undead
     * @param baseAmpPostHit           base Retribution amp
     * @param bonusAmpPerLvl           bonus Retribution amp
     * @param detonationChanceAddition bonus chance for detonation, an addition to base chance
     * @param explosionRange           explosion range
     * @param baseFireSeconds          base fire seconds applied to mobs hit by the explosion
     * @param bonusSecondsPerLvl       bonus fire seconds applied to mobs hit by the explosion
     * @param explosionDamage          damage
     * @param bonusDamagePerLvl        bonus damage per level
     * @param fearEffectDuration       fear effect is applied to mobs hit by the explosion
     */
    public VeilOfFireAbility(boolean affectAllEntities, int baseAmpPostHit, float bonusAmpPerLvl,
                             double detonationChanceAddition, float explosionRange, float baseFireSeconds,
                             float bonusSecondsPerLvl, float explosionDamage, float bonusDamagePerLvl,
                             int fearEffectDuration, int veilOfFireDuration, float veilOfFireBonusDurationPerLvl,
                             int veilOfFireAmp, float veilOfFireBonusAmpPerLvl,
                             int minCooldown, int cooldown, int reducedCooldownPerLvl
    ) {
        super(affectAllEntities, baseAmpPostHit, bonusAmpPerLvl, detonationChanceAddition, explosionRange,
                baseFireSeconds, bonusSecondsPerLvl, explosionDamage, bonusDamagePerLvl, fearEffectDuration);
        this.veilOfFireDuration = veilOfFireDuration;
        this.veilOfFireBonusDurationPerLvl = veilOfFireBonusDurationPerLvl;
        this.veilOfFireAmp = veilOfFireAmp;
        this.veilOfFireBonusAmpPerLvl = veilOfFireBonusAmpPerLvl;
        this.minCooldown = minCooldown;
        this.cooldown = cooldown;
        this.reducedCooldownPerLvl = reducedCooldownPerLvl;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.veilOfFireDuration + this.veilOfFireBonusDurationPerLvl * lvl);
            int amp = (int) (this.veilOfFireAmp + this.veilOfFireBonusAmpPerLvl * lvl);
            this.dawnbreakerEvent(world, player, player, stack);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.VEIL_OF_FIRE, duration, amp));
            this.applyItemCooldown(stack.getItem(), player, this.getScaledCooldown(stack));
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

    private int getScaledCooldown(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * WeaponUtil.getUpgradeLevel(stack));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>(super.getTooltipAbilities(stack));
        tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire").formatted(Formatting.GOLD).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire.2").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.veil_of_fire.3").formatted(Formatting.GRAY));
        return tooltip;
    }
}
