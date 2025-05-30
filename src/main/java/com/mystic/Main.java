package com.example;

import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegisterEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod(ExampleMod.MOD_ID)
public final class ExampleMod {
    public static final String MOD_ID = "example_mod";

    public ExampleMod() {
        MinecraftForge.EVENT_BUS.addListener(new Consumer<RegisterClientCommandsEvent>() {
            @Override
            public void accept(RegisterClientCommandsEvent event) {
                event.getDispatcher().register(Commands.literal("print_solid_blocks").executes(ctx -> {
                    try {
                        var string = BuiltInRegistries.BLOCK.stream().filter(a -> a.defaultBlockState().isSolid()).map(a -> a.builtInRegistryHolder()).map(a -> a.key()).map(a -> a.location()).map(a -> a.toString()).collect(Collectors.joining("\n"));


                        var path = FMLPaths.CONFIGDIR.get().resolve("custom-micromaterials.cfg");

                        Files.writeString(path, string, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                    return 1;
                }));
            }
        });
    }
}
