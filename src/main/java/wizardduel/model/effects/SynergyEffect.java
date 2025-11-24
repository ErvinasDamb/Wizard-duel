package wizardduel.model.effects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import wizardduel.model.enums.SynergyType;

/**
 * Active synergy effect created from two combined elements.
 * Example: FIRE + POISON → HEAVY_DOT, BLOOD + POISON → HEAL_BLOCK, etc.
 */
@Getter
@Setter
@AllArgsConstructor
public class SynergyEffect {

    /**
     * Which synergy is active on this character.
     */
    private SynergyType type;

    /**
     * How many turns this synergy will still be active.
     * Decremented each turn; when it hits 0, the effect is removed.
     */
    private int remainingTurns;
}
