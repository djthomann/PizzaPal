package de.hsrm.mi.swt.core.application.service;

import de.hsrm.mi.swt.core.model.LagerModel;
import de.hsrm.mi.swt.core.model.zutat.Zutat;

public class ServiceRegistry {

    private static ServiceRegistry instance;

    private final LagerService lagerService;
    private final PlatzierService platzierService;
    private final ZutatService zutatService;
    private final TemplateService templateService;

    private ServiceRegistry(ZutatService zutatService, LagerService lagerService, PlatzierService platzierService,
            TemplateService templateService) {
        this.zutatService = zutatService;
        this.lagerService = lagerService;
        this.platzierService = platzierService;
        this.templateService = templateService;
    }

    public static synchronized ServiceRegistry getInstance() {
        if (instance == null) {
            ZutatService zutatService = new ZutatService();

            TemplateService templateService = new TemplateService();

            LagerService lagerService = new LagerService();// Hier wird der ZutatService an den
            // LagerService übergeben

            PlatzierService platzierService = new PlatzierService();
            instance = new ServiceRegistry(zutatService, lagerService, platzierService,
                    templateService);
        }
        return instance;
    }

    public LagerService getLagerService() {
        return lagerService;
    }

    public PlatzierService getPlatzierService() {
        return platzierService;
    }

    public ZutatService getZutatService() {
        return zutatService;
    }

    public TemplateService getTemplateService() {
        return templateService;
    }

}
