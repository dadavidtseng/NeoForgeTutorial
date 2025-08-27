//----------------------------------------------------------------------------------------------------
// ModDataComponents.java
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
package com.dadavidtseng.tutorialmod.component;

import com.dadavidtseng.tutorialmod.TutorialMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

//----------------------------------------------------------------------------------------------------
public class ModDataComponents
{
    //----------------------------------------------------------------------------------------------------
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.createDataComponents(TutorialMod.MOD_ID);

    //----------------------------------------------------------------------------------------------------
    // [neoforge codes](https://docs.neoforged.net/docs/datastorage/codecs/)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> COORDINATES = register("coordinates",
            builder-> builder.persistent(BlockPos.CODEC));


    //----------------------------------------------------------------------------------------------------
    // [neoforge datacomponents](https://docs.neoforged.net/docs/items/datacomponents/)
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator)
    {
        return DATA_COMPONENT_TYPE.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }

    //----------------------------------------------------------------------------------------------------
    public static void register(IEventBus eventBus)
    {
        DATA_COMPONENT_TYPE.register(eventBus);
    }
}
