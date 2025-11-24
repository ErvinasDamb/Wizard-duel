package wizardduel.model.potions;

import wizardduel.model.Character;
import wizardduel.model.enums.PotionType;

public class LargeHealingPotion extends AbstractPotion {

    private final int healAmount;

    public LargeHealingPotion() {
        super("potion_large_heal_01", "Large Healing Potion", PotionType.LARGE_HEAL, 2);
        this.healAmount = 350; // 2x350 = 700
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
