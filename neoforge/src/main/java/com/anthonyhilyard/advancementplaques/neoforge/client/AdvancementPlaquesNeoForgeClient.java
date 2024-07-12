package com.anthonyhilyard.advancementplaques.neoforge.client;

import com.anthonyhilyard.advancementplaques.client.AdvancementPlaquesClient;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class AdvancementPlaquesNeoForgeClient
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
