package wizardduel.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WizardDuelApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene = TitleScreen.create(primaryStage);
        primaryStage.setTitle("Wizard Duel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
