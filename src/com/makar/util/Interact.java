package com.makar.util;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Interact {
	
	public static boolean walkOrTurnTo(Interactable object, String option, int chance) {
		if (object == null)
			return false;
		if (object.getVisibility() > 30 && object.interact(option)) {
			return true;
		} 
		if (object instanceof Locatable) {
			if (Random.nextGaussian(0, 100) > chance) {
				Camera.concurrentlyTurnTo((Locatable) object);
			} else {
				Movement.runToWithVariance((Locatable) object, (int) Math.round(Random.nextGaussian(1, 5, 3)));
			}
			return true;
		}
		return false;
	}

}
