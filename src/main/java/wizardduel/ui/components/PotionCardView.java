package wizardduel.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import wizardduel.model.enums.PotionType;

public class PotionCardView extends VBox {

    private final PotionType type;
    private final String displayName;
    private final int healPerUse;
    private final int maxUses;
    private boolean selected;

    private final Label titleLabel;
    private final Label healLabel;
    private final Label usesLabel;

    public PotionCardView(PotionType type,
                          String displayName,
                          int healPerUse,
                          int maxUses) {
        this.type = type;
        this.displayName = displayName;
        this.healPerUse = healPerUse;
        this.maxUses = maxUses;

        setPadding(new Insets(8));
        setSpacing(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("potion-card");

        setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        titleLabel = new Label(displayName);
        titleLabel.setTextFill(Color.BLACK);

        healLabel = new Label("Heal per use: " + healPerUse);
        healLabel.setTextFill(Color.BLACK);

        usesLabel = new Label("Uses: " + maxUses);
        usesLabel.setTextFill(Color.BLACK);

        getChildren().addAll(titleLabel, healLabel, usesLabel);

        updateBorder();
    }

    public PotionType getType() {
        return type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateBorder();
    }

    private void updateBorder() {
        Color color = selected ? Color.LIMEGREEN : Color.DARKGRAY;
        setBorder(new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                new BorderWidths(2)
        )));
    }
}
