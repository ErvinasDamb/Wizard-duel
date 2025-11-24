package wizardduel.mechanics.effects;

import wizardduel.model.Character;
import wizardduel.model.effects.ElementEffect;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.SynergyType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tikrina elementų kombinacijas ir kuria synergy efektus.
 */
public final class SynergyEngine {

    private SynergyEngine() {
    }

    /**
     * Pasitikrina aktyvius element efektus ant target ir,
     * jei randamos poros, sukuria atitinkamus synergy efektus.
     */
    public static void checkForSynergy(Character target) {
        List<ElementEffect> effects = target.getActiveElementEffects();
        if (effects.size() < 2) {
            return;
        }

        // Elementų sąrašas patogiam tikrinimui
        List<Element> elements = new ArrayList<>();
        for (ElementEffect e : effects) {
            elements.add(e.getElement());
        }

        // Kiekvienai kombinacijai pasižiūrim, ar yra abu elementai
        if (contains(elements, Element.FIRE, Element.POISON)) {
            createSynergy(target, SynergyType.BLAZING_VENOM, Element.FIRE, Element.POISON, 3);
        }
        if (contains(elements, Element.FIRE, Element.BLOOD)) {
            createSynergy(target, SynergyType.BLOODFIRE_FRENZY, Element.FIRE, Element.BLOOD, 3);
        }
        if (contains(elements, Element.ICE, Element.ELECTRIC)) {
            createSynergy(target, SynergyType.FROZEN_STATIC, Element.ICE, Element.ELECTRIC, 2);
        }
        if (contains(elements, Element.BLOOD, Element.POISON)) {
            createSynergy(target, SynergyType.CORRUPTED_VITALS, Element.BLOOD, Element.POISON, 3);
        }
        if (contains(elements, Element.LIGHT, Element.BLOOD)) {
            createSynergy(target, SynergyType.HOLY_SIPHON, Element.LIGHT, Element.BLOOD, 3);
        }
        if (contains(elements, Element.ICE, Element.POISON)) {
            createSynergy(target, SynergyType.TOXIC_FROSTBITE, Element.ICE, Element.POISON, 3);
        }
        if (contains(elements, Element.ELECTRIC, Element.WATER)) {
            createSynergy(target, SynergyType.SHOCKING_CURRENT, Element.ELECTRIC, Element.WATER, 3);
        }
    }

    private static boolean contains(List<Element> list, Element a, Element b) {
        return list.contains(a) && list.contains(b);
    }

    /**
     * Sukuria synergy efektą ir išmeta panaudotus element efektus.
     */
    private static void createSynergy(Character target,
                                      SynergyType type,
                                      Element a,
                                      Element b,
                                      int duration) {

        // Įdedam ar atnaujinam synergy
        target.addSynergyEffect(type, duration);

        // Pašalinam naudotus elementus iš aktyvių
        Iterator<ElementEffect> it = target.getActiveElementEffects().iterator();
        while (it.hasNext()) {
            ElementEffect e = it.next();
            Element el = e.getElement();
            if (el == a || el == b) {
                it.remove();
            }
        }
    }
}
