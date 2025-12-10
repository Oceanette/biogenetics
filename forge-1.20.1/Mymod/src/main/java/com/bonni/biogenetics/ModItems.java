package com.bonni.biogenetics;

import com.bonni.biogenetics.items.NeedleItem;
import com.bonni.biogenetics.items.SyringeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Biogenetics.MODID);

    public static final RegistryObject<Item> NEEDLE = ITEMS.register("needle",
            () -> new NeedleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PLANT_SAMPLE = ITEMS.register("plant_sample",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOB_SAMPLE = ITEMS.register("mob_sample",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MUTANT_SYRINGE = ITEMS.register("mutant_syringe",
            () -> new SyringeItem(new Item.Properties().stacksTo(1)));

    // NOVO: Item do Bloco Processador
    public static final RegistryObject<Item> PROCESSOR_BLOCK_ITEM = ITEMS.register("processor_block",
            () -> new BlockItem(ModBlocks.PROCESSOR_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}