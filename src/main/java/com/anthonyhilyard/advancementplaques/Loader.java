package com.anthonyhilyard.advancementplaques;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class Loader implements ClientModInitializer
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitializeClient()
	{
		AdvancementPlaques.onClientSetup();
	}

}
