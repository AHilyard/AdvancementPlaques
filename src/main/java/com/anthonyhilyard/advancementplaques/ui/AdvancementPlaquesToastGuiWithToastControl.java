 package com.anthonyhilyard.advancementplaques.ui;

import java.util.Arrays;
import java.util.Deque;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.ui.render.AdvancementPlaque;
import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import com.google.common.collect.Queues;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraftforge.fml.ModList;

public class AdvancementPlaquesToastGuiWithToastControl extends dev.shadowsoffire.toastcontrol.BetterToastComponent
{
	private final AdvancementPlaque[] plaques = new AdvancementPlaque[3];
	private final Deque<AdvancementToast> advancementToastsQueue = Queues.newArrayDeque();
	private final Minecraft mc;
	private final CustomItemRenderer itemRenderer;

	public AdvancementPlaquesToastGuiWithToastControl(Minecraft mcIn)
	{
		super();
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
			if (AdvancementPlaquesConfig.showPlaqueForAdvancement(advancementToast.advancement))
			{
				// Special logic for advancement toasts.  Store them seperately since they will be displayed seperately.
				advancementToastsQueue.add((AdvancementToast)toastIn);
				return;
			}
		}

		super.addToast(toastIn);
	}

	@Override
	public void render(GuiGraphics graphics)
	{
		if (!mc.options.hideGui)
		{
			try
			{
				// Do toasts.
				super.render(graphics);

				// If Waila/Hwyla/Jade/WTHIT is installed, turn it off while the plaque is drawing if configured to do so.
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

				// Do plaques.
				for (int i = 0; i < plaques.length; ++i)
				{
					AdvancementPlaque toastinstance = plaques[i];

					if (toastinstance != null && toastinstance.render(graphics.guiWidth(), i, graphics))
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