package com.teamabode.cave_enhancements.block;

import com.teamabode.cave_enhancements.block.entity.ReceiverBlockEntity;
import com.teamabode.cave_enhancements.registry.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReceiverBlock extends DiodeBlock implements EntityBlock, IForgeBlock {
    public static final BooleanProperty CAN_PASS = BooleanProperty.create("can_pass");
    public WeatheringCopper.WeatherState oxidationLevel;

    public ReceiverBlock(WeatheringCopper.WeatherState oxidationLevel, Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(CAN_PASS, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CAN_PASS, POWERED);
    }

    public int getRequiredPowerDurationTicks() {
        return switch (this.oxidationLevel) {
            case UNAFFECTED -> 2;
            case EXPOSED -> 5;
            case WEATHERED -> 10;
            case OXIDIZED -> 20;
        };
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        assert blockState != null;
        return blockState.setValue(CAN_PASS, false).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        int i = super.getInputSignal(level, pos, state);
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.hasAnalogOutputSignal()) {
            i = blockState.getAnalogOutputSignal(level, blockPos);
        } else if (i < 15 && blockState.isRedstoneConductor(level, blockPos)) {
            if(blockState.hasAnalogOutputSignal()) i = blockState.getAnalogOutputSignal(level, blockPos);
        }

        return i;
    }

    protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof ReceiverBlockEntity ? ((ReceiverBlockEntity)blockEntity).output : 0;
    }

    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (!state.getValue(CAN_PASS)) {
            return 0;
        } else {
            return state.getValue(FACING) == direction ? this.getOutputSignal(world, pos, state) : 0;
        }
    }

    public boolean isSignalSource(BlockState state) {
        return state.getValue(CAN_PASS);
    }

    protected int getDelay(BlockState state) {
        return 2;
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            Direction direction = state.getValue(FACING);
            double d = (double)pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            double e = (double)pos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
            double f = (double)pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
            float g = -5.0F;
            if (random.nextBoolean()) {
                g = (float)(4 * 2 - 1);
            }

            g /= 16.0F;
            double h = g * (float)direction.getStepX();
            double i = g * (float)direction.getStepZ();
            world.addParticle(DustParticleOptions.REDSTONE, d + h, e, f + i, 0.0D, 0.0D, 0.0D);
        }
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReceiverBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ReceiverBlockEntity receiverBlockEntity) receiverBlockEntity.tick(world1, pos, state1);
        };
    }

    private int calculateOutputSignal(Level level, BlockPos pos, BlockState state) {
        return getInputSignal(level, pos, state);
    }

    private void refreshOutputState(Level level, BlockPos pos, BlockState state) {
        int i = this.calculateOutputSignal(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        int j = 0;

        if (blockEntity instanceof ReceiverBlockEntity) {
            ReceiverBlockEntity receiverBlockEntity = (ReceiverBlockEntity)blockEntity;
            j = receiverBlockEntity.output;
            receiverBlockEntity.output = i;
        }

        if (j != i) {
            boolean bl = this.shouldTurnOn(level, pos, state);
            boolean bl2 = state.getValue(POWERED);

            if (bl2 && !bl) {
                level.setBlock(pos, state.setValue(POWERED, false), 2);
            } else if (!bl2 && bl) {
                level.setBlock(pos, state.setValue(POWERED, true), 2);
            }

            this.updateNeighborsInFront(level, pos, state);
        }

    }

    protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
        if (!level.getBlockTicks().willTickThisTick(pos, this)) {
            int i = this.calculateOutputSignal(level, pos, state);

            BlockEntity blockEntity = level.getBlockEntity(pos);

            int j = blockEntity instanceof ComparatorBlockEntity ? ((ComparatorBlockEntity)blockEntity).getOutputSignal() : 0;

            if (i != j || state.getValue(POWERED) != this.shouldTurnOn(level, pos, state)) {
                TickPriority tickPriority = this.shouldPrioritize(level, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
                level.scheduleTick(pos, this, 2, tickPriority);
            }
        }
    }

    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.refreshOutputState(serverLevel, blockPos, blockState);
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        Direction facing = state.getValue(ReceiverBlock.FACING);
        return facing == direction || facing.getOpposite() == direction;
    }
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = player.getItemInHand(hand).getItem();
        if (item instanceof AxeItem) {
            level.levelEvent(3004, pos, 0);
            level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);

            switch (this.oxidationLevel) {
                case UNAFFECTED -> {
                    level.setBlock(pos, ModBlocks.REDSTONE_RECEIVER.get().withPropertiesOf(blockState), 3);
                    return InteractionResult.SUCCESS;
                }
                case EXPOSED -> {
                    level.setBlock(pos, ModBlocks.EXPOSED_REDSTONE_RECEIVER.get().withPropertiesOf(blockState), 3);
                    return InteractionResult.SUCCESS;
                }
                case WEATHERED -> {
                    level.setBlock(pos, ModBlocks.WEATHERED_REDSTONE_RECEIVER.get().withPropertiesOf(blockState), 3);
                    return InteractionResult.SUCCESS;
                }
                case OXIDIZED -> {
                    level.setBlock(pos, ModBlocks.OXIDIZED_REDSTONE_RECEIVER.get().withPropertiesOf(blockState), 3);
                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.PASS;
    }
}
