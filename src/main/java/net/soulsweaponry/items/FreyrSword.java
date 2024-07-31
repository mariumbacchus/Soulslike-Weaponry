package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.FreyrSwordItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class FreyrSword extends ModdedSword implements IAnimatable, ISyncable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FreyrSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.sword_of_freyr_damage, ConfigConstructor.sword_of_freyr_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SUMMON_WEAPON);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        FreyrSwordEntity entity = new FreyrSwordEntity(world, user, stack);
        UUID uuid = entity.getUuid();
        UUID prevUuid = FreyrSwordSummonData.getSummonUuid(user);
        if (world instanceof ServerWorld serverWorld) {
            if (prevUuid == null) {
                FreyrSwordSummonData.setSummonUuid(user, uuid);
                prevUuid = uuid;
            }
            Entity sword = serverWorld.getEntity(prevUuid);
            if (sword instanceof FreyrSwordEntity) {
                return TypedActionResult.fail(stack);
            } else {
                user.getInventory().removeOne(stack);
                entity.setPos(user.getX(), user.getY(), user.getZ());
                user.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
                entity.setStationaryPos(FreyrSwordEntity.NULLISH_POS);
                world.spawnEntity(entity);
            }
            FreyrSwordSummonData.setSummonUuid(user, uuid);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {        
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private FreyrSwordItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new FreyrSwordItemRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_sword_of_freyr;
    }
}
