package com.relax.main.beans;

import java.util.Objects;

public class GridCell {
    private int x;
    private int y;
    private String data;

    public GridCell(int x, int y, String data) {
        this.x = x;
        this.y = y;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GridCell gridCell)) return false;
        return x == gridCell.x && y == gridCell.y && Objects.equals(data, gridCell.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, data);
    }
}
