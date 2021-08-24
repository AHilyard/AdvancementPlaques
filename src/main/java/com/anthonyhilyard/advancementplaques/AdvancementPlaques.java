package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Loader.MODID, bus = Bus.MOD)
public class AdvancementPlaques
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Loader.MODID);
	public static final RegistryObject<SoundEvent> TASK_COMPLETE = SOUND_EVENTS.register("ui.toast.task_complete", () -> new SoundEvent(new ResourceLocation(Loader.MODID, "ui.toast.task_complete")));
	public static final RegistryObject<SoundEvent> GOAL_COMPLETE = SOUND_EVENTS.register("ui.toast.goal_complete", () -> new SoundEvent(new ResourceLocation(Loader.MODID, "ui.toast.goal_complete")));

	public AdvancementPlaques()
	{
		SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SuppressWarnings("deprecation")
	public void onClientSetup(FMLClientSetupEvent event)
	{
		DeferredWorkQueue.runLater(new Runnable()
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
						Minecraft.getInstance().toast = newToastGui;
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
