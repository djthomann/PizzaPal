package de.hsrm.mi.swt.core.application.service;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.TemplateRepository;
import de.hsrm.mi.swt.core.model.templates.Template;

public class TemplateService {

    List<Template> templates = new ArrayList<>();

    RepositoryRegistry rr = RepositoryRegistry.getInstance();
    TemplateRepository repository = rr.getTemplateRepository();

    public TemplateService() {
    }

    // public void addTemplate() {
    // Template neu = new Template();
    // repository.add(neu);
    // }

    public void addTemplate(Template template) {
        repository.add(template);
    }

    public Iterator<Template> holeTemplates() {
        return repository.getTemplates().iterator();
    }

    public void loescheTemplate(Template t) {
        t.fireDelete();
        repository.remove(t);
    }

}
