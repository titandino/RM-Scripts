package com.makar.scripts.aiominer;

import com.makar.scripts.aiominer.OreData.Ore;
import com.runemate.game.api.hybrid.region.Region;
import com.runemate.game.api.script.framework.task.TaskBot;

public class AIOMiner extends TaskBot {
	
	public AIOMiner() {
		add(Ore.IRON.getTasks());
		Region.cacheCollisionFlags(true);
	}

}
