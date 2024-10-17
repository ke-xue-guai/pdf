package com.export.pdf.entity.page;

import com.export.pdf.entity.Table;
import com.export.pdf.entity.TableCol;
import com.export.pdf.entity.TableRow;
import com.export.pdf.style.TableGrid;

import java.util.ArrayList;
import java.util.List;

class SplitTable {

    private final Table table = new Table();
    private final List<Integer> indexes = new ArrayList<>();
    private TableRow row;

    SplitTable(int fixedNumberOfCol) {
        int i = 0;
        while (i <= fixedNumberOfCol) {
            indexes.add(i);
            i++;
        }
    }

    public void addIndex(int index) {
        this.indexes.add(index);
    }

    public boolean contains(int index) {
        return this.indexes.contains(index);
    }

    public void addTableGrid(TableGrid grid) {
        int index = grid.getIndex();
        int i = this.indexes.indexOf(index);
        TableGrid clone = new TableGrid();
        clone.setIndex(i);
        clone.setWidth(grid.getWidth());
        this.table.addTableGrid(clone);
    }

    public void addRow(float h) {
        TableRow row = new TableRow();
        row.setH(h);
        this.row = row;
        this.table.addRow(row);
    }

    public void addCol(TableCol col) {
        this.row.addCol(col);
    }

    public Table getTable() {
        return table;
    }

}
