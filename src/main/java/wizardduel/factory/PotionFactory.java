package wizardduel.factory;

import wizardduel.model.potions.HealingPotion;
import wizardduel.model.potions.LargeHealingPotion;
import wizardduel.model.potions.Potion;
import wizardduel.model.potions.SmallHealingPotion;

import java.util.List;

public final class PotionFactory {

    private PotionFactory() {}

    public static Potion createSmallHealingPotion() {
        return new SmallHealingPotion();
    }

    public static Potion createHealingPotion() {
        return new HealingPotion();
    }

    public static Potion createLargeHealingPotion() {
        return new LargeHealingPotion();
    }

    /**
     * Potion options available for pre-battle selection.
     */
    public static List<Potion> createSelectablePotions() {
        return List.of(
                createSmallHealingPotion(),
                createHealingPotion(),
                createLargeHealingPotion()
        );
    }
}
