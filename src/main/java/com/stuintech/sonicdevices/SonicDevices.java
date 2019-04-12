package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.extensions.IAction;
import com.stuintech.sonicdevices.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
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
		//Load items list
		ModItems.register();

		//Check for dependencies
		if(FabricLoader.getInstance().isModLoaded("computercraft")) {
			try {
				extensions.add(Class.forName("com.stuintech.sonicdevices.extensions.ComputerCraft").asSubclass(IAction.class).newInstance());
			} catch (Exception e) {
				System.out.println("SonicDevices: ComputerCraft integration failed");
			}
		}
	}


}
