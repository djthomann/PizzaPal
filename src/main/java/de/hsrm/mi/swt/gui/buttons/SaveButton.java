package de.hsrm.mi.swt.gui.buttons;

import java.io.File;
import java.io.IOException;

import de.hsrm.mi.swt.core.application.repository.DavidsFileHandler;
import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.validation.LagerValidator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveButton extends IconButton {

    private static final Image ACTIVE_ICON = loadImage("/icons/save_active.png");

    private DavidsFileHandler fileHandler = RepositoryRegistry.getInstance().getFileHandler();
    LagerValidator validator = ServiceRegistry.getInstance().getLagerService().getLagerValidator();

    public SaveButton(int x, int y) {
        super(ACTIVE_ICON, x, y, new Tooltip("Speichern"));
        setOnAction(event -> {
            try {
                Stage stage = (Stage) this.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(DavidsFileHandler.FILE_DIRECTORY));
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Lager-Dateien (*.lager)",
                        "*.lager");
                fileChooser.getExtensionFilters().add(extFilter);
                File selectedFile = fileChooser.showSaveDialog(stage);
                if (selectedFile != null) {
                    fileHandler.writeToFile(selectedFile);
                }
            } catch (IOException e) {
                // System.out.println(e.getMessage());
                validator.setzeInfo("Dateispeichern hat nicht geklappt");
            }
        });
    }
}
