package de.hsrm.mi.swt.core.application.repository;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RepositoryRegistry {

    private static RepositoryRegistry instance;

    private DavidsFileHandler fileHandler;

    private TemplateRepository templateRepository;
    private ZutatRepository zutatRepository;
    private LagerRepository lagerRepository;

    private RepositoryRegistry(TemplateRepository templateRepository, ZutatRepository zutatRepository,
            LagerRepository lagerRepository, DavidsFileHandler fileHandler) {
        this.templateRepository = templateRepository;
        this.zutatRepository = zutatRepository;
        this.lagerRepository = lagerRepository;
        this.fileHandler = fileHandler;
    }

    public static synchronized RepositoryRegistry getInstance() {
        if (instance == null) {
            ZutatRepository zutatRepository = new ZutatRepository();
            TemplateRepository templateRepository = new TemplateRepository(zutatRepository);
            LagerRepository lagerRepository = new LagerRepository(zutatRepository);

            DavidsFileHandler fileHandler = new DavidsFileHandler(zutatRepository, templateRepository, lagerRepository);

            instance = new RepositoryRegistry(templateRepository, zutatRepository, lagerRepository, fileHandler);
        }
        return instance;
    }

    public DavidsFileHandler getFileHandler() {
        return fileHandler;
    }

    public TemplateRepository getTemplateRepository() {
        return templateRepository;
    }

    public ZutatRepository getZutatRepository() {
        return zutatRepository;
    }

    public LagerRepository getLagerRepository() {
        return lagerRepository;
    }
    
    public void saveToJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            objectMapper.writeValue(new File(filePath), this);
            // System.out.println("Repositories saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving repositories: " + e.getMessage());
            throw e;
        }
    }
}
