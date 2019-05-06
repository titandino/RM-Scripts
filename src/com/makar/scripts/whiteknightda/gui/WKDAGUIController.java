package com.makar.scripts.whiteknightda.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.makar.scripts.whiteknightda.WhiteKnightDA;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class WKDAGUIController implements Initializable {
	
	private WhiteKnightDA script;
	
	@FXML
	private Button startButton;
	
	@FXML
	private ComboBox<Integer> priceSelector;
	
	public WKDAGUIController(WhiteKnightDA script) {
		this.script = script;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		priceSelector.getItems().addAll(200, 700, 1000, 1400, 2000, 4000);
		
		startButton.setOnAction(event -> {
			script.setPriceCutoff(priceSelector.getSelectionModel().getSelectedItem());
		});
	}

	
	
}
