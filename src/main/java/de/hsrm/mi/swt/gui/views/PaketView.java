package de.hsrm.mi.swt.gui.views;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.TemplateRepository;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.application.service.TemplateService;
import de.hsrm.mi.swt.core.application.service.ZutatService;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.templates.Template;
import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaketView extends VBox {

    private final TemplateService templateService;
    private final ZutatService zutatService;
    private final TemplateRepository templateRepository;
    private ComboBox<Zutat> zutatComboBox;
    private TextField hoeheField, breiteField, gewichtField, tragKraftField;
    private Rectangle colorBox;

    public PaketView() {
        templateService = ServiceRegistry.getInstance().getTemplateService();
        zutatService = ServiceRegistry.getInstance().getZutatService();
        templateRepository = RepositoryRegistry.getInstance().getTemplateRepository();
        setupUI();
        // setupListeners();
    }

    private void setupUI() {
        setWidth(200);
        setFillWidth(false);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        setupZutatComboBox();
        setupColorBox();
        setupTextFields();
        setupButtons(grid);

        grid.addRow(0, new Label("Zutat:"), zutatComboBox);
        grid.addRow(1, new Label(), colorBox);
        grid.addRow(2, new Label("Höhe:"), hoeheField);
        grid.addRow(3, new Label("Breite:"), breiteField);
        grid.addRow(4, new Label("Gewicht:"), gewichtField);
        grid.addRow(5, new Label("Tragkraft:"), tragKraftField);

        getChildren().add(grid);
    }

    private void setupZutatComboBox() {
        zutatComboBox = new ComboBox<>(FXCollections.observableArrayList(zutatService.holeAlleZutaten().values()));
        zutatComboBox.setPrefWidth(300);
        zutatComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Zutat item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        zutatComboBox.setButtonCell(zutatComboBox.getCellFactory().call(null));
        zutatComboBox.valueProperty().addListener((obs, oldZutat, newZutat)
                -> colorBox.setFill(newZutat != null ? newZutat.getColor() : Color.TRANSPARENT));
    }

    private void setupColorBox() {
        colorBox = new Rectangle(300, 50);
        colorBox.setFill(Color.TRANSPARENT);
        colorBox.setStroke(Color.BLACK);
    }

    private void setupTextFields() {
        hoeheField = createNumericTextField();
        breiteField = createNumericTextField();
        gewichtField = createNumericTextField();
        tragKraftField = createNumericTextField();
    }

    private TextField createNumericTextField() {
        TextField field = new TextField();
        field.setPrefWidth(300);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        return field;
    }

    private void setupButtons(GridPane grid) {
        Button createButton = new Button("Paket erstellen");
        Button closeButton = new Button("Schließen");

        createButton.setOnAction(e -> createPaket());
        closeButton.setOnAction(e -> LagerView.popFormView(this));

        VBox buttonContainer = new VBox(10, createButton, closeButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().forEach(button -> ((Button) button).setPrefWidth(150));

        grid.add(buttonContainer, 1, 6);
    }

    private void createPaket() {
        Zutat zutat = zutatComboBox.getValue();
        try {
            int hoehe = Integer.parseInt(hoeheField.getText());
            int breite = Integer.parseInt(breiteField.getText());
            int gewicht = Integer.parseInt(gewichtField.getText());
            int tragkraft = Integer.parseInt(tragKraftField.getText());
            if (zutat != null && hoehe > 0 && breite > 0 && gewicht > 0) {
                PaketModel paket = new PaketModel(breite, hoehe, 10, 20, gewicht, tragkraft, zutat, null);
                Template paketTemplate = new Template(paket);
                templateService.addTemplate(paketTemplate);
                clearForm();
                // System.out.println("Alle Pakete: " + templateRepository.getTemplates());
                LagerView.popFormView(this);
            } else {
                showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Werte für alle Felder ein.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Zahlenwerte ein.");
        }
    }

    private void clearForm() {
        zutatComboBox.setValue(null);
        hoeheField.clear();
        breiteField.clear();
        gewichtField.clear();
        colorBox.setFill(Color.TRANSPARENT);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}