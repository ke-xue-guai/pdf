package com.export.pdf.entity;


import com.export.pdf.context.Context;
import com.export.pdf.entity.abs.AbstractElement;
import com.export.pdf.style.Borders;
import com.export.pdf.style.Padding;
import com.export.pdf.style.TableGrid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Table extends AbstractElement {

    private Borders borders;

    /**
     * 是否是自适应的表
     */
    private boolean isAutoFit = true;

    private List<TableRow> rows = new ArrayList<>();

    private List<TableGrid> tableGrids = new ArrayList<>();

    private boolean hasBuild = false;

    private boolean hasFormalize = false;

    @Override
    public void draw(Context context, float x, float y) {
        this.build(context);
        // 边框
        if (this.borders != null) {
            this.borders.draw(context, this, x, y);
        }
        // 行
        float s = y;
        for (TableRow row : this.rows) {
            row.draw(context, x, s);
            s -= row.getH();
        }
    }

    /**
     * 构建表格对象的属性
     * 包括表格的宽度、行的高度和列的宽度
     * 在一般条件下
     * 单元格的高度是无效的，表格都是自适应宽度
     */
    public void build(Context context) {
        if (this.hasBuild) {
            return;
        }
        this.hasBuild = true;
        // 根据占比，重新构建单元格的宽度
        int max = this.getMaxColNumber();
        List<Float> widths = new ArrayList<>();
        if (this.isAutoFit) {
            Padding padding = context.getPagePadding();
            float pageW = context.getPageW();
            // 计算有效宽度和高度
            float w = pageW - padding.getLeft() - padding.getRight();
            // 补充未设置的列宽，如果未设置宽度，则默认宽度占比为100
            for (int i = 0; i < max; i++) {
                int _i = i;
                Optional<TableGrid> optional = this.tableGrids.stream()
                        .filter(grid -> grid.getIndex() == _i).findAny();
                if (optional.isPresent()) {
                    continue;
                }
                this.tableGrids.add(new TableGrid(i, 100));
            }
            this.tableGrids.sort(Comparator.comparingInt(TableGrid::getIndex));
            // 计算总宽度
            long total = 0;
            for (TableGrid grid : this.tableGrids) {
                total += grid.getWidth();
            }
            for (TableGrid grid : this.tableGrids) {
                float compute = grid.getWidth() / total * w;
                widths.add(compute);
            }
        } else {
            // 补充未设置的列宽，默认宽度为50
            for (int i = 0; i < max; i++) {
                int _i = i;
                Optional<TableGrid> optional = this.tableGrids.stream()
                        .filter(grid -> grid.getIndex() == _i).findAny();
                if (optional.isPresent()) {
                    continue;
                }
                this.tableGrids.add(new TableGrid(i, 50));
            }
            this.tableGrids.sort(Comparator.comparingInt(TableGrid::getIndex));
            for (TableGrid grid : this.tableGrids) {
                widths.add(grid.getWidth());
            }
        }
        // 设置表格的宽度
        float tableW = 0;
        for (float width : widths) {
            tableW += width;
        }
        this.w = tableW;
        // 补充行
        this.formalize();
        // 设置单元格宽度
        float tableH = 0;
        for (int x = 0; x < this.rows.size(); x++) {
            TableRow row = this.rows.get(x);
            if (row.getH() == 0) {
                throw new RuntimeException("未设置表格行的高");
            }
            List<TableCol> cols = row.getCols();
            int y = 0;
            for (TableCol col : cols) {
                // 设置其宽度
                float width = widths.get(y);
                for (int i = 1; i < col.getColspan(); i++) {
                    width += widths.get(y + i);
                }
                col.setW(width);
                // 设置其高度
                float height = row.getH();
                for (int i = 1; i < col.getRowspan(); i++) {
                    height += this.rows.get(x + i).getH();
                }
                col.setH(height);
                if (!this.hasFormalize) {
                    // 填充下一行的占位单元格
                    if (col.getRowspan() > 1) {
                        TableRow nextRow = this.rows.get(x + 1);
                        TableCol tableCol = new TableCol();
                        tableCol.setColspan(col.getColspan());
                        tableCol.setRowspan(col.getRowspan() - 1);
                        nextRow.addCol(y, tableCol);
                    }
                }
                y += col.getColspan();
            }
            tableH += row.getH();
        }
        this.h = tableH;
    }

    /**
     * 使结构完整
     */
    public void formalize() {
        if (this.hasFormalize) {
            return;
        }
        int i = 0;
        while (i < this.rows.size()) {
            while (i < this.rows.size()) {
                TableRow row = this.rows.get(i);
                int min = 999;
                for (TableCol col : row.getCols()) {
                    int rowspan = col.getRowspan();
                    if (min > rowspan) {
                        min = rowspan;
                    }
                    if (min == 1) {
                        break;
                    }
                }
                if (min != 999 && min > 1) {
                    // 向下新增行
                    int add = 1;
                    while (add < min) {
                        TableRow clone = new TableRow();
                        clone.setH(row.getH());
                        this.rows.add(i + add, clone);
                        add++;
                    }
                    i += min;
                    break;
                }
                i++;
            }
        }
    }

    /**
     * 获取最大列数
     */
    public Integer getMaxColNumber() {
        int max = 0;
        //获取最大列的下标
        for (TableRow row : this.rows) {
            int n = row.getColNumber();
            if (max < n) {
                max = n;
            }
        }
        return max;
    }

    public void addRow(TableRow row) {
        this.rows.add(row);
    }

    public void addRows(List<TableRow> rows) {
        this.rows.addAll(rows);
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    public Borders getBorders() {
        return borders;
    }

    public void setBorders(Borders borders) {
        this.borders = borders;
    }

    public boolean isAutoFit() {
        return isAutoFit;
    }

    public void setAutoFit(boolean autoFit) {
        isAutoFit = autoFit;
    }

    public void addTableGrid(TableGrid grid) {
        this.tableGrids.add(grid);
    }

    public List<TableGrid> getTableGrids() {
        return tableGrids;
    }

    public void setTableGrids(List<TableGrid> tableGrids) {
        this.tableGrids = tableGrids;
    }

    public void setHasFormalize(boolean hasFormalize) {
        this.hasFormalize = hasFormalize;
    }

    @Override
    public float getH() {
        if (this.hasBuild) {
            return this.h;
        }
        float h = 0;
        for (TableRow row : this.rows) {
            h += row.getH();
        }
        return h;
    }

}
