package com.anthonyhilyard.advancementplaques.ui.render;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;
import com.anthonyhilyard.advancementplaques.services.ModServices;
import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import com.anthonyhilyard.iceberg.services.Services;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast.Visibility;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import org.joml.Matrix3x2fStack;

public class AdvancementPlaque
{
	private final AdvancementToast toast;
	private long animationTime = -1L;
	private long visibleTime = -1L;
	private boolean hasPlayedSound = false;
	private boolean hasTakenScreenshot = false;
	private Visibility visibility = Visibility.SHOW;
	private final Minecraft mc;
	private static CustomItemRenderer itemRenderer;

	public AdvancementPlaque(AdvancementToast toastIn, Minecraft mcIn)
	{
		this.toast = toastIn;
		this.mc = mcIn;
		if (itemRenderer == null)
		{
			itemRenderer = new CustomItemRenderer(mcIn);
		}
	}

	public int width() { return 256; }
	public int height() { return 32; }

	private Visibility drawPlaque(GuiGraphics graphics, long displayTime)
	{
		if (mc.screen instanceof PauseScreen || mc.screen instanceof LevelLoadingScreen)
		{
			return Visibility.SHOW;
		}

		DisplayInfo displayInfo = toast.advancement.value().display().orElse(null);
		if (displayInfo == null) return Visibility.HIDE;

		float fadeInTime, fadeOutTime, duration;
		var config = AdvancementPlaquesConfig.getInstance();

		switch (displayInfo.getType())
		{
			case GOAL -> {
				fadeInTime = (float) (config.goalEffectFadeInTime.get() * 1000.0);
				fadeOutTime = (float) (config.goalEffectFadeOutTime.get() * 1000.0);
				duration = (float) (config.goalDuration.get() * 1000.0);
			}
			case CHALLENGE -> {
				fadeInTime = (float) (config.challengeEffectFadeInTime.get() * 1000.0);
				fadeOutTime = (float) (config.challengeEffectFadeOutTime.get() * 1000.0);
				duration = (float) (config.challengeDuration.get() * 1000.0);
			}
			default -> {
				fadeInTime = (float) (config.taskEffectFadeInTime.get() * 1000.0);
				fadeOutTime = (float) (config.taskEffectFadeOutTime.get() * 1000.0);
				duration = (float) (config.taskDuration.get() * 1000.0);
			}
		}

		if (displayTime >= fadeInTime)
		{
			float alpha = 1.0f;
			if (displayTime > duration)
			{
				alpha = Math.max(0.0f, Math.min(1.0f, 1.0f - ((float) displayTime - duration) / 1000.0f));
				if (Services.getPlatformHelper().isModLoaded("canvas"))
				{
					alpha = 0;
				}
			}

			int titleColor = config.getTitleColor(alpha).getValue();
			int nameColor = config.getNameColor(alpha).getValue();

			int frameOffset = switch (displayInfo.getType())
			{
				case GOAL -> 1;
				case CHALLENGE -> 2;
				default -> 0;
			};

			// Draw the plaque background.
			graphics.blit(RenderPipelines.GUI_TEXTURED, AdvancementPlaques.TEXTURE_PLAQUES,
					-1, -1, 0, height() * frameOffset, width(), height(),
					width(), height(), 256, 256, ARGB.white(alpha));

			if (alpha > 0.1f)
			{
				// First line.
				var typeText = displayInfo.getType().getDisplayName();
				int typeWidth = mc.font.width(typeText);
				graphics.drawString(mc.font, typeText.getVisualOrderText(),
						(int)((width() - typeWidth) / 2.0f + 15.0f), 5, titleColor, false);

				// Second line.
				var titleText = displayInfo.getTitle();
				int titleWidth = mc.font.width(titleText);

				if (titleWidth <= (220 / 1.5f))
				{
					graphics.pose().pushMatrix();
					graphics.pose().scale(1.5f, 1.5f);
					graphics.drawString(mc.font, titleText.getVisualOrderText(),
							(int)(((width() / 1.5f) - titleWidth) / 2.0f + (15.0f / 1.5f)), 9, nameColor, false);
					graphics.pose().popMatrix();
				}
				else
				{
					graphics.drawString(mc.font, titleText.getVisualOrderText(),
							(int)((width() - titleWidth) / 2.0f + 15.0f), 15, nameColor, false);
				}
			}

			// Draw the icon.
			graphics.pose().pushMatrix();
			graphics.pose().translate(1.0f, 1.0f);
			graphics.pose().scale(1.5f, 1.5f);
			itemRenderer.renderItemModelIntoGUIWithAlpha(graphics, displayInfo.getIcon(), 1, 1, alpha);

			graphics.pose().popMatrix();

			handleSounds(displayInfo);
			handleScreenshots(displayTime, fadeInTime, fadeOutTime, alpha, displayInfo);
		}

		// Draw the effects.
		if (displayTime < fadeInTime + fadeOutTime)
		{
			float effectAlpha = (displayTime < fadeInTime) ? (float) displayTime / fadeInTime : 1.0f - ((float) (displayTime - fadeInTime) / fadeOutTime);
			int effectColor = ARGB.white(effectAlpha);

			graphics.pose().pushMatrix();
			if (displayInfo.getType() == AdvancementType.CHALLENGE)
			{
				graphics.blit(RenderPipelines.GUI_TEXTURED, AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS,
						-16, -16, 0, height() + 32, width() + 32, height() + 32,
						width() + 32, height() + 32, 512, 512, effectColor);
			}
			else
			{
				graphics.blit(RenderPipelines.GUI_TEXTURED, AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS,
						-16, -16, 0, 0, width() + 32, height() + 32,
						width() + 32, height() + 32, 512, 512, effectColor);
			}
			graphics.pose().popMatrix();
		}

		return displayTime >= fadeInTime + fadeOutTime + duration ? Visibility.HIDE : Visibility.SHOW;
	}


