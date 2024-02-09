package net.soulsweaponry.client.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.items.*;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class PredicateRegistry {
    
    public static void initClient() {

        PredicateRegistry.registerPull(WeaponRegistry.GALEFORCE);
        PredicateRegistry.registerPulling(WeaponRegistry.GALEFORCE);
        PredicateRegistry.registerPull(WeaponRegistry.KRAKEN_SLAYER);
        PredicateRegistry.registerPulling(WeaponRegistry.KRAKEN_SLAYER);
        PredicateRegistry.registerPull(WeaponRegistry.DARKMOON_LONGBOW);
        PredicateRegistry.registerPulling(WeaponRegistry.DARKMOON_LONGBOW);

        PredicateRegistry.registerThrowing(WeaponRegistry.COMET_SPEAR);
        PredicateRegistry.registerThrowing(WeaponRegistry.NIGHTFALL);
        PredicateRegistry.registerThrowing(WeaponRegistry.DARKIN_BLADE);
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR);
        PredicateRegistry.registerThrowing(WeaponRegistry.WHIRLIGIG_SAWBLADE);
        PredicateRegistry.registerThrowing(GunRegistry.GATLING_GUN);
        PredicateRegistry.registerThrowing(WeaponRegistry.LEVIATHAN_AXE);
        PredicateRegistry.registerThrowing(WeaponRegistry.MJOLNIR);
        PredicateRegistry.registerThrowing(WeaponRegistry.MOONLIGHT_GREATSWORD);
        PredicateRegistry.registerThrowing(WeaponRegistry.PURE_MOONLIGHT_GREATSWORD);
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAUPNIR_SPEAR);

        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SOUL_REAPER);
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.FORLORN_SCYTHE);
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRE);
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRIME);
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE);

        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD);
        PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_SWORD);
        PredicateRegistry.registerCharged(WeaponRegistry.BLUEMOON_GREATSWORD);

        ModelPredicateProviderRegistry.register(WeaponRegistry.DRAUGR, new Identifier("night"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity != null && livingEntity.world.getDimension().hasSkyLight() && livingEntity.world.getTimeOfDay() % 24000 > 13000 && livingEntity.world.getTimeOfDay() % 24000 < 23000) {
                if (itemStack.getEnchantments().size() > 0) {
                    return 0.5F;
                }
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ModelPredicateProviderRegistry.register(ItemRegistry.BOSS_COMPASS, new Identifier("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> {
            if (stack.isOf(ItemRegistry.BOSS_COMPASS)) {
                BossCompass item = (BossCompass) stack.getItem();
                return item.getStructurePos(world, stack);
            }
            return null;
        }));

        ModelPredicateProviderRegistry.register(WeaponRegistry.SKOFNUNG, new Identifier("prime"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Skofnung) {
                boolean emp = Skofnung.isEmpowered(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });

        ModelPredicateProviderRegistry.register(WeaponRegistry.STING, new Identifier("prime"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.getItem() instanceof Sting) {
                boolean emp = ((Sting) itemStack.getItem()).isActive(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });

        ModelPredicateProviderRegistry.register(WeaponRegistry.MASTER_SWORD, new Identifier("prime"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (itemStack.isOf(WeaponRegistry.MASTER_SWORD) && livingEntity != null && livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
                return 1.0f;
            }
            return 0.0f;
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
            return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (20.0F - ((ModdedBow)itemStack.getItem()).getReducedPullTime());
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

    protected static void registerBetterCombatHold(Item item) {
        ModelPredicateProviderRegistry.register(item , new Identifier("bettercombat_hold"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (FabricLoader.getInstance().isModLoaded("bettercombat")) {
                return 1.0F;
            }
            return 0.0f;
        });
    }

    protected static void registerCharged(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("charged"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (((IChargeNeeded)item).isCharged(itemStack)) {
                return 1.0f;
            }
            return 0.0f;
        });
    }
}
