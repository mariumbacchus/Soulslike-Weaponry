package net.soulsweaponry.items.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.List;

public class CustomPotionItem extends PotionItem {

    private final Potion potion;

    public CustomPotionItem(Settings settings, Potion potion) {
        super(settings);
        this.potion = potion;
    }

    public List<StatusEffectInstance> getEffects() {
        return this.potion.getEffects();
    }

    public Potion getPotion() {
        return this.potion;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!this.canUse(itemStack)) {
            return TypedActionResult.fail(itemStack);
        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (entity.hasStatusEffect(EffectRegistry.CHUNGUS_TONIC_EFFECT) || !this.canUse(stack)) {
            return ActionResult.FAIL;
        }
        for (StatusEffectInstance effectInstance : this.getEffects()) {
            entity.addStatusEffect(effectInstance);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.getWorld().playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_GENERIC_DRINK,
                SoundCategory.PLAYERS,
                0.5F,
                0.4F / (user.getWorld().getRandom().nextFloat() * 0.4F + 0.8F)
        );
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    public boolean canUse(ItemStack stack) {
        return ConfigConstructor.chungus_tonic_can_use;
    }
}
