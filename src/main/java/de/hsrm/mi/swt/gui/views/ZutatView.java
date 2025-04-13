package de.hsrm.mi.swt.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.ZutatRepository;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.application.service.ZutatService;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ZutatView extends VBox {

    private final ZutatService zutatService;
    private final ZutatRepository repository;
    private final Set<Zutat> unvertraeglichkeitenSet;
    private final Map<String, Zutat> zutatMap;

    private ComboBox<String> nameComboBox;
    private ColorPicker colorPicker;
    private ComboBox<String> unvertraeglichkeitComboBox;
    private ListView<String> unvertraeglichkeitenListView;

    public ZutatView() {
        zutatService = ServiceRegistry.getInstance().getZutatService();
        repository = RepositoryRegistry.getInstance().getZutatRepository();
        unvertraeglichkeitenSet = new HashSet<>();
        zutatMap = zutatService.holeAlleZutaten();
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        this.setWidth(200);
        this.setFillWidth(false);
        this.setAlignment(Pos.CENTER);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        nameComboBox = setupComboBox("Name:", zutatMap.keySet());
        colorPicker = new ColorPicker();
        colorPicker.setPrefWidth(300);
        unvertraeglichkeitComboBox = setupComboBox("Unverträglichkeit:", zutatMap.keySet());

        Button addUnvertraeglichkeitButton = new Button("Hinzufügen");
        addUnvertraeglichkeitButton.setPrefWidth(300);
        addUnvertraeglichkeitButton.setOnAction(e -> addUnvertraeglichkeit());

        unvertraeglichkeitenListView = new ListView<>();
        unvertraeglichkeitenListView.setPrefWidth(300);
        unvertraeglichkeitenListView.setPrefHeight(100);
        setupListView();

        Button saveButton = new Button("Speichern");
        Button closeButton = new Button("Schließen");
        Button deleteButton = new Button("Löschen");
        deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

        HBox buttonBox = new HBox(10, saveButton, deleteButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Name:"), nameComboBox);
        grid.addRow(1, new Label("Color:"), colorPicker);
        grid.addRow(2, new Label("Unverträglichkeit:"), unvertraeglichkeitComboBox);
        grid.addRow(3, new Label(), addUnvertraeglichkeitButton);
        grid.addRow(4, new Label(), unvertraeglichkeitenListView);
        grid.addRow(5, new Label(), buttonBox);

        this.getChildren().add(grid);

        saveButton.setOnAction(e -> saveZutat());
        closeButton.setOnAction(e -> LagerView.popFormView(this));
        deleteButton.setOnAction(e -> deleteZutat());
    }

    private ComboBox<String> setupComboBox(String labelText, Set<String> items) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(items));
        comboBox.setEditable(true);
        comboBox.setPrefWidth(300);
        return comboBox;
    }

    private void setupListView() {
        unvertraeglichkeitenListView.setCellFactory(lv -> new ListCell<String>() {
            private final HBox cellLayout = new HBox();
            private final Label label = new Label();
            private final Button deleteButton = new Button("Löschen");

            {
                deleteButton.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        unvertraeglichkeitenSet.remove(zutatService.holeZutat(item));
                        updateUnvertraeglichkeitenListView();
                    }
                });

                cellLayout.setAlignment(Pos.CENTER_LEFT);
                cellLayout.setSpacing(10);
                HBox.setHgrow(label, Priority.ALWAYS);
                label.setMaxWidth(Double.MAX_VALUE);

                // Add a spacer to push the delete button to the right
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                cellLayout.getChildren().addAll(label, spacer, deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(cellLayout);
                }
            }
        });
    }

    private void setupListeners() {
        repository.addPropertyChangeListener(this::handlePropertyChange);
        nameComboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal))
                clearForm();
        });
        nameComboBox.setOnAction(e -> {
            String selectedName = nameComboBox.getValue();
            if (selectedName != null && zutatMap.containsKey(selectedName)) {
                loadZutatIntoForm(zutatService.holeZutat(selectedName));
            }
        });
    }

    private void handlePropertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "new Zutat" -> {
                Zutat newZutat = (Zutat) e.getNewValue();
                nameComboBox.getItems().add(newZutat.getName());
                unvertraeglichkeitComboBox.getItems().add(newZutat.getName());
            }
            case "removed Zutat" -> {
                Zutat removedZutatName = (Zutat) e.getOldValue();
                nameComboBox.getItems().remove(removedZutatName.getName());
                unvertraeglichkeitComboBox.getItems().remove(removedZutatName.getName());
            }
            case "updated Zutat" -> {
                updateUnvertraeglichkeitenListView();
            }
        }
    }

    private void loadZutatIntoForm(Zutat zutat) {
        if (zutat != null) {
            nameComboBox.setValue(zutat.getName());
            colorPicker.setValue(zutat.getColor());
            unvertraeglichkeitenSet.clear();
            unvertraeglichkeitenSet.addAll(zutat.getUnvertraeglichkeiten());
            updateUnvertraeglichkeitenListView();
        }
    }

    private void clearForm() {
        colorPicker.setValue(Color.WHITE);
        unvertraeglichkeitenSet.clear();
        updateUnvertraeglichkeitenListView();
    }

    private void addUnvertraeglichkeit() {
        String unvertraeglichkeit = unvertraeglichkeitComboBox.getValue();
        if (unvertraeglichkeit != null && !unvertraeglichkeit.isEmpty()) {
            if (nameComboBox.getValue().equals(unvertraeglichkeit)) {
                showAlert("Ungültige Unverträglichkeit", "Kann nicht mit sich selbst unverträglich sein");
                return;
            }
            Zutat zutat = zutatService.holeZutat(unvertraeglichkeit);
            if (zutat != null && unvertraeglichkeitenSet.add(zutat)) {
                updateUnvertraeglichkeitenListView();
            }
        }
    }

    private void updateUnvertraeglichkeitenListView() {
        unvertraeglichkeitenListView.setItems(FXCollections.observableArrayList(
                unvertraeglichkeitenSet.stream().map(Zutat::getName).collect(Collectors.toList())));
    }

    private void saveZutat() {
        String name = nameComboBox.getValue();
        if (name != null && !name.isEmpty()) {
            Zutat zutat = zutatMap.getOrDefault(name, new Zutat(name, colorPicker.getValue(), new HashSet<>()));
            zutat.setColor(colorPicker.getValue());
            zutat.setUnvertraeglichkeiten(new HashSet<>(unvertraeglichkeitenSet));
            if (zutatMap.containsKey(name)) {
                zutatService.updateZutat(zutat);
            } else {
                zutatService.addZutat(zutat);
            }
            //
            System.out
                    .println("Saved Zutat: " + zutat.getName() + "unver" + zutat.getUnvertraeglichkeiten().toString());
        }
    }

    private void deleteZutat() {
        String selectedName = nameComboBox.getValue();
        if (selectedName != null && !selectedName.isEmpty()) {
            zutatService.removeZutat(selectedName);
        } else {
            showAlert("Keine Zutat ausgewählt", "Bitte wählen Sie eine Zutat zum Löschen aus.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}