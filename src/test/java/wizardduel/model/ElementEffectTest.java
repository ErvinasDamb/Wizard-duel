package wizardduel.model;

import org.junit.jupiter.api.Test;
import wizardduel.model.enums.Element;

import static org.junit.jupiter.api.Assertions.*;

class ElementEffectTest {

    @Test
    void addElementEffectAddsAndExtendsDuration() {
        Character c = new Character("Test");

        c.addElementEffect(Element.FIRE, 2);
        c.addElementEffect(Element.FIRE, 4); // turėtų prailginti

        String summary = c.getElementEffectsSummary();
        assertTrue(summary.contains("FIRE("));
        assertTrue(summary.contains("4"), "Duration should be extended to 4");
    }

    @Test
    void elementEffectsSummaryNoneWhenEmpty() {
        Character c = new Character("Empty");
        assertEquals("None", c.getElementEffectsSummary());
    }
}
