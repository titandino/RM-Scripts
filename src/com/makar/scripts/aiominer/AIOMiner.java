package com.makar.scripts.aiominer;

import com.runemate.game.api.script.framework.task.TaskBot;

public class AIOMiner extends TaskBot {
	
	public AIOMiner() {
		add(Ore.COPPER.getTasks());
	}

}
