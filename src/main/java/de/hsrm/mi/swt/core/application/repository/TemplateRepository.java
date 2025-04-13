package de.hsrm.mi.swt.core.application.repository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.entities.StuetzeModel;
import de.hsrm.mi.swt.core.model.templates.Template;

public class TemplateRepository {

    List<Template> templates = new ArrayList<>();

    ZutatRepository zutatRepository;

    private PropertyChangeSupport support;

    public TemplateRepository(ZutatRepository zutatRepository) {
        this.zutatRepository = zutatRepository;
        this.support = new PropertyChangeSupport(this);
        // testData();
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public String saveTemplates() {

        StringBuilder sb = new StringBuilder();
        for (Template template : templates) {
            sb.append(template.toCSV());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void loadTemplate(String csv) {
        Template template = null;
        if (csv.startsWith("Paket")) {
            PaketModel paket = PaketModel.fromCSV(csv);
            paket.setInhalt(zutatRepository.getZutat(csv.split(",")[7]));
            template = new Template(paket);
        } else {
            template = Template.fromCSV(csv);
        }
        add(template);
    }

    public void testData() {
        templates.add(new Template(new PaketModel(20, 50, 40, 0, 10, 100,
                zutatRepository.getZutaten().get("Tomate"))));
        templates.add(new Template(new PaketModel(40, 20, 40, 10, 10,
                zutatRepository.getZutaten().get("Käse"))));
        templates.add(new Template(new PaketModel(60, 20, 40, 10, 10, 100,
                zutatRepository.getZutaten().get("Käse"))));
        templates.add(new Template(new PaketModel(150, 20, 40, 10, 10, 200,
                zutatRepository.getZutaten().get("Käse"))));

        templates.add(new Template(new StuetzeModel(20, 350)));
        templates.add(new Template(new StuetzeModel(20, 250)));

        templates.add(new Template(new BrettModel(120, 10, 200)));
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void remove(Template t) {
        templates.remove(t);
    }

    public void add(Template t) {
        templates.add(t);
        support.firePropertyChange("new template", null, t);
    }

    public void reset() {
        List<Template> oldValues = new ArrayList<>(this.templates);
        for(Template template : templates) {
            template.fireDelete();
        }
        templates.clear();
        support.firePropertyChange("reset", oldValues, new ArrayList<>());
        
    }
}
