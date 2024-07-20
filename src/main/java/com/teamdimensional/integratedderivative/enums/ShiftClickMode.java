package com.teamdimensional.integratedderivative.enums;

public enum ShiftClickMode {
    ONE_ITEM(0),
    STACK_ROUNDED_DOWN(1),
    STACK_ROUNDED_UP(2);

    /**
     * Value for this difficulty
     */
    public final int value;

    private ShiftClickMode(int value) {
        this.value = value;
    }
}
