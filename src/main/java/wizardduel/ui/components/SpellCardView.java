package wizardduel.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import wizardduel.model.Spell;
import wizardduel.model.enums.Element;
import wizardduel.model.enums.Rarity;
import wizardduel.model.enums.SpellType;

public class SpellCardView extends VBox {

    private final Spell spell;

    public SpellCardView(Spell spell) {
        this.spell = spell;
        setPadding(new Insets(8));
        setSpacing(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("spell-card");

        // Baltas fonas, kad matytųsi, ir truputis radius
        setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        Label nameLabel = new Label(spell.getName());
        nameLabel.getStyleClass().add("spell-name");
        nameLabel.setTextFill(Color.BLACK);

        Label damageLabel = new Label("Power: " + spell.getPower());
        damageLabel.setTextFill(Color.BLACK);

        Label manaLabel = new Label("Mana: " + spell.getManaCost());
        manaLabel.setTextFill(Color.BLACK);

        Label elementLabel = new Label("Element: " + elementToString(spell.getElement()));
        elementLabel.setTextFill(Color.BLACK);

        Label typeLabel = new Label("Type: " + spellTypeToString(spell.getType()));
        typeLabel.setTextFill(Color.BLACK);

        getChildren().addAll(nameLabel, damageLabel, manaLabel, elementLabel, typeLabel);

        Color borderColor = rarityToColor(spell.getRarity());
        setBorder(new javafx.scene.layout.Border(new BorderStroke(
                borderColor,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                new BorderWidths(2)
        )));
    }

    private static String elementToString(Element element) {
        if (element == null) return "None";
        return switch (element) {
            case FIRE -> "Fire";
            case WATER -> "Water";
            case ICE -> "Ice";
            case ELECTRIC -> "Electric";
            case LIGHT -> "Light";
            case BLOOD -> "Blood";
            case POISON -> "Poison";
            case NEUTRAL -> "Neutral";
        };
    }

    private static String spellTypeToString(SpellType type) {
        if (type == null) return "Unknown";
        return switch (type) {
            case DAMAGE -> "Damage";
            case DOT -> "Damage over Time";
            case SHIELD -> "Shield";
            case MANA_DRAIN -> "Mana Drain";
            case CHARGE -> "Charge";
        };
    }

    private static Color rarityToColor(Rarity rarity) {
        if (rarity == null) return Color.GRAY;
        return switch (rarity) {
            case COMMON -> Color.LIGHTGRAY;
            case UNCOMMON -> Color.GREEN;
            case RARE -> Color.BLUE;
            case EPIC -> Color.PURPLE;
            case LEGENDARY -> Color.GOLD;
        };
    }
}
