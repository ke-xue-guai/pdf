package com.export.pdf.context;

import com.export.pdf.style.Padding;

/**
 * 上下文
 */
public class Context {
    private final Padding pagePadding;
    private final float pageW;
    private final float pageH;
    private Pointer pointer;

    private int pageIndex = 1;

    public Context(Padding pagePadding, float pageW, float pageH) {
        this.pagePadding = pagePadding;
        this.pageW = pageW;
        this.pageH = pageH;
    }

    public Padding getPagePadding() {
        return pagePadding;
    }

    public float getPageW() {
        return pageW;
    }

    public float getPageH() {
        return pageH;
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void nextPage() {
        this.pageIndex++;
    }

}
