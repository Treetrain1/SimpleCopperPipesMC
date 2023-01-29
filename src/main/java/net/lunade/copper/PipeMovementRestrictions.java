package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.lunade.copper.registry.SimpleCopperRegistries;
import org.jetbrains.annotations.Nullable;

public class PipeMovementRestrictions {

    public record PipeMovementRestriction<T extends BlockEntity>(
            CanTransferTo<T> canTransferTo,
            CanTakeFrom<T> canTakeFrom
    ) {}

    public static <T extends BlockEntity> void register(Identifier id, CanTransferTo<T> canTransferTo, CanTakeFrom<T> canTakeFrom) {
        Registry.register(SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS, id, new PipeMovementRestriction<T>(canTransferTo, canTakeFrom));
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(Identifier id) {
        if (SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.containsId(id)) {
            return SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.get(id).canTransferTo;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(Identifier id) {
        if (SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.containsId(id)) {
            return SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.get(id).canTakeFrom;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(T entity) {
        return getCanTransferTo(Registry.BLOCK_ENTITY_TYPE.getId(entity.getType()));
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(T entity) {
        return getCanTakeFrom(Registry.BLOCK_ENTITY_TYPE.getId(entity.getType()));
    }

    @FunctionalInterface
    public interface CanTransferTo<T extends BlockEntity> {
        boolean canTransfer(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom<T extends BlockEntity> {
        boolean canTake(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    public static void init() {

    }
}
