package wizardduel.factory;

import wizardduel.model.Spell;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;
import wizardduel.model.spells.DamageSpell;
import wizardduel.model.spells.DotSpell;
import wizardduel.model.spells.ManaDrainSpell;
import wizardduel.model.spells.ShieldSpell;
import wizardduel.model.spells.ChargeSpell;

import java.util.ArrayList;
import java.util.List;

public final class SpellFactory {

    private SpellFactory() {
    }

    public static Spell fireball() {
        return new DamageSpell(
                "fireball_common_01",
                "Fireball",
                14,
                110,
                90,
                130,
                0.85,
                0.20,
                1.7,
                SpellType.DAMAGE,
                Element.FIRE,
                0.35,
                2,
                Rarity.COMMON
        );
    }

    public static Spell iceShard() {
        return new DamageSpell(
                "ice_shard_common_01",
                "Ice Shard",
                12,
                90,
                70,
                95,
                0.9,
                0.10,
                1.5,
                SpellType.DAMAGE,
                Element.ICE,
                0.45,
                2,
                Rarity.COMMON
        );
    }

    public static Spell poisonBolt() {
        return new DamageSpell(
                "poison_bolt_common_01",
                "Poison Bolt",
                12,
                85,
                65,
                90,
                0.85,
                0.10,
                1.5,
                SpellType.DAMAGE,
                Element.POISON,
                0.55,
                3,
                Rarity.COMMON
        );
    }

    public static Spell magicShield() {
        return new ShieldSpell(
                "magic_shield_common_01",
                "Magic Shield",
                14,
                80,
                120,
                SpellType.SHIELD,
                Element.LIGHT,
                0.4,
                2,
                Rarity.COMMON
        );
    }

    public static Spell burningCurse() {
        return new DotSpell(
                "burning_curse_common_01",
                "Burning Curse",
                16,
                95,
                40,
                3,
                SpellType.DOT,
                Element.FIRE,
                0.4,
                2,
                Rarity.UNCOMMON
        );
    }

    public static Spell manaDrain() {
        return new ManaDrainSpell(
                "mana_drain_common_01",
                "Mana Drain",
                16,
                80,
                30,
                SpellType.MANAD_RAIN,
                Element.ELECTRIC,
                0.35,
                2,
                Rarity.UNCOMMON
        );
    }

    public static Spell chargedStrike() {
        return new ChargeSpell(
                "charged_strike_common_01",
                "Charged Strike",
                18,
                130,
                110,
                160,
                0.8,
                0.25,
                1.8,
                2.0,
                Element.ELECTRIC,
                0.3,
                2,
                Rarity.RARE
        );
    }

    // ================= DECK BUILDING SUPPORT =================

    /**
     * Returns a preview pool of all spells that can be chosen for a deck.
     * These instances are for selection only, not used in battle directly.
     */
    public static List<Spell> createSpellPool() {
        List<Spell> pool = new ArrayList<>();
        pool.add(fireball());
        pool.add(iceShard());
        pool.add(poisonBolt());
        pool.add(magicShield());
        pool.add(burningCurse());
        pool.add(manaDrain());
        pool.add(chargedStrike());
        return pool;
    }

    /**
     * Creates a fresh spell instance by its ID.
     * Used to build decks after the player has selected spells.
     */
    public static Spell createById(String id) {
        switch (id) {
            case "fireball_common_01":
                return fireball();
            case "ice_shard_common_01":
                return iceShard();
            case "poison_bolt_common_01":
                return poisonBolt();
            case "magic_shield_common_01":
                return magicShield();
            case "burning_curse_common_01":
                return burningCurse();
            case "mana_drain_common_01":
                return manaDrain();
            case "charged_strike_common_01":
                return chargedStrike();
            default:
                throw new IllegalArgumentException("Unknown spell id: " + id);
        }
    }

    public static List<Spell> createDefaultPlayerDeck() {
        // default deck, jeigu kada norėsi skipint deck building
        return List.of(
                fireball(),
                iceShard(),
                poisonBolt(),
                magicShield(),
                burningCurse(),
                manaDrain(),
                chargedStrike()
        );
    }

    public static List<Spell> createDefaultEnemyDeck() {
        // AI gali naudoti tą patį pool'ą – vėliau galėsim atskirti
        return createDefaultPlayerDeck();
    }
}
