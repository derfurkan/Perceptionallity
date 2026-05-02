package de.furkan.perceptionallity.menu.components.label;

import de.furkan.perceptionallity.menu.components.MenuComponent;
import de.furkan.perceptionallity.util.font.GameFont;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends MenuComponent {

    private final JLabel rawComponent;

    public MenuLabel(int x, int y, String text, float size, Color color, GameFont gameFont) {
        super(x, y, new Dimension());
        rawComponent = new JLabel(text);
        rawComponent.setForeground(color);
        rawComponent.setFont(gameFont.getFont().deriveFont(size));

        // Calculating dimension by font metrics
        recalculateDimension();
    }

    public MenuLabel(int x, int y, String text, float size, Color color) {
        super(x, y, new Dimension());
        rawComponent = new JLabel(text);
        rawComponent.setForeground(color);
        rawComponent.setFont(
                getResourceManager().getResource("menu_font", GameFont.class).getFont().deriveFont(size));

        // Calculating dimension by font metrics
        recalculateDimension();
    }

    public String getText() {
        return rawComponent.getText();
    }

    public void setText(String newText) {
        rawComponent.setText(newText);
    }

    public void recalculateDimension() {
        FontMetrics rawFontMetrics = rawComponent.getFontMetrics(rawComponent.getFont());
        Dimension dimension =
                new Dimension(
                        rawFontMetrics.stringWidth(rawComponent.getText()) + 16, // +4 for some padding
                        rawFontMetrics.getAscent() + rawFontMetrics.getDescent()); // Height includes ascent and descent

        setDimension(dimension);
    }

    @Override
    public JComponent getJComponent() {
        return rawComponent;
    }
}
