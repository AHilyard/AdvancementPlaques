package com.anthonyhilyard.advancementplaques;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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

import com.anthonyhilyard.advancementplaques.ui.ToastComponentWrapper;

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
				Minecraft minecraft = Minecraft.getInstance();

				try
				{
					if (minecraft.toast != null)
					{
						AdvancementPlaques.LOGGER.debug("Installing Advancement Plaques toast component.");
						minecraft.toast = new ToastComponentWrapper(minecraft, minecraft.toast);
					}
					else
					{
						AdvancementPlaques.LOGGER.debug("Unable to update Toast GUI, Advancement Plaques will not function properly. Maybe another mod is interfering?");
					}
				}
				catch (Exception e)
				{
					AdvancementPlaques.LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
		});
	}
}