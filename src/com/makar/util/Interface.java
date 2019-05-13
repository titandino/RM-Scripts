package com.makar.util;

import java.awt.event.KeyEvent;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;

public class Interface {
	
	public static boolean close(InterfaceComponent closeButton) {
		if (closeButton == null)
			return false;
		if (Util.gaussian(0, 1000, 400) < 100) {
			return Keyboard.typeKey(KeyEvent.VK_ESCAPE);
		} else {
			return closeButton.click();
		}
	}
	
}
