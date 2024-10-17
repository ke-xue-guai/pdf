package com.export.pdf.entity;


import com.export.pdf.context.Context;
import com.export.pdf.entity.abs.AbstractElement;

import java.util.ArrayList;
import java.util.List;

public class TableRow extends AbstractElement {

    private List<TableCol> cols = new ArrayList<>();

    public void addCol(TableCol row) {
        this.cols.add(row);
    }

    public void addCol(int index, TableCol row) {
        int y = 0;
        if (y == index) {
            this.cols.add(0, row);
            return;
        }
        for (int i = 0; i < this.cols.size(); i++) {
            y += cols.get(i).getColspan();
            if (y >= index) {
                this.cols.add(i + 1, row);
                break;
            }
        }
    }

    @Override
    public void draw(Context context, float x, float y) {
        float s = x;
        for (TableCol col : this.cols) {
            col.draw(context, s, y);
            s += col.getW();
            float h = col.getH();
        }
    }

    public int getColNumber() {
        int count = 0;
        for (TableCol col : this.cols) {
            count += col.getColspan();
        }
        return count;
    }

    public List<TableCol> getCols() {
        return cols;
    }

    public void setCols(List<TableCol> cols) {
        this.cols = cols;
    }

}
