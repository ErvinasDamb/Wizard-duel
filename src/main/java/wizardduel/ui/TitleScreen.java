package wizardduel.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wizardduel.model.enums.AiDifficulty;

public class TitleScreen {

    private static final GameSession SESSION = new GameSession();

    public static Scene create(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Wizard Duel");
        title.getStyleClass().add("title-label");

        Button startButton = new Button("Start Game");
        Button deckButton = new Button("Create Deck");
        ComboBox<AiDifficulty> difficultyBox = new ComboBox<>();

        difficultyBox.getItems().addAll(AiDifficulty.values());
        difficultyBox.getSelectionModel().select(SESSION.getDifficulty());

        startButton.setPrefWidth(200);
        deckButton.setPrefWidth(200);
        difficultyBox.setPrefWidth(200);

        startButton.setDisable(!SESSION.hasValidDeck());

        startButton.setOnAction(e -> {
            if (!SESSION.hasValidDeck()) {
                return;
            }
            Scene battleScene = BattleScreen.create(stage, SESSION);
            stage.setScene(battleScene);
        });

        deckButton.setOnAction(e -> {
            Scene deckScene = DeckBuilderScreen.create(stage, SESSION);
            stage.setScene(deckScene);
        });

        difficultyBox.setOnAction(e -> {
            AiDifficulty selected = difficultyBox.getValue();
            if (selected != null) {
                SESSION.setDifficulty(selected);
            }
        });

        VBox centerBox = new VBox(15, startButton, deckButton, difficultyBox);
        centerBox.setAlignment(Pos.CENTER);

        HBox topBox = new HBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 30, 0));

        root.setTop(topBox);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1000, 700);

        var cssUrl = TitleScreen.class.getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        return scene;

    }
}
