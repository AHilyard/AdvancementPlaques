package com.anthonyhilyard.advancementplaques.neoforge.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_neoforge.util.Util;
import net.minecraft.network.chat.Component;

public class NeoForgeAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot(Component title)
	{
		Util.takeScreenshot(title);
	}
}
