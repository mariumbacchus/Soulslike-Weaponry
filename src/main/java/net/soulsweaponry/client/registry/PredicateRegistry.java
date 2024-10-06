package net.soulsweaponry.client.registry;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.loading.FMLLoader;
import net.soulsweaponry.items.*;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class PredicateRegistry {

    public static void register() {
        PredicateRegistry.registerPullAndPulling(WeaponRegistry.GALEFORCE.get());
        PredicateRegistry.registerPullAndPulling(WeaponRegistry.KRAKEN_SLAYER.get());
        PredicateRegistry.registerPullAndPulling(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get());
        PredicateRegistry.registerCrossbowCharged(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get());
        PredicateRegistry.registerPullAndPulling(WeaponRegistry.DARKMOON_LONGBOW.get());

        PredicateRegistry.registerThrowing(WeaponRegistry.COMET_SPEAR.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.NIGHTFALL.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.DARKIN_BLADE.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.WHIRLIGIG_SAWBLADE.get());
        PredicateRegistry.registerThrowing(GunRegistry.GATLING_GUN.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.LEVIATHAN_AXE.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.MJOLNIR.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.MOONLIGHT_GREATSWORD.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.PURE_MOONLIGHT_GREATSWORD.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAUPNIR_SPEAR.get());

        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.SOUL_REAPER.get(), "bettercombat");
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.FORLORN_SCYTHE.get(), "bettercombat");
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.DARKIN_SCYTHE_PRE.get(), "bettercombat");
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE.get(), "bettercombat");
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.DARKIN_SCYTHE_PRIME.get(), "bettercombat");
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.KRAKEN_SLAYER.get(), "epicfight");

        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD.get());
        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_SWORD.get());
        PredicateRegistry.registerCharged(WeaponRegistry.BLUEMOON_GREATSWORD.get());

        PredicateRegistry.registerNightActive(WeaponRegistry.DRAUGR.get());

        PredicateRegistry.registerPrime(WeaponRegistry.SKOFNUNG.get(), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Skofnung) {
                boolean emp = Skofnung.isEmpowered(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });

        PredicateRegistry.registerPrime(WeaponRegistry.STING.get(), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Sting) {
                boolean emp = ((Sting) itemStack.getItem()).isActive(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });

        BossCompassPredicate.init();

        PredicateRegistry.registerPrime(WeaponRegistry.MASTER_SWORD.get(), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.isOf(WeaponRegistry.MASTER_SWORD.get()) && livingEntity != null && livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
                return 1.0f;
            }
            return 0.0f;
        });
    }

    protected static void registerPrime(Item item, ModelPredicateProvider predicate) {
        ModelPredicateProviderRegistry.register(item, new Identifier("prime"), predicate);
    }

    protected static void registerPullAndPulling(Item item) {
        registerPulling(item);
        registerPull(item);
    }

    protected static void registerNightActive(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("night"), (stack, clientWorld, livingEntity, number) -> {
            if (livingEntity != null && livingEntity.world.getDimension().hasSkyLight() && livingEntity.world.getTimeOfDay() % 24000 > 13000 && livingEntity.world.getTimeOfDay() % 24000 < 23000) {
                if (stack.getEnchantments().size() > 0) {
                    return 0.5F;
                }
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }

    protected static void registerPulling(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("pulling"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    protected static void registerPull(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("pull"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) ((IShootModProjectile)itemStack.getItem()).getPullTime();
        });
    }

    protected static void registerThrowing(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("throwing"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    protected static void registerOtherModIsLoaded(Item item, String id) {
        ModelPredicateProviderRegistry.register(item , new Identifier(id), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> WeaponUtil.isModLoaded(id) ? 1f : 0f);
    }

   protected static void registerCharged(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charged"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (((IChargeNeeded)item).isCharged(itemStack)) {
                return 1.0f;
            }
            return 0.0f;
        });
    }

    protected static void registerCrossbowCharged(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
    }
}
