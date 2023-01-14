package com.anthonyhilyard.advancementplaques.ui;

import java.util.Arrays;
import java.util.Deque;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.ui.render.AdvancementPlaque;
import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraftforge.fml.ModList;

public class AdvancementPlaquesToastGui extends ToastComponent
{
	private final AdvancementPlaque[] plaques = new AdvancementPlaque[1];
	private final Deque<AdvancementToast> advancementToastsQueue = Queues.newArrayDeque();
	private final Minecraft mc;
	private final CustomItemRenderer itemRenderer;

	public AdvancementPlaquesToastGui(Minecraft mcIn)
	{
		super(mcIn);
		mc = mcIn;
		itemRenderer = new CustomItemRenderer(mc.getTextureManager(), mc.getModelManager(), mc.getItemColors(), mc.getItemRenderer().getBlockEntityRenderer(), mc);
	}

	@Override
	@SuppressWarnings("null")
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
			// Do toasts.
			super.render(stack);

			try
			{
				// If Waila/Hwyla/Jade is installed, turn it off while the plaque is drawing if configured to do so.
				boolean wailaLoaded = ModList.get().isLoaded("waila");
				boolean jadeLoaded = ModList.get().isLoaded("jade");
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
			}
			catch (Exception e)
			{
				AdvancementPlaques.LOGGER.error(e);
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
	}

	@Override
	public void clear()
	{
		super.clear();
		Arrays.fill(plaques, null);
		advancementToastsQueue.clear();
	}
}