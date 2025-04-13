package de.hsrm.mi.swt.core.application.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DavidsFileHandler {

    public static final String SHEBANG = "#!/Lagerfile";
    public static final String ZUTATEN = "#Zutaten";
    public static final String UNVERTRAEGLICHKEITEN = "#Unvertraeglichkeiten";
    public static final String TEMPLATES = "#Templates";
    public static final String LAGER = "#Lager";
    public static final String VERKNUEPFUNGEN_STUETZEN_BRETTER = "#VerknuepfungenStuetzenBretter";
    public static final String VERKNUEPFUNGEN_BRETTER_PAKETE = "#VerknuepfungenBretterPakete";
    public static final String VERKNUEPFUNGEN_PAKETE_PAKETE = "#VerknuepfungenPaketePakete";
    public static final String REGALE = "#Regale";

    public static final String FILE_DIRECTORY = "src/main/resources/files/";

    private ZutatRepository zutatRepository;
    private TemplateRepository templateRepository;
    private LagerRepository lagerRepository;

    public DavidsFileHandler(ZutatRepository zutatRepository, TemplateRepository templateRepository,
            LagerRepository lagerRepository) {
        this.zutatRepository = zutatRepository;
        this.templateRepository = templateRepository;
        this.lagerRepository = lagerRepository;
    }

    public void readFromFile(File file) throws IOException {

        

        zutatRepository.reset();
        templateRepository.reset();
        lagerRepository.reset();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (!line.equals(SHEBANG)) {
                throw new IOException("Invalid file format");
            }
            line = reader.readLine();
            if (!line.equals(ZUTATEN)) {
                throw new IOException("Invalid file format");
            }
            while (!(line = reader.readLine()).equals(UNVERTRAEGLICHKEITEN)) {
                zutatRepository.loadZutat(line);
            }
            while (!(line = reader.readLine()).equals(LAGER)) {
                zutatRepository.loadUnvertraeglichkeiten(line);
            }
            while (!(line = reader.readLine()).equals(VERKNUEPFUNGEN_STUETZEN_BRETTER)) {
                lagerRepository.loadLager(line);
            }
            while (!(line = reader.readLine()).equals(VERKNUEPFUNGEN_BRETTER_PAKETE)) {
                lagerRepository.verknuepfeBretterUndStuetzen(line);
            }
            while (!(line = reader.readLine()).equals(VERKNUEPFUNGEN_PAKETE_PAKETE)) {
                lagerRepository.verknuepfeBretterUndPakete(line);
            }
            while (!(line = reader.readLine()).equals(REGALE)) {
                lagerRepository.verknuepfePaketeUndPakete(line);
            }
            while (!(line = reader.readLine()).equals(TEMPLATES)) {
                lagerRepository.loadRegale(line);
            }
            while ((line = reader.readLine()) != null) {
                templateRepository.loadTemplate(line);
            }
        }
    }

    public void writeToFile(File file) throws IOException {

        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_DIRECTORY + "Test.txt"))) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(SHEBANG);
            writer.newLine();

            writer.write(ZUTATEN);
            writer.newLine();
            writer.write(zutatRepository.saveZutaten());

            writer.write(UNVERTRAEGLICHKEITEN);
            writer.newLine();
            writer.write(zutatRepository.saveUnvertraeglichkeiten());

            writer.write(LAGER);
            writer.newLine();
            writer.write(lagerRepository.saveLager());

            writer.write(TEMPLATES);
            writer.newLine();
            writer.write(templateRepository.saveTemplates());
        }
    }

}
