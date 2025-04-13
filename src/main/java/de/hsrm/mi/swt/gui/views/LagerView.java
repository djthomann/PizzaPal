package de.hsrm.mi.swt.gui.views;

import java.util.Iterator;

import de.hsrm.mi.swt.core.application.service.LagerService;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.gui.GUIConfig;
import de.hsrm.mi.swt.gui.components.BrettComponent;
import de.hsrm.mi.swt.gui.components.StuetzeComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class LagerView extends StackPane {

    ServiceRegistry sr = ServiceRegistry.getInstance();

    LagerService service = sr.getLagerService();

    UIView uiView;

    Pane componentView;

    static StackPane formView = new StackPane();

    public LagerView() {

        this.setBackground(
                new Background(new BackgroundFill(GUIConfig.LAGER_HINTERGRUND, CornerRadii.EMPTY, Insets.EMPTY)));

        componentView = new ComponentView();
        uiView = new UIView();

        setAlignment(uiView, Pos.TOP_CENTER);
        formView.setPickOnBounds(false);

        this.getChildren().add(componentView);
        this.getChildren().add(uiView);
        this.getChildren().add(formView);
    }

    public static void pushFormView(Pane pane) {
        // System.out.println("push" + pane);
        formView.getChildren().add(pane);
    }

    public static void popFormView(Pane pane) {
        // System.out.println("pop" + pane);
        formView.getChildren().remove(pane);
    }

}
