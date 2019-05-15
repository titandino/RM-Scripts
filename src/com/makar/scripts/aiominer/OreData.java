package com.makar.scripts.aiominer;

import java.util.Arrays;
import java.util.List;

import com.makar.scripts.aiominer.task.MineOre;
import com.makar.task.QuickDepositAll;
import com.makar.task.TravelTo;
import com.makar.util.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.script.framework.task.Task;

public class OreData {
	private static List<Coordinate> IRON_BANK_PATH = Arrays.asList(new Coordinate[] { new Coordinate(3048, 9782, 0), new Coordinate(3045, 9788, 0), new Coordinate(3044, 9795, 0), new Coordinate(3041, 9801, 0), new Coordinate(3040, 9807, 0), new Coordinate(3040, 9813, 0), new Coordinate(3040, 9818, 0), new Coordinate(3040, 9823, 0), new Coordinate(3040, 9829, 0), new Coordinate(3036, 9831, 0), new Coordinate(3033, 9831, 0), new Coordinate(3030, 9828, 0), new Coordinate(3027, 9824, 0), new Coordinate(3025, 9820, 0), new Coordinate(3023, 9816, 0), new Coordinate(3020, 9813, 0), new Coordinate(3016, 9812, 0), new Coordinate(3014, 9812, 0) });
	private static List<Coordinate> IRON_MINE_PATH = Util.reverseList(IRON_BANK_PATH);

	public enum Ore {
		COPPER(new QuickDepositAll(), new MineOre("Copper rock")), 
		
		TIN(new QuickDepositAll(), new MineOre("Tin rock")), 
		
		IRON(new TravelTo(new Coordinate(3013, 9812, 0), PredefinedPath.create(IRON_BANK_PATH), () -> Inventory.isFull()),
				new QuickDepositAll(), 
				new TravelTo(new Coordinate(3049, 9782, 0), PredefinedPath.create(IRON_MINE_PATH), () -> !Inventory.isFull()), 
				new MineOre("Iron rock"));

		private Task[] tasks;

		private Ore(Task... tasks) {
			this.tasks = tasks;
		}
		
		public Task[] getTasks() {
			return tasks;
		}
	}

}
