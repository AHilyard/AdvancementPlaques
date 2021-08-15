package com.anthonyhilyard.advancementplaques;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.Registry;

public class Loader implements ClientModInitializer
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitializeClient()
	{
		// Register new sounds.
		Registry.register(Registry.SOUND_EVENT, AdvancementPlaques.TASK_COMPLETE_ID, AdvancementPlaques.TASK_COMPLETE);
		Registry.register(Registry.SOUND_EVENT, AdvancementPlaques.GOAL_COMPLETE_ID, AdvancementPlaques.GOAL_COMPLETE);

		AdvancementPlaques.onClientSetup();
	}

}
