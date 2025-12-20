package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ShootChungusHeads(double speed, int ticksBeforeExplode, int cooldown) implements IAbility {

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        TntEntity tnt = EntityRegistry.CHUNGUS_HEAD.create(world, SpawnReason.SPAWN_ITEM_USE);
        assert tnt != null;
        tnt.setPos(user.getX(), user.getEyeY() - 0.4f, user.getZ());
        tnt.noClip = true;
        Vec3d look = user.getRotationVec(1.0F);
        double speed = this.speed;
        tnt.setVelocity(look.x * speed, look.y * speed, look.z * speed);
        tnt.setPitch(user.getPitch());
        tnt.setYaw(user.getYaw());
        tnt.setFuse(this.ticksBeforeExplode);
        tnt.setNoGravity(true);
        world.spawnEntity(tnt);
        this.applyItemCooldown(stack, user, this.cooldown);
        stack.damage(3, user, WeaponUtil.getActiveHandSlot(user));
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chungus_explosion").formatted(Formatting.LIGHT_PURPLE),
                Text.translatable("tooltip.soulsweapons.chungus_explosion.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.chungus_explosion.2", this.ticksBeforeExplode).formatted(Formatting.GRAY)
        );
    }
}
