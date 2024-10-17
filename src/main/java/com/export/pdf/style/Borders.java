package com.export.pdf.style;


import com.export.pdf.context.Context;
import com.export.pdf.entity.abs.AbstractChildElement;

import java.util.ArrayList;
import java.util.List;

public class Borders extends AbstractChildElement {

    private final List<Border> borders = new ArrayList<>();

    public void addBorder(Border border) {
        this.borders.add(border);
    }

    @Override
    public void draw(Context context, float x, float y) {
        for (Border border : this.borders) {
            border.draw(context, this.parent, x, y);
        }
    }

}
