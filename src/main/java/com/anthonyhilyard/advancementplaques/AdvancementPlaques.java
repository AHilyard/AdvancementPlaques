package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anthonyhilyard.advancementplaques.ui.AdvancementPlaquesToastGui;

@Mod.EventBusSubscriber(modid = Loader.MODID, bus = Bus.MOD)
public class AdvancementPlaques
{
	public static final Logger LOGGER = LogManager.getLogger(Loader.MODID);

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Loader.MODID);
	public static final RegistryObject<SoundEvent> TASK_COMPLETE = SOUND_EVENTS.register("ui.toast.task_complete", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Loader.MODID, "ui.toast.task_complete")));
	public static final RegistryObject<SoundEvent> GOAL_COMPLETE = SOUND_EVENTS.register("ui.toast.goal_complete", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Loader.MODID, "ui.toast.goal_complete")));

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
						newToastComponent = (ToastComponent) Class.forName("com.anthonyhilyard.advancementplaques.ui.AdvancementPlaquesToastGuiWithToastControl").getConstructor(Minecraft.class).newInstance(Minecraft.getInstance());
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
						LOGGER.debug("Unable to update Toast GUI, Advancement Plaques will not function properly. Maybe another mod is interfering?");
					}
				}
				catch (Exception e)
				{
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
		});
	}
}