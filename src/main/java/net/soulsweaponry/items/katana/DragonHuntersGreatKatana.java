package net.soulsweaponry.items.katana;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.DragonHuntersGreatKatanaRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.DragonBonus;
import net.soulsweaponry.items.abilities.posthit.Bleed;
import net.soulsweaponry.items.abilities.stoppedusing.DragonwoundSlash;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DragonHuntersGreatKatana extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private static final DragonBonus DRAGON_BONUS = new DragonBonus(
            ConfigConstructor.dragon_hunters_great_katana_dragons_scourge_base_bonus, ConfigConstructor.dragon_hunters_great_katana_dragons_scourge_bonus_per_level
    );
    private static final Bleed BLEED = new Bleed((int) ConfigConstructor.dragon_hunters_great_katana_bleed_post_hit, ConfigConstructor.dragon_hunters_great_katana_bleed_post_hit_bonus_per_bloodthirsty_amp);
    private static final DragonwoundSlash DRAGONWOUND_SLASH = new DragonwoundSlash(
            (int) ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_projectile_max_age,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_projectile_velocity,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_projectile_base_damage,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_projectile_bonus_damage_per_level,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_explosion_damage,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_explosion_bonus_damage_per_level,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_explosion_radius,
            ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_explosion_bonus_radius_per_level,
            (int) ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_explosion_delay_ticks,
            (int) ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_min_cooldown,
            (int) ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_cooldown,
            (int) ConfigConstructor.dragon_hunters_great_katana_dragonwound_slash_reduced_cooldown_per_level
    );

    public DragonHuntersGreatKatana(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.dragon_hunters_great_katana_damage, ConfigConstructor.dragon_hunters_great_katana_attack_speed, settings);
        this.addAbility(DRAGON_BONUS, BLEED, DRAGONWOUND_SLASH);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dragon_hunters_great_katana;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DragonHuntersGreatKatanaRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DragonHuntersGreatKatanaRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
