package net.soulsweaponry.client.registry;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.items.Sting;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class PredicateRegistry {

    public static void register() {
        //TODO add weapons
        //PredicateRegistry.registerPullAndPulling(WeaponRegistry.GALEFORCE);
        //PredicateRegistry.registerPullAndPulling(WeaponRegistry.KRAKEN_SLAYER);
        //PredicateRegistry.registerPullAndPulling(WeaponRegistry.DARKMOON_LONGBOW);

        PredicateRegistry.registerThrowing(WeaponRegistry.COMET_SPEAR.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.NIGHTFALL.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.DARKIN_BLADE.get());
        //PredicateRegistry.registerThrowing(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR);
        //PredicateRegistry.registerThrowing(WeaponRegistry.WHIRLIGIG_SAWBLADE);
        //PredicateRegistry.registerThrowing(GunRegistry.GATLING_GUN);
        PredicateRegistry.registerThrowing(WeaponRegistry.LEVIATHAN_AXE.get());
        PredicateRegistry.registerThrowing(WeaponRegistry.MJOLNIR.get());
        //PredicateRegistry.registerThrowing(WeaponRegistry.MOONLIGHT_GREATSWORD);
        //PredicateRegistry.registerThrowing(WeaponRegistry.PURE_MOONLIGHT_GREATSWORD);
        PredicateRegistry.registerThrowing(WeaponRegistry.DRAUPNIR_SPEAR.get());

        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SOUL_REAPER.get());
        PredicateRegistry.registerBetterCombatHold(WeaponRegistry.FORLORN_SCYTHE.get());
        //PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRE);
        //PredicateRegistry.registerBetterCombatHold(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE);
        //PredicateRegistry.registerBetterCombatHold(WeaponRegistry.DARKIN_SCYTHE_PRIME);

        //PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD);
        //PredicateRegistry.registerCharged(WeaponRegistry.HOLY_MOONLIGHT_SWORD);

        //PredicateRegistry.registerNightActive(WeaponRegistry.DRAUGR);

        //TODO add all weapons
        /*ItemProperties.register(WeaponRegistry.SKOFNUNG, new ResourceLocation("prime"), (itemStack, clientWorld, livingEntity, number) -> {
            if (itemStack.getItem() instanceof Skofnung) {
                boolean emp = Skofnung.isEmpowered(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });*/

        ItemProperties.register(WeaponRegistry.STING.get(), new ResourceLocation("prime"), (itemStack, clientWorld, livingEntity, number) -> {
            if (itemStack.getItem() instanceof Sting) {
                boolean emp = ((Sting) itemStack.getItem()).isActive(itemStack);
                if (emp) {
                    return 1.0F;
                }
            }
            return 0.0f;
        });

        //BossCompassPredicate.init();TODO fix boss compass

        /*ItemProperties.register(WeaponRegistry.MASTER_SWORD, new ResourceLocation("prime"), (itemStack, clientWorld, livingEntity, number) -> {
            if (itemStack.is(WeaponRegistry.MASTER_SWORD) && livingEntity != null && livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
                return 1.0f;
            }
            return 0.0f;
        });*/
    }

    protected static void registerPullAndPulling(Item item) {
        registerPulling(item);
        registerPull(item);
    }

    protected static void registerPulling(Item item) {
        ItemProperties.register(item, new ResourceLocation("pulling"), (stack, clientWorld, livingEntity, number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

    protected static void registerPull(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, clientWorld, livingEntity, number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (20.0F - ((ModdedBow)stack.getItem()).getReducedPullTime());
        });//TODO Kan hende man må bruke mixin i methoden som henter pull time/status til buer i stedet for å bruke egen ModdedBow.getReducedPullTime pga compatibility
    }

    protected static void registerThrowing(Item item) {
        ItemProperties.register(item, new ResourceLocation("throwing"), (stack, clientWorld, livingEntity, number) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

    protected static void registerBetterCombatHold(Item item) {
        ItemProperties.register(item, new ResourceLocation("bettercombat_hold"), (stack, clientWorld, livingEntity, number) -> {
            if (ModList.get().isLoaded("bettercombat")) {
                return 1.0F;
            }
            return 0.0f;
        });
    }

    protected static void registerCharged(Item item) {
        ItemProperties.register(item, new ResourceLocation("charged"), (stack, clientWorld, livingEntity, number) -> {
            if (WeaponUtil.isCharged(stack)) {
                return 1.0f;//TODO add this to bluemoon swords
            }
            return 0.0f;
        });
    }

    protected static void registerNightActive(Item item) {
        ItemProperties.register(item, new ResourceLocation("night"), (stack, clientWorld, livingEntity, number) -> {
            if (livingEntity != null && livingEntity.level.dimensionType().hasSkyLight() && livingEntity.level.getDayTime() % 24000 > 13000 && livingEntity.level.getDayTime() % 24000 < 23000) {
                if (stack.getEnchantmentTags().size() > 0) {
                    return 0.5F;
                }
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }
}
