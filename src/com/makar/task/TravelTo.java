package com.makar.task;

import java.util.List;
import java.util.function.Supplier;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.region.Region;
import com.runemate.game.api.script.framework.task.Task;

public class TravelTo extends Task {

	private Supplier<Boolean> validation;
	private Coordinate target;
	private List<Coordinate> pathCoords;
	private Path path;

	public TravelTo(Coordinate target, List<Coordinate> pathCoords, Supplier<Boolean> validation) {
		this.target = target;
		this.pathCoords = pathCoords;
		this.validation = validation;
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
		if (!Region.isCachingCollisionFlags())
			Region.cacheCollisionFlags(true);
		if (path == null || path.getNext() == null) {
			path = pathCoords != null ? PredefinedPath.create(pathCoords) : Traversal.getDefaultWeb().getPathBuilder().buildTo(target);
		}
		if (path != null)
			path.step();
	}

}
