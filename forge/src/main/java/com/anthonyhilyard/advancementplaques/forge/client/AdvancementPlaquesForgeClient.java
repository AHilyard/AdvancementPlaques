package com.anthonyhilyard.advancementplaques.forge.client;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.client.AdvancementPlaquesClient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = AdvancementPlaques.MODID, bus = Bus.MOD)
public class AdvancementPlaquesForgeClient
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
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
