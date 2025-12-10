package com.bonni.biogenetics.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SyringeItem extends Item {
    public SyringeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide && pInteractionTarget instanceof Pig) {
            Pig pig = (Pig) pInteractionTarget;
            ZombifiedPiglin mutant = pig.convertTo(EntityType.ZOMBIFIED_PIGLIN, true);

            if (mutant != null) {
                mutant.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                pPlayer.sendSystemMessage(Component.literal("Mutacao bem sucedida!"));
                pStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}