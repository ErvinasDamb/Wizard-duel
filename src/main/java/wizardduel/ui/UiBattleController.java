package wizardduel.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.effects.SynergyEffect;
import wizardduel.model.potions.Potion;
import javafx.scene.control.Alert;


import java.util.List;
import java.util.stream.Collectors;

public class UiBattleController {

    private final PlayerWizard player;
    private final EnemyWizard enemy;
    private final Label playerHpLabel;
    private final Label playerManaLabel;
    private final Label enemyHpLabel;
    private final VBox effectsBox;
    private final Label logLabel;
    private final Button skipButton;
    private final List<Button> spellButtons;
    private final Button potionButton;

    public UiBattleController(
            PlayerWizard player,
            EnemyWizard enemy,
            Label playerHpLabel,
            Label playerManaLabel,
            Label enemyHpLabel,
            VBox effectsBox,
            Label logLabel,
            Button skipButton,
            List<Button> spellButtons,
            Button potionButton
    ) {
        this.player = player;
        this.enemy = enemy;
        this.playerHpLabel = playerHpLabel;
        this.playerManaLabel = playerManaLabel;
        this.enemyHpLabel = enemyHpLabel;
        this.effectsBox = effectsBox;
        this.logLabel = logLabel;
        this.skipButton = skipButton;
        this.spellButtons = spellButtons;
        this.potionButton = potionButton;
    }

    public void refreshAll() {
        updateHealthMana();
        updateEffects();
        updateSpellButtons();
        updatePotionButton();
    }

    public void updateHealthMana() {
        playerHpLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        playerManaLabel.setText("Mana: " + player.getMana());
        enemyHpLabel.setText("Enemy HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
    }

    public void updateEffects() {
        effectsBox.getChildren().clear();

        addElementEffects("You", player.getActiveElementEffects());
        addSynergyEffects("You", player.getActiveSynergyEffects());

        addElementEffects("Enemy", enemy.getActiveElementEffects());
        addSynergyEffects("Enemy", enemy.getActiveSynergyEffects());
    }

    private void addElementEffects(String owner, List<ElementEffect> effects) {
        for (ElementEffect effect : effects) {
            Label lbl = new Label(
                    owner + ": " + effect.getElement().name()
                            + " (" + effect.getRemainingTurns() + " turns left)"
            );
            effectsBox.getChildren().add(lbl);
        }
    }

    private void addSynergyEffects(String owner, List<SynergyEffect> effects) {
        for (SynergyEffect effect : effects) {
            Label lbl = new Label(
                    owner + ": " + effect.getType().name()
                            + " (" + effect.getRemainingTurns() + " turns left)"
            );
            effectsBox.getChildren().add(lbl);
        }
    }

    public void updateSpellButtons() {
        List<Spell> hand = player.getHand();
        for (int i = 0; i < spellButtons.size(); i++) {
            Button btn = spellButtons.get(i);
            if (i >= hand.size()) {
                btn.setDisable(true);
                btn.setText("Empty");
                continue;
            }
            Spell spell = hand.get(i);
            String text = formatSpellButtonText(spell);
            btn.setText(text);

            boolean canCast =
                    spell.isActive() &&
                            spell.getManaCost() <= player.getMana() &&
                            player.isAlive();

            btn.setDisable(!canCast);
        }
    }

    private String formatSpellButtonText(Spell spell) {
        return spell.getName()
                + "\nPWR:" + spell.getPower()
                + " M:" + spell.getManaCost()
                + " [" + spell.getElement() + "]"
                + " (" + spell.getType() + ")";
    }

    public void updatePotionButton() {
        Potion p = player.getEquippedPotion();
        if (p == null) {
            potionButton.setDisable(true);
            potionButton.setText("No potion");
            return;
        }
        boolean canUse = p.canUse() && player.isAlive();
        potionButton.setDisable(!canUse);

        String text = p.getName()
                + " (" + p.getRemainingUses()
                + "/" + p.getMaxUses() + ")";
        potionButton.setText(text);
    }

    public void appendLog(String message) {
        String existing = logLabel.getText();
        if (existing == null || existing.isEmpty()) {
            logLabel.setText(message);
        } else {
            String combined = existing + "\n" + message;
            List<String> lines = combined.lines().collect(Collectors.toList());
            if (lines.size() > 8) {
                lines = lines.subList(lines.size() - 8, lines.size());
            }
            logLabel.setText(String.join("\n", lines));
        }
    }

    public void showEnemyAction(String actionText) {
        appendLog("Enemy: " + actionText);
    }

    public void showPlayerAction(String actionText) {
        appendLog("You: " + actionText);
    }

    public void showEnd(String winnerName) {
        Platform.runLater(() -> {
            // Disable all controls
            skipButton.setDisable(true);
            for (Button b : spellButtons) {
                b.setDisable(true);
            }
            potionButton.setDisable(true);

            appendLog("Battle ended! Winner: " + winnerName);

            // Popup langas su dideliu tekstu
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Battle Finished");
            alert.setHeaderText(null);

            Label content = new Label("Winner: " + winnerName);
            content.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            alert.getDialogPane().setContent(content);
            alert.showAndWait();
        });
    }

}
