package com.mageanimationreplacer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Mage Animation Replacer",
	description = "Replace tridents and barrages with their old animations."
)

public class MageAnimationPlugin extends Plugin {

	//tridents
	private static final int OLD_TRIDENT_ANIMATION_ID = 1167;
	private static final int NEW_TRIDENT_ANIMATION_ID = 11430;
	//barrage animations
	private static final int OLD_BARRAGE_ANIMATION_ID = 1979;
	private static final int NEW_BARRAGE_ANIMATION_ID = 10092;

	@Inject
	private Client client;

	@Inject
	private MageAnimationConfig config;

	@Subscribe(priority = -1001.0f) // post Weapon/Gear/Animation Replacer
	public void onAnimationChanged(AnimationChanged e) {

		//disable in pvp
		if (client.getVarbitValue(VarbitID.INSIDE_WILDERNESS) == 1) {
			return;
		}

		if (client.getWorldType().contains(WorldType.PVP) || client.getWorldType().contains(WorldType.PVP_ARENA)) {
			return;
		}

		Actor actor = e.getActor();
		int actorAnim = actor.getAnimation();

		if (actor == client.getLocalPlayer()) {
			if (config.tridentReplace() && actorAnim == NEW_TRIDENT_ANIMATION_ID) {
				actor.setAnimation(OLD_TRIDENT_ANIMATION_ID);
			}

			if (config.barrageReplace() && actorAnim == NEW_BARRAGE_ANIMATION_ID) {
				actor.setAnimation(OLD_BARRAGE_ANIMATION_ID);
			}
		}
	}

	@Provides
	MageAnimationConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MageAnimationConfig.class);
	}
}
