package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

public abstract class EntityTagBonus implements IAbility {

    private final TagKey<EntityType<?>> tag;
    @Nullable private final EntityGroup entityGroup;
    private final float baseBonusDamage;
    private final float bonusDamagePerLvl;

    public EntityTagBonus(TagKey<EntityType<?>> tag, @Nullable EntityGroup entityGroup, float baseBonusDamage, float bonusDamagePerLvl) {
        this.tag = tag;
        this.entityGroup = entityGroup;
        this.baseBonusDamage = baseBonusDamage;
        this.bonusDamagePerLvl = bonusDamagePerLvl;
    }

    public EntityTagBonus(TagKey<EntityType<?>> tag, float baseBonusDamage, float bonusDamagePerLvl) {
        this.tag = tag;
        this.entityGroup = null;
        this.baseBonusDamage = baseBonusDamage;
        this.bonusDamagePerLvl = bonusDamagePerLvl;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (target.getType().isIn(this.tag) || (this.entityGroup != null && target instanceof LivingEntity livingEntity && livingEntity.getGroup().equals(this.entityGroup))) {
            if (damageSource.getAttacker() instanceof PlayerEntity player) {
                ItemStack stack = player.getMainHandStack();
                return this.getTotalBonus(stack);
            }
        }
        return 0;
    }

    public float getTotalBonus(ItemStack stack) {
        return this.baseBonusDamage + this.bonusDamagePerLvl * WeaponUtil.getUpgradeLevel(stack);
    }
}
