package com.export.pdf.entity.abs;

import com.export.pdf.context.Context;

public abstract class AbstractChildValueElement extends AbstractElement {

    protected IValueElement parent;

    public void draw(Context pointer, IValueElement parent, float x, float y) {
        this.parent = parent;
        this.draw(pointer, x, y);
    }

}
