package net.soulsweaponry.items.abilities.userdamaged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.DamageSourceRegistry;

import java.util.List;

public record FireThorns(float chance, float damage, int fireSeconds) implements IAbility {

    @Override
    public void onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity user, LivingEntity attacker) {
        if (user.getRandom().nextFloat() < this.chance) {
            attacker.damage(DamageSourceRegistry.create(user.getWorld(), DamageSourceRegistry.PLAYER_FIRE, user), this.damage);
            attacker.setOnFireFor(this.fireSeconds);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.firethorns").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.firethorns.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.firethorns.2").formatted(Formatting.GRAY)
        );
    }
}
