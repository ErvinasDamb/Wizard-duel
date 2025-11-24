package wizardduel.mechanics;

import wizardduel.factory.SpellFactory;
import wizardduel.model.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console-based deck building for the player.
 */
public final class DeckBuilder {

    private static final int DECK_SIZE = 7;

    private DeckBuilder() {
    }

    public static List<Spell> buildPlayerDeck(Scanner scanner) {
        List<Spell> pool = SpellFactory.createSpellPool();
        List<String> chosenIds = new ArrayList<>();

        while (chosenIds.size() < DECK_SIZE) {
            System.out.println("\nChoose spell " + (chosenIds.size() + 1) +
                    " of " + DECK_SIZE + ":");
            printSpellPool(pool, chosenIds);

            int choice = readChoice(scanner, pool.size());
            int index = choice - 1;
            Spell spell = pool.get(index);

            if (chosenIds.contains(spell.getId())) {
                System.out.println("You already picked this spell.");
                continue;
            }

            chosenIds.add(spell.getId());
            System.out.println("Added to deck: " + spell.getName());
        }

        return createDeckFromIds(chosenIds);
    }

    private static void printSpellPool(List<Spell> pool, List<String> chosenIds) {
        for (int i = 0; i < pool.size(); i++) {
            Spell s = pool.get(i);
            String picked = chosenIds.contains(s.getId()) ? " [PICKED]" : "";
            System.out.println((i + 1) + ". " + s.getName()
                    + " (" + s.getElement() + ", mana " + s.getManaCost() + ")" + picked);
        }
    }

    private static int readChoice(Scanner scanner, int maxIndex) {
        while (true) {
            System.out.print("Enter spell number (1-" + maxIndex + "): ");
            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Please enter a number.");
                continue;
            }
            int value = scanner.nextInt();
            if (value < 1 || value > maxIndex) {
                System.out.println("Invalid index.");
                continue;
            }
            return value;
        }
    }

    private static List<Spell> createDeckFromIds(List<String> ids) {
        List<Spell> deck = new ArrayList<>();
        for (String id : ids) {
            deck.add(SpellFactory.createById(id));
        }
        return deck;
    }
}
