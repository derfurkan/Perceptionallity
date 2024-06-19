package de.furkan.perceptionallity.menu.components.checkbox;

import de.furkan.perceptionallity.menu.components.MenuComponent;
import java.awt.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class MenuCheckbox extends MenuComponent {

    private final JCheckBox rawComponent;

    public MenuCheckbox(int x, int y,int size,int thickness) {
    super(x, y, new Dimension(size,size-5));
        rawComponent = new JCheckBox();
        rawComponent.setIcon(new CustomCheckBoxIcon(false,getDimension(),thickness));
        rawComponent.setSelectedIcon(new CustomCheckBoxIcon(true,getDimension(),thickness));
        rawComponent.setFocusPainted(false);
        rawComponent.setFocusable(false);
        rawComponent.setOpaque(false);
    }

    @Override
    public JComponent getJComponent() {
        return rawComponent;
    }
}
class CustomCheckBoxIcon implements Icon {
    private final boolean selected;
    private final Dimension bounds;
    private final int thickness;

    public CustomCheckBoxIcon(boolean selected, Dimension bounds,int thickness) {
        this.selected = selected;
        this.bounds = bounds;
        this.thickness = thickness;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = getIconWidth();
        g2.setStroke(new BasicStroke(thickness));
        if (selected) {
            g2.setColor(new Color(255, 255, 255, 255));
            g2.fillRect(x, y, size, size);
        } else {
            g2.setColor(new Color(255, 255, 255, 0));
            g2.fillRect(x, y, size, size);
            g2.setColor(Color.WHITE);
            g2.drawRect(x + thickness / 2, y + thickness / 2, size - thickness, size - thickness);
        }
    }

    @Override
    public int getIconWidth() {
        return bounds.width-5;
    }

    @Override
    public int getIconHeight() {
        return bounds.height;
    }
}