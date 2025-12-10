package com.bonni.biogenetics;

import com.bonni.biogenetics.blocks.ProcessorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biogenetics.MODID);

    public static final RegistryObject<BlockEntityType<ProcessorBlockEntity>> PROCESSOR_BE =
            BLOCK_ENTITIES.register("processor_be",
                    () -> BlockEntityType.Builder.of(ProcessorBlockEntity::new,
                            ModBlocks.PROCESSOR_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}