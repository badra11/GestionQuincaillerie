package com.bad.gestionquincaillerie.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.Alert;

import org.mindrot.jbcrypt.BCrypt;
import com.bad.gestionquincaillerie.models.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        boolean baseOk = InitialisationBD.initialiserSiNecessaire();

        if (!baseOk) {
            Alert erreur = new Alert(Alert.AlertType.ERROR);
            erreur.setTitle("Erreur de base de données");
            erreur.setHeaderText(null);
            erreur.setContentText("Impossible de se connecter à MySQL. Vérifiez que XAMPP est bien démarré (service MySQL actif).");
            erreur.showAndWait();
            return; // empêche l'application de continuer sans base fonctionnelle
        }

        scene = new Scene(loadFXML("views/Login"), 640, 480);
        stage.setTitle("Gestion Quincaillerie - Connexion");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/bad/gestionquincaillerie/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        

    }

}
