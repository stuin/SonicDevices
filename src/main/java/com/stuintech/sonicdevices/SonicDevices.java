package com.stuintech.sonicdevices;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class SonicDevices implements ModInitializer {
	public static final String MODID = "sonicdevices";

	public static final ItemGroup SONIC_GROUP = FabricItemGroupBuilder.create(
			new Identifier("mod_id", "other"))
			.icon(() -> new ItemStack(ModItems.mark1))
			.build();

	@Override
	public void onInitialize() {
		//Load items list
		ModItems.register();
	}


}
