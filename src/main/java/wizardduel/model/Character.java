package wizardduel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for any wizard character.
 */
@Getter
@Setter
public abstract class Character {
    protected String name;
    protected int hp;
    protected int mana;
    protected Stats stats;
}
