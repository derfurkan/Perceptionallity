package de.furkan.perceptionallity.discord;

import lombok.Getter;

@Getter
public enum RPCStates {
  IN_MENU("In Menu"),
  IN_GAME("In Game");

  private final String value;

  RPCStates(String value) {
    this.value = value;
  }
}
