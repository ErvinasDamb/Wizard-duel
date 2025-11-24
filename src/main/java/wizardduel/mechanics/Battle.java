package wizardduel.mechanics;

import wizardduel.mechanics.effects.ElementEngine;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.Wizard;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SpellType;
import wizardduel.model.effects.ElementEffect;
import wizardduel.mechanics.ai.AIStrategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Nauja paprasta kovos logika ant Character/StatusEngine/ElementEngine/SynergyEngine.
 */
public class Battle {

    private static final int BASE_MANA_REGEN = 10;
    private static final int SKIP_MANA_BONUS = 20;

    private final PlayerWizard player;
    private final EnemyWizard enemy;
    private final Scanner scanner = new Scanner(System.in);
    private final Random rng = new Random();
    private int round = 1;

    public Battle(PlayerWizard player, EnemyWizard enemy) {
        this.player = player;
        this.enemy = enemy;

        // random deck order at start
        shuffleDeck(player.getDeck());
        shuffleDeck(enemy.getDeck());

        // initial active spells (first 3)
        refreshActiveFlags();
    }

    private void shuffleDeck(List<Spell> deck) {
        java.util.Collections.shuffle(deck);
    }

    public void start() {
        System.out.println("=== Wizard Duel V2 ===");
        System.out.println(player.getName() + " vs " + enemy.getName());
        System.out.println();

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n--- Raundas " + round + " ---");
            printStatus();

            // Žaidėjo ėjimas
            playerTurn();
            if (!enemy.isAlive()) break;

            // Priešo ėjimas
            enemyTurn();
            if (!player.isAlive()) break;

            // Raundo pabaiga: bazinė mana regen
            endOfRound();
            round++;
        }

