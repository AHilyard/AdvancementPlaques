package com.anthonyhilyard.advancementplaques.forge;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdvancementPlaques.MODID)
public final class AdvancementPlaquesForge
{
	public AdvancementPlaquesForge(FMLJavaModLoadingContext context)
	{
		context.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
	}
}
