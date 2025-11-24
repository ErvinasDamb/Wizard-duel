package wizardduel.model;

import org.junit.jupiter.api.Test;
import wizardduel.factory.SpellFactory;
import wizardduel.model.potions.HealingPotion;

import static org.junit.jupiter.api.Assertions.*;

class WizardPotionIntegrationTest {

    @Test
    void wizardUsesEquippedPotionAndHpIncreases() {
        PlayerWizard w = new PlayerWizard(
                "Tester",
                SpellFactory.createDefaultPlayerDeck()
        );

        // duodam damage
        w.takeDamage(500);
        int hpBefore = w.getHp();

        HealingPotion potion = new HealingPotion();
        int usesBefore = potion.getRemainingUses();

        w.equipPotion(potion);
        boolean result = w.useEquippedPotion();

        assertTrue(result, "useEquippedPotion should return true when potion is available");
        assertTrue(w.getHp() > hpBefore, "HP should increase after using potion");
        assertEquals(usesBefore - 1, potion.getRemainingUses(),
                "Potion remaining uses should decrease by 1");
    }
}