        printResult();
    }

    private void printStatus() {
        System.out.println("Player: " + player);
        System.out.println("  Elements: " + player.getElementEffectsSummary());
        System.out.println("  Synergies: " + player.getSynergyEffectsSummary());
        System.out.println("  Potion  : " + player.getPotionSummary());

        System.out.println("Enemy : " + enemy);
        System.out.println("  Elements: " + enemy.getElementEffectsSummary());
        System.out.println("  Synergies: " + enemy.getSynergyEffectsSummary());
        System.out.println("  Potion  : " + enemy.getPotionSummary());
    }


    // ---------------- PLAYER TURN ----------------

    private void playerTurn() {
        System.out.println("\nYour turn.");
        player.onTurnStart();

        while (true) {
            printPlayerHand();

            int choice = readInt("Choose spell (0 – skip, 9 – use potion): ");

            if (choice == 0) {
                System.out.println(player.getName() + " skips turn and gains mana.");
                player.regenerateMana(SKIP_MANA_BONUS);
                return;
            }

            if (choice == 9) {
                if (player.hasPotion()) {
                    player.useEquippedPotion();
                    return; // potion consumes the turn
                } else {
                    System.out.println("You have no usable potion.");
                    continue;
                }
            }


            int index = choice - 1;
            if (index < 0 || index > 2) {
                System.out.println("You can only choose from 1 to 3.");
                continue;
            }

            List<Spell> deck = player.getDeck();
            if (deck.size() <= index) {
                System.out.println("Not enough spells in deck.");
                continue;
            }

            Spell spell = deck.get(index);

            if (!spell.isActive()) {
                System.out.println("This spell is not active.");
                continue;
            }

            if (spell.getManaCost() > player.getMana()) {
                System.out.println("Not enough mana.");
                continue;
            }

            System.out.println(player.getName() + " casts: " + spell.getName());
            castSpell(player, enemy, spell);
            onSpellPlayed(deck, index);
            return;
        }
    }

    private void printPlayerHand() {
        System.out.println("\nYour hand (top 3 spells):");
        List<Spell> deck = player.getDeck();

        for (int i = 0; i < 3 && i < deck.size(); i++) {
            Spell s = deck.get(i);
            String activeTag = s.isActive() ? "ACTIVE" : "INACTIVE";
            String manaInfo = "mana " + s.getManaCost();
            if (s.getManaCost() > player.getMana()) {
                manaInfo += " (NOT ENOUGH)";
            }
            System.out.println((i + 1) + ". " + s.getName()
                    + " [" + activeTag + ", " + manaInfo + "]");
        }
        System.out.println("0. Skip turn (+ " + SKIP_MANA_BONUS + " mana)");
        System.out.println("9. Use potion (" + player.getPotionSummary() + ")");
    }

    /**
     * Rotates deck after playing the spell at given index:
     * used card goes to the end, others shift forward,
     * then active flags are updated (top 3 active).
     */
    private void onSpellPlayed(List<Spell> deck, int index) {
        if (index < 0 || index >= deck.size()) {
            return;
        }
        Spell used = deck.remove(index);
        used.setActive(false);
        deck.add(used);
        setActiveFlags(deck);
    }

    // ---------------- ENEMY TURN ----------------

    private void enemyTurn() {
        System.out.println("\nEnemy turn.");
        enemy.onTurnStart();

        if (enemy.getAiStrategy() == null) {
            System.out.println("No AI strategy set – enemy skips.");
            enemy.regenerateMana(SKIP_MANA_BONUS);
            return;
        }

        // Potion pirmiau
        if (enemy.hasPotion()
                && enemy.getAiStrategy().shouldUsePotion(enemy, player)) {
            System.out.println(enemy.getName() + " uses a potion!");
            enemy.useEquippedPotion();
            return;
        }

        List<Spell> deck = enemy.getDeck();
        List<Spell> hand = new ArrayList<>();
        for (int i = 0; i < 3 && i < deck.size(); i++) {
            Spell s = deck.get(i);
            if (s.isActive()) {
                hand.add(s);
            }
        }

        Spell chosen = enemy.getAiStrategy().chooseSpell(enemy, player, hand, rng);

        if (chosen == null) {
            System.out.println(enemy.getName() + " skips and gains mana.");
            enemy.regenerateMana(SKIP_MANA_BONUS);
            return;
        }

        int index = deck.indexOf(chosen);
        if (index < 0) {
            // jei dėl kokios nors priežasties nepavyko – fallback
            System.out.println("AI picked invalid spell, enemy skips.");
            enemy.regenerateMana(SKIP_MANA_BONUS);
            return;
        }

        System.out.println(enemy.getName() + " casts: " + chosen.getName());
        castSpell(enemy, player, chosen);
        onSpellPlayed(deck, index);
    }


    private Spell chooseEnemySpell() {
        List<Spell> deck = enemy.getDeck();
        List<Spell> candidates = new ArrayList<>();

        for (int i = 0; i < 3 && i < deck.size(); i++) {
            Spell s = deck.get(i);
            if (!s.isActive()) {
                continue;
            }
            if (s.getManaCost() <= enemy.getMana()) {
                candidates.add(s);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // Kol kas paprastas AI: random iš aktyvių, kuriems užtenka manos
        int idx = rng.nextInt(candidates.size());
        return candidates.get(idx);
    }



    // ---------------- COMMON CAST ----------------

    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        if (!caster.useMana(spell.getManaCost())) {
            System.out.println(caster.getName() + " does not have enough mana.");
            return;
        }

        spell.cast(caster, target);
        ElementEngine.applyElementIfNeeded(spell, caster, target);
    }

    // ---------------- ROUND END & UTILS ----------------

    private void endOfRound() {
        player.regenerateMana(BASE_MANA_REGEN);
        enemy.regenerateMana(BASE_MANA_REGEN);

        System.out.println("\nEnd of round. Both gain "
                + BASE_MANA_REGEN + " mana.");
    }


    private void printResult() {
        System.out.println("\n=== Kova baigėsi ===");
        if (player.isAlive() && !enemy.isAlive()) {
            System.out.println(player.getName() + " laimėjo!");
        } else if (!player.isAlive() && enemy.isAlive()) {
            System.out.println(enemy.getName() + " laimėjo!");
        } else {
            System.out.println("Lygiosios (abu krito)!");
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Įvesk skaičių: ");
        }
        return scanner.nextInt();
    }

    private void refreshActiveFlags() {
        setActiveFlags(player.getDeck());
        setActiveFlags(enemy.getDeck());
    }

    private void setActiveFlags(List<Spell> deck) {
        for (int i = 0; i < deck.size(); i++) {
            Spell s = deck.get(i);
            s.setActive(i < 3); // pirmi 3 aktyvūs, likę neaktyvūs
        }
    }

}
