package de.furkan.perceptionallity.animation;

import lombok.Getter;
import lombok.Setter;

public class ValueIterator {

  private final InterpolationType interpolationType;
  private final int index = 0;
  @Setter @Getter private float targetValue;
  private float[] targetValues;
  @Setter @Getter private float currentValue, initialValue;
  private float[] currentValues, initialValues;
  private float step;

  public ValueIterator(
      float initialValue, float targetValue, float step, InterpolationType interpolationType) {
    this.initialValue = initialValue;
    this.currentValue = initialValue;
    this.targetValue = targetValue;
    this.step = step;
    this.interpolationType = interpolationType;
  }

  public ValueIterator(
      float[] initialValues,
      float[] targetValues,
      float step,
      InterpolationType interpolationType) {
    throw new RuntimeException("Not implemented yet.");
  }

  public boolean isFinished() {
    return currentValue == targetValue;
  }

  public void updateValue() {
    if (currentValue == targetValue) {
      return;
    }

    float value = currentValue;
    float remainingDistance =
        currentValue > targetValue ? currentValue - targetValue : targetValue - currentValue;
    float effectiveStep = Math.min(step, remainingDistance);

    switch (interpolationType) {
      case SMOOTH_END ->
          step =
              (int)
                  Math.ceil(step * (1.0 - (double) effectiveStep / (double) (targetValue - value)));
    }

    if (currentValue < targetValue) currentValue += effectiveStep;
    else currentValue -= effectiveStep;
  }

  public void reverse() {
    currentValue = targetValue;
    targetValue = initialValue;
    initialValue = currentValue;
  }

  public void reset() {
    currentValue = initialValue;
  }
}
