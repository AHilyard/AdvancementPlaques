package com.anthonyhilyard.advancementplaques.forge;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.forge.client.AdvancementPlaquesForgeClient;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AdvancementPlaques.MODID)
public final class AdvancementPlaquesForge
{
	public AdvancementPlaquesForge()
	{
		// Run our common setup.
		AdvancementPlaques.init();

		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			MinecraftForge.EVENT_BUS.register(AdvancementPlaquesForgeClient.class);
		}

		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
	}
}
