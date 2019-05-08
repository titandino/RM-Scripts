package com.makar.scripts.gemminer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.makar.scripts.gemminer.gui.GemGUIController;
import com.makar.scripts.gemminer.task.BankTask;
import com.makar.scripts.gemminer.task.MineTask;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.Skills;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.task.TaskBot;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class GemMiner extends TaskBot implements EmbeddableUI, InventoryListener {
	private ObjectProperty<Node> interfaceProperty;
	private GemGUIController controller;

	private HashMap<String, String> gemsMined = new HashMap<String, String>();
	private double levelProgress = 0.0;
	private String levelText = "No data";
	private String rockType = "Uncommon";
	private double craftingXpBanked = 0.0;

	public GemMiner() {
		controller = new GemGUIController(this);
		setEmbeddableUI(this);
		add(new BankTask(this), new MineTask(this));
	}

	@Override
	public void onStart(String... args) {
		getEventDispatcher().addListener(this);
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
				.append(" (TNL: ").append(new DecimalFormat("#,###,###").format(Skill.MINING.getExperienceToNextLevel()))
				.append(") Craft: ").append(new DecimalFormat("#,###,###").format(craftingXpBanked))
				.append(" XP (").append(Skills.getLevelAtExperience(Skill.CRAFTING, (int) (craftingXpBanked + Skill.CRAFTING.getExperience()))).append(")")
				.toString();
		Platform.runLater(() -> controller.update());
	}
	
	@Override
	public ObjectProperty<? extends Node> botInterfaceProperty() {
		if (interfaceProperty == null) {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(controller);
			try {
				Node node = loader.load(Resources.getAsStream("com/makar/scripts/gemminer/gui/GUI.fxml"));
				interfaceProperty = new SimpleObjectProperty<>(node);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return interfaceProperty;
	}
	
	public void refreshCraftingXP() {
		double xp = 0;
		List<SpriteItem> gems = Bank.getItems(Arrays.stream(Uncut.values()).mapToInt(u -> u.getId()).toArray()).asList();
		for (SpriteItem gem : gems) {
			xp += Uncut.forId(gem.getId()).getXp() * gem.getQuantity();
		}
		craftingXpBanked = xp;
	}
	
	public void setRockType(String rockType) {
		this.rockType = rockType;
	}
	
	public String getRockType() {
		return rockType;
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