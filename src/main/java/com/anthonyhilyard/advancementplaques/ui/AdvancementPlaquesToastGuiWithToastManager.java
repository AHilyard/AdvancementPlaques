package com.anthonyhilyard.advancementplaques.ui;

import java.util.Arrays;
import java.util.Deque;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.ui.render.AdvancementPlaque;
import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.banzetta.toastmanager.ManagedToastComponent;

import org.apache.commons.lang3.exception.ExceptionUtils;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.fabricmc.loader.api.FabricLoader;

public class AdvancementPlaquesToastGuiWithToastManager extends ManagedToastComponent
{
	private final AdvancementPlaque[] plaques = new AdvancementPlaque[3];
	private final Deque<AdvancementToast> advancementToastsQueue = Queues.newArrayDeque();
	private final Minecraft mc;
	private final CustomItemRenderer itemRenderer;

	public AdvancementPlaquesToastGuiWithToastManager(Minecraft mcIn)
	{
		super();
		mc = mcIn;
		itemRenderer = new CustomItemRenderer(mc.getTextureManager(), mc.getModelManager(), mc.itemColors, mc.getItemRenderer().blockEntityRenderer, mc);
	}

	@Override
	public void addToast(Toast toastIn)
	{
		if (toastIn instanceof AdvancementToast)
		{
			AdvancementToast advancementToast = (AdvancementToast)toastIn;
			DisplayInfo displayInfo = advancementToast.advancement.getDisplay();
			if ((displayInfo.getFrame() == FrameType.TASK && AdvancementPlaquesConfig.INSTANCE.tasks.get()) ||
				(displayInfo.getFrame() == FrameType.GOAL && AdvancementPlaquesConfig.INSTANCE.goals.get()) ||
				(displayInfo.getFrame() == FrameType.CHALLENGE && AdvancementPlaquesConfig.INSTANCE.challenges.get()) ||
				AdvancementPlaquesConfig.INSTANCE.whitelist.get().contains(advancementToast.advancement.getId().toString()))
			{
				// Special logic for advancement toasts.  Store them seperately since they will be displayed seperately.
				advancementToastsQueue.add((AdvancementToast)toastIn);
				return;
			}
		}

		super.addToast(toastIn);
	}

	@Override
	public void render(PoseStack stack)
	{
		if (!mc.options.hideGui)
		{
			try
			{
				// Do toasts.
				super.render(stack);

				// If Waila/Hwyla/Jade/WTHIT is installed, turn it off while the plaque is drawing if configured to do so.
				boolean wailaLoaded = FabricLoader.getInstance().isModLoaded("waila");
				boolean jadeLoaded = FabricLoader.getInstance().isModLoaded("jade");
				if (AdvancementPlaquesConfig.INSTANCE.hideWaila.get() && (wailaLoaded || jadeLoaded))
				{
					boolean anyPlaques = false;
					for (int i = 0; i < plaques.length; i++)
					{
						if (plaques[i] != null)
						{
							anyPlaques = true;
							break;
						}
					}

					if (anyPlaques)
					{
						if (wailaLoaded)
						{
							Class.forName("com.anthonyhilyard.advancementplaques.compat.WailaHandler").getMethod("disableWaila").invoke(null);
						}
						if (jadeLoaded)
						{
							Class.forName("com.anthonyhilyard.advancementplaques.compat.JadeHandler").getMethod("disableJade").invoke(null);
						}
					}
					else
					{
						if (wailaLoaded)
						{
							Class.forName("com.anthonyhilyard.advancementplaques.compat.WailaHandler").getMethod("enableWaila").invoke(null);
						}
						if (jadeLoaded)
						{
							Class.forName("com.anthonyhilyard.advancementplaques.compat.JadeHandler").getMethod("enableJade").invoke(null);
						}
					}
				}

				// Do plaques.
				for (int i = 0; i < plaques.length; ++i)
				{
					AdvancementPlaque toastinstance = plaques[i];

					if (toastinstance != null && toastinstance.render(mc.getWindow().getGuiScaledWidth(), i, stack))
					{
						plaques[i] = null;
					}

					if (plaques[i] == null && !advancementToastsQueue.isEmpty())
					{
						plaques[i] = new AdvancementPlaque(advancementToastsQueue.removeFirst(), mc, itemRenderer);
					}
				}
			}
			catch (Exception e)
			{
				AdvancementPlaques.LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@Override
	public void clear()
	{
		super.clear();
		Arrays.fill(plaques, null);
		advancementToastsQueue.clear();
	}
}