package com.anthonyhilyard.advancementplaques.ui.render;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;
import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import com.anthonyhilyard.iceberg.services.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.Util;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast.Visibility;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;


public class AdvancementPlaque
{
	private final AdvancementToast toast;
	private long animationTime = -1L;
	private long visibleTime = -1L;
	private boolean hasPlayedSound = false;
	private boolean hasTakenScreenshot = false;
	private Visibility visibility = Visibility.SHOW;
	private Minecraft mc;
	private CustomItemRenderer itemRenderer;

	public AdvancementPlaque(AdvancementToast toastIn, Minecraft mcIn, CustomItemRenderer itemRendererIn)
	{
		toast = toastIn;
		mc = mcIn;
		itemRenderer = itemRendererIn;
	}

	public AdvancementToast getToast()
	{
		return toast;
	}

	public int width()
	{
		return 256;
	}

	public int height()
	{
		return 32;
	}

	private float getVisibility(long currentTime)
	{
		float f = Mth.clamp((float)(currentTime - animationTime) / 200.0f, 0.0f, 1.0f);
		f = f * f;
		return visibility == Visibility.HIDE ? 1.0f - f : f;
	}

	private Visibility drawPlaque(GuiGraphics graphics, long displayTime)
	{
		// Don't show plaques while paused or loading.
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen instanceof PauseScreen || mc.screen instanceof LevelLoadingScreen)
		{
			return Visibility.SHOW;
		}

		DisplayInfo displayInfo = toast.advancement.value().display().orElse(null);
		PoseStack poseStack = graphics.pose();

