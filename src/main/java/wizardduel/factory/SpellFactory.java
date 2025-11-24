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

import java.util.List;

public final class SpellFactory {

    private SpellFactory() {
    }

    // --- Single target DAMAGE spells ---

    public static Spell fireball() {
        return new DamageSpell(
                "fireball_common_01",
                "Fireball",
                14,             // manaCost (brangus, bet stiprus)
                110,            // power (AI heuristic)
                90,             // damageMin
                130,            // damageMax
                0.85,           // accuracy
                0.20,           // critChance
                1.7,            // critMultiplier
                SpellType.DAMAGE,
                Element.FIRE,
                0.35,           // 35% šansas uždėti FIRE
                2,              // 2 turnai FIRE solo efekto
                Rarity.COMMON
        );
    }

    public static Spell iceShard() {
        return new DamageSpell(
                "ice_shard_common_01",
                "Ice Shard",
                12,             // pigesnis, bet kiek silpnesnis
                90,
                70,
                95,
                0.9,            // labai tikslus
                0.10,
                1.5,
                SpellType.DAMAGE,
                Element.ICE,
                0.45,           // ICE lengviau uždėt, nes debuffinis
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
                0.55,           // 55% POISON
                3,              // ilgesnis poison
                Rarity.COMMON
        );
    }

    // --- Shield / utility ---

    public static Spell magicShield() {
        return new ShieldSpell(
                "magic_shield_common_01",
                "Magic Shield",
                14,             // manaCost
                80,             // power (AI)
                120,            // shieldValue (12% HP)
                SpellType.SHIELD,
                Element.LIGHT,
                0.4,            // gera proga užsidėti LIGHT
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
                SpellType.MANADRAIN,
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

    // --- Deck'ai ---

    public static List<Spell> createDefaultPlayerDeck() {
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
        // Kol kas tas pats – vėliau galėsim diferencijuoti
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
}
