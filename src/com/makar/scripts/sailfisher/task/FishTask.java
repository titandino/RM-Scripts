package com.makar.scripts.sailfisher.task;

import com.makar.util.Antiban;
import com.makar.util.Interact;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class FishTask extends Task {
	
	private static final int MIN_AFKWARDEN_TIMER = 60_000; //60 seconds because nice even number threshholds aren't horrific and definitely human-like
	private static final int MAX_AFKWARDEN_TIMER = 240_000; //240 seconds

	private static final Coordinate FISHING_TILE = new Coordinate(2140, 7129, 0);

	@Override
	public boolean validate() {
		return !Inventory.isFull();
	}

	@Override
	public void execute() {
		Npc fish = Npcs.newQuery().names("Sailfish", "Swift sailfish").actions("Catch").results().nearest();
		if (Interact.walkOrTurnTo(fish, "Catch", 70)) {
			Execution.delay(1200, 3000);
			Execution.delayWhile(() -> Players.getLocal().isMoving());
			Antiban.mouseOff(((int) Math.round(Random.nextGaussian(20, 40, 35))));
			Execution.delayWhile(() ->  Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), MIN_AFKWARDEN_TIMER, MAX_AFKWARDEN_TIMER);
		} else {
			RegionPath path = RegionPath.buildTo(FISHING_TILE);
			path.setStepDeviation(5);
			if (path != null && path.step())
				Execution.delayWhile(() -> Players.getLocal().isMoving(), 6000, 12000);
		}
	}
}
