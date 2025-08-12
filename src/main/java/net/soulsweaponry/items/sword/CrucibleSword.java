package net.soulsweaponry.items.sword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class CrucibleSword extends ModdedSword {

    public CrucibleSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.crucible_sword_normal_damage, ConfigConstructor.crucible_sword_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.DOOM);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !this.isDisabled(stack)) {
            if (!player.getItemCooldownManager().isCoolingDown(this)) {
                World world = player.getWorld();
                float cooldownMod = !world.isClient && world.getDimension().ultrawarm() ? ConfigConstructor.crucible_sword_empowered_cooldown_modifier_in_nether : 1f;
                this.applyItemCooldown(player, (int) (Math.max(ConfigConstructor.crucible_sword_empowered_min_cooldown,
                                        ConfigConstructor.crucible_sword_empowered_cooldown - this.getReduceCooldownEnchantLevel(stack) * 20)
                                        * cooldownMod));
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack) || world.isClient) {
            return;
        }
        if (entity instanceof PlayerEntity player) {
            float damage = this.getAttackDamage();
            float attackSpeed = this.getAttackSpeed();
            if (!player.getItemCooldownManager().isCoolingDown(this)) {
                damage = ConfigConstructor.crucible_sword_empowered_damage;
            }
            WeaponUtil.modifyStackAttributes(stack, damage - 1, attackSpeed);
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_crucible_sword;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.crucible_sword_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.crucible_sword_enchant_reduces_cooldown_ids;
    }
}