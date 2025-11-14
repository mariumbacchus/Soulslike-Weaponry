package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.predicate.FullSetEquipped;
import net.soulsweaponry.util.TooltipAbilities;

public abstract class SetBonusArmor extends ModdedArmor {

    public SetBonusArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
        //FullSetEquipped setEquipped = new FullSetEquipped(this.getMatchingHead(), this.getMatchingChest(), this.getMatchingLegs(), this.getMatchingBoots());
        //this.addAbility(setEquipped);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity instanceof PlayerEntity player) {
            if (false) {//this.hasFullSet(player)
                for (StatusEffectInstance instance : this.getFullSetEffects()) {
                    player.addStatusEffect(instance);
                }
                this.tickAdditionalSetEffects(stack, player);
            }
        }
    }

    protected abstract void tickAdditionalSetEffects(ItemStack stack, PlayerEntity player);
    protected abstract Item getMatchingBoots();
    protected abstract Item getMatchingLegs();
    protected abstract Item getMatchingChest();
    protected abstract Item getMatchingHead();

    /**
     * Returns a list of status effects that gets constantly applied to the user
     * and automatically documented in the tooltip of the item
     */
    public StatusEffectInstance[] getFullSetEffects() {
        return new StatusEffectInstance[0];
    }

    /**
     * Returns a list of text describing other abilities the set-bonus has, such as
     * healing from dying mobs in ForlornArmor's case
     */
    public Text[] getFullSetAbilities() {
        return new Text[0];
    }
}