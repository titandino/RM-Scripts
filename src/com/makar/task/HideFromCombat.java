package com.makar.task;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class HideFromCombat extends Task {
	
	private Coordinate tile;
	
	public HideFromCombat(Coordinate tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean validate() {
		return Players.getLocal().getHealthGauge() != null && !Players.getLocal().isMoving();
	}

	@Override
	public void execute() {
		if (Players.getLocal().distanceTo(tile) > 1) {
			BresenhamPath path = BresenhamPath.buildTo(tile);
			if (path != null && path.getNext() != null)
				path.step();
		}
	}

}
