package net.soulsweaponry.registry;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.soulsweaponry.items.misc.LoreItem;

import static net.soulsweaponry.registry.ItemRegistry.registerItem;

public class FoodRegistry {

    public static final FoodComponent DEMON_HEART_FOOD = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(6f)
            .alwaysEdible()
            .build();

    public static final ConsumableComponent DEMON_HEART_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 150, 0),
                    1.0f
            ))
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 150, 0),
                    1
            ))
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0),
                    1.0f
            ))
            .build();

    public static final Item DEMON_HEART = registerItem(
            "demon_heart",
            settings -> new LoreItem(settings.food(DEMON_HEART_FOOD, DEMON_HEART_CONSUMABLE), 3)
    );

    public static void init() {}
}
