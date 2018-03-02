package com.earthsun.calendarview.calendarView;

/**
 * Created by Administrator on 2018/3/1.
 */

public enum DateType {
    /**
     * 已签到状态，时间已过
     */
    SIGNED(0),
    /**
     * 未签到状态，时间已过
     */
    UNSIGNED(1),
    /**
     * 等待状态，即当日还未签到
     */
    WAITING(2),
    /**
     * 不可达到状态，未到时间
     */
    UNREACHABLE(3),
    /**
     * 不可用状态，非当前月份
     */
    DISABLED(4);

    private int value;

    DateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DateType valueOf(int value) {
        switch (value) {
            case 0:
                return SIGNED;
            case 1:
                return UNSIGNED;
            case 2:
                return WAITING;
            case 3:
                return UNREACHABLE;
            case 4:
                return DISABLED;
            default:
                return DISABLED;
        }
    }
}
