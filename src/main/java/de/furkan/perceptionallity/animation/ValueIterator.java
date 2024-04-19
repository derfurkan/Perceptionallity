package de.furkan.perceptionallity.animation;


public class ValueIterator {

    private int currentValue;
    private final int targetValue;
    private int step;
    private final InterpolationType interpolationType;

    public ValueIterator(int initialValue, int targetValue, int step,InterpolationType interpolationType) {
        this.currentValue = initialValue;
        this.targetValue = targetValue;
        this.step = step;
        this.interpolationType = interpolationType;
    }

    public boolean isFinished() {
        return currentValue>=targetValue;
    }

    public int retrieveCurrentAndUpdateValue() {
        if (currentValue >= targetValue) {
            return targetValue;
        }

        int value = currentValue;
        int remainingDistance = targetValue - currentValue;
        int effectiveStep = Math.min(step, remainingDistance);

        switch (interpolationType) {
            case DEFAULT:
                currentValue += step; // Just add the steps
                break;
            case SMOOTH_END:
                currentValue += effectiveStep; // Update currentValue by the effective step size
                step = (int)Math.ceil(step * (1.0 - (double)effectiveStep / (double)(targetValue - value))); // Decrease step size proportional to remaining distance
                break;
            case SMOOTH_START:
                currentValue += effectiveStep; // Update currentValue by the effective step size
                step = (int)Math.ceil(step * (1.0 + (double)effectiveStep / (double)(targetValue - value))); // Increase step size proportional to remaining distance
                break;
            default:
                throw new IllegalArgumentException("Unsupported InterpolationType: " + interpolationType);
        }

        return value;
    }

}