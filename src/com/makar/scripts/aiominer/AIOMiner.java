package com.makar.scripts.aiominer;

import com.makar.scripts.aiominer.task.MineOre;
import com.makar.task.QuickDepositAll;
import com.makar.task.TravelTo;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.task.TaskBot;

public class AIOMiner extends TaskBot {
	
	public AIOMiner() {
		add(new TravelTo(new Coordinate(3013, 9812, 0), () -> Inventory.isFull()),
				new QuickDepositAll(),
				new TravelTo(new Coordinate(3049, 9782, 0), () -> !Inventory.isFull()),
				new MineOre("Iron rock"));
	}

}
