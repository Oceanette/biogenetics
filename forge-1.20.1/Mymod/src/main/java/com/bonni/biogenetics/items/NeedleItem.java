package com.bonni.biogenetics.items;

import com.bonni.biogenetics.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class NeedleItem extends Item {
    public NeedleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            var blockState = pContext.getLevel().getBlockState(pContext.getClickedPos());

            if (blockState.is(Blocks.GRASS_BLOCK)) {
                Player player = pContext.getPlayer();
                if (player != null) {
                    player.sendSystemMessage(Component.literal("Sample de Planta coletada!"));
                    player.addItem(new ItemStack(ModItems.PLANT_SAMPLE.get()));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide) {
            pPlayer.sendSystemMessage(Component.literal("Sample de DNA coletada!"));
            pPlayer.addItem(new ItemStack(ModItems.MOB_SAMPLE.get()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}