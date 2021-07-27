package com.anthonyhilyard.advancementplaques;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

// import net.minecraftforge.api.distmarker.Dist;
// import net.minecraftforge.common.MinecraftForge;
// import net.minecraftforge.fml.ModLoadingContext;
// import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
// import net.minecraftforge.fml.loading.FMLEnvironment;
// import net.minecraftforge.fml.config.ModConfig;

public class Loader implements ClientModInitializer
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitializeClient()
	{
		//AdvancementPlaques mod = new AdvancementPlaques();
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(mod::onClientSetup);
		//MinecraftForge.EVENT_BUS.register(AdvancementPlaques.class);

		//ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AdvancementPlaquesConfig.SPEC);

		// ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

}