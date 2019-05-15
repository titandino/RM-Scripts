package com.makar.task;

import java.util.function.Supplier;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class TravelTo extends Task {

	private Supplier<Boolean> validation;
	private Coordinate target;
	private Path path;

	public TravelTo(Coordinate target, Path path, Supplier<Boolean> validation) {
		this.target = target;
		this.path = path;
		this.validation = validation;
		if (path instanceof PredefinedPath) {
			((PredefinedPath) path).setStepDeviation(3);
		}
	}

	public TravelTo(Coordinate target, Supplier<Boolean> validation) {
		this(target, null, validation);
	}

	@Override
	public boolean validate() {
		return Players.getLocal().getPosition().distanceTo(target) > 5 && (validation != null ? validation.get() : true);
	}

	@Override
	public void execute() {
		if (path == null || (!(path instanceof PredefinedPath) && path.getNext() == null)) {
			path = Traversal.getDefaultWeb().getPathBuilder().buildTo(target);
		}
		if (path != null)
			path.step();
	}

}
