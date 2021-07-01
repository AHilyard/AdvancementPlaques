package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
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
					final ToastGui newToastGui;
					if (ModList.get().isLoaded("toastcontrol"))
					{
						newToastGui = (ToastGui) Class.forName("com.anthonyhilyard.advancementplaques.AdvancementPlaquesToastGuiWithToastControl").getConstructor(Minecraft.class).newInstance(Minecraft.getInstance());
					}
					else
					{
						newToastGui = new AdvancementPlaquesToastGui(Minecraft.getInstance());
					}

					if (newToastGui != null)
					{
						LOGGER.debug("replacing toast gui with {}", newToastGui.getClass());
						Minecraft.getInstance().toastGui = newToastGui;
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
