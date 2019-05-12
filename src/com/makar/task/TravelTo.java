package com.makar.task;

import java.util.function.Supplier;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class TravelTo extends Task {
	
	private Supplier<Boolean> validation;
	private Coordinate target;
	private Path path;
	
	public TravelTo(Coordinate target, Supplier<Boolean> validation) {
		this.validation = validation;
		this.target = target;
	}
	
	@Override
	public boolean validate() {
		return Players.getLocal().getPosition().distanceTo(target) > 5 && (validation != null ? validation.get() : true);
	}

	@Override
	public void execute() {
		if (path == null || path.getNext() == null) {
			//path = Traversal.getDefaultWeb().getPathBuilder().buildTo(target);
			path = RegionPath.buildTo(target);
		}
		if (path != null && path.getNext() != null)
			path.step();
	}

}
