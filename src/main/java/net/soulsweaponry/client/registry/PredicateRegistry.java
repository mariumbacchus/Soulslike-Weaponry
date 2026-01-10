package net.soulsweaponry.client.registry;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.IHasEssence;
import net.soulsweaponry.items.abilities.ISharpened;
import net.soulsweaponry.items.abilities.inventorytick.Luminate;
import net.soulsweaponry.items.abilities.use.InvisibleItem;
import net.soulsweaponry.items.misc.BossCompass;
import net.soulsweaponry.items.sword.Skofnung;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public class PredicateRegistry {

    //TODO ikke glem custom resource packs!!!!
    public static void initClient() {
        // TODO there is a new predicate pipeline now entirely done with components and jsons
        // predicates for items now exist in soulsweapons/items instead of within the item model inside soulsweapons/models/items
        // an example of one handled is comet_spear
        // do this for the rest of the items
        // TODO those commented out are done but may not be tested yet
        PredicateRegistry.registerPulling(WeaponRegistry.KRAKEN_SLAYER);//TODO disse er krevende...
        PredicateRegistry.registerOtherModIsLoaded(WeaponRegistry.KRAKEN_SLAYER, "epicfight"); //TODO del av over!!

        PredicateRegistry.registerCrossbowCharged(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW); //TODO krevende...
        PredicateRegistry.registerThrowing(WeaponRegistry.MOONLIGHT_GREATSWORD);//TODO 2d
        PredicateRegistry.registerThrowing(WeaponRegistry.PURE_MOONLIGHT_GREATSWORD);//TODO 2d

        ModelPredicateProviderRegistry.register(ItemRegistry.BOSS_COMPASS, Identifier.of("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> {
            if (stack.isOf(ItemRegistry.BOSS_COMPASS)) {
                BossCompass item = (BossCompass) stack.getItem();
                return item.getStructurePos(world, stack);
            }
            return null;
        }));

        ModelPredicateProviderRegistry.register(WeaponRegistry.TONITRUS, Identifier.of("charged"), (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int number) -> {
            if (livingEntity != null && livingEntity.hasStatusEffect(EffectRegistry.STORMVEIL)) {//TODO gjorde om til "stormveil_active", gjelder 2d
                return 1f;
            }
            return 0.0f;
        });

        registerChungusPotion(Items.POTION);
        registerChungusPotion(Items.SPLASH_POTION);
        registerChungusPotion(Items.LINGERING_POTION);
        registerChungusPotion(Items.TIPPED_ARROW);
    }

    private static void registerChungusPotion(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.of("chungus_tonic"), (itemStack, clientWorld, livingEntity, seed) -> {
            PotionContentsComponent contents = itemStack.get(DataComponentTypes.POTION_CONTENTS);
            return contents != null && contents.matches(EffectRegistry.CHUNGUS_TONIC_POTION) ? 1f : 0f;
        });
    }
}
