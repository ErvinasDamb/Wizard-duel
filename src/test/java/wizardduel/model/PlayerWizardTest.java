package wizardduel.model;

import org.junit.jupiter.api.Test;
import wizardduel.factory.SpellFactory;
import wizardduel.model.Spell;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWizardTest {

    @Test
    void handReturnsTopThreeSpellsInOrder() {
        List<Spell> deck = List.of(
                SpellFactory.fireball(),
                SpellFactory.iceShard(),
                SpellFactory.poisonBolt(),
                SpellFactory.magicShield()
        );
        PlayerWizard player = new PlayerWizard("Player", deck);

        List<Spell> hand = player.getHand();

        assertEquals(3, hand.size());
        assertEquals(deck.get(0).getId(), hand.get(0).getId());
        assertEquals(deck.get(1).getId(), hand.get(1).getId());
        assertEquals(deck.get(2).getId(), hand.get(2).getId());
    }
}
