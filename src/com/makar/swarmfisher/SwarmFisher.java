package com.makar.swarmfisher;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.makar.swarmfisher.gui.SwarmGUIController;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox.Message.Type;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class SwarmFisher extends LoopingBot implements EmbeddableUI, InventoryListener, ChatboxListener {
	
	private static final int MIN_AFKWARDEN_TIMER = 60_000; //60 seconds because nice even number threshholds aren't horrific and definitely human-like
	private static final int MAX_AFKWARDEN_TIMER = 240_000; //240 seconds

	private enum Stage {
		FISHING, BANKING, CLAIM_FISHING_NOTES
	}

	private ObjectProperty<Node> interfaceProperty;
	private SwarmGUIController controller;
	private Stage stage = Stage.FISHING;

	private HashMap<String, String> fishCaught = new HashMap<String, String>();
	private double levelProgress = 0.0;
	private int toolLevel = 0;
	private String levelText = "No data";

	public SwarmFisher() {
		controller = new SwarmGUIController(this);
		setEmbeddableUI(this);
	}

	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (interfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(controller);
			Node node;
			try {
				node = loader.load(Resources.getAsStream("com/makar/swarmfisher/gui/GUI.fxml"));
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

		case FISHING:
			if (Inventory.isFull()) {
				stage = Stage.BANKING;
				return;
			}

			Npc swarm = Npcs.newQuery().names("Swarm").actions("Net").results().nearest();
			if (swarm != null) {
				if (swarm.getVisibility() > 30 && swarm.interact("Net")) {
					Execution.delayWhile(() -> Players.getLocal().getAnimationId() != -1 && !Inventory.isFull(), MIN_AFKWARDEN_TIMER, MAX_AFKWARDEN_TIMER);
					Util.mouseOff(((int) Math.round(Random.nextGaussian(20, 40, 35))));
				} else {
					if (Random.nextGaussian(0, 100) > 70) {
						Camera.concurrentlyTurnTo(swarm);
					} else {
						Util.runToAdvanced(swarm, (int) Math.round(Random.nextGaussian(1, 5, 3)));
					}
				}
			}
			break;

		case BANKING:
			if (Inventory.isEmpty()) {
				stage = Stage.FISHING;
				return;
			}

			if (Inventory.contains("Fishing notes")) {
				stage = Stage.CLAIM_FISHING_NOTES;
				return;
			}

			GameObject net = GameObjects.newQuery().names("Magical net").results().nearest();
			if (net != null) {
				if (net.getVisibility() > 30 && net.interact("Deposit-All")) {
					Execution.delayUntil(() -> Inventory.isEmpty());
				} else {
					if (Random.nextGaussian(0, 100) > 75) {
						Camera.concurrentlyTurnTo(net);
					} else {
						Util.runToAdvanced(net, (int) Math.round(Random.nextGaussian(1, 5, 3)));
					}
				}
			}
			break;

		case CLAIM_FISHING_NOTES:
			SpriteItem notes = Inventory.newQuery().names("Fishing notes").results().first();
			if (notes != null) {
				InterfaceComponent okButton = Interfaces.getAt(1048, 26);
				if (okButton != null) {
					okButton.click();
				} else {
					notes.click();
				}
			} else {
				stage = Stage.BANKING;
			}
			break;
		}
	}

	@Override
	public void onMessageReceived(MessageEvent event) {
		if (event.getType() != Type.SERVER)
			return;
		if (event.getMessage().contains("It is now level ")) {
			try {
				toolLevel = Integer.valueOf(event.getMessage().substring(event.getMessage().indexOf("now level ")).replace("now level ", "").replace(".", ""));
			} catch (Exception e) {
				System.out.println("Error parsing from message: " + event.getMessage());
			}
		}
	}

	@Override
	public void onItemAdded(ItemEvent event) {
		String name = event.getItem().getDefinition().getName();
		String current = fishCaught.get(name);
		if (current == null)
			fishCaught.put(name, "" + 1);
		else
			fishCaught.put(name, "" + (Integer.valueOf(current) + event.getQuantityChange()));
		levelProgress = (double) (100 - Skill.FISHING.getExperienceToNextLevelAsPercent()) / 100.0;
		levelText = Skill.FISHING.getCurrentLevel() + " (TNL: " + new DecimalFormat("#,###,###").format(Skill.FISHING.getExperienceToNextLevel()) + ") Tool: " + toolLevel;
		Platform.runLater(() -> controller.update());
	}

	public HashMap<String, String> getFishCaught() {
		return fishCaught;
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