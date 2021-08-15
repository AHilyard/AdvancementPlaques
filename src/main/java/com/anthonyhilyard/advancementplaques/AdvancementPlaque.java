package com.anthonyhilyard.advancementplaques;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.Util;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast.Visibility;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.locale.Language;


public class AdvancementPlaque
{
	private final AdvancementToast toast;
	private long animationTime = -1L;
	private long visibleTime = -1L;
	private boolean hasPlayedSound = false;
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
		float f = Mth.clamp((float)(currentTime - animationTime) / 600.0f, 0.0f, 1.0f);
		f = f * f;
		return visibility == Visibility.HIDE ? 1.0f - f : f;
	}

	private Visibility drawPlaque(PoseStack poseStack, long displayTime)
	{
		DisplayInfo displayInfo = toast.advancement.getDisplay();

		if (displayInfo != null)
		{
			float fadeInTime, fadeOutTime, duration;
			
			switch (displayInfo.getFrame())
			{
				default:
				case TASK:
					fadeInTime = (float)(AdvancementPlaquesConfig.INSTANCE.taskEffectFadeInTime * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.INSTANCE.taskEffectFadeOutTime * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.INSTANCE.taskDuration * 1000.0);
					break;
				case GOAL:
					fadeInTime = (float)(AdvancementPlaquesConfig.INSTANCE.goalEffectFadeInTime * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.INSTANCE.goalEffectFadeOutTime * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.INSTANCE.goalDuration * 1000.0);
					break;
				case CHALLENGE:
					fadeInTime = (float)(AdvancementPlaquesConfig.INSTANCE.challengeEffectFadeInTime * 1000.0);
					fadeOutTime = (float)(AdvancementPlaquesConfig.INSTANCE.challengeEffectFadeOutTime * 1000.0);
					duration = (float)(AdvancementPlaquesConfig.INSTANCE.challengeDuration * 1000.0);
					break;
			}

			if (displayTime >= fadeInTime)
			{
				float alpha = 1.0f;
				if (displayTime > duration)
				{
					alpha = Math.max(0.0f, Math.min(1.0f, 1.0f - ((float)displayTime - duration) / 1000.0f));
				}
				int alphaMask = (int)(alpha * 255.0f);

				RenderSystem.enableBlend();
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
				RenderSystem.setShaderTexture(0, AdvancementPlaques.TEXTURE_PLAQUES);
				int frameOffset = 0;
				if (displayInfo.getFrame() == FrameType.GOAL)
				{
					frameOffset = 1;
				}
				else if (displayInfo.getFrame() == FrameType.CHALLENGE)
				{
					frameOffset = 2;
				}
				AdvancementPlaquesToastGui.blit(poseStack, -1, -1, 0, height() * frameOffset, width(), height(), 256, 256);

				// Only bother drawing text if alpha is greater than 0.1.
				if (alpha > 0.1f)
				{
					alphaMask <<= 24;
					
					// Text like "Challenge Complete!" at the top of the plaque.
					int typeWidth = mc.font.width(displayInfo.getFrame().getDisplayName());
					mc.font.draw(poseStack, displayInfo.getFrame().getDisplayName(), (width() - typeWidth) / 2.0f + 15.0f, 5.0f, 0x332200 | alphaMask);

					int titleWidth = mc.font.width(displayInfo.getTitle());

					// If the width of the advancement title is less than the full available width, display it normally.
					if (titleWidth <= (220 / 1.5f))
					{
						PoseStack modelViewStack = RenderSystem.getModelViewStack();
						modelViewStack.pushPose();
						modelViewStack.scale(1.5f, 1.5f, 1.0f);
						RenderSystem.applyModelViewMatrix();
						mc.font.draw(poseStack, Language.getInstance().getVisualOrder(displayInfo.getTitle()), ((width() / 1.5f) - titleWidth) / 2.0f + (15.0f / 1.5f), 9.0f, 0xFFFFFF | alphaMask);
						modelViewStack.popPose();
						RenderSystem.applyModelViewMatrix();
					}
					// Otherwise, display it with a smaller (default) font.
					else
					{
						mc.font.draw(poseStack, Language.getInstance().getVisualOrder(displayInfo.getTitle()), (width() - titleWidth) / 2.0f + 15.0f, 15.0f, 0xFFFFFF | alphaMask);
					}
				}

				PoseStack modelViewStack = RenderSystem.getModelViewStack();
				modelViewStack.pushPose();
				modelViewStack.translate(1.0f, 1.0f, 0.0f);
				modelViewStack.scale(1.5f, 1.5f, 1.0f);

				RenderSystem.applyModelViewMatrix();
				itemRenderer.renderGuiItemWithAlpha(displayInfo.getIcon(), 1, 1, alpha);
				
				
				modelViewStack.popPose();
				RenderSystem.applyModelViewMatrix();

				if (!hasPlayedSound)
				{
					hasPlayedSound = true;

					// Play sound based on frame type.
					switch (displayInfo.getFrame())
					{
						case TASK:
							if (!AdvancementPlaquesConfig.INSTANCE.muteTasks)
							{
								mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.TASK_COMPLETE, 1.0f, 1.0f));
							}
							break;
						case GOAL:
							if (!AdvancementPlaquesConfig.INSTANCE.muteGoals)
							{
								mc.getSoundManager().play(SimpleSoundInstance.forUI(AdvancementPlaques.GOAL_COMPLETE, 1.0f, 1.0f));
							}
							break;
						default:
						case CHALLENGE:
							if (!AdvancementPlaquesConfig.INSTANCE.muteChallenges)
							{
								mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f));
							}
							break;
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

				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
				poseStack.pushPose();
				poseStack.translate(0.0f, 0.0f, 95.0f);
				RenderSystem.setShaderTexture(0, AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS);

				if (displayInfo.getFrame() == FrameType.CHALLENGE)
				{
					AdvancementPlaquesToastGui.blit(poseStack, -16, -16, 0, height() + 32, width() + 32, height() + 32, 512, 512);
				}
				else
				{
					AdvancementPlaquesToastGui.blit(poseStack, -16, -16, 0, 0, width() + 32, height() + 32, 512, 512);
				}
				poseStack.popPose();
			}

			return displayTime >= fadeInTime + fadeOutTime + duration ? Visibility.HIDE : Visibility.SHOW;
		}
		else
		{
			return Visibility.HIDE;
		}
	}

	public boolean render(int screenWidth, int index, PoseStack poseStack)
	{
		long currentTime = Util.getMillis();
		if (animationTime == -1L)
		{
			animationTime = currentTime;
		}

		if (visibility == Visibility.SHOW && currentTime - animationTime <= 600L)
		{
			visibleTime = currentTime;
		}
		
		RenderSystem.disableDepthTest();
		PoseStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushPose();

		if (AdvancementPlaquesConfig.INSTANCE.onTop)
		{
			modelViewStack.translate((float)(mc.getWindow().getGuiScaledWidth() - width()) / 2.0f,
									 AdvancementPlaquesConfig.INSTANCE.distance,
									 900.0f + index);
		}
		else
		{
			modelViewStack.translate((float)(mc.getWindow().getGuiScaledWidth() - width()) / 2.0f,
									 (float)(mc.getWindow().getGuiScaledHeight() - (height() + AdvancementPlaquesConfig.INSTANCE.distance)),
									 900.0f + index);
		}
		RenderSystem.applyModelViewMatrix();
		Visibility newVisibility = drawPlaque(poseStack, currentTime - visibleTime);

		modelViewStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();

		if (newVisibility != visibility)
		{
			animationTime = currentTime - (long)((int)((1.0f - getVisibility(currentTime)) * 600.0f));
			visibility = newVisibility;
		}

		return visibility == Visibility.HIDE && currentTime - animationTime > 600L;
	}
}