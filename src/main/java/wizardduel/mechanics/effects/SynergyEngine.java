package wizardduel.mechanics.effects;

import wizardduel.model.Character;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SynergyType;

import java.util.ArrayList;
import java.util.List;

public class SynergyEngine {

    /**
     * Checks active element effects and transforms matching pairs into synergy effects.
     */
    public static void checkForSynergy(Character target) {
        List<ElementEffect> effects = target.getActiveElementEffects();
        if (effects.size() < 2) {
            return; // no synergy possible
        }

        // Copy so we can safely modify
        List<Element> elements = new ArrayList<>();
        for (ElementEffect e : effects) {
            elements.add(e.getElement());
        }

        // Check all combos (order does not matter)
        if (contains(elements, Element.FIRE, Element.POISON)) {
            create(target, SynergyType.BLAZING_VENOM, Element.FIRE, Element.POISON);
        }

        if (contains(elements, Element.FIRE, Element.BLOOD)) {
            create(target, SynergyType.BLOODFIRE_FRENZY, Element.FIRE, Element.BLOOD);
        }

        if (contains(elements, Element.ICE, Element.ELECTRIC)) {
            create(target, SynergyType.FROZEN_STATIC, Element.ICE, Element.ELECTRIC);
        }

        if (contains(elements, Element.BLOOD, Element.POISON)) {
            create(target, SynergyType.CORRUPTED_VITALS, Element.BLOOD, Element.POISON);
        }

        if (contains(elements, Element.LIGHT, Element.BLOOD)) {
            create(target, SynergyType.HOLY_SIPHON, Element.LIGHT, Element.BLOOD);
        }

        if (contains(elements, Element.ICE, Element.POISON)) {
            create(target, SynergyType.TOXIC_FROSTBITE, Element.ICE, Element.POISON);
        }

        if (contains(elements, Element.ELECTRIC, Element.WATER)) {
            create(target, SynergyType.SHOCKING_CURRENT, Element.ELECTRIC, Element.WATER);
        }
    }

    private static boolean contains(List<Element> list, Element a, Element b) {
        return list.contains(a) && list.contains(b);
    }

    private static void create(Character target, SynergyType type, Element a, Element b) {
        // 1. Add synergy effect
        target.addSynergyEffect(type, 2); // placeholder duration for now

        // 2. Remove both element effects
        target.getActiveElementEffects().removeIf(e ->
                e.getElement() == a || e.getElement() == b
        );
    }
}
