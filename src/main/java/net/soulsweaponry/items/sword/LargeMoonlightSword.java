package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.LargeMoonlightSwordRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.OnlyMagicDamage;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;
import net.soulsweaponry.items.abilities.posthit.ApplyMagicFrailty;
import net.soulsweaponry.items.abilities.use.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class LargeMoonlightSword extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final OnlyMagicDamage MOONLIT = new OnlyMagicDamage();
    private static final UndeadBonus UNDEAD_BONUS = new UndeadBonus(
            ConfigConstructor.large_sword_of_moonlight_righteous_base_undead_bonus_damage,
            ConfigConstructor.large_sword_of_moonlight_righteous_undead_bonus_damage_per_level
    );
    private static final Block BLOCK = new Block(ConfigConstructor.large_sword_of_moonlight_magic_damage_reduction_when_blocking);
    private static final ApplyMagicFrailty APPLY_MAGIC_FRAILTY = new ApplyMagicFrailty(
            (int) ConfigConstructor.large_sword_of_moonlight_magic_frailty_duration,
            ConfigConstructor.large_sword_of_moonlight_magic_frailty_duration_per_level,
            (int) ConfigConstructor.large_sword_of_moonlight_magic_frailty_amp,
            ConfigConstructor.large_sword_of_moonlight_magic_frailty_amp_per_level
    );

    public LargeMoonlightSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.large_sword_of_moonlight_damage, ConfigConstructor.large_sword_of_moonlight_attack_speed, settings);
        this.addAbility(MOONLIT, APPLY_MAGIC_FRAILTY, UNDEAD_BONUS, BLOCK);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_large_sword_of_moonlight;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private LargeMoonlightSwordRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new LargeMoonlightSwordRenderer();

                return this.renderer;
            }
        });
    }
}
