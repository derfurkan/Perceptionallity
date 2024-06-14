package de.furkan.perceptionallity;

import de.furkan.perceptionallity.discord.DiscordRPCHandler;
import javax.swing.*;
import lombok.Getter;

public class Perceptionallity {

  @Getter private static Game game;
  @Getter private static DiscordRPCHandler discordRPCHandler;

  public static void main(String[] args) {
    game = new Game();
    discordRPCHandler = new DiscordRPCHandler(game);
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      enableHiResTimer();
      game.start();
    } catch (Exception e) {
      getGame().handleFatalException(e);
    }

    try {
      discordRPCHandler.initializeRPC();
    } catch (Exception e) {
      e.printStackTrace();
      // Ignore
    }
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
