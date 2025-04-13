package de.hsrm.mi.swt.core.application.repository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.hsrm.mi.swt.core.model.zutat.Zutat;
import javafx.scene.paint.Color;

public class ZutatRepository {
    @JsonManagedReference

    private Map<String, Zutat> zutaten;
    private static final String filePath = "src/main/resources/zutaten.json";
    private final PropertyChangeSupport support;

    public ZutatRepository() {
        this.zutaten = new HashMap<>();
        this.support = new PropertyChangeSupport(this);
        // testData();
    }

    public String saveZutaten() {

        StringBuilder sb = new StringBuilder();
        for (Entry<String, Zutat> entry : zutaten.entrySet()) {
            sb.append(entry.getValue().toCSV());
            sb.append("\n");
        }

        return sb.toString();

    }

    public String saveUnvertraeglichkeiten() {

        StringBuilder sb = new StringBuilder();
        for (Entry<String, Zutat> entry : zutaten.entrySet()) {
            if (entry.getValue().getUnvertraeglichkeiten().size() > 0) {
                sb.append(entry.getValue().unvertraeglichkeitenToCSV());
                sb.append("\n");
            }
        }

        return sb.toString();

    }

    public void loadUnvertraeglichkeiten(String csv) {
        String[] parts = csv.split(",");
        String[] unvertraeglichkeiten = parts[1].split(";");
        Zutat zutat = getZutat(parts[0]);
        for (String unvertraeglichkeit : unvertraeglichkeiten) {
            zutat.addUnvertraeglichkeit(getZutat(unvertraeglichkeit));
        }
    }

    public void loadZutat(String csv) {
        Zutat zutat = Zutat.fromCSV(csv);
        addZutat(zutat);
    }

    private void testData() {
        Set<Zutat> testunvertraeglichkeiten = new HashSet<>();
        Zutat zutat1 = new Zutat("Tomate", new Color(1, 0, 0, 1), testunvertraeglichkeiten);
        zutaten.put(zutat1.getName(), zutat1);
        Zutat zutat2 = new Zutat("Käse", new Color(1, 1, 0, 1), testunvertraeglichkeiten);
        zutaten.put(zutat2.getName(), zutat2);
        Zutat zutat3 = new Zutat("Aubergine", Color.PURPLE, testunvertraeglichkeiten);
        zutaten.put(zutat3.getName(), zutat3);

        zutat1.addUnvertraeglichkeit(zutat2);
        zutat2.addUnvertraeglichkeit(zutat1);
        zutat3.addUnvertraeglichkeit(zutat2);
    }

    public Zutat getZutat(String zutat) {
        support.firePropertyChange("fetch Zutat", null, zutat);
        return zutaten.get(zutat);
    }

    public void addZutat(Zutat zutat) {
        zutaten.put(zutat.getName(), zutat);
        support.firePropertyChange("new Zutat", null, zutat);
        // speichereZustand();
    }

    public void removeZutat(Zutat zutat) {
        zutaten.remove(zutat.getName());
        zutaten.values().forEach(z -> z.getUnvertraeglichkeiten().remove(zutat));
        support.firePropertyChange("removed Zutat", zutat, null);
        speichereZustand();
    }

    public void updateZutat(Zutat zutat) {
        zutaten.put(zutat.getName(), zutat);
        support.firePropertyChange("updated Zutat", null, zutat);
        speichereZustand();
    }

    public Map<String, Zutat> getZutaten() {
        return zutaten;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void speichereZustand() {
        // Gson gson = new GsonBuilder()
        // .registerTypeAdapter(Color.class, new ColorSerializer())
        // .setPrettyPrinting()
        // .create();
        // try (FileWriter writer = new FileWriter(FILE_PATH)) {
        // gson.toJson(zutaten, writer);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

    public void save() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            objectMapper.writeValue(new File(filePath), zutaten);
            // System.out.println("Repositories saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving repositories: " + e.getMessage());
            throw e;
        }
    }

    public void ladeZustand() {
        // Gson gson = new GsonBuilder()
        // .registerTypeAdapter(Color.class, new ColorDeserializer())
        // .create();
        // try (FileReader reader = new FileReader(FILE_PATH)) {
        // zutaten = gson.fromJson(reader, new TypeToken<ObservableMap<String,
        // Zutat>>(){}.getType());
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }
    // public void ladeZustand() {
    // Gson gson = new GsonBuilder()
    // .registerTypeAdapter(Color.class, new ColorDeserializer())
    // .create();
    // try (FileReader reader = new FileReader(FILE_PATH)) {
    // zutaten = gson.fromJson(reader, new TypeToken<Map<String, Zutat>>() {
    // }.getType());
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // Testausgabe der geladenen Zutaten
    // for (Map.Entry<String, Zutat> entry : zutaten.entrySet()) {
    // Zutat zutat = entry.getValue();
    // System.out.println("Name: " + zutat.getName());
    // System.out.println("Farbe: " + zutat.getColor());
    // System.out.println("Unverträglichkeiten: " +
    // zutat.getUnvertraeglichkeiten());
    // System.out.println();
    // }
    public void reset() {
        Map<String, Zutat> oldValues = new HashMap<>(this.zutaten);
        this.zutaten.clear();
        support.firePropertyChange("zutaten reset", oldValues, new HashMap<>());
    }
}
