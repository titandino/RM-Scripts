package com.makar.util;

import java.awt.event.KeyEvent;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.Execution;

public class Interface {
	
	public static boolean close(int interfaceId, int componentId) {
		InterfaceComponent closeButton = Interfaces.getAt(interfaceId, componentId);
		if (closeButton == null)
			return false;
		if (Util.gaussian(0, 1000, 400) < 100) {
			Keyboard.typeKey(KeyEvent.VK_ESCAPE);
			Execution.delayWhile(() -> Interfaces.getAt(interfaceId, componentId) != null, 600, 4000);
			return true;
		} else {
			return closeButton.click();
		}
	}
	
}
