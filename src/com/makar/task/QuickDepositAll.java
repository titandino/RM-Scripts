package com.makar.task;

import java.util.regex.Pattern;

import com.makar.util.Interact;
import com.makar.util.Interface;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class QuickDepositAll extends Task {
	
	private GameObject deposit;

	@Override
	public boolean validate() {
		if (!Inventory.isFull())
			return false;
		deposit = GameObjects.newQuery().actions(Pattern.compile(".*Deposit-All.*")).results().first();
		return deposit != null;
	}
	
	@Override
	public void execute() {
		if (Interface.close(37, 42)) {
			return;
		}
		if (Interact.walkOrTurnTo(deposit, Pattern.compile(".*Deposit-All.*"))) {
			Execution.delayWhile(() -> Players.getLocal().isMoving() || Inventory.isFull(), 6000, 15000);
		}
	}

}
