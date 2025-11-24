package wizardduel.factory;

import org.junit.jupiter.api.Test;
import wizardduel.model.Spell;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SpellFactoryTest {

    @Test
    void createByIdReturnsEquivalentSpell() {
        Spell original = SpellFactory.fireball();

        Spell byId = SpellFactory.createById(original.getId());

        assertNotNull(byId, "SpellFactory.createById should not return null");
        assertEquals(original.getId(), byId.getId());
        assertEquals(original.getName(), byId.getName());
        assertEquals(original.getManaCost(), byId.getManaCost());
        assertEquals(original.getType(), byId.getType());
    }

    @Test
    void createSpellPoolContainsOnlyUniqueIds() {
        List<Spell> pool = SpellFactory.createSpellPool();

        Set<String> ids = pool.stream()
                .map(Spell::getId)
                .collect(Collectors.toSet());

        assertEquals(pool.size(), ids.size(),
                "Spell pool should not contain duplicate spell IDs");
    }
}
