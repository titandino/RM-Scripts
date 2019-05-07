package com.makar.scripts.gemminer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import com.makar.scripts.gemminer.gui.GemGUIController;
import com.makar.util.Interact;
import com.makar.util.Util;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.input.Mouse.Button;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.Skills;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Landmark;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.sun.glass.events.KeyEvent;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class GemMiner extends LoopingBot implements EmbeddableUI, InventoryListener {
	
	private static final Area MINE = Area.circular(new Coordinate(3298, 3298, 0), 5);
	private static final Area PRECIOUS_MINE = Area.circular(new Coordinate(1182, 4510, 0), 5);
	
	private enum Uncut {
		OPAL(1625, 15.0),
		JADE(1627, 20),
		RED_TOPAZ(1629, 25),
		SAPPHIRE(1623, 50),
		EMERALD(1621, 67),
		RUBY(1619, 85),
		DIAMOND(1617, 107.5),
		DRAGONSTONE(1631, 137.5);
		
		public static Uncut forId(int id) {
			for (Uncut u : Uncut.values()) {
				if (u.id == id)
					return u;
			}
			return null;
		}
		
		private int id;
		private double xp;
		
		private Uncut(int id, double xp) {
			this.id = id;
			this.xp = xp;
		}
	}

	private enum Stage {
		MINING, BANKING
	}

	private ObjectProperty<Node> interfaceProperty;
	private GemGUIController controller;
	private Stage stage = Stage.MINING;

	private HashMap<String, String> gemsMined = new HashMap<String, String>();
	private double levelProgress = 0.0;
	private String levelText = "No data";
	private double craftingXpBanked = 0.0;

	public GemMiner() {
		controller = new GemGUIController(this);
		setEmbeddableUI(this);
	}

	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (interfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(controller);
			Node node;
			try {
				node = loader.load(Resources.getAsStream("com/makar/scripts/gemminer/gui/GUI.fxml"));
				interfaceProperty = new SimpleObjectProperty<>(node);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return interfaceProperty;
	}

	@Override
	public void onStart(String... args) {
		getEventDispatcher().addListener(this);
		setLoopDelay(2200, 3600);
	}

	@Override
	public void onLoop() {
		String gemRockName = getGemRockType();
		
		switch (stage) {
		case MINING:
			if (Inventory.isFull()) {
				stage = Stage.BANKING;
				return;
			}

			GameObject gemRock = GameObjects.newQuery().names(gemRockName).actions("Mine").results().nearest();
			if (Players.getLocal().getAnimationId() != -1 && gemRock != null) {
				if (gemRock.hovered() || gemRock.hover())
					Mouse.click(Button.LEFT);
				Execution.delay(3300, 4500);
				return;
			}
			if (Interact.walkOrTurnTo(gemRock, "Mine", 70)) {
				Execution.delayWhile(() -> Players.getLocal().isMoving(), 3500, 5000);
				Execution.delayWhile(() -> Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), 5000, 15000);
			} else {
				WebPath path = Traversal.getDefaultWeb().getPathBuilder().useLodestoneTeleports(true).buildTo(gemRockName.contains("Precious") ? PRECIOUS_MINE : MINE);
				if (path != null) {
					path.step();
				}
			}
			break;

		case BANKING:
			if (Inventory.isEmpty()) {
				stage = Stage.MINING;
				return;
			}

			if (!Bank.open()) {
				WebPath path = Traversal.getDefaultWeb().getPathBuilder().useLodestoneTeleports(true).useTeleports(true).buildTo(Landmark.BANK);
				if (path != null) {
					path.step();
				}
			} else {
				Execution.delayUntil(() -> Bank.isOpen(), 3500, 6700);
				Bank.depositInventory();
				Execution.delayUntil(() -> Inventory.isEmpty(), 3200, 5600);
				
				double xp = 0;
				List<SpriteItem> gems = Bank.getItems(Uncut.OPAL.id, Uncut.JADE.id, Uncut.RED_TOPAZ.id, Uncut.SAPPHIRE.id, Uncut.EMERALD.id, Uncut.RUBY.id, Uncut.DIAMOND.id, Uncut.DRAGONSTONE.id).asList();
				for (SpriteItem gem : gems) {
					xp += Uncut.forId(gem.getId()).xp * gem.getQuantity();
				}
				craftingXpBanked = xp;
				
				if (Util.gaussian(0, 100, 75) < 30)
					Keyboard.pressKey(KeyEvent.VK_ESCAPE);
				else
					Bank.close();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemAdded(ItemEvent event) {
		String name = event.getItem().getDefinition().getName();
		String current = gemsMined.get(name);
		if (current == null)
			gemsMined.put(name, "" + 1);
		else
			gemsMined.put(name, "" + (Integer.valueOf(current) + event.getQuantityChange()));
		levelProgress = (double) (100 - Skill.MINING.getExperienceToNextLevelAsPercent()) / 100.0;
		levelText = new StringBuilder()
				.append(Skill.MINING.getCurrentLevel())
				.append(" (TNL: ")
				.append(new DecimalFormat("#,###,###").format(Skill.MINING.getExperienceToNextLevel()))
				.append(") Craft: ")
				.append(new DecimalFormat("#,###,###").format(craftingXpBanked))
				.append(" XP (")
				.append(Skills.getLevelAtExperience(Skill.CRAFTING, (int) (craftingXpBanked + Skill.CRAFTING.getExperience())))
				.append(")")
				.toString();
		Platform.runLater(() -> controller.update());
	}
	
	public String getGemRockType() {
		if (Skill.MINING.getCurrentLevel() >= 60)
			return "Precious gem rock";
		if (Skill.MINING.getCurrentLevel() >= 20)
			return "Uncommon gem rock";
		return "Common gem rock";
	}

	public HashMap<String, String> getGemsMined() {
		return gemsMined;
	}

	public double getLevelProgress() {
		return levelProgress;
	}

	public String getLevelText() {
		return levelText;
	}

	@Override
	public void onStop() {
	}
}