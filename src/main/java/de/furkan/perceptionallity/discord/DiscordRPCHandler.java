package de.furkan.perceptionallity.discord;

import de.furkan.perceptionallity.Game;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import java.awt.*;
import java.time.Instant;
import lombok.Getter;

@Getter
public class DiscordRPCHandler {
  private final Game game;
  private Activity currentActivity;
  private Core discordCore;
  private CreateParams createParams;

  public DiscordRPCHandler(Game game) {
    this.game = game;
  }

  public void initializeRPC() throws Exception {
    if (ProcessHandle.allProcesses()
        .noneMatch(
            processHandle ->
                processHandle.info().command().orElse("").toLowerCase().contains("discord"))) {
      game.getLogger().warning("Discord not active. RPC initialization cancelled.");
      return;
    }
    if (discordCore != null) {
      game.getLogger()
          .warning(
              "Discord core has already been initialized. Please close it first before initializing it again.");
      return;
    }
    Core.initDownload();

    createParams = new CreateParams();
    createParams.setClientID(1250987665940152411L);
    createParams.setFlags(CreateParams.Flags.DEFAULT);

    discordCore = new Core(createParams);
    currentActivity = new Activity();
    currentActivity.setType(ActivityType.PLAYING);
    currentActivity.timestamps().setStart(Instant.now());
    currentActivity.assets().setLargeImage("main");

    discordCore.activityManager().updateActivity(currentActivity);

    new Thread(
            () -> {
              while (discordCore.isOpen()) {
                try {
                  discordCore.runCallbacks();
                  Thread.sleep(16);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
  }

  public void setState(RPCStates rpcStates, String additionalText) {
    if (currentActivity == null) return;
    currentActivity.setDetails(rpcStates.getValue());
    currentActivity.setState(additionalText);
    discordCore.activityManager().updateActivity(currentActivity);
  }

  public void closeRPC() {
    discordCore.close();
    currentActivity.close();
    createParams.close();
  }
}
