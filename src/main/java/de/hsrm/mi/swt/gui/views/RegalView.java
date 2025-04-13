package de.hsrm.mi.swt.gui.views;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.TemplateRepository;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.application.service.TemplateService;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.templates.Template;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RegalView extends VBox {

    private final TemplateService templateService;
    private final TemplateRepository templateRepository;
    private TextField hoeheField, breiteField, gewichtField;

    public RegalView() {
        templateService = ServiceRegistry.getInstance().getTemplateService();
        templateRepository = RepositoryRegistry.getInstance().getTemplateRepository();
        setupUI();
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

        setupTextFields();
        setupButtons(grid);

        grid.addRow(0, new Label("Höhe:"), hoeheField);
        grid.addRow(1, new Label("Breite:"), breiteField);
        grid.addRow(2, new Label("(Brett-)Tragkraft:"), gewichtField);

        getChildren().add(grid);
    }

    private void setupTextFields() {
        hoeheField = createNumericTextField();
        breiteField = createNumericTextField();
        gewichtField = createNumericTextField();
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
        Button createStuetzeButton = new Button("Stütze erstellen");
        Button createBrettButton = new Button("Brett erstellen");
        Button closeButton = new Button("Schließen");

        createStuetzeButton.setOnAction(e -> createStuetze());
        createBrettButton.setOnAction(e -> createBrett());
        closeButton.setOnAction(e -> LagerView.popFormView(this));

        VBox buttonContainer = new VBox(10, createStuetzeButton, createBrettButton, closeButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().forEach(button -> ((Button) button).setPrefWidth(150));

        grid.add(buttonContainer, 1, 3);
    }

    private void createBrett() {
        try {
            int hoehe = Integer.parseInt(hoeheField.getText());
            int breite = Integer.parseInt(breiteField.getText());
            int gewicht = Integer.parseInt(gewichtField.getText());
            if (hoehe > 0 && breite > 0) {
                BrettModel brett = new BrettModel(breite, hoehe, gewicht);
                Template brettTemplate = new Template(brett);
                templateService.addTemplate(brettTemplate);
                clearForm();
                // System.out.println("Alle Templates: " + templateRepository.getTemplates());
                LagerView.popFormView(this);
            } else {
                showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Werte für alle Felder ein.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Zahlenwerte ein.");
        }
    }

    private void createStuetze() {
        try {
            int hoehe = Integer.parseInt(hoeheField.getText());
            int breite = Integer.parseInt(breiteField.getText());
            if (hoehe > 0 && breite > 0) {
                StuetzeModel stuetze = new StuetzeModel(breite, hoehe);
                Template stuetzenTemplate = new Template(stuetze);
                templateService.addTemplate(stuetzenTemplate);
                clearForm();
                LagerView.popFormView(this);
            } else {
                showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Werte für alle Felder ein.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Ungültige Eingabe", "Bitte geben Sie gültige Zahlenwerte ein.");
        }
    }

    private void clearForm() {
        hoeheField.clear();
        breiteField.clear();
        gewichtField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}