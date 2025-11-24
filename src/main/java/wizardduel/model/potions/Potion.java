package wizardduel.model.potions;

import wizardduel.model.Character;

/**
 * Base potion interface.
 */
public interface Potion {
    void apply(Character target);
}
