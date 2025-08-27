//----------------------------------------------------------------------------------------------------
// ModBlockTagProvider.java
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
package com.dadavidtseng.tutorialmod.datagen;

import com.dadavidtseng.tutorialmod.TutorialMod;
import com.dadavidtseng.tutorialmod.block.ModBlocks;
import com.dadavidtseng.tutorialmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

//----------------------------------------------------------------------------------------------------
public class ModBlockTagProvider extends BlockTagsProvider
{
    public ModBlockTagProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, TutorialMod.MOD_ID, existingFileHelper);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BISMUTH_BLOCK.get())
                .add(ModBlocks.BISMUTH_ORE.get())
                .add(ModBlocks.BISMUTH_LAMP.get())
                .add(ModBlocks.BISMUTH_DEEPSLATE_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.BISMUTH_DEEPSLATE_ORE.get());
        tag(BlockTags.FENCES).add(ModBlocks.BISMUTH_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.BISMUTH_FENCE_GATE.get());
        tag(BlockTags.WALLS).add(ModBlocks.BISMUTH_WALL.get());

        // Any block that requires an iron tool or better can also be mined with a bismuth tool
        tag(ModTags.Blocks.NEED_BISMUTH_TOOL)
                .add(ModBlocks.BISMUTH_LAMP.get())
                .addTag(BlockTags.NEEDS_IRON_TOOL);


        tag(ModTags.Blocks.INCORRECT_FOR_BISMUTH_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)  // Adds all blocks that iron tools can't mine (like blocks requiring diamond+ tools)
                .remove(ModTags.Blocks.NEED_BISMUTH_TOOL);  // Removes blocks that bismuth tools CAN mine from the "incorrect" list
    }
}
