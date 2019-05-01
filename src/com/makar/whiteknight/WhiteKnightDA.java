package com.makar.whiteknight;

import java.io.IOException;
import java.util.List;
import com.makar.whiteknight.gui.WKDAGUIController;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.Shop;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar.Slot;
import com.runemate.game.api.script.framework.LoopingBot;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class WhiteKnightDA extends LoopingBot implements EmbeddableUI {

	private enum Stage {
		BUYING, DISASSEMBLING
	}

	private ObjectProperty<Node> interfaceProperty;
	private Stage stage = Stage.DISASSEMBLING;
	private Slot disassemble = null;
	private int priceCutoff = -1;
	
	public WhiteKnightDA() {
		setEmbeddableUI(this);
	}
	
	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (interfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(new WKDAGUIController(this));
			Node node;
			try {
				node = loader.load(Resources.getAsStream("com/makar/whiteknight/gui/GUI.fxml"));
				interfaceProperty = new SimpleObjectProperty<>(node);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return interfaceProperty;
	}

	@Override
	public void onStart(String... args) {
		
	}

	@Override
	public void onLoop() {
		if (priceCutoff < 0)
			return;
		
		setLoopDelay(((int) Math.round(Random.nextGaussian(1130, 2232, 1500))));

		switch (stage) {
		case BUYING:
			if (Inventory.getEmptySlots() <= 2) {
				stage = Stage.DISASSEMBLING;
				return;
			}
			if (Shop.isOpen()) {
				SpriteItemQueryResults results = Shop.newQuery()
						.filter(item -> item.getQuantity() >= 3 && item.getDefinition().getShopValue() <= priceCutoff)
						.results()
						.sort((i1, i2) -> i2.getQuantity() - i1.getQuantity());

				SpriteItem shopItem = getPrioritizedItem(results);

				if (shopItem != null) {
					//Only way to click a shop item since shop API has zero click data. Woo hoo.
					InterfaceComponent item = Interfaces.getAt(1265, 33, shopItem.getIndex());
					if (item != null) {
						item.interact("Buy All");
						setLoopDelay(((int) Math.round(Random.nextGaussian(1130, 2232, 1500))));
					}
				}
			} else {
				Npc owner = Npcs.newQuery().names("Sir Vyvin").results().first();
				if (owner != null) {
					if (!owner.getPosition().isReachable()) {
						GameObject door = GameObjects.newQuery().names("Door").filter(o -> o.getPosition().isReachable()).results().nearest();
						if (door != null && door.interact("Open")) {
							setLoopDelay(((int) Math.round(Random.nextGaussian(2138, 4293, 2850))));
						}
						return;
					}
					owner.interact("Trade");
					setLoopDelay(((int) Math.round(Random.nextGaussian(1130, 2232, 1500))));
				}
			}
			break;
		case DISASSEMBLING:
			if (Players.getLocal().getAnimationId() == 27997) {
				setLoopDelay(((int) Math.round(Random.nextGaussian(1130, 2232, 1500))));
				return;
			}
			if (Inventory.isEmpty()) {
				stage = Stage.BUYING;
				return;
			}
			if (Shop.isOpen()) {
				Shop.close();
				return;
			}
			if (disassemble != null) {
				SpriteItem item = Inventory.getItems().first();
				if (item != null) {
					disassemble.activate();
					item.click();
					setLoopDelay(((int) Math.round(Random.nextGaussian(2138, 4293, 2850))));
				}
			} else {
				disassemble = ActionBar.newQuery().names("Disassemble").results().first();
			}
			break;
		}
	}

	public SpriteItem getPrioritizedItem(SpriteItemQueryResults results) {
		if (results == null || results.isEmpty())
			return null;

		SpriteItem item = getItemContainingName(results.asList(), "scimitar");
		if (item == null)
			item = getItemContainingName(results.asList(), "claw");
		if (item == null)
			item = getItemContainingName(results.asList(), "staff");
		if (item == null)
			item = getItemContainingName(results.asList(), "sword");
		if (item == null)
			item = getItemContainingName(results.asList(), "dagger");
		if (item == null) {
			item = results.first();
		}
		return item;
	}

	public SpriteItem getItemContainingName(List<SpriteItem> items, String name) {
		for (SpriteItem item : items) {
			if (item == null)
				continue;
			if (item.getDefinition().getName().toLowerCase().contains(name)) {
				return item;
			}
		}
		return null;
	}

	public void setPriceCutoff(int priceCutoff) {
		this.priceCutoff = priceCutoff;
	}

	@Override
	public void onStop() {
	}
}