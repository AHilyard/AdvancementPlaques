package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Loader.MODID, bus = Bus.MOD, value = Dist.CLIENT)
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
