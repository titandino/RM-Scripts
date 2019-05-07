package com.makar.scripts.gemminer.gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.makar.scripts.gemminer.GemMiner;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class GemGUIController implements Initializable {

	private GemMiner script;

	@FXML
	private Text levelText;

	@FXML
	private ProgressBar levelBar;

	@FXML
	private TableView<Map.Entry<String, String>> gemTable;

	public GemGUIController(GemMiner script) {
		this.script = script;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TableColumn<Map.Entry<String, String>, String> fishColumn = new TableColumn<>("Gem");
		fishColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
				return new SimpleStringProperty(p.getValue().getKey());
			}
		});

		TableColumn<Map.Entry<String, String>, String> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
				return new SimpleStringProperty(p.getValue().getValue());
			}
		});
		
		fishColumn.setMinWidth(180);
	
		gemTable.getColumns().add(fishColumn);
		gemTable.getColumns().add(amountColumn);
		update();
	}
	
	public void update() {
		gemTable.setItems(FXCollections.observableArrayList(script.getGemsMined().entrySet()));
		gemTable.refresh();
		levelBar.setProgress(script.getLevelProgress());
		levelText.setText(script.getLevelText());
	}
}
