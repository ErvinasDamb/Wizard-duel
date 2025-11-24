package wizardduel.mechanics;

import wizardduel.factory.PotionFactory;
import wizardduel.model.potions.Potion;

import java.util.List;
import java.util.Scanner;

/**
 * Handles pre-battle potion selection for the player.
 */
public final class PotionSelector {

    private PotionSelector() {
    }

    public static Potion choosePotion(Scanner scanner) {
        List<Potion> options = PotionFactory.createSelectablePotions();

        while (true) {
            System.out.println("\nChoose your potion type (you will use only this one in battle):");
            for (int i = 0; i < options.size(); i++) {
                Potion p = options.get(i);
                System.out.println((i + 1) + ". " + p.getName()
                        + " [" + p.getType()
                        + ", uses " + p.getMaxUses() + "]");
            }
            System.out.print("Enter number (1-" + options.size() + "): ");

            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Please enter a number.");
                continue;
            }
            int choice = scanner.nextInt();
            if (choice < 1 || choice > options.size()) {
                System.out.println("Invalid choice.");
                continue;
            }
            Potion selected = options.get(choice - 1);
            // Return a fresh instance (no shared state)
            return clonePotion(selected);
        }
    }

    private static Potion clonePotion(Potion base) {
        switch (base.getType()) {
            case SMALL_HEAL:
                return PotionFactory.createSmallHealingPotion();
            case HEAL:
                return PotionFactory.createHealingPotion();
            case LARGE_HEAL:
                return PotionFactory.createLargeHealingPotion();
            default:
                throw new IllegalArgumentException("Unsupported potion type: " + base.getType());
        }
    }
}
