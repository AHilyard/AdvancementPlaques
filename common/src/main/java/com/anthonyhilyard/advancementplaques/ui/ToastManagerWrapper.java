package com.anthonyhilyard.advancementplaques.ui;

import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.ui.render.AdvancementPlaque;
import com.anthonyhilyard.iceberg.services.Services;
import com.google.common.collect.Queues;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;

public class ToastManagerWrapper extends ToastManager
{
	private final AdvancementPlaque[] plaques = new AdvancementPlaque[1];
	private final Deque<AdvancementToast> advancementToastsQueue = Queues.newArrayDeque();
	private final Minecraft mc;
	private final ToastManager wrapped;
	private final ReentrantLock wrapLock = new ReentrantLock(true);

	public ToastManagerWrapper(Minecraft mcIn, ToastManager wrapped)
	{
		super(mcIn, mcIn.options);
		mc = mcIn;
		this.wrapped = wrapped;
	}

	@Override
	public <T extends Toast> T getToast(Class<? extends T> class_, Object object)
	{
		wrapLock.lock();
		T toast = wrapped.getToast(class_, object);
		wrapLock.unlock();

		return toast;
	}

	@Override
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

		wrapLock.lock();
		wrapped.addToast(toastIn);
		wrapLock.unlock();
	}


	@Override
	public void render(GuiGraphics graphics)
	{
		if (!mc.options.hideGui)
		{
			try
			{
				// Do toasts.
				wrapLock.lock();
				wrapped.render(graphics);
				wrapLock.unlock();

				// If Waila/Hwyla/Jade is installed, turn it off while the plaque is drawing if configured to do so.
				boolean wailaLoaded = Services.getPlatformHelper().isModLoaded("waila");
				boolean jadeLoaded = Services.getPlatformHelper().isModLoaded("jade");
				if (AdvancementPlaquesConfig.getInstance().hideWaila.get() && (wailaLoaded || jadeLoaded))
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
				AdvancementPlaque plaque = plaques[i];

				if (plaque != null && plaque.render(graphics.guiWidth(), i, graphics))
				{
					plaques[i] = null;
				}

				if (plaques[i] == null && !advancementToastsQueue.isEmpty())
				{
					plaques[i] = new AdvancementPlaque(advancementToastsQueue.removeFirst(), mc);
				}
			}
		}
	}

	@Override
	public void clear()
	{
		wrapLock.lock();
		wrapped.clear();
		wrapLock.unlock();

		Arrays.fill(plaques, null);
		advancementToastsQueue.clear();
	}
}