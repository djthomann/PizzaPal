package de.hsrm.mi.swt.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.TemplateRepository;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.core.application.service.TemplateService;
import de.hsrm.mi.swt.core.model.templates.Template;
import de.hsrm.mi.swt.gui.buttons.TemplateButton;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class TemplateBar extends HBox {

    ServiceRegistry sr = ServiceRegistry.getInstance();

    TemplateService templateService = sr.getTemplateService();
    TemplateRepository templateRepository = RepositoryRegistry.getInstance().getTemplateRepository();

    public TemplateBar() {
        templateRepository.addPropertyChangeListener((PropertyChangeEvent e) -> {

            if (e.getPropertyName().equals("new template")) {
                addTemplateComponent(e.getNewValue());
            }
        });

        this.setAlignment(Pos.CENTER_LEFT);

        initComponents();
    }

    public void initComponents() {
        Iterator<Template> templateIterator = templateService.holeTemplates();

        while (templateIterator.hasNext()) {
            addTemplateComponent(templateIterator.next());
        }
    }

    public void addTemplateComponent(Object t) {
        Template template = (Template) t;
        TemplateButton templateButton = new TemplateButton(this, template);
        setMargin(templateButton, new Insets(2, 10, 0, 10));

        this.getChildren().add(templateButton);
    }

}
