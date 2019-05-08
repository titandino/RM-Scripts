package com.makar.scripts.gemminer.task;

import com.makar.scripts.gemminer.GemMiner;
import com.makar.util.Interact;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.input.Mouse.Button;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Lodestone;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class MineTask extends Task {
	
	private static final Area NORMAL_AREA = Area.rectangular(new Coordinate(3295, 3308, 0), new Coordinate(3304, 3320, 0));
	private static final Area PRECIOUS_AREA = Area.circular(new Coordinate(1181, 4511, 0), 15);
	
	private static final PredefinedPath MINE_PATH = PredefinedPath.create(new Coordinate(3293, 3190, 0), new Coordinate(3298, 3199, 0), new Coordinate(3300, 3209, 0), new Coordinate(3307, 3218, 0), new Coordinate(3307, 3228, 0), new Coordinate(3307, 3236, 0), new Coordinate(3309, 3243, 0),
			new Coordinate(3307, 3254, 0), new Coordinate(3306, 3262, 0), new Coordinate(3306, 3273, 0), new Coordinate(3302, 3281, 0), new Coordinate(3299, 3290, 0), new Coordinate(3298, 3299, 0), new Coordinate(3300, 3310, 0));
	
	private GemMiner script;
	
	public MineTask(GemMiner script) {
		this.script = script;
	}
	
	@Override
	public boolean validate() {
		return !Inventory.isFull();
	}
	
	@Override
	public void execute() {
		if (script.getRockType().equals("Precious") ? PRECIOUS_AREA.contains(Players.getLocal()) : NORMAL_AREA.contains(Players.getLocal())) {
			GameObject gemRock = GameObjects.newQuery().names(script.getRockType() + " gem rock").actions("Mine").results().nearest();
			if (Players.getLocal().getAnimationId() != -1 && gemRock != null) {
				if (gemRock.hovered() || gemRock.hover())
					Mouse.click(Button.LEFT);
				Execution.delay(3300, 4500);
				return;
			}
			if (Interact.walkOrTurnTo(gemRock, "Mine", 70)) {
				Execution.delayWhile(() -> Players.getLocal().isMoving(), 3500, 5000);
				Execution.delayWhile(() -> Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), 5000, 15000);
			}
		} else {
			GameObject entrance = GameObjects.newQuery().names("Mysterious entrance").results().first();
			if (script.getRockType().equals("Precious") && entrance != null) {
				Interact.walkOrTurnTo(entrance, "Enter", 70);
			} else {
				if (MINE_PATH.getNext().distanceTo(Players.getLocal()) > 50) {
					Lodestone.AL_KHARID.teleport();
				} else {
					MINE_PATH.step();
				}
			}
		}
	}
}
