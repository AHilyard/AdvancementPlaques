package com.anthonyhilyard.advancementplaques;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class Loader implements ClientModInitializer
{
	public static final String MODID = "advancementplaques";

	@Override
	public void onInitializeClient()
	{
		// Register new sounds.
		Registry.register(BuiltInRegistries.SOUND_EVENT, AdvancementPlaques.TASK_COMPLETE_ID, AdvancementPlaques.TASK_COMPLETE);
		Registry.register(BuiltInRegistries.SOUND_EVENT, AdvancementPlaques.GOAL_COMPLETE_ID, AdvancementPlaques.GOAL_COMPLETE);

		AdvancementPlaques.onClientSetup();
	}

}
