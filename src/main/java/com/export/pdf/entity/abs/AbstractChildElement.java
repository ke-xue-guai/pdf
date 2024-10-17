package com.export.pdf.entity.abs;

import com.export.pdf.context.Context;

public abstract class AbstractChildElement extends AbstractElement {

    protected AbstractElement parent;

    public void draw(Context pointer, AbstractElement parent, float x, float y) {
        this.parent = parent;
        this.draw(pointer, x, y);
    }

}
