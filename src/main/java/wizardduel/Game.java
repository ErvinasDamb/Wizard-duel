package wizardduel;

import wizardduel.factory.SpellFactory;
import wizardduel.factory.PotionFactory;
import wizardduel.mechanics.Battle;
import wizardduel.mechanics.DeckBuilder;
import wizardduel.mechanics.PotionSelector;
import wizardduel.mechanics.ai.AIRandomStrategy;
import wizardduel.mechanics.ai.AIDefensiveStrategy;
import wizardduel.mechanics.ai.AIAgressiveStrategy;
import wizardduel.mechanics.ai.AIStrategy;
import wizardduel.model.EnemyWizard;
import wizardduel.model.PlayerWizard;
import wizardduel.model.Spell;
import wizardduel.model.potions.Potion;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point of the application.
 */
public class Game {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Wizard Duel ===");
        System.out.println("Build your deck (7 spells).");

        List<Spell> playerDeck = DeckBuilder.buildPlayerDeck(scanner);
        List<Spell> enemyDeck = SpellFactory.createDefaultEnemyDeck();

        System.out.println("\nNow choose your potion type.");
        Potion playerPotion = PotionSelector.choosePotion(scanner);
        Potion enemyPotion = PotionFactory.createHealingPotion();

        System.out.println("\nChoose AI difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        AIStrategy aiStrategy = null;
        while (aiStrategy == null) {
            System.out.print("Enter 1-3: ");
            if (!scanner.hasNextInt()) {
                scanner.next(); // discard junk
                System.out.println("Please enter a number.");
                continue;
            }
            int difficulty = scanner.nextInt();
            switch (difficulty) {
                case 1:
                    aiStrategy = new AIRandomStrategy();
                    System.out.println("AI difficulty set to EASY.");
                    break;
                case 2:
                    aiStrategy = new AIDefensiveStrategy();
                    System.out.println("AI difficulty set to MEDIUM.");
                    break;
                case 3:
                    aiStrategy = new AIAgressiveStrategy();
                    System.out.println("AI difficulty set to HARD.");
                    break;
                default:
                    System.out.println("Invalid choice. Enter 1, 2 or 3.");
            }
        }

        PlayerWizard player = new PlayerWizard("Player", playerDeck);
        EnemyWizard enemy = new EnemyWizard("Enemy", enemyDeck);

        player.equipPotion(playerPotion);
        enemy.equipPotion(enemyPotion);
        enemy.setAiStrategy(aiStrategy);

        Battle battle = new Battle(player, enemy);
        battle.start();
    }
}
