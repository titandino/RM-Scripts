package com.makar.scripts.sailfisher.task;

import com.makar.util.Interact;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class BankTask extends Task {
	
	private static final Coordinate BANK_TILE = new Coordinate(2123, 7122, 0);
	
	@Override
	public boolean validate() {
		return Inventory.isFull();
	}
	
	@Override
	public void execute() {
		GameObject net = GameObjects.newQuery().names("Magical net").results().nearest();
		if (Interact.walkOrTurnTo(net, "Deposit-All", 75)) {
			Execution.delayWhile(() -> Inventory.isFull(), 5000, 10000);
		} else {
			RegionPath path = RegionPath.buildTo(BANK_TILE);
			path.setStepDeviation(3);
			if (path != null && path.step())
				Execution.delayWhile(() -> Players.getLocal().isMoving(), 6000, 12000);
		}
	}
}
