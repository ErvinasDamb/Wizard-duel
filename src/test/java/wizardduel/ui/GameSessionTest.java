package wizardduel.ui;

import org.junit.jupiter.api.Test;
import wizardduel.factory.SpellFactory;
import wizardduel.model.Spell;
import wizardduel.factory.SpellFactory;
import wizardduel.factory.PotionFactory;
import wizardduel.model.enums.AiDifficulty;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
    void hasValidDeckFalseWhenLessThanSevenCards() {
        GameSession session = new GameSession();

        List<Spell> shortDeck = List.of(
                SpellFactory.fireball(),
                SpellFactory.iceShard(),
                SpellFactory.poisonBolt()
        );

        session.setDeck(shortDeck);

        assertFalse(session.hasValidDeck(),
                "Deck with less than 7 cards should not be valid");
    }

    @Test
    void hasValidDeckTrueWhenExactlySevenCards() {
        GameSession session = new GameSession();

        // createDefaultPlayerDeck grąžina 7 spells
        List<Spell> fullDeck = SpellFactory.createDefaultPlayerDeck();
        session.setDeck(fullDeck);

        assertTrue(session.hasValidDeck(),
                "Deck with exactly 7 cards should be valid");
    }

    @Test
    void clearResetsDeckPotionAndDifficulty() {
        GameSession session = new GameSession();

        // užpildom kažkuo
        session.setDeck(java.util.List.of(
                SpellFactory.fireball(),
                SpellFactory.iceShard()
        ));
        session.setSelectedPotion(PotionFactory.createHealingPotion());
        session.setDifficulty(AiDifficulty.HARD);

        session.clear();

        assertTrue(session.getPlayerDeck().isEmpty(), "Deck should be empty after clear()");
        assertNull(session.getSelectedPotion(), "Selected potion should be null after clear()");
        assertEquals(AiDifficulty.EASY, session.getDifficulty(), "Difficulty should reset to EASY");
    }

}
