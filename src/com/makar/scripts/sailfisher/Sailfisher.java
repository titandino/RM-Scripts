package com.makar.scripts.sailfisher;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import com.makar.scripts.sailfisher.gui.SailfishGUIController;
import com.makar.scripts.sailfisher.task.BankTask;
import com.makar.scripts.sailfisher.task.FishTask;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.task.TaskBot;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Sailfisher extends TaskBot implements EmbeddableUI, InventoryListener {
	private ObjectProperty<Node> interfaceProperty;
	private SailfishGUIController controller;

	private HashMap<String, String> products = new HashMap<String, String>();
	private double levelProgress = 0.0;
	private String levelText = "No data";

	public Sailfisher() {
		controller = new SailfishGUIController(this);
		setEmbeddableUI(this);
		add(new BankTask(), new FishTask());
	}

	@Override
	public void onStart(String... args) {
		getEventDispatcher().addListener(this);
	}

	@Override
	public void onItemAdded(ItemEvent event) {
		String name = event.getItem().getDefinition().getName();
		String current = products.get(name);
		if (current == null)
			products.put(name, "" + 1);
		else
			products.put(name, "" + (Integer.valueOf(current) + event.getQuantityChange()));
		levelProgress = (double) (100 - Skill.FISHING.getExperienceToNextLevelAsPercent()) / 100.0;
		levelText = new StringBuilder().append(Skill.FISHING.getCurrentLevel()).append(" (TNL: ").append(new DecimalFormat("#,###,###").format(Skill.FISHING.getExperienceToNextLevel())).append(")").toString();
		Platform.runLater(() -> controller.update());
	}
	
	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (interfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(controller);
			try {
				Node node = loader.load(Resources.getAsStream("com/makar/scripts/sailfisher/gui/GUI.fxml"));
				interfaceProperty = new SimpleObjectProperty<>(node);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return interfaceProperty;
	}

	public HashMap<String, String> getProducts() {
		return products;
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