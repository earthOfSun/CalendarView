package com.earthsun.calendarview.calendarView;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SignDate implements Serializable {

    private int x;
    private int y;
    private boolean isSign;
    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }
}
