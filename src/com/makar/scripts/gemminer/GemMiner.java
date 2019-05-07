package com.makar.scripts.gemminer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.makar.scripts.gemminer.gui.GemGUIController;
import com.makar.util.Antiban;
import com.makar.util.Interactions;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class GemMiner extends LoopingBot implements EmbeddableUI, InventoryListener {

	private enum Stage {
		MINING, BANKING
	}

	private ObjectProperty<Node> interfaceProperty;
	private GemGUIController controller;
	private Stage stage = Stage.MINING;

	private HashMap<String, String> gemsMined = new HashMap<String, String>();
	private double levelProgress = 0.0;
	private String levelText = "No data";

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
	}

	@Override
	public void onLoop() {
		switch (stage) {
		case MINING:
			if (Inventory.isFull()) {
				stage = Stage.BANKING;
				return;
			}

			GameObject swarm = Npcs.newQuery().names("Swarm").actions("Net").results().nearest();
			if (swarm != null) {
				if (Interactions.walkOrTurnTo(swarm, "Net", 70)) {
					Execution.delayWhile(() -> Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), 2, 1);
					Antiban.mouseOff(((int) Math.round(Random.nextGaussian(20, 40, 35))));
				}
			}
			break;

		case BANKING:
			if (Inventory.isEmpty()) {
				stage = Stage.MINING;
				return;
			}

			if (!Bank.open()) {
				
			}
			if (net != null) {
				if (Interactions.walkOrTurnTo(net, "Deposit-All", 75)) {
					Execution.delayUntil(() -> Inventory.isEmpty());
				}
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
		levelText = Skill.MINING.getCurrentLevel() + " (TNL: " + new DecimalFormat("#,###,###").format(Skill.MINING.getExperienceToNextLevel()) + ")";
		Platform.runLater(() -> controller.update());
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