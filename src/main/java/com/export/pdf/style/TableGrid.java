package com.export.pdf.style;

public class TableGrid {

    private int index;

    private float width;

    public TableGrid() {

    }

    public TableGrid(int index, float width) {
        this.index = index;
        this.width = width;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

}
