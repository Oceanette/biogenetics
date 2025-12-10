package com.bonni.biogenetics.blocks;

import com.bonni.biogenetics.ModBlockEntities;
import com.bonni.biogenetics.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProcessorBlockEntity extends BlockEntity implements MenuProvider {
    // Define o inventário com 3 slots
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 72; // Tempo de processamento

    public ProcessorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PROCESSOR_BE.get(), pPos, pBlockState);
        this.data = new SimpleContainerData(2) {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ProcessorBlockEntity.this.progress;
                    case 1 -> ProcessorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> ProcessorBlockEntity.this.progress = pValue;
                    case 1 -> ProcessorBlockEntity.this.maxProgress = pValue;
                }
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Processador Genetico");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ProcessorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("processor_progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("processor_progress");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ProcessorBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(ProcessorBlockEntity pEntity) {
        if(hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(0, 1, false);
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.MUTANT_SYRINGE.get(),
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 1));

            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(ProcessorBlockEntity entity) {
        boolean hasPlant = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.PLANT_SAMPLE.get();
        boolean hasMob = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.MOB_SAMPLE.get();

        return hasPlant && hasMob && canInsertAmountIntoOutputSlot(entity.itemHandler)
                && canInsertItemIntoOutputSlot(entity.itemHandler, ModItems.MUTANT_SYRINGE.get());
    }

    // Corrigido: Agora usa ItemStackHandler diretamente
    private static boolean canInsertItemIntoOutputSlot(ItemStackHandler inventory, Item item) {
        return inventory.getStackInSlot(2).isEmpty() || inventory.getStackInSlot(2).getItem() == item;
    }

    // Corrigido: Agora usa ItemStackHandler diretamente
    private static boolean canInsertAmountIntoOutputSlot(ItemStackHandler inventory) {
        return inventory.getStackInSlot(2).getMaxStackSize() > inventory.getStackInSlot(2).getCount();
    }
}