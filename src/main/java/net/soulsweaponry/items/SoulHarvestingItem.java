package net.soulsweaponry.items;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.TooltipAbilities;

public abstract class SoulHarvestingItem extends ModdedSword {

    public static final String KILLS = "kills";

    public SoulHarvestingItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(TooltipAbilities.SOUL_TRAP, TooltipAbilities.COLLECT);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.isDead()) {
            this.handleKill(target, stack);
        }
        // Include dead entities hit by sweeping, Better Combat already does this so ignore if loaded
        if (attacker instanceof PlayerEntity player && !FabricLoader.getInstance().isModLoaded("bettercombat")) {
            for (LivingEntity livingEntity : player.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.0, 0.25, 1.0))) {
                if (livingEntity != player
                        && livingEntity != target
                        && !player.isTeammate(livingEntity)
                        && (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingEntity).isMarker())
                        && player.squaredDistanceTo(livingEntity) < 9.0
                        && livingEntity.isDead()) {
                    this.handleKill(livingEntity, stack);
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public void handleKill(LivingEntity target, ItemStack stack) {
        if (target.getType().isIn(ModTags.Entities.BOSSES)) {
            this.addAmount(stack, 50);
        } else {
            this.addKillCounter(stack);
        }
    }

    public void addKillCounter(ItemStack stack) {
        this.addAmount(stack, 1);
    }

    public void addAmount(ItemStack stack, int amount) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(KILLS)) {
                stack.getNbt().putInt(KILLS, stack.getNbt().getInt(KILLS) + amount);
            } else {
                stack.getNbt().putInt(KILLS, amount);
            }
        }
    }

    public int getSouls(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(KILLS)) {
            return stack.getNbt().getInt(KILLS);
        } else {
            return 0;
        }
    }
}
