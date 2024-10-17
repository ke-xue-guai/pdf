package com.export.pdf.style;

import com.export.pdf.entity.abs.AbstractChildElement;

import java.awt.*;

public abstract class Border extends AbstractChildElement {

    protected Color color = Color.black;

    public static final int BORDER_TYPE_TOP = 1;
    public static final int BORDER_TYPE_LEFT = 2;
    public static final int BORDER_TYPE_RIGHT = 3;
    public static final int BORDER_TYPE_BOTTOM = 4;

    protected int type;
    protected float width;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

}
