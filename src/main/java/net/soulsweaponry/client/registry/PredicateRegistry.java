package net.soulsweaponry.client.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.items.BossCompass;
import net.soulsweaponry.items.Skofnung;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class PredicateRegistry {
    
    public static void initClient() {

        PredicateRegistry.registerePull(WeaponRegistry.GALEFORCE);
        PredicateRegistry.registerPulling(WeaponRegistry.GALEFORCE);

        PredicateRegistry.registerThrowing(WeaponRegistry.COMET_SPEAR);
        PredicateRegistry.registerThrowing(WeaponRegistry.NIGHTFALL);
        PredicateRegistry.registerThrowing(WeaponRegistry.DARKIN_BLADE);
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR);
        PredicateRegistry.registerThrowing(WeaponRegistry.WHIRLIGIG_SAWBLADE);
        PredicateRegistry.registerThrowing(GunRegistry.GATLING_GUN);
        PredicateRegistry.registerThrowing(WeaponRegistry.LEVIATHAN_AXE);
        PredicateRegistry.registerThrowing(WeaponRegistry.MJOLNIR);

        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SOUL_REAPER);
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.FORLORN_SCYTHE);

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
                boolean emp = ((Skofnung) itemStack.getItem()).isEmpowered(itemStack);
                if (emp) {
                    return 1.0F;
                }
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

    protected static void registerePull(Item item) {
        ModelPredicateProviderRegistry.register(item, new Identifier("pull"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (20.0F - 2*EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, itemStack));
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
}
