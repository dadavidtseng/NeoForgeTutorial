//----------------------------------------------------------------------------------------------------
// ChiselItem.java
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
package com.dadavidtseng.tutorialmod.item.custom;

import com.dadavidtseng.tutorialmod.block.ModBlocks;
import com.dadavidtseng.tutorialmod.component.ModDataComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

//----------------------------------------------------------------------------------------------------
public class ChiselItem extends Item
{
    //----------------------------------------------------------------------------------------------------
    private static final Map<Block, Block> CHISEL_MAP =
            Map.of(
                    Blocks.STONE, Blocks.STONE_BRICKS,
                    Blocks.END_STONE, Blocks.END_STONE_BRICKS,
                    Blocks.DEEPSLATE, Blocks.DEEPSLATE_BRICKS,
                    Blocks.GOLD_BLOCK, Blocks.IRON_BLOCK,
                    Blocks.IRON_BLOCK, Blocks.STONE,
                    Blocks.NETHERRACK, ModBlocks.BISMUTH_BLOCK.get()
            );

    //----------------------------------------------------------------------------------------------------
    public ChiselItem(Properties properties)
    {
        super(properties);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        // On server side
        if (!level.isClientSide())
        {
            level.setBlockAndUpdate(context.getClickedPos(), CHISEL_MAP.get(clickedBlock).defaultBlockState());

            // Damage the proper item
            context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                    item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

            level.playSound(null, context.getClickedPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);

            // Save custom data `COORDINATES` to this particular item in the item stack.
            // @Nullable set
            // if you want to reset the data, you can pass in a `null` in the second parameter.
            context.getItemInHand().set(ModDataComponents.COORDINATES, context.getClickedPos());
        }

        return InteractionResult.SUCCESS;
    }

    // Delete later
//    @Override
//    public UseAnim getUseAnimation(ItemStack stack)
//    {
//        return UseAnim.DRINK;
//    }

    @Override
    public void appendHoverText(ItemStack stack,
                                TooltipContext context,
                                List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag)
    {
        if (Screen.hasShiftDown())
        {
            tooltipComponents.add(Component.translatable("tooltip.tutorialmod.chisel.shift_down"));
        } else
        {
            tooltipComponents.add(Component.translatable("tooltip.tutorialmod.chisel"));
        }

        // @Nullable get
        // this annotation applied only when the value should always be checked against null
        // because the developer could do nothing to prevent null from happening.
        if (stack.get(ModDataComponents.COORDINATES) != null)
        {
            tooltipComponents.add(Component.literal("Last Block change at: " + stack.get(ModDataComponents.COORDINATES)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
