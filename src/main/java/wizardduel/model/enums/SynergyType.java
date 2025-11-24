package wizardduel.model.enums;

/**
 * Type of synergy effect created from two combined elements.
 * Real numeric impact will be handled in the effect processing logic.
 */
public enum SynergyType {
    BLAZING_VENOM,      // Fire + Poison
    BLOODFIRE_FRENZY,   // Fire + Blood
    FROZEN_STATIC,      // Ice + Electric
    CORRUPTED_VITALS,   // Blood + Poison
    HOLY_SIPHON,        // Light + Blood
    TOXIC_FROSTBITE,    // Ice + Poison
    SHOCKING_CURRENT      // Electric + Water
}

