package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AdvancementPlaques
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	public void onClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final ToastComponent newToastComponent;
					if (ModList.get().isLoaded("toastcontrol"))
					{
						newToastComponent = (ToastComponent) Class.forName("com.anthonyhilyard.advancementplaques.AdvancementPlaquesToastGuiWithToastControl").getConstructor(Minecraft.class).newInstance(Minecraft.getInstance());
					}
					else
					{
						newToastComponent = new AdvancementPlaquesToastGui(Minecraft.getInstance());
					}

					if (newToastComponent != null)
					{
						Minecraft.getInstance().toast = newToastComponent;
					}
					else
					{
						LOGGER.debug("something went wrong");
					}
				}
				catch (Exception e)
				{
					LOGGER.error(e);
				}
			}
		});
	}
}
