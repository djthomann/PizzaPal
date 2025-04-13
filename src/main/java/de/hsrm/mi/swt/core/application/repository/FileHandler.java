package de.hsrm.mi.swt.core.application.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.RegalModel;
import de.hsrm.mi.swt.core.model.entities.BrettModel;
import de.hsrm.mi.swt.core.model.entities.PaketModel;
import de.hsrm.mi.swt.core.model.templates.Template;
import de.hsrm.mi.swt.core.model.zutat.Zutat;

public class FileHandler {

    RepositoryRegistry registry;
    ZutatRepository zutatenRepository;
    LagerRepository lagerRepository;
    TemplateRepository templateRepository;

    private Map<String, Zutat> zutaten;
    private List<RegalModel> regale;
    private List<BrettModel> bretter;
    private List<PaketModel> pakete;
    private LagerModel lager;
    private List<Template> templates;

    public FileHandler() throws IOException {
        this.registry = RepositoryRegistry.getInstance();
        this.zutatenRepository = registry.getZutatRepository();
        this.lagerRepository = registry.getLagerRepository();
        this.templateRepository = registry.getTemplateRepository();

        this.zutaten = zutatenRepository.getZutaten();
        // regale      = lagerRepository.getRegale();
        this.bretter = lagerRepository.getBretter();
        this.pakete = lagerRepository.getPakete();
        this.lager = lagerRepository.getLager();
        this.templates = templateRepository.getTemplates();
    }

    public void saveRepositories(String filePath) throws IOException {
        registry.saveToJson(filePath);
    }
}
