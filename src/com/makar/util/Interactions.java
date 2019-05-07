package com.makar.util;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Interactions {
	
	public static boolean walkOrTurnTo(Interactable object, String option, int chance) {
		if (object.getVisibility() > 30 && object.interact(option)) {
			return true;
		} else {
			if (object instanceof Locatable) {
				if (Random.nextGaussian(0, 100) > chance) {
					Camera.concurrentlyTurnTo((Locatable) object);
				} else {
					Movement.runToWithVariance((Locatable) object, (int) Math.round(Random.nextGaussian(1, 5, 3)));
				}
			}
		}
		return false;
	}

}
