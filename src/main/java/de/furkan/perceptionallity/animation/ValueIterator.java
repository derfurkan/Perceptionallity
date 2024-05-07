package de.furkan.perceptionallity.animation;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class ValueIterator {

    private final InterpolationType interpolationType;
    private final int index = 0;
    @Setter
    @Getter
    private float targetValue;
    private float[] targetValues;
    @Setter
    @Getter
    private float currentValue, initialValue;
    private float[] currentValues, initialValues;
    private float step;

    /**
     * Constructs a new ValueIterator for single float values. This constructor initializes the
     * iterator with initial and target values, a step size, and an interpolation type.
     *
     * @param initialValue      the initial value from which the interpolation starts.
     * @param targetValue       the target value to which the interpolation should proceed.
     * @param step              the step size used for each update towards the target value.
     * @param interpolationType the type of interpolation to use, which can affect how the step size
     *                          is adjusted.
     */
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

    /**
     * Checks if the current value has reached the target value.
     *
     * @return true if the current value is equal to the target value, false otherwise.
     */
    public boolean isFinished() {
        return currentValue == targetValue;
    }

    /**
     * Updates the current value towards the target value based on the step size and interpolation
     * type. If the interpolation type is SMOOTH_END, the step size is dynamically adjusted as the
     * value approaches the target.
     */
    public void updateValue() {
        if (currentValue == targetValue) {
            return;
        }

        float value = currentValue;
        float remainingDistance =
                currentValue > targetValue ? currentValue - targetValue : targetValue - currentValue;
        float effectiveStep = Math.min(step, remainingDistance);

        if (Objects.requireNonNull(interpolationType) == InterpolationType.SMOOTH_END) {
            step =
                    (int) Math.ceil(step * (1.0 - (double) effectiveStep / (double) (targetValue - value)));
        }

        if (currentValue < targetValue) currentValue += effectiveStep;
        else currentValue -= effectiveStep;
    }

    /**
     * Reverses the direction of interpolation by swapping the current value with the target value,
     * and setting the initial value to the new current value.
     */
    public void reverse() {
        currentValue = targetValue;
        targetValue = initialValue;
        initialValue = currentValue;
    }

    /**
     * Resets the current value to the initial value.
     */
    public void reset() {
        currentValue = initialValue;
    }
}
