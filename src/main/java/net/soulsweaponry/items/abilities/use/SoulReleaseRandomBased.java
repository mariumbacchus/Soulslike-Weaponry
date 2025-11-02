package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;

import java.util.List;
import java.util.NavigableMap;

public record SoulReleaseRandomBased(
        int maxSummons, String summonListId, NavigableMap<Integer, EntityType<?>> entityPowerMap,
        float bonusHealthPerPower, float bonusHealthIncreasePerLvl, float maxBonusHealth,
        float bonusAttackPerPower, float bonusAttackIncreasePerLvl, float maxBonusAttack
) implements ISoulHarvest, ISummonAlliesAbility {

    private static final SoulReleaseRandomBased SOUL_RELEASE_RANDOM_BASED = new SoulReleaseRandomBased(
            ConfigConstructor.frostmourne_summoned_allies_cap,
            "FrostmourneSummons"
    );



    @Override
    public int getMaxSummons() {
        return this.maxSummons;
    }

    @Override
    public String getSummonsListId() {
        return this.summonListId;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
