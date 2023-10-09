package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.FlamePillar;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Also known as "Genesis Fracture"
 */
public class EmpoweredDawnbreaker extends AbstractDawnbreaker {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public EmpoweredDawnbreaker(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.empowered_dawnbreaker_damage, attackSpeed, settings);
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

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                this.summonFlamePillars(world, stack, user);
                player.getItemCooldownManager().set(this, ConfigConstructor.empowered_dawnbreaker_ability_cooldown);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0));
            }
        }
    }

    private void summonFlamePillars(World world, ItemStack stack, LivingEntity user) {
        if (!world.isClient) {
            int i = 0;
            List<BlockPos> list = new ArrayList<>();
            list.add(new BlockPos(0, 0, 0));
            while (i < 20 + 3 * WeaponUtil.getEnchantDamageBonus(stack)) {
                int x = user.getBlockX() + user.getRandom().nextInt(12) - 6;
                int y = user.getBlockY();
                int z = user.getBlockZ() + user.getRandom().nextInt(12) - 6;
                BlockPos pos = new BlockPos(x, y, z);
                for (BlockPos listPos : list) {
                    if (listPos != pos) {
                        FlamePillar pillar = new FlamePillar(EntityRegistry.FLAME_PILLAR, world);
                        pillar.setDamage(ConfigConstructor.empowered_dawnbreaker_ability_damage + WeaponUtil.getEnchantDamageBonus(stack) * 2);
                        pillar.setPos(x, y, z);
                        pillar.setOwner(user);
                        pillar.setWarmup(i * 2);
                        world.spawnEntity(pillar);
                        i++;
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DAWNBREAKER, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.BLAZING_BLADE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHAOS_STORM, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
