package de.hsrm.mi.swt.gui;

import de.hsrm.mi.swt.Config;
import de.hsrm.mi.swt.Helper;
import de.hsrm.mi.swt.core.application.repository.RepositoryRegistry;
import de.hsrm.mi.swt.core.application.repository.ZutatRepository;
import de.hsrm.mi.swt.core.application.service.ServiceRegistry;
import de.hsrm.mi.swt.gui.views.LagerView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Hauptfenster für Lager-Anzeige, bettet im Wesentlichen
 * eine AmpelComponent zur Visualisierung des AmpelModel ein.
 */
public class LagerApplication extends Application {

	private ZutatRepository zutatRepository;

	@Override
	public void start(Stage primaryStage) throws Exception {

		zutatRepository = new ZutatRepository();

		// zutatRepository.ladeZustand();

		RepositoryRegistry repositoryRegistry = RepositoryRegistry.getInstance();
		ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

		LagerView lagerView = new LagerView();

		Parent root = lagerView;

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Config.APP_TITLE);
		primaryStage.setResizable(Config.RESIZABLE);
		primaryStage.setHeight(Config.FRAME_HEIGHT);
		primaryStage.setWidth(Config.FRAME_WIDTH);
		primaryStage.show();

		Image icon = Helper.loadImage("/icons/app_icon.png");
		primaryStage.getIcons().add(icon);

	}

}
