package wizardduel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic stat container (max HP, max mana, defense etc.).
 */
@Getter
@Setter
public class Stats {
    private int maxHp;
    private int maxMana;
}
