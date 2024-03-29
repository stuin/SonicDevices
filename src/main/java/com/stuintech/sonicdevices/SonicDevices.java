package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.block.ModBlocks;
import com.stuintech.sonicdevices.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class SonicDevices implements ModInitializer {
	public static final String MODID = "sonicdevices";
	public static final Logger LOGGER = LogManager.getLogger("SonicDevices");

	public static final ItemGroup SONIC_GROUP = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModItems.mark7[0]))
			.build();

	@Override
	public void onInitialize() {
		//Register mod resources
		ModItems.register();
		ModSounds.register();
		ModBlocks.register();
	}
}
