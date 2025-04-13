package de.hsrm.mi.swt.core.application.service;

import java.util.Map;

import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.ZutatRepository;
import de.hsrm.mi.swt.core.model.zutat.Zutat;

public class ZutatService {

    private final ZutatRepository zutatRepository;

    public ZutatService() {
        this.zutatRepository = RepositoryRegistry.getInstance().getZutatRepository();
        this.zutatRepository.ladeZustand();
    }

    public void addZutat(Zutat zutat) {
        if (zutat == null || zutat.getName() == null) {
            throw new IllegalArgumentException("Zutat or Zutat name cannot be null");
        }
        zutatRepository.addZutat(zutat);
    }

    public void updateZutat(Zutat zutat) {
        if (zutat == null || zutat.getName() == null) {
            throw new IllegalArgumentException("Zutat or Zutat name cannot be null");
        }

        if (zutatRepository.getZutaten().containsKey(zutat.getName())) {
            zutatRepository.updateZutat(zutat);
        } else {
            addZutat(zutat);
        }
    }

    public void removeZutat(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid Zutat name provided");
        }

        Zutat zutat = zutatRepository.getZutaten().get(name);
        if (zutat != null) {
            zutatRepository.removeZutat(zutat);
        } else {
            // System.out.println("Zutat not found: " + name);
        }
    }

    public Zutat holeZutat(String name) {
        return zutatRepository.getZutat(name);
    }

    public Map<String, Zutat> holeAlleZutaten() {
        return zutatRepository.getZutaten();
    }
}