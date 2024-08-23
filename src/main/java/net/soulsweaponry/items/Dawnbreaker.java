package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.DawnbreakerRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Dawnbreaker extends AbstractDawnbreaker {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Dawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.dawnbreaker_damage, ConfigConstructor.dawnbreaker_attack_speed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {}

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_dawnbreaker;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DawnbreakerRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DawnbreakerRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dawnbreaker;
    }
}