package com.anthonyhilyard.advancementplaques.fabric;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;

import net.fabricmc.api.ModInitializer;

public final class AdvancementPlaquesFabric implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		// Run our common setup.
		AdvancementPlaques.init();
	}
}
