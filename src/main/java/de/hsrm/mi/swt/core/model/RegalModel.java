package de.hsrm.mi.swt.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.hsrm.mi.swt.core.model.entities.*;
import de.hsrm.mi.swt.core.validation.exceptions.StuetzeInLagerException;

public class RegalModel {

    private StuetzeModel stuetzeLinks;
    private StuetzeModel stuetzeRechts;

    private List<BrettModel> bretter = new ArrayList<>();
    private List<StuetzeModel> stuetzen = new ArrayList<>();

    public RegalModel(StuetzeModel stuetzeLinks, StuetzeModel stuetzeRechts, Collection<BrettModel> bretter) {

        stuetzen.add(stuetzeLinks);
        stuetzen.add(stuetzeRechts);

        this.stuetzeLinks = stuetzeLinks;
        this.stuetzeRechts = stuetzeRechts;

        if (bretter != null) {
            this.bretter.addAll(bretter);
        }
    }

    public boolean hasBrett(BrettModel b) {
        return bretter.contains(b);
    }

    public Iterator<StuetzeModel> stuetzenIterator() {
        return stuetzen.iterator();
    }

    public StuetzeModel getStuetzeLinks() {
        return stuetzeLinks;
    }

    public StuetzeModel getStuetzeRechts() {
        return stuetzeRechts;
    }

    public void rechtsErweitern(StuetzeModel s) {
        stuetzen.add(s);
        bretter.addAll(s.getBretterLinks());
        stuetzeRechts = s;
    }

    public void linksErweitern(StuetzeModel s) {

        stuetzen.add(s);
        stuetzeLinks = s;
    }

    public void addStuetze(StuetzeModel s) {
        stuetzen.add(s);
    }

    public void removeStuetze(StuetzeModel s) {
        stuetzen.remove(s);
    }

    public void setStuetzeLinks(StuetzeModel stuetzeLinks) {
        // stuetzen.add(0, stuetzeLinks);
        this.stuetzeLinks = stuetzeLinks;
    }

    public void setStuetzeRechts(StuetzeModel stuetzeRechts) {
        // stuetzen.add(stuetzeRechts);
        this.stuetzeRechts = stuetzeRechts;
    }

    public void setBretter(List<BrettModel> bretter) {
        this.bretter = bretter;
    }

    public List<StuetzeModel> getStuetzen() {
        return stuetzen;
    }

    public void setStuetzen(List<StuetzeModel> stuetzen) {
        this.stuetzen = stuetzen;
    }

    public Iterator<BrettModel> brettIterator() {
        return bretter.iterator();
    }

    public void addBrett(BrettModel b) {
        bretter.add(b);
    }

    public List<BrettModel> getBretter() {
        return bretter;
    }

    public void removeBrett(BrettModel b) {
        bretter.remove(b);
    }

}
