package com.anthonyhilyard.advancementplaques.fabric.client;

import com.anthonyhilyard.advancementplaques.client.AdvancementPlaquesClient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public final class AdvancementPlaquesFabricClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientLifecycleEvents.CLIENT_STARTED.register(AdvancementPlaquesClient::wrapToasts);
	}
}
