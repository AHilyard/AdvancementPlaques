package com.anthonyhilyard.advancementplaques;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.fml.config.ModConfig;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.ui.AdvancementPlaquesToastGui;

@SuppressWarnings("deprecation")
public class AdvancementPlaques
{
	public static final Logger LOGGER = LogManager.getLogger(Loader.MODID);

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	public static final ResourceLocation TASK_COMPLETE_ID = new ResourceLocation(Loader.MODID, "ui.toast.task_complete");
	public static final ResourceLocation GOAL_COMPLETE_ID = new ResourceLocation(Loader.MODID, "ui.toast.goal_complete");
	public static final SoundEvent TASK_COMPLETE = SoundEvent.createVariableRangeEvent(TASK_COMPLETE_ID);
	public static final SoundEvent GOAL_COMPLETE = SoundEvent.createVariableRangeEvent(GOAL_COMPLETE_ID);

	public static void onClientSetup()
	{
		ForgeConfigRegistry.INSTANCE.register(Loader.MODID, ModConfig.Type.COMMON, AdvancementPlaquesConfig.SPEC);

		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			try
			{
				final ToastComponent newToastComponent;

				// Check if Toast Manager is loaded.
				if (FabricLoader.getInstance().isModLoaded("toastmanager"))
				{
					newToastComponent = (ToastComponent) Class.forName("com.anthonyhilyard.advancementplaques.ui.AdvancementPlaquesToastGuiWithToastManager").getConstructor(Minecraft.class).newInstance(client);
				}
				else
				{
					newToastComponent = new AdvancementPlaquesToastGui(client);
				}

				if (newToastComponent != null)
				{
					client.toast = newToastComponent;
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
		});
	}
}