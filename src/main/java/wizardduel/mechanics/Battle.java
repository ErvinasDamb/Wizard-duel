package wizardduel.mechanics;

import wizardduel.mechanics.effects.ElementEngine;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.Wizard;

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

        System.out.println("Enemy : " + enemy);
        System.out.println("  Elements: " + enemy.getElementEffectsSummary());
        System.out.println("  Synergies: " + enemy.getSynergyEffectsSummary());
    }


    // ---------------- PLAYER TURN ----------------

    private void playerTurn() {
        System.out.println("\nTavo ėjimas.");
        player.onTurnStart();

        List<Spell> spells = player.getDeck();

        while (true) {
            printPlayerSpellMenu(spells);

            int choice = readInt("Pasirink burtą (0 – praleisti): ");

            if (choice == 0) {
                System.out.println(player.getName() + " praleidžia ėjimą ir kaupia maną.");
                player.regenerateMana(SKIP_MANA_BONUS);
                return;
            }

            int index = choice - 1;
            if (index < 0 || index >= spells.size()) {
                System.out.println("Neteisingas numeris.");
                continue;
            }

            Spell spell = spells.get(index);

            if (spell.getManaCost() > player.getMana()) {
                System.out.println("Nepakanka manos šiam burtui.");
                continue;
            }

            System.out.println(player.getName() + " naudoja: " + spell.getName());
            castSpell(player, enemy, spell);
            return;
        }
    }

    private void printPlayerSpellMenu(List<Spell> spells) {
        System.out.println("\nTavo burtai:");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            String manaInfo = " [kaina: " + s.getManaCost() + "]";
            if (s.getManaCost() > player.getMana()) {
                manaInfo += " (NEPAKANKA MANOS)";
            }
            System.out.println((i + 1) + ". " + s.getName() + manaInfo);
        }
        System.out.println("0. Praleisti ėjimą (+ " + SKIP_MANA_BONUS + " manos)");
    }

    // ---------------- ENEMY TURN ----------------

    private void enemyTurn() {
        System.out.println("\nPriešininko ėjimas.");
        enemy.onTurnStart();

        Spell chosen = chooseEnemySpell();

        if (chosen == null) {
            System.out.println(enemy.getName() + " praleidžia ėjimą ir kaupia maną.");
            enemy.regenerateMana(SKIP_MANA_BONUS);
            return;
        }

        System.out.println(enemy.getName() + " naudoja: " + chosen.getName());
        castSpell(enemy, player, chosen);
    }

    private Spell chooseEnemySpell() {
        List<Spell> spells = enemy.getDeck();

        // Filtruojam tuos, kuriems užtenka manos
        List<Spell> affordable = spells.stream()
                .filter(s -> s.getManaCost() <= enemy.getMana())
                .toList();

        if (affordable.isEmpty()) {
            return null;
        }

        // Labai paprastas AI: random pasirinkimas
        int idx = rng.nextInt(affordable.size());
        return affordable.get(idx);
    }


    // ---------------- COMMON CAST ----------------

    private void castSpell(Wizard caster, Wizard target, Spell spell) {
        // Pirmiausia bandom nusimokėti manos kainą
        if (!caster.useMana(spell.getManaCost())) {
            System.out.println(caster.getName() + " neturi pakankamai manos.");
            return;
        }

        // Tada spell logika
        spell.cast(caster, target);

        // Ir galiausiai – elementas ir synergy
        ElementEngine.applyElementIfNeeded(spell, caster, target);
    }

    // ---------------- ROUND END & UTILS ----------------

    private void endOfRound() {
        player.regenerateMana(BASE_MANA_REGEN);
        enemy.regenerateMana(BASE_MANA_REGEN);
        System.out.println("\nRaundo pabaiga. Abiems suteikta po " +
                BASE_MANA_REGEN + " manos.");
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
}
