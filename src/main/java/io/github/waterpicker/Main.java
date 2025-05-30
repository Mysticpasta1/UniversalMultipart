package io.github.waterpicker;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mod(Main.MOD_ID)
public final class Main {
    public static final String MOD_ID = "multipart";

    public Main() {
        MinecraftForge.EVENT_BUS.addListener((Consumer<RegisterClientCommandsEvent>)
                event -> event.getDispatcher()
                        .register(Commands.literal("print_solid_blocks")
                                .executes(ctx -> {
                                    try {
                                        var string = BuiltInRegistries.BLOCK.stream().filter(a -> !(a instanceof BaseEntityBlock) && (a.isShapeFullBlock(a.getShape(
                                                        a.defaultBlockState(), Minecraft.getInstance().level, BlockPos.ZERO, CollisionContext.empty()
                                                )) && a.defaultBlockState().isSolid()))
                                                .map(a -> a.builtInRegistryHolder())
                                                .map(a -> a.key())
                                                .map(a -> a.location())
                                                .map(a -> a.toString())
                                                .collect(Collectors.joining("\n"));
                                        var path = FMLPaths.CONFIGDIR.get().resolve("custom-micromaterials.cfg");
                                        Files.writeString(path, string, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return 1;
                                })));
    }
}
