package com.export.pdf.style;


import com.export.pdf.context.Context;
import com.export.pdf.context.Pointer;

public class InnerBorder extends Border {

    public InnerBorder(int type, float width) {
        this.type = type;
        this.width = width;
    }

    @Override
    public void draw(Context context, float x, float y) {
        Pointer pointer = context.getPointer();
        float w = this.parent.getW();
        float h = this.parent.getH();
        switch (this.type) {
            case BORDER_TYPE_TOP:
                pointer.line(
                        new float[]{x + this.width, y - this.width},
                        new float[]{x, y},
                        new float[]{x + w, y},
                        new float[]{x + w - this.width, y - this.width},
                        this.color
                );
                break;
            case BORDER_TYPE_LEFT:
                pointer.line(
                        new float[]{x, y - h},
                        new float[]{x, y},
                        new float[]{x + this.width, y - this.width},
                        new float[]{x + this.width, y - h + this.width},
                        this.color
                );
                break;
            case BORDER_TYPE_RIGHT:
                pointer.line(
                        new float[]{x + w - this.width, y - h + this.width},
                        new float[]{x + w - this.width, y - this.width},
                        new float[]{x + w, y},
                        new float[]{x + w, y - h},
                        this.color
                );
                break;
            case BORDER_TYPE_BOTTOM:
                pointer.line(
                        new float[]{x, y - h},
                        new float[]{x + this.width, y - h + this.width},
                        new float[]{x + w - this.width, y - h + this.width},
                        new float[]{x + w, y - h},
                        this.color
                );
                break;
        }
    }

}
