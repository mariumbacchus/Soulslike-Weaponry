package net.soulsweaponry.items.abilities.targetdeath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SoulHarvestTransform extends SoulHarvest {

    private final int maxSouls;
    private final Supplier<? extends Item> blueWeapon;
    private final Supplier<? extends Item> redWeapon;
    private final float maxBonusDamage;

    /**
     * Item that harvests souls when a mob dies post hit. Upon reaching {@link #maxSouls}, the item
     * transforms into either the {@link #blueWeapon} or {@link #redWeapon} based on whether most
     * killed mobs were ranged/passive (blue) or melee (red).
     * Each kill grants the weapon increased base damage up to {@link #maxBonusDamage}.
     * @param maxSouls max souls before reaching max bonus damage and transforming into another weapon
     * @param blueWeapon transforms to this item when most killed mobs were passive or ranged, is supplier in case the input is static (null initially)
     * @param redWeapon transforms to this item when most killed mobs were melee, is supplier in case the input is static (null initially)
     * @param maxBonusDamage max bonus damage based on collected souls
     */
    public SoulHarvestTransform(int maxSouls, Supplier<? extends Item> blueWeapon, Supplier<? extends Item> redWeapon, float maxBonusDamage) {
        this.maxSouls = maxSouls;
        this.blueWeapon = blueWeapon;
        this.redWeapon = redWeapon;
        this.maxBonusDamage = maxBonusDamage;
    }

    @Override
    public void handleKill(LivingEntity target, ItemStack stack) {
        int amount = target.getType().isIn(Tags.EntityTypes.BOSSES) ? 20 : 1;
        if (target.getType().isIn(ModTags.Entities.RANGED_MOBS) || target.getMainHandStack().getItem() instanceof RangedWeaponItem || target instanceof PassiveEntity) {
            this.addAmount(stack, amount, NbtIds.BLUE_SOULS);
        } else {
            this.addAmount(stack, amount, NbtIds.RED_SOULS);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && entity.age % 20 == 0) {
            if (this.getSouls(stack) >= this.maxSouls && slot == player.getInventory().selectedSlot) {
                if (!world.isClient) {
                    ParticleHandler.particleSphere(world, 1000, entity.getX(), entity.getY() + .1f, entity.getZ(), ParticleTypes.FLAME, 1f);
                    ParticleHandler.particleOutburstMap(world, 200, entity.getX(), entity.getY() + .1f, entity.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
                }
                world.playSound(null, entity.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT.get(), SoundCategory.HOSTILE, 0.8f, 1f);
                Item item = this.redWeapon.get();
                if (this.getDominantType(stack).equals(SoulType.BLUE)) {
                    item = this.blueWeapon.get();
                }
                ItemStack newStack = new ItemStack(item);
                WeaponUtil.copyOverItemComponents(world, stack, newStack);
                player.getInventory().removeStack(slot);
                player.getInventory().insertStack(slot, newStack);
            }
            double damage = WeaponUtil.getBaseItemAttackDamage(stack);
            double attackSpeed = WeaponUtil.getBaseItemAttackSpeed(stack);
            WeaponUtil.modifyStackAttributes(stack, damage + this.getBonusDamage(stack), attackSpeed);
        }
    }

    public void addAmount(ItemStack stack, int amount, String soulType) {
        amount += NbtHelper.getInt(stack, soulType, 0);
        NbtHelper.putInt(stack, soulType, amount);
    }

    public SoulType getDominantType(ItemStack stack) {
        int blue = NbtHelper.getInt(stack, NbtIds.BLUE_SOULS, 0);
        int red = NbtHelper.getInt(stack, NbtIds.RED_SOULS, 0);
        if (blue > red) {
            return SoulType.BLUE;
        } else {
            return SoulType.RED;
        }
    }

    public float getBonusDamage(ItemStack stack) {
        float soulPercent = (float) this.getSouls(stack) / (float) this.maxSouls;
        return this.maxBonusDamage * soulPercent;
    }

    public int getSouls(ItemStack stack) {
        int amount = 0;
        amount += NbtHelper.getInt(stack, NbtIds.BLUE_SOULS, 0);
        amount += NbtHelper.getInt(stack, NbtIds.RED_SOULS, 0);
        return amount;
    }

    @Override
    public boolean canCollectSouls() {
        return false;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.transformation").formatted(Formatting.LIGHT_PURPLE));
        for (int i = 1; i <= 7; i++) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.transformation." + i).formatted(Formatting.GRAY));
        }
        tooltip.add(Text.translatable("tooltip.soulsweapons.transformation.8").formatted(Formatting.GRAY)
                .append(Text.literal(MathHelper.floor(((float)this.getSouls(stack)/ this.maxSouls) * 100) + "%")
                        .formatted(this.getDominantType(stack).equals(SoulType.BLUE) ? Formatting.AQUA : Formatting.RED)));
        return tooltip;
    }

    public enum SoulType {
        BLUE, RED
    }
}
