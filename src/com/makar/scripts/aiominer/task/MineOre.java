package com.makar.scripts.aiominer.task;

import com.makar.util.Antiban;
import com.makar.util.Interact;
import com.makar.util.Util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.input.Mouse.Button;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class MineOre extends Task {
	
	private String rockName;
	private GameObject rock;
	
	public MineOre(String rockName) {
		this.rockName = rockName;
	}
	
	@Override
	public boolean validate() {
		if (Inventory.isFull())
			return false;
		rock = GameObjects.newQuery().names(rockName).actions("Mine").results().nearest();
		return rock != null;
	}

	@Override
	public void execute() {
		if (ChatDialog.isOpen()) {
			Interact.walkOrTurnTo(rock, "Mine");
		}
		if (Players.getLocal().getAnimationId() != -1 && rock != null) {
			if (rock.hovered() || rock.hover())
				Mouse.click(Button.LEFT);
			if (Util.gaussian(0, 1000, 400) < 100) {
				Antiban.mouseOff(60);
				Execution.delay(25000, 50000);
			}
			Execution.delay(Util.gaussian(2400, 9000, 3000));
			return;
		}
		if (Interact.walkOrTurnTo(rock, "Mine")) {
			Execution.delay(3000, 6000);
			Execution.delayWhile(() -> Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), 5000, 15000);
		}
	}
}
