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
	private static List<Coordinate> IRON_BANK_PATH = Arrays.asList(new Coordinate[] { new Coordinate(3181, 3372, 0), new Coordinate(3183, 3370, 0), new Coordinate(3186, 3371, 0), new Coordinate(3186, 3374, 0), new Coordinate(3184, 3376, 0), new Coordinate(3179, 3376, 0), new Coordinate(3179, 3380, 0), new Coordinate(3177, 3384, 0), new Coordinate(3175, 3388, 0), new Coordinate(3173, 3392, 0), new Coordinate(3171, 3396, 0), new Coordinate(3169, 3400, 0), new Coordinate(3171, 3403, 0), new Coordinate(3171, 3407, 0), new Coordinate(3172, 3410, 0), new Coordinate(3172, 3413, 0), new Coordinate(3172, 3416, 0), new Coordinate(3172, 3419, 0), new Coordinate(3172, 3422, 0), new Coordinate(3173, 3424, 0), new Coordinate(3175, 3426, 0), new Coordinate(3178, 3425, 0), new Coordinate(3181, 3425, 0), new Coordinate(3183, 3424, 0), new Coordinate(3187, 3424, 0) });
	private static List<Coordinate> IRON_MINE_PATH = Util.reverseList(IRON_BANK_PATH);

	public enum Ore {
		COPPER(new QuickDepositAll(), new MineOre("Copper rock")), 
		
		TIN(new QuickDepositAll(), new MineOre("Tin rock")),
		
		IRON(new TravelTo(new Coordinate(3187, 3424, 0), PredefinedPath.create(IRON_BANK_PATH), () -> Inventory.isFull()),
				new QuickDepositAll(), 
				new TravelTo(new Coordinate(3181, 3373, 0), PredefinedPath.create(IRON_MINE_PATH), () -> !Inventory.isFull()), 
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
