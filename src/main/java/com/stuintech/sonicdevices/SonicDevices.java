package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.extensions.IAction;
import com.stuintech.sonicdevices.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class SonicDevices implements ModInitializer {
	public static final String MODID = "sonicdevices";

	public static final ItemGroup SONIC_GROUP = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModItems.mark7[0]))
			.build();

	public static ArrayList<IAction> extensions = new ArrayList<>();

	@Override
	public void onInitialize() {
		//Register mod resources
		ModItems.register();
		ModSounds.register();

		//Check for dependencies

	}


}
