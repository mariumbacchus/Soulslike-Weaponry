package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DragonStaffProjectile;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import net.soulsweaponry.entity.projectile.WitheredWabbajackProjectile;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.Random;

public class WitheredWabbajack extends ModdedSword {

    public WitheredWabbajack(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.withered_wabbajack_damage, ConfigConstructor.withered_wabbajack_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.WABBAJACK, WeaponUtil.TooltipAbilities.LUCK_BASED);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(itemStack);
        }
        user.getItemCooldownManager().set(this, 1);
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.NEUTRAL, 0.5f, 2/(world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            Vec3d look = user.getRotationVector();
            ProjectileEntity entity = this.calculateProjectile(user, world, look, itemStack);
            if (entity instanceof DragonStaffProjectile dragonStaffProjectile) {
                dragonStaffProjectile.setRadius(2f + user.getRandom().nextFloat() * this.getLuckFactor(user));
            }
            entity.setPos(user.getX(), user.getEyeY(), user.getZ());
            if (entity instanceof GrowingFireball ball) {
                float power = 1f + user.getRandom().nextFloat() * 10 * this.getLuckFactor(user);
                int duration = user.getRandom().nextInt(10, 100 + 20 * this.getLuckFactor(user));
                float speed = user.getRandom().nextInt(25, 300 + 20 * this.getLuckFactor(user)) / 100f;
                ball.setMaxAge(duration);
                ball.setRadiusGrowth(power / (float) duration);
                ball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, speed, 0f);
            } else {
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
            }
            world.spawnEntity(entity);
            
            itemStack.damage(1, user, (p_220045_0_) -> {
                p_220045_0_.sendToolBreakStatus(hand);
            });
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    /**
     * TODO: Rewrite this.
     *
     * The new way to get a random projectile while including the status effect Luck found through
     * {@link #getLuckFactor(LivingEntity)} as a factor. <p>
     * All the objects/projectiles have an enum that says whether the projectile is benefiting or not for the user. <p>
     * It then puts all the projectiles with the {@link LuckType} given earlier with a number that represents the chance for
     * the projectile to be chosen. The more benefiting it is, the bigger the chance number. <p>
     * Lastly, it chooses the random projectile based on all the projectiles' chances by generating a number between
     * 0 and the total sum of all the chances of all the projectiles combined. It chooses the projectile by constantly
     * changing the range based on the current projectile's chance and the previous projectile's chance.
     * <p></p>
     * EDIT: Hey, past me! This looks cool, but also it sucks! Message to future me: rework it one day when you feel like it :)
     */
    private ProjectileEntity calculateProjectile(LivingEntity user, World world, Vec3d look, ItemStack stack) {
        /* 
         * Generisk oversikt over alle prosjektiler sammen med luck type, om de er bra eller dårlig.
         */
        int power = new Random().nextInt((6 + this.getLuckFactor(user)) - this.getLuckFactor(user)) + this.getLuckFactor(user);
        Object[][] projectileTypes = {
                {new ArrowEntity(world, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new DragonStaffProjectile(world, user, stack), LuckType.NEUTRAL},
                {new EggEntity(world, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new EnderPearlEntity(world, user), LuckType.BAD},
                {new ExperienceBottleEntity(world, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new FireballEntity(world, user, look.getX(), look.getY(), look.getZ(), power), LuckType.NEUTRAL},
                {new SmallFireballEntity(world, user, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new SnowballEntity(world, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new WitherSkullEntity(world, user, look.getX(), look.getY(), look.getZ()), LuckType.BAD},
                {new WitheredWabbajackProjectile(world, user, look.getX(), look.getY(), look.getZ()), LuckType.GOOD},
                {new GrowingFireball(world, user), LuckType.NEUTRAL},
        };
        ArrayList<ArrayList<Object>> projectileList = new ArrayList<>();
        for (Object[] projectileType : projectileTypes) {
            /*
             * Legger til definerte prosjektiler sammen med luck type i en ArrayList.
             */
            ArrayList<Object> list = new ArrayList<>();
            list.add(projectileType[0]);
            list.add(projectileType[1]);
            /*
             * Legger til luckFactor basert på hva slags type prosjektil den er, om den er bra eller dårlig.
             */
            int luckFactor;
            switch ((LuckType) list.get(1)) {
                case BAD -> {
                    luckFactor = (10 - this.getLuckFactor(user));
                    list.add(luckFactor);
                }
                case GOOD -> {
                    luckFactor = (10 + this.getLuckFactor(user));
                    list.add(luckFactor);
                }
                default -> {
                    luckFactor = 10;
                    list.add(luckFactor);
                }
            }
            /*
             * Hvis luckFactor er mindre eller lik 0, legges ikke prosjektilen til listen.
             */
            if (!(luckFactor <= 0)) {
                projectileList.add(list);
            }
        }

        /* 
         * Finner ut total sjansen basert på modifiserte sjanser til prosjektilene.
         */
        int totalChance = 0;
        for (ArrayList<Object> objects : projectileList) {
            totalChance += (int) objects.get(2);
        }

        /* 
         * Variabel "counter" holder tellingen på sjansene til alle prosjektilene oppover, mens "under"
         * holder tellingen på alle untatt forste, som lager boundsene til prosjektilene.
         */
        int random = new Random().nextInt(totalChance);
        int counter = 0;
        int under = 0;
        ProjectileEntity chosenProjectile = null;
        for (int i = 0; i < projectileList.size(); i++) {
            counter += (int) projectileList.get(i).get(2);
            if (i > 0) {
                under += (int) projectileList.get(i - 1).get(2);
            } 
            if (random < counter && random >= under) {
                chosenProjectile = (ProjectileEntity) projectileList.get(i).get(0);
            }

            /* 
             * Skriver ut alle boundsene for debugging.
             */
            //System.out.println(under + " || " + counter);
        }
        return chosenProjectile;
        //System.out.println("Random: " + random);
        //System.out.println("Total chance: " + totalChance);
        //System.out.println("List size: " + projectileList.size());
        //System.out.println("Chosen projectile: " + chosenProjectile);
    }

    /**
     * Gets the luck factor that will be used to calculate the chance for each projectile or
     * effect based on whether it's unlucky or not through the LuckType enum. The returned value is
     * a percent out of a hundred as a float, for example with amplifier 0 (which is effect 
     * level 1 ingame) it will increase the chance of good effects by 20% by returning 1.2f.
     * With unlucky status effect, the returned value is negative, which will have an impact
     * on the calculation later.
     */
    public int getLuckFactor(LivingEntity entity) {
        if (entity.hasStatusEffect(StatusEffects.LUCK)) {
            return entity.getStatusEffect(StatusEffects.LUCK).getAmplifier()*2 + 2;
        } else if (entity.hasStatusEffect(StatusEffects.UNLUCK)) {
            return -(entity.getStatusEffect(StatusEffects.UNLUCK).getAmplifier()*2 + 2);
        } else {
            return 0;
        }
    }

    public enum LuckType {
        GOOD, NEUTRAL, BAD
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_withered_wabbajack;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_withered_wabbajack;
    }
}
