package net.soulsweaponry.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModdedSword extends SwordItem implements IConfigDisable, ICooldownItem, ITooltipInfo {

    protected final float attackSpeed;
    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedSword(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, - (4f - ingameAttackSpeed), settings);
        this.attackSpeed = - (4f - ingameAttackSpeed);
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public List<TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    @Override
    public void addTooltipAbility(TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, world, tooltip, context);
        super.appendTooltip(stack, world, tooltip, context);
    }

    public void notifyCooldown(LivingEntity user) {
        if (!ConfigConstructor.inform_player_about_cooldown_effect) {
            return;
        }
        if (user instanceof PlayerEntity player) {
            player.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.on_cooldown","Can't cast this ability with the Cooldown effect!"), true);
        } else {
            user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.on_cooldown","Can't cast this ability with the Cooldown effect!"));
        }
    }

    @Override
    public abstract boolean isFireproof();
}