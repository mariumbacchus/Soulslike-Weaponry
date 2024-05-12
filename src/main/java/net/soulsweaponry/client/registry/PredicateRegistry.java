package net.soulsweaponry.client.registry;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Predicate;

public class PredicateRegistry {

    public static void register() {
        /*PredicateRegistry.registerPullAndPulling(WeaponRegistry.GALEFORCE.get()); TODO weapons
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

        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SOUL_REAPER.get());
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.FORLORN_SCYTHE.get());
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRE.get());
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE.get());
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRIME.get());

        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD);
        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_SWORD);
        PredicateRegistry.registerCharged(WeaponRegistry.BLUEMOON_GREATSWORD);

        PredicateRegistry.registerNightActive(WeaponRegistry.DRAUGR.get());*/

        /*PredicateRegistry.registerPrime(WeaponRegistry.SKOFNUNG, (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Skofnung) {
                boolean emp = Skofnung.isEmpowered(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;TODO items
        });

        PredicateRegistry.registerPrime(WeaponRegistry.STING, (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Sting) {
                boolean emp = ((Sting) itemStack.getItem()).isActive(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;TODO items
        });*/

        //BossCompassPredicate.init(); TODO

        /*PredicateRegistry.registerPrime(WeaponRegistry.MASTER_SWORD, (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.isOf(WeaponRegistry.MASTER_SWORD) && livingEntity != null && livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
                return 1.0f;
            }
            return 0.0f;TODO weapons
        });*/
    }

    protected static void registerPrime(Item item, ModelPredicateProvider predicate) {
        ModelPredicateProviderRegistry.register(item, new Identifier("prime"), predicate);
    }

    protected static void registerPullAndPulling(Item item) {
        registerPulling(item);
        //registerPull(item);TODO IReducedPullTime
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

    /*protected static void registerPull(Item item) {TODO IReducedPullTime
        ModelPredicateProviderRegistry.register(item, new Identifier("pull"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (20.0F - ((IReducedPullTime)itemStack.getItem()).getReducedPullTime());
        });
    }*/

    protected static void registerThrowing(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("throwing"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    protected static void registerBetterCombatHold(Item item) {
        ModelPredicateProviderRegistry.register(item , new Identifier("bettercombat_hold"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (FMLLoader.getLoadingModList().getModFileById("bettercombat") != null) {
                return 1.0F;
            }
            return 0.0f;
        });
    }

   /*protected static void registerCharged(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charged"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (((IChargeNeeded)item).isCharged(itemStack)) { TODO IChargeNeeded
                return 1.0f;
            }
            return 0.0f;
        });
    }*/

    protected static void registerCrossbowCharged(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charged"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
    }
}
