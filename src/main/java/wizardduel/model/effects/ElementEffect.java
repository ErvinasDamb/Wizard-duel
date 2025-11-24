package wizardduel.model.effects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import wizardduel.model.enums.Element;

/**
 * Single active elemental effect on a character.
 * Example: FIRE for 2 turns (burn), ICE for 2 turns (miss chance up), etc.
 */
@Getter
@Setter
@AllArgsConstructor
public class ElementEffect {

    /**
     * Which element is affecting the character.
     */
    private Element element;

    /**
     * How many turns this effect will still be active.
     * Decremented each turn; when it hits 0, the effect is removed.
     */
    private int remainingTurns;
}
