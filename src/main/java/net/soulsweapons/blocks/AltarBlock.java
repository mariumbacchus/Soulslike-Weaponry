package net.soulsweapons.blocks;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.soulsweapons.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AltarBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public AltarBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (world.isClientSide && random.nextInt(5) < 2) {
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

    private void particleCircle(Level world, BlockPos pos, int r) {
        List<Vector3d> points = new ArrayList<>();
        double y = pos.getY() + 0.2f;
        for (int theta = 0; theta < 360; theta += 2) {
            float x0 = pos.getX() + .5f;
            float z0 = pos.getZ() + .5f;
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            world.addParticle(this.getParticleType(), x, y, z, 0, 0, 0);
            if (theta % 72 == 0) {
                points.add(new Vector3d(x, y, z));
            }
        }
        particlePentagon(world, r, points, y);

        // If it works, it works. I don't give a **** that it's bad code at this point.
        HashMap<Vector3d, Vector3d> map1 = new HashMap<>();
        HashMap<Vector3d, Vector3d> map2 = new HashMap<>();
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
            Vector3d start = points.get(i);
            for (int k = 0; k < points.size(); k++) {
                if (i != k) {
                    Vector3d target = points.get(k);
                    map.put(start, target);
                }
            }
        }
        particleStar(world, r, map, y);*/
    }

    private void particlePentagon(Level world, int modifier, List<Vector3d> points, double y) {
        for (int i = 0; i < points.size(); i++) {
            Vector3d start = points.get(i);
            Vector3d target = i == points.size() - 1 ? points.get(0) : points.get(i + 1);
            double e = target.x - start.x;
            double g = target.z - start.z;
            double h = Math.sqrt(e * e + g * g);
            /*double x = pos.x + .5D;
            double z = pos.z + .5D;*/
            double x = start.x;
            double z = start.z;
            e /= h;
            g /= h;
            double length = 0D;
            for (int k = 0; k < 6*modifier; k++) {
                length += (double)5*modifier/100;
                world.addParticle(this.getParticleType(), x + e * length, y, z + g * length, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void particleStar(Level world, int modifier, HashMap<Vector3d, Vector3d> map, double y) {
        for (Vector3d start : map.keySet()) {
            Vector3d target = map.get(start);
            double e = target.x - start.x;
            double g = target.z - start.z;
            double h = Math.sqrt(e * e + g * g);
            double x = start.x;
            double z = start.z;
            e /= h;
            g /= h;
            double length = 0D;
            for (int k = 0; k < 6*modifier; k++) {
                length += 7.5D*(double)modifier/100;
                world.addParticle(this.getParticleType(), x + e * length, y, z + g * length, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private ParticleOptions getParticleType() {
        return ParticleTypes.SOUL_FIRE_FLAME;//ParticleRegistry.NIGHTFALL_PARTICLE; TODO add particles here
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemRegistry.LOST_SOUL.get())) {
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }//TODO add bosses here
//            ReturningKnight boss = new ReturningKnight(EntityRegistry.RETURNING_KNIGHT, world);
//            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
//            boss.setSpawning(true);
//            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
//            world.spawnEntity(boss);
//            world.removeBlock(pos, false);
//            return ActionResult.SUCCESS;
//        } else if (itemStack.isOf(WeaponRegistry.DRAUGR)) {
//            DraugrBoss boss = new DraugrBoss(EntityRegistry.DRAUGR_BOSS, world);
//            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
//            boss.setSpawning();
//            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
//            world.spawnEntity(boss);
//            world.removeBlock(pos, false);
//            return ActionResult.SUCCESS;
//        } else if (itemStack.isOf(ItemRegistry.ESSENCE_OF_EVENTIDE)) {
//            if (!player.getAbilities().creativeMode) {
//                itemStack.decrement(1);
//            }
//            Moonknight boss = new Moonknight(EntityRegistry.MOONKNIGHT, world);
//            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
//            boss.setSpawning(true);
//            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
//            world.spawnEntity(boss);
//            world.removeBlock(pos, false);
//            return ActionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
