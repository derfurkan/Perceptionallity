package de.furkan.perceptionallity.animation;

import lombok.Getter;
import lombok.Setter;

public class ValueIterator {

  @Setter
  @Getter
  private float targetValue;
  private final InterpolationType interpolationType;
  @Setter
  private float currentValue;
  private float step;

  public ValueIterator(
      float initialValue, float targetValue, float step, InterpolationType interpolationType) {
    this.currentValue = initialValue;
    this.targetValue = targetValue;
    this.step = step;
    this.interpolationType = interpolationType;
  }

  public boolean isFinished() {
    return currentValue == targetValue;
  }

  public float retrieveCurrentAndUpdateValue() {
    if (currentValue == targetValue) {
      return targetValue;
    }

    float value = currentValue;
    float remainingDistance = currentValue > targetValue ? currentValue - targetValue : targetValue - currentValue;
    float effectiveStep = Math.min(step, remainingDistance);

    switch (interpolationType) {
      case DEFAULT -> {
        if(currentValue < targetValue)
          currentValue += effectiveStep;
        else
          currentValue -= effectiveStep;
      }
      case SMOOTH_END -> {
        if(currentValue < targetValue)
          currentValue += effectiveStep;
        else
          currentValue -= effectiveStep;
        step =
            (int) Math.ceil(step * (1.0 - (double) effectiveStep / (double) (targetValue - value)));
      }
      case SMOOTH_START -> {
        if(currentValue < targetValue)
          currentValue += effectiveStep;
        else
          currentValue -= effectiveStep;
        step =
            (int) Math.ceil(step * (1.0 + (double) effectiveStep / (double) (targetValue - value)));
      }
      default ->
          throw new IllegalArgumentException("Unsupported InterpolationType: " + interpolationType);
    }

    return value;
  }
}