		if (displayInfo != null)
		{
			float fadeInTime, fadeOutTime, duration;
			
			switch (displayInfo.getType())
			{
				default:
				case TASK:
					fadeInTime = (float)(AdvancementPlaquesConfig.getInstance().taskEffectFadeInTime.get() * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.getInstance().taskEffectFadeOutTime.get() * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.getInstance().taskDuration.get() * 1000.0);
					break;
				case GOAL:
					fadeInTime = (float)(AdvancementPlaquesConfig.getInstance().goalEffectFadeInTime.get() * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.getInstance().goalEffectFadeOutTime.get() * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.getInstance().goalDuration.get() * 1000.0);
					break;
				case CHALLENGE:
					fadeInTime = (float)(AdvancementPlaquesConfig.getInstance().challengeEffectFadeInTime.get() * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.getInstance().challengeEffectFadeOutTime.get() * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.getInstance().challengeDuration.get() * 1000.0);
					break;
			}

			graphics.drawSpecial(bufferSource ->
			{
				if (displayTime >= fadeInTime)
				{
					float alpha = 1.0f;
					if (displayTime > duration)
					{
						alpha = Math.max(0.0f, Math.min(1.0f, 1.0f - ((float)displayTime - duration) / 1000.0f));
						
						if (Services.getPlatformHelper().isModLoaded("canvas"))
						{
							alpha = 0;
						}
					}

					// Grab the title and name colors.
					int titleColor = AdvancementPlaquesConfig.getInstance().getTitleColor(alpha).getValue();
					int nameColor  = AdvancementPlaquesConfig.getInstance().getNameColor(alpha).getValue();

					RenderSystem.enableBlend();

					int frameOffset = 0;
					if (displayInfo.getType() == AdvancementType.GOAL)
					{
						frameOffset = 1;
					}
					else if (displayInfo.getType() == AdvancementType.CHALLENGE)
					{
						frameOffset = 2;
					}

					int color = 0xFFFFFF | (int)(alpha * 255.0f) << 24;

					graphics.blit(RenderType::guiTextured, AdvancementPlaques.TEXTURE_PLAQUES, -1, -1, 0, height() * frameOffset, width(), height(), width(), height(), 256, 256, color);

					// Only bother drawing text if alpha is greater than 0.1.
					if (alpha > 0.1f)
					{
						// Text like "Challenge Complete!" at the top of the plaque.
						int typeWidth = mc.font.width(displayInfo.getType().getDisplayName());

						// GuiGraphics.drawString doesn't support alpha, so draw the string manually.
						mc.font.drawInBatch(displayInfo.getType().getDisplayName(), (int)((width() - typeWidth) / 2.0f + 15.0f), 5, titleColor, false, poseStack.last().pose(), bufferSource, DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);
						graphics.flush();

						int titleWidth = mc.font.width(displayInfo.getTitle());

						// If the width of the advancement title is less than the full available width, display it normally.
						if (titleWidth <= (220 / 1.5f))
						{
							poseStack.pushPose();
							poseStack.scale(1.5f, 1.5f, 1.0f);

							// GuiGraphics.drawString doesn't support alpha, so draw the string manually.
							mc.font.drawInBatch(displayInfo.getTitle(), (int)(((width() / 1.5f) - titleWidth) / 2.0f + (15.0f / 1.5f)), 9, nameColor, false, poseStack.last().pose(), bufferSource, DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);

							poseStack.popPose();
						}
						// Otherwise, display it with a smaller (default) font.
						else
						{
							// GuiGraphics.drawString doesn't support alpha, so draw the string manually.
							mc.font.drawInBatch(displayInfo.getTitle(), (int)((width() - titleWidth) / 2.0f + 15.0f), 15, nameColor, false, poseStack.last().pose(), bufferSource, DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);
						}
						graphics.flush();
					}

					poseStack.pushPose();
					poseStack.translate(1.0f, 1.0f, 0.0f);
					poseStack.scale(1.5f, 1.5f, 1.0f);

					if (Services.getPlatformHelper().isModLoaded("canvas"))
					{
						if (alpha > 0)
						{
							poseStack.translate(0.0f, 0.0f, -2000.0f);
							graphics.renderItem(displayInfo.getIcon(), 1, 1);
						}
					}
					else
					{
						itemRenderer.renderItemModelIntoGUIWithAlpha(graphics, displayInfo.getIcon(), 1, 1, alpha);
					}
					
					poseStack.popPose();

					if (!hasPlayedSound)
					{
						hasPlayedSound = true;

						try
						{
							// Play sound based on frame type.
							switch (displayInfo.getType())
							{
								case TASK:
									if (AdvancementPlaquesConfig.getInstance().taskVolume.get() > 0.0)
									{
										mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.TASK_COMPLETE, 1.0f, AdvancementPlaquesConfig.getInstance().taskVolume.get().floatValue()));
									}
									break;
								case GOAL:
									if (AdvancementPlaquesConfig.getInstance().goalVolume.get() > 0.0)
									{
										mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.GOAL_COMPLETE, 1.0f, AdvancementPlaquesConfig.getInstance().goalVolume.get().floatValue()));
									}
									break;
								default:
								case CHALLENGE:
									if (AdvancementPlaquesConfig.getInstance().challengeVolume.get() > 0.0)
									{
										mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, AdvancementPlaquesConfig.getInstance().challengeVolume.get().floatValue()));
									}
									break;
							}
						}
						catch (NullPointerException e)
						{
							AdvancementPlaques.LOGGER.warn("Tried to play a custom sound for an advancement, but that sound was not registered! Install Advancement Plaques on the server or mute tasks and goals in the config file.");
						}
					}

					// If Advancement Screenshot is installed and we're ready to take a screenshot, do it.
					if (displayTime >= fadeInTime + fadeOutTime && alpha == 1.0f && !hasTakenScreenshot && Services.getPlatformHelper().isModLoaded("advancementscreenshot"))
					{
						try
						{
							Class.forName("com.anthonyhilyard.advancementplaques.compat.AdvancementScreenshotHandler").getMethod("takeScreenshot").invoke(null);
							hasTakenScreenshot = true;
						}
						catch (Exception e)
						{
							AdvancementPlaques.LOGGER.error(e);
						}
					}
				}

				if (displayTime < fadeInTime + fadeOutTime)
				{
					float alpha = 1.0f - ((float)(displayTime - fadeInTime) / fadeOutTime);
					if (displayTime < fadeInTime)
					{
						alpha = (float)displayTime / fadeInTime;
					}

					int color = 0xFFFFFF | (int)(alpha * 255.0f) << 24;

					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					poseStack.pushPose();
					poseStack.translate(0.0f, 0.0f, 95.0f);

					if (displayInfo.getType() == AdvancementType.CHALLENGE)
					{
						graphics.blit(RenderType::guiTextured, AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS, -16, -16, 0, height() + 32, width() + 32, height() + 32, width() + 32, height() + 32, 512, 512, color);

					}
					else
					{
						graphics.blit(RenderType::guiTextured, AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS, -16, -16, 0, 0, width() + 32, height() + 32, width() + 32, height() + 32, 512, 512, color);
					}
					poseStack.popPose();
				}

				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			});

			return displayTime >= fadeInTime + fadeOutTime + duration ? Visibility.HIDE : Visibility.SHOW;
		}
		else
		{
			return Visibility.HIDE;
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
		
		RenderSystem.disableDepthTest();
		PoseStack poseStack = graphics.pose();
		poseStack.pushPose();

		if (AdvancementPlaquesConfig.getInstance().onTop.get())
		{
			poseStack.translate((float)(graphics.guiWidth() - width()) / 2.0f + AdvancementPlaquesConfig.getInstance().horizontalOffset.get(),
									 AdvancementPlaquesConfig.getInstance().distance.get(),
									 800.0f + index);
		}
		else
		{
			poseStack.translate((float)(graphics.guiWidth() - width()) / 2.0f + AdvancementPlaquesConfig.getInstance().horizontalOffset.get(),
									 (float)(mc.getWindow().getGuiScaledHeight() - (height() + AdvancementPlaquesConfig.getInstance().distance.get())),
									 800.0f + index);
		}
		Visibility newVisibility = drawPlaque(graphics, currentTime - visibleTime);

		poseStack.popPose();
		RenderSystem.enableDepthTest();

		if (newVisibility != visibility)
		{
			animationTime = currentTime - (long)((int)((1.0f - getVisibility(currentTime)) * 200.0f));
			visibility = newVisibility;
		}

		return visibility == Visibility.HIDE && currentTime - animationTime > 200L;
	}
}