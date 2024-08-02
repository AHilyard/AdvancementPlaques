package com.anthonyhilyard.advancementplaques.neoforge.client;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.client.AdvancementPlaquesClient;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = AdvancementPlaques.MODID, dist = Dist.CLIENT)
public class AdvancementPlaquesNeoForgeClient
{
	public AdvancementPlaquesNeoForgeClient(IEventBus modBus)
	{
		AdvancementPlaques.init();

		modBus.register(AdvancementPlaquesNeoForgeClient.class);
	}

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
