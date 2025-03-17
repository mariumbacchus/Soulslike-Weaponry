package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class DamagingNoClipEntity extends NoClipEntity {

    private final Set<UUID> entitiesHit = new HashSet<>();
    public final int maxAge;

    public DamagingNoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.maxAge = 100;
    }

    public DamagingNoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner, int maxAge) {
        super(entityType, world);
        this.maxAge = maxAge;
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        // NOTE: This is needed because no-clip makes the entity rotate in weird ways
        this.setPitch(0f);
        this.setYaw(0f);
        super.tick();
        this.setPitch(0f);
        this.setYaw(0f);
        if (!this.getWorld().isClient) {
            List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.4D));
            for (LivingEntity living : list) {
                if (this.isOwner(living) || this.entitiesHit.contains(living.getUuid())) {
                    continue;
                }
                this.updateEntityDamage(living);
                boolean wasHit;
                if (this.getOwner() instanceof LivingEntity) {
                    wasHit = living.damage(this.getWorld().getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), (float) this.getDamage());
                } else {
                    wasHit = living.damage(this.getWorld().getDamageSources().mobProjectile(this, null), (float) this.getDamage());
                }
                this.applyDamageEffects(wasHit, living);
                this.entitiesHit.add(living.getUuid());
            }
        }
        if (this.age > this.maxAge) {
            this.discard();
        }
    }

    /**
     * Apply custom effects after damage has been dealt, like adding effects.
     */
    public abstract void applyDamageEffects(boolean wasHit, LivingEntity target);

    public void updateEntityDamage(LivingEntity target) {
        this.setDamage(this.getDamage() + EnchantmentHelper.getAttackDamage(this.getStack(), target.getGroup()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("EntitiesHit", 9)) {
            NbtList list = nbt.getList("EntitiesHit", 10);
            this.entitiesHit.clear();
            for (int i = 0; i < list.size(); ++i) {
                NbtCompound tag = list.getCompound(i);
                this.entitiesHit.add(tag.getUuid("UUID"));
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtList list = new NbtList();
        for (UUID uuid : this.entitiesHit) {
            list.add(this.saveUuid(uuid));
        }
        nbt.put("EntitiesHit", list);
    }

    private NbtCompound saveUuid(UUID uuid) {
        NbtCompound tag = new NbtCompound();
        tag.putUuid("UUID", uuid);
        return tag;
    }
}