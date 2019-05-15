package com.makar.util;

import java.util.regex.Pattern;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.MenuItem;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Interact {
	
	public static boolean walkOrTurnTo(Interactable object, Pattern option) {
		return walkOrTurnTo(object, option, Util.gaussian(0, 500, 150), true);
	}
	
	public static boolean walkOrTurnTo(Interactable object, Pattern option, int chance) {
		return walkOrTurnTo(object, option, chance, true);
	}
	
	public static boolean walkOrTurnTo(Interactable object, String option) {
		return walkOrTurnTo(object, option, Util.gaussian(0, 500, 150), false);
	}
	
	public static boolean walkOrTurnTo(Interactable object, String option, int chance) {
		return walkOrTurnTo(object, option, chance, false);
	}
	
	private static boolean walkOrTurnTo(Interactable object, Object option, int chance, boolean pattern) {
		if (object == null)
			return false;
		if (Menu.isOpen()) {
			MenuItem item;
            if ((item = pattern ? Menu.getItem((Pattern) option) : Menu.getItem((String) option)) != null) {
                item.click();
            } else {
                Menu.close();
            }
		} else {
			if (object.isVisible() && (pattern ? object.interact((Pattern) option) : object.interact((String) option))) {
				return true;
			} 
			if (object instanceof Locatable) {
				if (Random.nextGaussian(0, 100) < chance) {
					Camera.concurrentlyTurnTo((Locatable) object);
				} else {
					Movement.runToWithVariance((Locatable) object, Util.gaussian(1, 4, 2));
				}
				return true;
			}
		}
		return false;
	}

}
