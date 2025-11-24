package wizardduel.model.potions;

import wizardduel.model.Character;
import wizardduel.model.enums.PotionType;

public class HealingPotion extends AbstractPotion {

    private final int healAmount;

    public HealingPotion() {
        super("potion_heal_01", "Healing Potion", PotionType.HEAL, 3);
        this.healAmount = 220; // 3x220 = 660
    }

    @Override
    public void use(Character user) {
        if (!canUse()) {
            System.out.println(user.getName() + " has no uses left for " + getName() + ".");
            return;
        }
        user.heal(healAmount);
        consumeOneUse();
        System.out.println(user.getName() + " uses " + getName() +
                " and restores " + healAmount + " HP (" + remainingUses + "/" + maxUses + " left).");
    }
}
