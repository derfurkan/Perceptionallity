package de.furkan.perceptionallity.menu.components;

import de.furkan.perceptionallity.Perceptionallity;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import lombok.Setter;

public class MenuButton extends MenuComponent {

  private final JComponent rawComponent;

  @Setter private MenuButtonClick buttonClick;

  public MenuButton(int x, int y, int size, String text) {
    super(x, y, new Dimension());
    MenuLabel menuLabel = new MenuLabel(0, 0, text, size, Color.WHITE);
    setDimension(menuLabel.getDimension());
    rawComponent = menuLabel.getJComponent();

    rawComponent.addMouseListener(
        new MouseListener() {
          @Override
          public void mouseClicked(MouseEvent e) {}

          @Override
          public void mousePressed(MouseEvent e) {}

          @Override
          public void mouseReleased(MouseEvent e) {
            if (buttonClick == null) {
              Perceptionallity.getGame()
                  .getLogger()
                  .warning("No buttonClick defined for button " + text);
              return;
            }
            if (e.getButton() != MouseEvent.BUTTON1 || !isSteadyComponent()) return;
            buttonClick.onClick();
          }

          @Override
          public void mouseEntered(MouseEvent e) {
            if (isSteadyComponent()) {
              //              Perceptionallity.getGame()
              //                  .getSoundEngine()
              //                  .playAudio(
              //                      getResourceManager().getResource("button_hover", Sound.class),
              // 1f, false);
              setOpacity(0.5f);
            }
          }

          @Override
          public void mouseExited(MouseEvent e) {
            if (isSteadyComponent()) setOpacity(1f);
          }
        });
  }

  @Override
  public JComponent getJComponent() {
    return rawComponent;
  }
}
