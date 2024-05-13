package de.furkan.perceptionallity;

import lombok.Getter;

import javax.swing.*;

public class Perceptionallity {

  @Getter private static Game game;

  public static void main(String[] args) {
    game = new Game();
      try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
               UnsupportedLookAndFeelException e) {
          throw new RuntimeException(e);
      }
      enableHiResTimer();
    game.start();
  }

  // Workaround for windows slowing down Frame time
  static void enableHiResTimer() {
    Thread thread =
        new Thread("HiResTimer") {
          @Override
          public void run() {
            while (true) {
              try {
                sleep(Integer.MAX_VALUE);
              } catch (InterruptedException ignore) {
              }
            }
          }
        };
    thread.setDaemon(true);
    thread.start();
  }
}
