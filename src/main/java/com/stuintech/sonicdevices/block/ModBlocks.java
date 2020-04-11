package com.stuintech.sonicdevices.block;

import com.stuintech.sonicdevices.SonicDevices;
import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class ModBlocks {
    public static FabricBlockSettings settings = FabricBlockSettings.of(Material.PORTAL).breakInstantly().nonOpaque();
    public static final Block shifted = new ShiftedBlock(settings.collidable(false).build());
    public static final Block clear = new ShiftedBlock(settings.collidable(true).build());

    public static BlockEntityType<ShiftedBlockEntity> shiftedEntity;

    public static void register() {
        Registry.register(Registry.BLOCK, SonicDevices.MODID + ":shifted", shifted);
        Registry.register(Registry.BLOCK, SonicDevices.MODID + ":clear", clear);

        shiftedEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SonicDevices.MODID, "shifted_entity"),
                BlockEntityType.Builder.create(ShiftedBlockEntity::new, new Block[]{ModBlocks.shifted, ModBlocks.clear}).build(null));

    }
}
