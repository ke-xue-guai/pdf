package com.export.pdf.entity;


import com.export.pdf.context.Context;
import com.export.pdf.entity.abs.AbstractElement;
import com.export.pdf.entity.abs.IValueElement;
import com.export.pdf.style.Borders;

/**
 * 单元格的高度，依赖行的高度
 * 单元格的宽度，依赖table的grid属性
 */
public class TableCol extends AbstractElement implements IValueElement {

    private int rowspan = 1;

    private int colspan = 1;

    private String value;

    private Text text;

    private Borders borders;

    @Override
    public void draw(Context context, float x, float y) {
        if (this.text != null) {
            this.text.draw(context, this, x, y);
        }
        if (this.borders != null) {
            this.borders.draw(context, this, x, y);
        }
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Borders getBorders() {
        return borders;
    }

    public void setBorders(Borders borders) {
        this.borders = borders;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
