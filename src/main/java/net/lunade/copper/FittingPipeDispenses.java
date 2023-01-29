package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class FittingPipeDispenses {

    private static final ArrayList<ItemConvertible> items = new ArrayList<>();
    private static final ArrayList<FittingDispense> dispenses = new ArrayList<>();

    public static void register(ItemConvertible item, FittingDispense dispense) {
        if (!items.contains(item)) {
            items.add(item);
            dispenses.add(dispense);
        } else {
            dispenses.set(items.indexOf(item), dispense);
        }
    }

    @Nullable
    public static FittingDispense getDispense(ItemConvertible item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return dispenses.get(index);
        }
        return null;
    }

    @FunctionalInterface
    public interface FittingDispense {
        void dispense(ServerWorld world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }

    public static double getYOffset(Direction.Axis axis, double e) {
        if (axis == Direction.Axis.Y) {
            return e - 0.125D;
        } else {
            return e - 0.15625D;
        }
    }

    public static double getRandomA(Random random) {
        return (random.nextDouble() * 6) - 3;
    }

    public static double getRandomB(Random random) {
        return (random.nextDouble() * 7) - 3.5;
    }

    public static double getVelX(Direction.Axis axis, int offX, int i, boolean corroded, double random1, double random2) {
        return axis == Direction.Axis.X ? (i * offX) * 2 : (axis == Direction.Axis.Z ? corroded ? random2 : random2 * 0.1 : corroded ? random1 : random1 * 0.1);
    }

    public static double getVelY(Direction.Axis axis, int offY, int i, boolean corroded, double random1) {
        return axis == Direction.Axis.Y ? (i * offY) * 2 : corroded ? random1 : random1 * 0.1;
    }

    public static double getVelZ(Direction.Axis axis, int offZ, int i, boolean corroded, double random2) {
        return axis == Direction.Axis.Z ? (i * offZ) * 2 : corroded ? random2 : random2 * 0.1;
    }

    public static UniformIntProvider uniformInt3() {
        return UniformIntProvider.create(-3, 3);
    }

    public static UniformIntProvider uniformInt1() {
        return UniformIntProvider.create(-1, 1);
    }

    public static void init() {
        register(Items.INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandomB(random);
            double random2 = getRandomB(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            if (state.getBlock() instanceof CopperPipe copperPipe) {
                ParticleEffect ink = copperPipe.ink;
                if (world.getBlockState(pos.offset(direction.getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    if (ink == ParticleTypes.SQUID_INK) {
                        ink = fitting.ink;
                    }
                    for (int o = 0; o < 30; o++) {
                        world.spawnParticles(ink, d + uniformInt3().get(world.random) * 0.1, e + uniformInt1().get(world.random) * 0.1, f + uniformInt3().get(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
                    }
                }
            }
        });
        register(Items.GLOW_INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandomB(random);
            double random2 = getRandomB(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            for (int o = 0; o < 30; o++) {
                world.spawnParticles(ParticleTypes.GLOW_SQUID_INK, d + uniformInt3().get(world.random) * 0.1, e + uniformInt1().get(world.random) * 0.1, f + uniformInt3().get(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
            }
        });
        register(Items.SCULK_SENSOR, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            Random random = world.random;
            Direction.Axis axis = direction.getAxis();
            double vibX = position.getX();
            double vibY = position.getY();
            double vibZ = position.getZ();
            double random1 = getRandomA(random);
            double random2 = getRandomA(random);
            vibX = axis == Direction.Axis.X ? vibX + (10 * direction.getOffsetX()) : corroded ? (axis == Direction.Axis.Z ? vibX + random2 : vibX + random1) : vibX;
            vibY = axis == Direction.Axis.Y ? vibY + (10 * direction.getOffsetY()) : corroded ? vibY + random1 : vibY;
            vibZ = axis == Direction.Axis.Z ? vibZ + (10 * direction.getOffsetZ()) * 2 : corroded ? vibZ + random2 : vibZ;
            RegisterPipeNbtMethods.spawnDelayedVibration(world, pos, new BlockPos(vibX, vibY, vibZ), 32);
        });
    }

}
