package com.makar.scripts.swarmfisher.gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.makar.scripts.swarmfisher.SwarmFisher;

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

public class SwarmGUIController implements Initializable {

	private SwarmFisher script;

	@FXML
	private Text levelText;

	@FXML
	private ProgressBar levelBar;

	@FXML
	private TableView<Map.Entry<String, String>> fishTable;

	public SwarmGUIController(SwarmFisher script) {
		this.script = script;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TableColumn<Map.Entry<String, String>, String> fishColumn = new TableColumn<>("Fish");
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
	
		fishTable.getColumns().add(fishColumn);
		fishTable.getColumns().add(amountColumn);
		update();
	}
	
	public void update() {
		fishTable.setItems(FXCollections.observableArrayList(script.getFishCaught().entrySet()));
		fishTable.refresh();
		levelBar.setProgress(script.getLevelProgress());
		levelText.setText(script.getLevelText());
	}
}
