package com.makar.scripts.aiominer;

import java.util.Arrays;
import java.util.List;

import com.makar.scripts.aiominer.task.MineOre;
import com.makar.task.QuickDepositAll;
import com.makar.task.TravelTo;
import com.makar.util.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.task.Task;

public class OreData {
	
	//Would love to delete these disgusting variables but runemate's rs3 web walker makes your CPU overheat
	private static List<Coordinate> IRON_BANK_PATH = Arrays.asList(new Coordinate[] { new Coordinate(3181, 3372, 0), new Coordinate(3183, 3370, 0), new Coordinate(3186, 3371, 0), new Coordinate(3186, 3374, 0), new Coordinate(3184, 3376, 0), new Coordinate(3179, 3376, 0), new Coordinate(3179, 3380, 0), new Coordinate(3177, 3384, 0), new Coordinate(3175, 3388, 0), new Coordinate(3173, 3392, 0), new Coordinate(3171, 3396, 0), new Coordinate(3169, 3400, 0), new Coordinate(3171, 3403, 0), new Coordinate(3171, 3407, 0), new Coordinate(3172, 3410, 0), new Coordinate(3172, 3413, 0), new Coordinate(3172, 3416, 0), new Coordinate(3172, 3419, 0), new Coordinate(3172, 3422, 0), new Coordinate(3173, 3424, 0), new Coordinate(3175, 3426, 0), new Coordinate(3178, 3425, 0), new Coordinate(3181, 3425, 0), new Coordinate(3183, 3424, 0), new Coordinate(3187, 3424, 0) });
	private static List<Coordinate> IRON_MINE_PATH = Util.reverseList(IRON_BANK_PATH);
	
	private static List<Coordinate> MITHRIL_BANK_PATH = Arrays.asList(new Coordinate[] {new Coordinate(3184, 3376, 0), new Coordinate(3179, 3376, 0), new Coordinate(3179, 3380, 0), new Coordinate(3177, 3384, 0), new Coordinate(3175, 3388, 0), new Coordinate(3173, 3392, 0), new Coordinate(3171, 3396, 0), new Coordinate(3169, 3400, 0), new Coordinate(3171, 3403, 0), new Coordinate(3171, 3407, 0), new Coordinate(3172, 3410, 0), new Coordinate(3172, 3413, 0), new Coordinate(3172, 3416, 0), new Coordinate(3172, 3419, 0), new Coordinate(3172, 3422, 0), new Coordinate(3173, 3424, 0), new Coordinate(3175, 3426, 0), new Coordinate(3178, 3425, 0), new Coordinate(3181, 3425, 0), new Coordinate(3183, 3424, 0), new Coordinate(3187, 3424, 0)});
	private static List<Coordinate> MITHRIL_MINE_PATH = Util.reverseList(MITHRIL_BANK_PATH);
	
	private static List<Coordinate> ADAMANT_BANK_PATH = Arrays.asList(new Coordinate[] { new Coordinate(3291, 3359, 0), new Coordinate(3289, 3362, 0), new Coordinate(3288, 3366, 0), new Coordinate(3288, 3370, 0), new Coordinate(3290, 3372, 0), new Coordinate(3292, 3374, 0), new Coordinate(3293, 3377, 0), new Coordinate(3294, 3380, 0), new Coordinate(3293, 3384, 0), new Coordinate(3292, 3388, 0), new Coordinate(3291, 3393, 0), new Coordinate(3291, 3396, 0), new Coordinate(3291, 3399, 0), new Coordinate(3290, 3402, 0), new Coordinate(3289, 3406, 0), new Coordinate(3289, 3409, 0), new Coordinate(3288, 3412, 0), new Coordinate(3286, 3415, 0), new Coordinate(3285, 3418, 0), new Coordinate(3284, 3421, 0), new Coordinate(3283, 3424, 0), new Coordinate(3281, 3426, 0), new Coordinate(3278, 3428, 0), new Coordinate(3275, 3428, 0), new Coordinate(3271, 3428, 0), new Coordinate(3267, 3428, 0), new Coordinate(3264, 3428, 0), new Coordinate(3260, 3429, 0), new Coordinate(3256, 3429, 0), new Coordinate(3253, 3429, 0), new Coordinate(3250, 3429, 0), new Coordinate(3247, 3429, 0), new Coordinate(3244, 3429, 0), new Coordinate(3241, 3429, 0), new Coordinate(3238, 3430, 0), new Coordinate(3235, 3430, 0), new Coordinate(3232, 3430, 0), new Coordinate(3230, 3430, 0), new Coordinate(3229, 3434, 0) });
	private static List<Coordinate> ADAMANT_MINE_PATH = Util.reverseList(ADAMANT_BANK_PATH);
	
