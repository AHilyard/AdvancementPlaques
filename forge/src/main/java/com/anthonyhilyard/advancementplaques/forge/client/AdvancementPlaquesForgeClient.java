package com.anthonyhilyard.advancementplaques.forge.client;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.client.AdvancementPlaquesClient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@EventBusSubscriber(modid = AdvancementPlaques.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class AdvancementPlaquesForgeClient
{
	@SubscribeEvent
	public static void onConstructMod(final FMLConstructModEvent event)
	{
		AdvancementPlaques.init();

		MinecraftForge.EVENT_BUS.register(AdvancementPlaquesForgeClient.class);
	}

	@SubscribeEvent(priority = Priority.LOWEST)
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(new Runnable()
		{
			@Override
			public void run()
			{
				AdvancementPlaquesClient.wrapToasts(Minecraft.getInstance());
			}
		});
	}
}