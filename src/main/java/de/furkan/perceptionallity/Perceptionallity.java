package de.furkan.perceptionallity;

import de.furkan.perceptionallity.discord.DiscordRPCHandler;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
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
      discordRPCHandler.initializeRPC();
    } catch (Exception e) {
      handleFatalException(e);
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

  public static void handleFatalException(String exception) {
    handleFatalException(new RuntimeException(exception));
  }

  public static void handleFatalException(Exception e) {
    e.printStackTrace();

    if (getGame().getGameFrame() != null) getGame().getGameFrame().dispose();

    StringBuilder message = new StringBuilder();
    message
        .append("Engine encountered fatal error")
        .append("\n\nTheir reality crashed for you but not for them..")
        .append("\nTry again or let them be.. forever.")
        .append("\n\n\n-- Debug --\n\n")
        .append("Class: ")
        .append(e.getClass().getSimpleName())
        .append(" (")
        .append(e.getMessage())
        .append(")")
        .append("\n\nGame State: ")
        .append(getGame().getGameManager().getGameState().name())
        .append("\n\nTotal Active Threads: ")
        .append(Thread.activeCount())
        .append("\n\n")
        .append("Current Thread: ")
        .append(Thread.currentThread().getName())
        .append("(")
        .append(Thread.currentThread().threadId())
        .append(")");
    message.append("\n\n\nStackTrace:\n\n");
    for (int i = 0; i < e.getStackTrace().length; i++) {
      message.append(e.getStackTrace()[i].toString()).append("\n");
    }

    long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    message
        .append("\n\n\nAvailable Processors: ")
        .append(Runtime.getRuntime().availableProcessors())
        .append("\n\nAllocated memory: ")
        .append(Runtime.getRuntime().freeMemory() / 1000 / 1000)
        .append(" MB")
        .append("\n\nUsed Memory: ")
        .append(usedMemory / 1000 / 1000)
        .append(" MB")
        .append("\n\nFree Memory: ")
        .append((Runtime.getRuntime().maxMemory() - usedMemory) / 1000 / 1000)
        .append(" MB")
        .append("\n\nTotal Memory: ")
        .append(Runtime.getRuntime().maxMemory() / 1000 / 1000)
        .append(" MB");
    JTextArea textArea = new JTextArea(message.toString());
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new java.awt.Dimension(1000, 300));

    Toolkit.getDefaultToolkit()
        .getSystemClipboard()
        .setContents(new StringSelection(message.toString()), null);

    JOptionPane.showMessageDialog(
        null,
        scrollPane,
        "Perceptionallity | Fatal Error - Reality crashed",
        JOptionPane.ERROR_MESSAGE);

    System.exit(0);
  }
}
