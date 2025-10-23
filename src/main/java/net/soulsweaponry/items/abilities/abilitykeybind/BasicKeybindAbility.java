package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.soulsweaponry.util.WeaponUtil;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

/**
 * Template for a simple keybind ability such as just adding an effect (server side) and playing a sound effect (client side).
 * Will not trigger if the player has {@link net.soulsweaponry.registry.EffectRegistry#COOLDOWN} effect.
 * @param serverEffects server side effects, such as spawning entities
 * @param clientEffects client side effects, such as particles or sounds
 * @param tooltips tooltips of the ability
 * @param stackDamage
 * @param minCooldown
 * @param cooldown
 * @param reducedCooldownPerLvl
 */
public record BasicKeybindAbility(
        TriConsumer<ServerWorld, ItemStack, PlayerEntity> serverEffects,
        TriConsumer<ClientWorld, ItemStack, PlayerEntity> clientEffects,
        List<Text> tooltips, int stackDamage, int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.hasCooldownEffect(player)) {
            this.notifyCooldown(player);
            return;
        }
        this.serverEffects.accept(world, stack, player);
        if (!player.isCreative()) {
            stack.damage(this.stackDamage, player, WeaponUtil.getActiveHandSlot(player));
            this.applyEffectCooldown(player, Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl));
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
        if (this.hasCooldownEffect(player)) {
            return;
        }
        this.clientEffects.accept(world, stack, player);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltips;
    }
}
