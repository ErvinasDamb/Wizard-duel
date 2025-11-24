package wizardduel.model.potions;

import org.junit.jupiter.api.Test;
import wizardduel.model.Character;

import static org.junit.jupiter.api.Assertions.*;

class PotionTest {

    @Test
    void healingPotionHealsAndConsumesUse() {
        Character c = new Character("Test");
        c.takeDamage(400);
        int hpBefore = c.getHp();

        HealingPotion potion = new HealingPotion();
        int usesBefore = potion.getRemainingUses();

        potion.use(c);

        assertTrue(c.getHp() > hpBefore, "HP should increase after using potion");
        assertEquals(usesBefore - 1, potion.getRemainingUses());
    }

    @Test
    void potionCannotBeUsedWhenNoUsesLeft() {
        Character c = new Character("Test");
        HealingPotion potion = new HealingPotion();

        // sunaudojam visas uses
        while (potion.getRemainingUses() > 0) {
            potion.use(c);
        }

        int hpBefore = c.getHp();
        potion.use(c); // neturėtų nieko daryti
        assertEquals(hpBefore, c.getHp());
    }
}
