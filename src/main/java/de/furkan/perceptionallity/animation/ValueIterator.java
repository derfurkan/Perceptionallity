package de.furkan.perceptionallity.animation;

public class ValueIterator {

  private final int targetValue;
  private final InterpolationType interpolationType;
  private int currentValue;
  private int step;

  public ValueIterator(
      int initialValue, int targetValue, int step, InterpolationType interpolationType) {
    this.currentValue = initialValue;
    this.targetValue = targetValue;
    this.step = step;
    this.interpolationType = interpolationType;
  }

  public boolean isFinished() {
    return currentValue >= targetValue;
  }

  public int retrieveCurrentAndUpdateValue() {
    if (currentValue >= targetValue) {
      return targetValue;
    }

    int value = currentValue;
    int remainingDistance = targetValue - currentValue;
    int effectiveStep = Math.min(step, remainingDistance);

    switch (interpolationType) {
      case DEFAULT -> currentValue += step;
      case SMOOTH_END -> {
        currentValue += effectiveStep;
        step =
            (int) Math.ceil(step * (1.0 - (double) effectiveStep / (double) (targetValue - value)));
      }
      case SMOOTH_START -> {
        currentValue += effectiveStep;
        step =
            (int) Math.ceil(step * (1.0 + (double) effectiveStep / (double) (targetValue - value)));
      }
      default ->
          throw new IllegalArgumentException("Unsupported InterpolationType: " + interpolationType);
    }

    return value;
  }
}