	public enum Ore {
		COPPER(new QuickDepositAll(), new MineOre("Copper rock")),
		
		TIN(new QuickDepositAll(), new MineOre("Tin rock")),
		
		IRON(new TravelTo(new Coordinate(3187, 3424, 0), IRON_BANK_PATH, () -> Inventory.isFull()),
				new QuickDepositAll(), 
				new TravelTo(new Coordinate(3181, 3373, 0), IRON_MINE_PATH, () -> !Inventory.isFull()), 
				new MineOre("Iron rock")),
		
		COAL(new QuickDepositAll(), new MineOre("Coal rock")),
		
		MITHRIL(new TravelTo(new Coordinate(3187, 3424, 0), MITHRIL_BANK_PATH, () -> Inventory.isFull()),
				new QuickDepositAll(), 
				new TravelTo(new Coordinate(3184, 3376, 0), MITHRIL_MINE_PATH, () -> !Inventory.isFull()), 
				new MineOre("Mithril rock")),
		
		ADAMANTITE(new TravelTo(new Coordinate(3229, 3434, 0), ADAMANT_BANK_PATH, () -> Inventory.isFull()),
				new QuickDepositAll(), 
				new TravelTo(new Coordinate(3289, 3361, 0), ADAMANT_MINE_PATH, () -> !Inventory.isFull()), 
				new MineOre("Adamantite rock")),
		
		LUMINITE(), //TODO https://runescape.wiki/w/Dwarven_luminite_mine aggro monsters
		//https://runescape.wiki/w/Mining_Guild_resource_dungeon 45 dg 60 mining but safe
		//https://runescape.wiki/w/Keldagrim_north_mine fastest bank probably aside from
		//banking in the 15 dungeoneering bank
		
		RUNITE(), //TODO https://runescape.wiki/w/Mining_Guild probably best location
		
		ORICHALCITE(), //TODO https://runescape.wiki/w/Mining_Guild
		
		DRAKOLITH(), //TODO https://runescape.wiki/w/Mining_Guild_resource_dungeon
		
		NECRITE(), //TODO https://runescape.wiki/w/Al_Kharid_resource_dungeon 75 dg
		//https://runescape.wiki/w/Uzer_mine probably not worth it. hard ore in general to mine
		
		PHASMATITE(), //TODO https://runescape.wiki/w/Port_Phasmatys_South_mine
		//https://runescape.wiki/w/Dwarven_Mine_resource_dungeon (hard damonheim tasks)
		
		BANITE(), //TODO https://runescape.wiki/w/Wilderness_(Level_54)_Pirates%27_Hideout_mine Definitely fastest bank
		//https://runescape.wiki/w/Arctic_(azure)_habitat_mine long walk but safer
		
		LIGHT_ANIMICA(), //TODO https://runescape.wiki/w/Lletya_mine don't even mine it without this tbh
		//https://runescape.wiki/w/Tumeken%27s_remnant desert heat unlucky
		
		DARK_ANIMICA(); //TODO https://runescape.wiki/w/Empty_Throne_Room_mine

		private Task[] tasks;

		private Ore(Task... tasks) {
			this.tasks = tasks;
		}
		
		public Task[] getTasks() {
			return tasks;
		}
	}

}
