package com.anthonyhilyard.advancementplaques.neoforge;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.neoforge.client.AdvancementPlaquesNeoForgeClient;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AdvancementPlaques.MODID)
public final class AdvancementPlaquesNeoForge
{
	public AdvancementPlaquesNeoForge(ModContainer container, IEventBus modBus)
	{
		// Run our common setup.
		AdvancementPlaques.init();

		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			modBus.register(AdvancementPlaquesNeoForgeClient.class);
		}
	}
}

