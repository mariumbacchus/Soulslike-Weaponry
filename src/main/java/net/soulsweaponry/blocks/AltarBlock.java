package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.registry.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AltarBlock extends Block {

    public static final DirectionProperty FACING = FacingBlock.FACING;

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.isClient && random.nextInt(5) < 2) {
            double e = .5f;
            double d = .375f;
            double f = .375f;
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() + d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() + f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() - d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() - f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() + d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() - f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() - d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() + f + e, 0.0D, 0.0D, 0.0D);

            particleCircle(world, pos, 4);
        }
    }

    private void particleCircle(World world, BlockPos pos, int r) {
        List<Vec3d> points = new ArrayList<>();
        double y = pos.getY() + 0.2f;
        for (int theta = 0; theta < 360; theta += 2) {
            float x0 = pos.getX() + .5f;
            float z0 = pos.getZ() + .5f;
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            world.addParticle(this.getParticleType(), x, y, z, 0, 0, 0);
            if (theta % 72 == 0) {
                points.add(new Vec3d(x, y, z));
            }
        }
        particlePentagon(world, r, points, y);

        // If it works, it works. I don't give a **** that it's bad code at this point.
        HashMap<Vec3d, Vec3d> map1 = new HashMap<>();
        HashMap<Vec3d, Vec3d> map2 = new HashMap<>();
        map1.put(points.get(0), points.get(2));
        map1.put(points.get(0), points.get(3));
        map1.put(points.get(1), points.get(3));
        map1.put(points.get(1), points.get(4));
        map1.put(points.get(2), points.get(0));
        map2.put(points.get(2), points.get(4));
        map2.put(points.get(3), points.get(0));
        map2.put(points.get(3), points.get(1));
        map2.put(points.get(4), points.get(1));
        map2.put(points.get(4), points.get(2));

        particleStar(world, r, map1, y);
        particleStar(world, r, map2, y);
        /*for (int i = 0; i < points.size(); i++) {
            Vec3d start = points.get(i);
            for (int k = 0; k < points.size(); k++) {
                if (i != k) {
                    Vec3d target = points.get(k);
                    map.put(start, target);
                }
            }
        }
        particleStar(world, r, map, y);*/
    }

    private void particlePentagon(World world, int modifier, List<Vec3d> points, double y) {
        for (int i = 0; i < points.size(); i++) {
            Vec3d start = points.get(i);
            Vec3d target = i == points.size() - 1 ? points.get(0) : points.get(i + 1);
            double e = target.getX() - start.getX();
            double g = target.getZ() - start.getZ();
            double h = Math.sqrt(e * e + g * g);
            /*double x = pos.getX() + .5D;
            double z = pos.getZ() + .5D;*/
            double x = start.getX();
            double z = start.getZ();
            e /= h;
            g /= h;
            double length = 0D;
            for (int k = 0; k < 6*modifier; k++) {
                length += (double)5*modifier/100;
                world.addParticle(this.getParticleType(), x + e * length, y, z + g * length, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void particleStar(World world, int modifier, HashMap<Vec3d, Vec3d> map, double y) {
        for (Vec3d start : map.keySet()) {
            Vec3d target = map.get(start);
            double e = target.getX() - start.getX();
            double g = target.getZ() - start.getZ();
            double h = Math.sqrt(e * e + g * g);
            double x = start.getX();
            double z = start.getZ();
            e /= h;
            g /= h;
            double length = 0D;
            for (int k = 0; k < 6*modifier; k++) {
                length += 7.5D*(double)modifier/100;
                world.addParticle(this.getParticleType(), x + e * length, y, z + g * length, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private DefaultParticleType getParticleType() {
        return ParticleRegistry.NIGHTFALL_PARTICLE;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ItemRegistry.LOST_SOUL)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            ReturningKnight boss = new ReturningKnight(EntityRegistry.RETURNING_KNIGHT, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning(true);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(WeaponRegistry.DRAUGR)) {
            DraugrBoss boss = new DraugrBoss(EntityRegistry.DRAUGR_BOSS, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning();
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(ItemRegistry.ESSENCE_OF_EVENTIDE)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            Moonknight boss = new Moonknight(EntityRegistry.MOONKNIGHT, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning(true);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
