package wizardduel.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wizardduel.factory.SpellFactory;
import wizardduel.mechanics.Battle;
import wizardduel.mechanics.ai.AIStrategyFactory;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen {

    public static Scene create(Stage stage, GameSession session) {
        PlayerWizard player = new PlayerWizard(
                "Player",
                session.getPlayerDeck()
        );
        if (session.getSelectedPotion() != null) {
            player.equipPotion(session.getSelectedPotion());
        }

        EnemyWizard enemy = new EnemyWizard(
                "Enemy",
                SpellFactory.createDefaultEnemyDeck()
        );

        var aiStrategy = AIStrategyFactory.create(session.getDifficulty());

        Label playerHpLabel = new Label();
        Label playerManaLabel = new Label();
        Label enemyHpLabel = new Label();

        Label logLabel = new Label();
        logLabel.setWrapText(true);

        VBox effectsBox = new VBox(5);
        effectsBox.setPadding(new Insets(5));

        Button skipTurnButton = new Button("Skip Turn\n(Regain extra mana)");
        skipTurnButton.setMaxWidth(Double.MAX_VALUE);

        Button spellButton1 = new Button();
        Button spellButton2 = new Button();
        Button spellButton3 = new Button();

        List<Button> spellButtons = List.of(spellButton1, spellButton2, spellButton3);

        for (Button b : spellButtons) {
            b.setWrapText(true);
            b.setMaxWidth(Double.MAX_VALUE);
        }

        Button potionButton = new Button();
        potionButton.setWrapText(true);
        potionButton.setMaxWidth(Double.MAX_VALUE);

        UiBattleController ui = new UiBattleController(
                player,
                enemy,
                playerHpLabel,
                playerManaLabel,
                enemyHpLabel,
                effectsBox,
                logLabel,
                skipTurnButton,
                spellButtons,
                potionButton
        );

        Battle battle = new Battle(
                player,
                enemy,
                aiStrategy,
                ui
        );

        skipTurnButton.setOnAction(e -> {
            if (!player.isAlive() || !enemy.isAlive()) return;
            battle.playerSkipTurn();
            ui.refreshAll();
        });

        for (int i = 0; i < spellButtons.size(); i++) {
            Button btn = spellButtons.get(i);
            int index = i;
            btn.setOnAction(e -> {
                if (!player.isAlive() || !enemy.isAlive()) return;
                List<Spell> hand = new ArrayList<>(player.getHand());
                if (index >= hand.size()) return;
                Spell selected = hand.get(index);
                battle.playerCastSpell(selected);
                ui.refreshAll();
            });
        }

        potionButton.setOnAction(e -> {
            if (!player.isAlive() || !enemy.isAlive()) return;
            if (player.getEquippedPotion() == null) return;
            battle.playerUsePotion();
            ui.refreshAll();
        });

        VBox playerInfoBox = new VBox(5, playerHpLabel, playerManaLabel);
        playerInfoBox.setPadding(new Insets(5));

        VBox enemyInfoBox = new VBox(5, enemyHpLabel);
        enemyInfoBox.setPadding(new Insets(5));
        enemyInfoBox.setAlignment(Pos.TOP_RIGHT);

        VBox spellButtonsBox = new VBox(10, spellButton1, spellButton2, spellButton3);
        spellButtonsBox.setPadding(new Insets(10));
        VBox.setVgrow(spellButton1, Priority.ALWAYS);
        VBox.setVgrow(spellButton2, Priority.ALWAYS);
        VBox.setVgrow(spellButton3, Priority.ALWAYS);

        VBox bottomBox = new VBox(10, spellButtonsBox, potionButton, skipTurnButton);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER);

        VBox logBox = new VBox(new Label("Battle Log:"), logLabel);
        logBox.setPadding(new Insets(10));

        VBox effectsContainer = new VBox(new Label("Active Effects:"), effectsBox);
        effectsContainer.setPadding(new Insets(10));

        HBox topBox = new HBox(10, playerInfoBox, enemyInfoBox);
        topBox.setPadding(new Insets(10));
        HBox.setHgrow(enemyInfoBox, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(logBox);
        root.setRight(effectsContainer);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                BattleScreen.class.getResource("/styles.css").toExternalForm()
        );

        ui.refreshAll();

        return scene;
    }
}
