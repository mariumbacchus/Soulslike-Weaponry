package net.soulsweaponry.items;

import com.google.common.collect.Lists;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ModdedCrossbow extends CrossbowItem implements IReducedPullTime {

    private boolean loaded = false;
    private boolean charged = false;
    private static final String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";

    public ModdedCrossbow(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (CrossbowItem.isCharged(itemStack)) {
            this.shootProjectiles(world, user, hand, itemStack, this.getSpeed(itemStack, user), this.getDivergence(itemStack, user));
            CrossbowItem.setCharged(itemStack, false);
            return TypedActionResult.consume(itemStack);
        }
        if (!user.getArrowType(itemStack).isEmpty()) {
            if (!CrossbowItem.isCharged(itemStack)) {
                this.charged = false;
                this.loaded = false;
                user.setCurrentHand(hand);
            }
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = this.getModdedPullProgress(i, stack);
        if (f >= 1.0f && !CrossbowItem.isCharged(stack) && this.loadProjectiles(user, stack)) {
            CrossbowItem.setCharged(stack, true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }

    private boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, crossbow);
        int j = i == 0 ? 1 : 3;
        boolean bl = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).getAbilities().creativeMode;
        ItemStack itemStack = shooter.getArrowType(crossbow);
        ItemStack itemStack2 = itemStack.copy();
        for (int k = 0; k < j; ++k) {
            if (k > 0) {
                itemStack = itemStack2.copy();
            }
            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(Items.ARROW);
                itemStack2 = itemStack.copy();
            }
            if (this.loadProjectile(shooter, crossbow, itemStack, k > 0, bl)) continue;
            return false;
        }
        return true;
    }

    private boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) {
        ItemStack itemStack;
        boolean bl = creative && projectile.getItem() instanceof ArrowItem;
        if (projectile.isEmpty()) {
            return false;
        }
        if (!(bl || creative || simulated)) {
            itemStack = projectile.split(1);
            if (projectile.isEmpty() && shooter instanceof PlayerEntity) {
                ((PlayerEntity)shooter).getInventory().removeOne(projectile);
            }
        } else {
            itemStack = projectile.copy();
        }
        this.putProjectile(crossbow, itemStack);
        return true;
    }

    private void putProjectile(ItemStack crossbow, ItemStack projectile) {
        NbtCompound nbtCompound = crossbow.getOrCreateNbt();
        NbtList nbtList = nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) ? nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE) : new NbtList();
        NbtCompound nbtCompound2 = new NbtCompound();
        projectile.writeNbt(nbtCompound2);
        nbtList.add(nbtCompound2);
        nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
    }

    public abstract float getSpeed(ItemStack stack, PlayerEntity player);
    public abstract float getDivergence(ItemStack stack, PlayerEntity player);
    public abstract void modifyProjectile(PersistentProjectileEntity projectile, ItemStack stack);

    /**
     * This should be overwritten to put in custom projectiles.
     */
    public void shootProjectiles(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
        List<ItemStack> list = this.getProjectiles(stack);
        float[] soundPitches = this.getSoundPitches(entity.getRandom());
        boolean bl = entity instanceof PlayerEntity && ((PlayerEntity)entity).getAbilities().creativeMode;
        for (int i = 0; i < list.size(); i++) {
            ItemStack arrowStack = list.get(i);
            if (arrowStack.isEmpty()) continue;
            if (i == 0) {
                PersistentProjectileEntity projectile = this.createArrow(world, entity, stack, arrowStack);
                this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, 0.0f);
                continue;
            }
            if (i == 1) {
                PersistentProjectileEntity projectile = this.createArrow(world, entity, stack, arrowStack);
                this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, -10.0f);
                continue;
            }
            if (i != 2) continue;
            PersistentProjectileEntity projectile = this.createArrow(world, entity, stack, arrowStack);
            this.shoot(world, entity, hand, stack, projectile, arrowStack, soundPitches[i], bl, speed, divergence, 10.0f);
        }
        this.postShoot(world, entity, stack);
    }

    public void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, PersistentProjectileEntity projectile, ItemStack arrowStack, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (world.isClient) {
            return;
        }
        this.modifyProjectile(projectile, crossbow);
        this.applyPiercing(projectile, crossbow);
        projectile.setPos(shooter.getX(), shooter.getY() + 1.5F, shooter.getZ());
        projectile.setOwner(shooter);
        if (creative || simulated != 0.0f) {
            projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        if (shooter instanceof CrossbowUser) {
            CrossbowUser crossbowUser = (CrossbowUser)(shooter);
            crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectile, simulated);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternion quaternion = new Quaternion(new Vec3f(vec3d), simulated, true);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            Vec3f vec3f = new Vec3f(vec3d2);
            vec3f.rotate(quaternion);
            projectile.setVelocity(vec3f.getX(), vec3f.getY(), vec3f.getZ(), speed, divergence);
        }
        crossbow.damage(1, shooter, e -> e.sendToolBreakStatus(hand));
        world.spawnEntity(projectile);
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, soundPitch);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float)this.getModdedPullTime(stack);
            if (f < 0.2f) {
                this.charged = false;
                this.loaded = false;
            }
            if (f >= 0.2f && !this.charged) {
                this.charged = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
            if (f >= 0.5f && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
        }
    }

    public float getModdedPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float)this.getModdedPullTime(stack);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    public int getModdedPullTime(ItemStack stack) {
        return 25 - 5 * EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack) - (int) this.getReducedPullTime();
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1 -> {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            }
            case 2 -> {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            }
            case 3 -> {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            }
        }
        return SoundEvents.ITEM_CROSSBOW_LOADING_START;
    }

    public void postShoot(World world, LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            if (!world.isClient) {
                Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
            }
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }
        this.clearProjectiles(stack);
    }

    public void applyPiercing(PersistentProjectileEntity projectile, ItemStack crossbow) {
        int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
        if (i > 0) {
            projectile.setPierceLevel((byte)i);
        }
    }

    public PersistentProjectileEntity createArrow(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
        ArrowItem arrowItem = (ArrowItem)(arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, arrow, entity);
        if (entity instanceof PlayerEntity) {
            persistentProjectileEntity.setCritical(true);
        }
        persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        persistentProjectileEntity.setShotFromCrossbow(true);
        this.applyPiercing(persistentProjectileEntity, crossbow);
        return persistentProjectileEntity;
    }

    private void clearProjectiles(ItemStack crossbow) {
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null) {
            NbtList nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE);
            nbtList.clear();
            nbtCompound.put(CHARGED_PROJECTILES_KEY, nbtList);
        }
    }

    public float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0f, this.getSoundPitch(bl, random), this.getSoundPitch(!bl, random)};
    }

    private float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    public List<ItemStack> getProjectiles(ItemStack crossbow) {
        NbtList nbtList;
        ArrayList<ItemStack> list = Lists.newArrayList();
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null && nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) && (nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE)) != null) {
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                list.add(ItemStack.fromNbt(nbtCompound2));
            }
        }
        return list;
    }
}