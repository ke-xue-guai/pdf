package com.export.pdf.entity.abs;


import com.export.pdf.context.Context;

public abstract class AbstractElement {

    protected float w = 0;
    protected float h = 0;

    /**
     * 绘画从左上角开始，x坐标0->w，y坐标h->0
     *
     * @param context 画笔
     * @param x       x坐标
     * @param y       y坐标
     */
    public abstract void draw(Context context, float x, float y);

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

}
