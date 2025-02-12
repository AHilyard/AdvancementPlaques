package com.anthonyhilyard.advancementplaques.services;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.anthonyhilyard.iceberg.services.Services;

public class ModServices extends Services
{
	public static IAdvancementScreenshotHandler getAdvancementScreenshotHandler() { return (IAdvancementScreenshotHandler) Services.serviceCache.computeIfAbsent(IAdvancementScreenshotHandler.class, x -> createLazySupplier(x)).get(); }
}
