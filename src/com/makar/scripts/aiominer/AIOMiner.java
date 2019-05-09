package com.makar.scripts.aiominer;

import com.makar.scripts.aiominer.task.MineOre;
import com.makar.task.QuickDepositAll;
import com.runemate.game.api.script.framework.task.TaskBot;

public class AIOMiner extends TaskBot {
	
	public AIOMiner() {
		add(new QuickDepositAll(), new MineOre("Copper rock"));
	}

}
