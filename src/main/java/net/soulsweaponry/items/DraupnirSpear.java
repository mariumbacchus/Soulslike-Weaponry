package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class DraupnirSpear extends SwordItem implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String SPEARS_ID = "thrown_spears_id";

    public DraupnirSpear(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.draupnir_spear_damage, attackSpeed, settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                int enchant = WeaponUtil.getEnchantDamageBonus(stack);
                DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (stack.hasNbt()) {
                    List<Integer> ids = new ArrayList<>();
                    if (stack.getNbt().contains(SPEARS_ID)) {
                        int[] arr = stack.getNbt().getIntArray(SPEARS_ID);
                        ids = arrayToList(arr);
                    }
                    ids.add(entity.getId());
                    stack.getNbt().putIntArray(SPEARS_ID, ids);
                }

                playerEntity.getItemCooldownManager().set(this, ConfigConstructor.draupnir_spear_throw_cooldown - enchant * 5);
                stack.damage(1, (LivingEntity)playerEntity, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(user.getActiveHand());
                });
            }
        }
    }

    public static List<Integer> arrayToList(int array[]) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.infinity").formatted(Formatting.WHITE));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.infinity_description").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears").formatted(Formatting.RED));
            for (int i = 1; i <= 5; i++) {
                if (i == 3) tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears_description_" + i).append(new LiteralText(String.valueOf(
                                ConfigConstructor.draupnir_spear_detonate_power + ((float) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) / 2.5f)))
                        .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                else tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears_description_" + i).formatted(Formatting.GRAY));
            }
            tooltip.add(new TranslatableText("tooltip.soulsweapons.detonate_spears_description_6").formatted(Formatting.DARK_GRAY));
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}