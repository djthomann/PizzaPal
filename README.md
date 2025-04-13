# PizzaPal

Lagersoftware für Pizzerien zum einfachen Verwalten von Zutaten, Lagerbeständen und Lageranordnung im Modul "Softwaretechnik"

![Lageransicht](/doku/readme/Lageransicht.png)

 Projektthema: Wir unterstützen die Pizza-Produktion
Nach der Homeoffice-Epidemie bestellen viele Menschen ihr Mittagessen nach Hause. Die Vorratsräume der bekannten Pizza-Manufakturen platzen durch 
die erhöhte Nachfrage aus allen Nähten, und da bauliche Erweiterungen nichtimmer möglich sind, muss der vorhandene Ablageplatz für Zutaten effizienter 
genutzt werden. Der Zentralverband der Freunde italienischen Soßenbrotes e.V. möchte seine Mitglieder bei der Nutzungsoptimierung ihrer Vorratsräume 
unterstützen und beauftragt ein interaktives Software-Tool, mit die Verteilung der Zutaten- Packungen (repräsentiert als farbige Rechtecke, Farbe 
repräsentiert Art der Zutat) auf den verfügbaren Ablageflächen optimieren können. Ein Ablagesystem besteht aus einer Anzahl von Stützen, die in einem 
bestimmten Abstand aufgestellt sind, und darin eingehängten Ablageflächen (mit möglicherweise unterschiedlicher, aber begrenzter Tragkraft). 
Die einzulagernden Packungen haben jeweils neben Breite, Höhe, Gewicht und Inhalt auch eine begrenzte Tragfähigkeit. Packungen dürfen zwar gestapelt 
werden, allerdings nur bis zum Erreichen ihrer individuellen Tragfähigkeit und die Ablagefläche einer Packung muss vollständig auf dem Deckel der 
darunterliegenden ruhen (darf also nicht überstehen). Darüber hinaus kann zu einer Packung auch eine Liste von Inhalten konfiguriert werden, mit denen 
sie unverträglich ist. Eine solche Packung darf nicht zusammen mit einer anderen im Bereich derselben Ablagefläche landen, wenn der Inhalt einer Packung 
in der „Sperrliste“ einer anderen vorkommt (Bereich = Ablagefläche mit darüberliegendem Raum bis zur nächsthöheren Ablagefläche (oder „Decke“), in der 
Abb. beispielhaft unten rechts rot gestrichelter Bereich zwischen zwei benachbarten Ablageflächen und -stützen) gelagert werden. Packungen können 
gelöscht oder im Ablagesystem umgelagert werden. Wird eine Packung verschoben, die andere Packungen trägt, so wird der ganze Packungs-Stapel bewegt 
(Kartonstapel bilden verschieben zu können ist im Pizza-Business ja nicht unwichtig)
