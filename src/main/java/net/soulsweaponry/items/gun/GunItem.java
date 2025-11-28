package net.soulsweaponry.items.gun;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class GunItem extends RangedWeaponItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public GunItem(Settings settings) {
        super(settings);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return IHasAbilities.super.getProjectiles();
    }

    @Override
    public int getEnchantability() {
        return 7;
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {}
}