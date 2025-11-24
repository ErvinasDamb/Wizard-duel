package wizardduel.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void takeDamageReducesHpWithoutShield() {
        Character c = new Character("Test");
        int start = c.getHp();

        c.takeDamage(150);

        assertEquals(start - 150, c.getHp());
    }

    @Test
    void shieldAbsorbsDamageBeforeHp() {
        Character c = new Character("Test");
        c.applyShield(100);
        int startHp = c.getHp();

        c.takeDamage(80);

        assertEquals(startHp, c.getHp());
        assertEquals(20, c.getShield());
    }

    @Test
    void healDoesNotExceedMaxHp() {
        Character c = new Character("Test");
        c.takeDamage(500);
        assertTrue(c.getHp() < c.getMaxHp());

        c.heal(1000);

        assertEquals(c.getMaxHp(), c.getHp());
    }

    @Test
    void dotDealsDamageOverTurns() {
        Character c = new Character("Test");
        int start = c.getHp();

        c.applyDot(50, 2);  // 2 turnai po 50
        c.tickBaseDot();
        c.tickBaseDot();
        c.tickBaseDot();    // trečias neturi veikti

        int expected = start - 100;
        assertEquals(expected, c.getHp());
    }
}
