package com.anthonyhilyard.advancementplaques;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Loader.MODID)
public class Loader
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public Loader()
	{
		if (FMLEnvironment.dist == Dist.CLIENT)
		{
			AdvancementPlaques mod = new AdvancementPlaques();
			FMLJavaModLoadingContext.get().getModEventBus().addListener(mod::onClientSetup);
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AdvancementPlaquesConfig.SPEC);
		}
		else
		{
			LOGGER.error("Running on a dedicated server, disabling mod.");
		}
	}

}