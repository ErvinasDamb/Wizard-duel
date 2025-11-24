package wizardduel.mechanics;

import wizardduel.mechanics.effects.ElementEngine;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.Wizard;
import wizardduel.mechanics.ai.AIStrategy;
import wizardduel.ui.UiBattleController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {

    private static final int BASE_MANA_REGEN = 10;
    private static final int SKIP_MANA_BONUS = 20;

    private final PlayerWizard player;
    private final EnemyWizard enemy;
    private final AIStrategy aiStrategy;
    private final UiBattleController ui;
    private final Random rng = new Random();
    private int round = 1;

    public Battle(PlayerWizard player,
                  EnemyWizard enemy,
                  AIStrategy aiStrategy,
                  UiBattleController ui) {
        this.player = player;
        this.enemy = enemy;
        this.aiStrategy = aiStrategy;
        this.ui = ui;

        shuffleDeck(player.getDeck());
        shuffleDeck(enemy.getDeck());
        refreshActiveFlags();
        ui.refreshAll();
    }

    public void playerSkipTurn() {
        if (isBattleOver()) return;

        player.onTurnStart();
        player.regenerateMana(SKIP_MANA_BONUS);
        ui.showPlayerAction("skips turn and regains " + SKIP_MANA_BONUS + " mana");

        if (checkAfterPlayerAction()) return;

        enemyTurn();
        if (checkAfterEnemyAction()) return;

        endOfRound();
    }

    public void playerCastSpell(Spell spell) {
        if (isBattleOver()) return;
        if (spell == null) return;

        player.onTurnStart();

        if (!spell.isActive()) {
            ui.showPlayerAction("tried to cast inactive spell " + spell.getName());
            return;
        }
        if (spell.getManaCost() > player.getMana()) {
            ui.showPlayerAction("does not have enough mana for " + spell.getName());
            return;
        }

        ui.showPlayerAction("casts " + spell.getName());
        castSpell(player, enemy, spell);

        onSpellPlayed(player.getDeck(), player.getDeck().indexOf(spell));
        ui.updateEffects();
        ui.updateHealthMana();

        if (checkAfterPlayerAction()) return;

        enemyTurn();
        if (checkAfterEnemyAction()) return;

        endOfRound();
    }

    public void playerUsePotion() {
        if (isBattleOver()) return;
        if (!player.hasPotion()) {
            ui.showPlayerAction("tries to use a potion, but has none left");
            return;
        }

        player.onTurnStart();
        player.useEquippedPotion();
        ui.updateHealthMana();
        ui.updatePotionButton();
        ui.showPlayerAction("uses a potion");

        if (checkAfterPlayerAction()) return;

        enemyTurn();
        if (checkAfterEnemyAction()) return;

        endOfRound();
    }

    private void enemyTurn() {
        enemy.onTurnStart();

        if (!enemy.isAlive()) {
            return;
        }

        if (enemy.hasPotion() && aiStrategy.shouldUsePotion(enemy, player)) {
            enemy.useEquippedPotion();
            ui.showEnemyAction("uses a potion");
            ui.updateHealthMana();
            ui.updatePotionButton();
            return;
        }

        List<Spell> hand = getTopActiveSpells(enemy.getDeck(), 3);
        Spell chosen = aiStrategy.chooseSpell(enemy, player, hand, rng);

        if (chosen == null) {
            enemy.regenerateMana(SKIP_MANA_BONUS);
            ui.showEnemyAction("skips turn and regains " + SKIP_MANA_BONUS + " mana");
            return;
        }

        if (chosen.getManaCost() > enemy.getMana()) {
            enemy.regenerateMana(SKIP_MANA_BONUS);
            ui.showEnemyAction("wanted to cast " + chosen.getName() +
                    " but didn't have mana, skips instead");
            return;
        }

        ui.showEnemyAction("casts " + chosen.getName());
        castSpell(enemy, player, chosen);
        onSpellPlayed(enemy.getDeck(), enemy.getDeck().indexOf(chosen));
        ui.updateEffects();
        ui.updateHealthMana();
    }

    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        if (!caster.useMana(spell.getManaCost())) {
            ui.appendLog(caster.getName() + " fails to cast " + spell.getName() + " (no mana).");
            return;
        }

        int beforeHp = target.getHealth();
        spell.cast(caster, target);
        int afterHp = target.getHealth();
        int dealt = Math.max(0, beforeHp - afterHp);

        if (dealt > 0) {
            ui.appendLog(caster.getName() + " hits " + target.getName() +
                    " for " + dealt + " damage.");
        } else {
            ui.appendLog(caster.getName() + " casts " + spell.getName() + " but deals no damage.");
        }

        ElementEngine.applyElementIfNeeded(spell, caster, target);
    }

    private void endOfRound() {
        player.regenerateMana(BASE_MANA_REGEN);
        enemy.regenerateMana(BASE_MANA_REGEN);
        ui.appendLog("End of round " + round +
                ". Both wizards regain " + BASE_MANA_REGEN + " mana.");
        round++;
        ui.refreshAll();
    }

    private boolean checkAfterPlayerAction() {
        if (!enemy.isAlive()) {
            finishBattle(player.getName());
            return true;
        }
        return false;
    }

    private boolean checkAfterEnemyAction() {
        if (!player.isAlive()) {
            finishBattle(enemy.getName());
            return true;
        }
        return false;
    }

    private boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive();
    }

    private void finishBattle(String winnerName) {
        ui.showEnd(winnerName);
        ui.refreshAll();
    }

    private void shuffleDeck(List<Spell> deck) {
        java.util.Collections.shuffle(deck);
    }

    private void refreshActiveFlags() {
        setActiveFlags(player.getDeck());
        setActiveFlags(enemy.getDeck());
    }

    private void setActiveFlags(List<Spell> deck) {
        for (int i = 0; i < deck.size(); i++) {
            Spell s = deck.get(i);
            s.setActive(i < 3);
        }
    }

    private void onSpellPlayed(List<Spell> deck, int index) {
        if (index < 0 || index >= deck.size()) {
            return;
        }
        Spell used = deck.remove(index);
        used.setActive(false);
        deck.add(used);
        setActiveFlags(deck);
    }

    private List<Spell> getTopActiveSpells(List<Spell> deck, int count) {
        List<Spell> hand = new ArrayList<>();
        for (int i = 0; i < count && i < deck.size(); i++) {
            Spell s = deck.get(i);
            if (s.isActive()) {
                hand.add(s);
            }
        }
        return hand;
    }
}
