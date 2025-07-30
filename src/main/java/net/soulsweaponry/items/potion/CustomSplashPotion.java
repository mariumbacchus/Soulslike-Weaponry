package net.soulsweaponry.items.potion;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomSplashPotion extends CustomPotionItem {

    private final int splashParticleColor;

    /**
     * @param settings item settings
     * @param potion potion
     * @param splashParticleColor If it equals -1 => random color, otherwise just the input
     */
    public CustomSplashPotion(Settings settings, Potion potion, int splashParticleColor) {
        super(settings, potion);
        this.splashParticleColor = splashParticleColor;
    }
    //TODO consider adding a mixin that changes the splash color for chungus potion only and predicates for the
    // potion item so if it has chungus tonic effect just change that model instead of doing all this nonsense

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        //itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(this.getPotion(), Optional.of(this.splashParticleColor == -1 ? randomVibrantRGBA() : this.splashParticleColor), List.of()))
        if (!this.canUse(itemStack)) {
            return TypedActionResult.fail(itemStack);
        }
        if (!world.isClient) {
            PotionEntity potionEntity = new PotionEntity(world, user);
            potionEntity.setItem(itemStack);
            potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
            world.spawnEntity(potionEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SPLASH_POTION_THROW,
                SoundCategory.PLAYERS,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            potionContentsComponent.buildTooltip(tooltip::add, 0.86f, context.getUpdateTickRate());
        }
    }

    public static int randomVibrantRGBA() {
        float h = ThreadLocalRandom.current().nextFloat();
        float s = 0.65f + ThreadLocalRandom.current().nextFloat() * 0.35f; // 0.65–1.0
        float v = 0.75f + ThreadLocalRandom.current().nextFloat() * 0.25f; // 0.75–1.0
        int rgb = Color.HSBtoRGB(h, s, v); // to hex
        return 0xFF000000 | rgb; // full alpha
    }
}
