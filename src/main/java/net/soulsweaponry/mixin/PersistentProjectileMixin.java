package net.soulsweaponry.mixin;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileMixin {

    @Accessor
    IntOpenHashSet getPiercedEntities();

    @Accessor
    List<Entity> getPiercingKilledEntities();

    @Accessor("piercingKilledEntities")
    void setPiercingKilledEntities(List<Entity> list);

    @Accessor("piercedEntities")
    void setPiercedEntities(IntOpenHashSet set);
}