	private void handleSounds(DisplayInfo displayInfo)
	{
		if (!hasPlayedSound)
		{
			hasPlayedSound = true;
			var config = AdvancementPlaquesConfig.getInstance();
			try
			{
				switch (displayInfo.getType())
				{
					case TASK -> {
						if (config.taskVolume.get() > 0.0)
							mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.TASK_COMPLETE, 1.0f, config.taskVolume.get().floatValue()));
					}
					case GOAL -> {
						if (config.goalVolume.get() > 0.0)
							mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.GOAL_COMPLETE, 1.0f, config.goalVolume.get().floatValue()));
					}
					case CHALLENGE -> {
						if (config.challengeVolume.get() > 0.0)
							mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, config.challengeVolume.get().floatValue()));
					}
				}
			} catch (Exception ignored) {}
		}
	}

	private void handleScreenshots(long displayTime, float fadeIn, float fadeOut, float alpha, DisplayInfo displayInfo)
	{
		if (displayTime >= fadeIn + fadeOut && alpha > 0.9f && !hasTakenScreenshot && Services.getPlatformHelper().isModLoaded("advancementscreenshot"))
		{
			ModServices.getAdvancementScreenshotHandler().takeScreenshot(displayInfo.getTitle());
			hasTakenScreenshot = true;
		}
	}

	public boolean render(int screenWidth, int index, GuiGraphics graphics)
	{
		long currentTime = Util.getMillis();
		if (animationTime == -1L)
		{
			animationTime = currentTime;
		}
		if (visibility == Visibility.SHOW && currentTime - animationTime <= 200L)
		{
			visibleTime = currentTime;
		}

		Matrix3x2fStack poseStack = graphics.pose();
		poseStack.pushMatrix();

		float x = (float)(graphics.guiWidth() - width()) / 2.0f + AdvancementPlaquesConfig.getInstance().horizontalOffset.get();
		float y = AdvancementPlaquesConfig.getInstance().onTop.get()
				? AdvancementPlaquesConfig.getInstance().distance.get()
				: (float)(mc.getWindow().getGuiScaledHeight() - (height() + AdvancementPlaquesConfig.getInstance().distance.get()));

		poseStack.translate(x, y);

		Visibility newVisibility = drawPlaque(graphics, currentTime - visibleTime);

		poseStack.popMatrix();

		if (newVisibility != visibility)
		{
			animationTime = currentTime - (long)((1.0f - getVisibility(currentTime)) * 200.0f);
			visibility = newVisibility;
		}

		return visibility == Visibility.HIDE && currentTime - animationTime > 200L;
	}

	private float getVisibility(long currentTime)
	{
		float f = Mth.clamp((float)(currentTime - animationTime) / 200.0f, 0.0f, 1.0f);
		f = f * f;
		return visibility == Visibility.HIDE ? 1.0f - f : f;
	}
}