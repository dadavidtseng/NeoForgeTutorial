//----------------------------------------------------------------------------------------------------
// ModEvents.java
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
package com.dadavidtseng.tutorialmod.event;

import com.dadavidtseng.tutorialmod.TutorialMod;
import com.dadavidtseng.tutorialmod.item.custom.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashSet;
import java.util.Set;

//----------------------------------------------------------------------------------------------------

/// Event handler class for mod-specific game events.
/// This class handles events related to block breaking, specifically for hammer tools. (for now)
///
/// The @EventBusSubscriber annotation registers this class to listen for game events
/// on the GAME event bus for the TutorialMod.
@EventBusSubscriber(modid = TutorialMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents
{

    /// A thread-safe set that keeps track of blocks currently being harvested.
    ///
    /// This prevents infinite recursion when the hammer breaks multiple blocks,
    /// as each block break would trigger this event again.
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    /// Event handler for hammer area-of-effect block breaking functionality.
    /// This method is called whenever a block is broken in the game.
    ///
    /// When a player uses a hammer tool, this handler will automatically break
    /// additional blocks in the surrounding area, creating an AOE mining effect.
    ///
    /// Implementation inspired by CoFH Core's AreaEffectEvents.java
    ///
    /// @param event The BlockEvent.BreakEvent containing information about the block being broken
    /// @see <a href="https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java">CoFH Core AreaEffectEvents</a>
    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event)
    {
        Player player = event.getPlayer();                  // Get the player who broke the block
        ItemStack mainHandItem = player.getMainHandItem();  // Get the item currently held in the player's main hand

        // Check if the player is using a hammer and is a server player (not client-side)
        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer)
        {
            BlockPos initialBlockPos = event.getPos();

            // Prevent infinite recursion: if this block is already being processed, skip it
            if (HARVESTED_BLOCKS.contains(initialBlockPos)) return;

            // Get all blocks that should be destroyed by the hammer's AOE effect
            // The "1" parameter likely represents the hammer's mining level or area size
            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer))
            {
                if (pos == initialBlockPos || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos)))
                {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }
}
