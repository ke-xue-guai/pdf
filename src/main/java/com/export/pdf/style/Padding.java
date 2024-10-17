package com.export.pdf.style;

public class Padding {

    private float top;
    private float left;
    private float right;
    private float bottom;

    public Padding(float one) {
        this.top = one;
        this.left = one;
        this.right = one;
        this.bottom = one;
    }

    public Padding(float one, float two) {
        this.top = one;
        this.left = two;
        this.right = two;
        this.bottom = one;
    }

    public Padding(float top, float left, float right, float bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

}
