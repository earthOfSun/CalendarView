package com.earthsun.calendarview.calendarView;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SignDate implements Serializable {

    private int coluIndex;
    private int rowIndex;
    private DateType dateType;
    private int day;

    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getColuIndex() {
        return coluIndex;
    }

    public void setColuIndex(int coluIndex) {
        this.coluIndex = coluIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

}
