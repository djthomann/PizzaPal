package de.hsrm.mi.swt.gui.views;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UIView extends BorderPane {

    ToolBarView toolBarView;

    public UIView() {
        toolBarView = new ToolBarView();

        ValidatorInfo validatorInfo = new ValidatorInfo();

        this.setMaxHeight(50);
        this.setCenter(toolBarView);
        this.setBottom(validatorInfo);
        // this.setCenter(formView);
    }

}
