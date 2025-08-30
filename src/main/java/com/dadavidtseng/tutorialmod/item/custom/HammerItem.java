//----------------------------------------------------------------------------------------------------
// HammerItem.java
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
package com.dadavidtseng.tutorialmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

//----------------------------------------------------------------------------------------------------
public class HammerItem extends DiggerItem
{
    //----------------------------------------------------------------------------------------------------
    public HammerItem(Tier tier, Properties properties)
    {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    //----------------------------------------------------------------------------------------------------
    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initialBlockPos, ServerPlayer serverPlayer)
    {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = serverPlayer.level().clip(new ClipContext(serverPlayer.getEyePosition(1f),
                (serverPlayer.getEyePosition(1f).add(serverPlayer.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, serverPlayer));

        if (traceResult.getType() == BlockHitResult.Type.MISS)
        {
            return positions;
        }

        if (traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP)
        {
            for (int x = -range; x <= range; x++)
            {
                for (int y = -range; y <= range; y++)
                {
                    positions.add(new BlockPos(initialBlockPos.getX() + x, initialBlockPos.getY(), initialBlockPos.getZ() + y));
                }
            }
        }

        if (traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH)
        {
            for (int x = -range; x <= range; x++)
            {
                for (int y = -range; y <= range; y++)
                {
                    positions.add(new BlockPos(initialBlockPos.getX() + x, initialBlockPos.getY() + y, initialBlockPos.getZ()));
                }
            }
        }

        if (traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST)
        {
            for (int x = -range; x <= range; x++)
            {
                for (int y = -range; y <= range; y++)
                {
                    positions.add(new BlockPos(initialBlockPos.getX(), initialBlockPos.getY() + y, initialBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }
}
