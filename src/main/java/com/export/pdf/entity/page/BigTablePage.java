package com.export.pdf.entity.page;

import com.export.pdf.context.Context;
import com.export.pdf.context.Pointer;
import com.export.pdf.entity.Table;
import com.export.pdf.entity.abs.AbstractElement;
import com.export.pdf.entity.abs.AbstractPage;
import com.export.pdf.style.Padding;

import java.util.ArrayList;
import java.util.List;

public class BigTablePage extends AbstractPage {

    public static final int SPLIT_TYPE_Z = 0;
    public static final int SPLIT_TYPE_N = 1;

    private AbstractElement title;

    private AbstractElement remark;

    private Table table;

    /**
     * 列分页属性
     */
    private String splitTableProperty;

    /**
     * 拆分类型
     * 0:Z型，1:N型
     */
    private int splitType = SPLIT_TYPE_Z;

    /**
     * 固定列数
     */
    private int fixedNumberOfCol = 0;

    /**
     * 固定行数
     */
    private int fixedNumberOfRow = 0;

    /**
     * 页内的页码
     */
    private int index = 1;

    @Override
    public void draw(Context context, float x, float y) {
        float titleH = 0;
        float remarkH = 0;
        if (this.title != null) {
            titleH = this.title.getH();
        }
        if (this.remark != null) {
            remarkH = this.remark.getH();
        }
        List<Table> tables = this.split(context, titleH, remarkH);
        // 分完的每一个表都是单独的一页
        for (Table table : tables) {
            Pointer pointer = this.pointerFactory.createPointer();
            context.setPointer(pointer);
            // 内容
            if (this.title != null) {
                this.title.draw(context, x, y);
            }
            table.setBorders(this.table.getBorders());
            table.setHasFormalize(true);
            table.draw(context, x, y - titleH);
            if (this.footer != null) {
                this.footer.draw(context, this, 0, 0);
            }
            // 备注
            if (this.remark != null) {
                this.remark.draw(context, x, y - titleH - table.getH());
            }
            pointer.close();
            // 下一页
            context.nextPage();
            this.index++;
        }
    }

    private List<Table> split(Context context, float titleH, float remarkH) {
        this.table.build(context);
        Padding padding = context.getPagePadding();
        float pageH = context.getPageH();
        // 页面内可使用的高度
        float usableH = pageH - padding.getTop() - padding.getBottom() - titleH - remarkH;
        List<Table> splits = new ArrayList<>();
        // 先跨列拆分(N型)
        if (this.splitType == SPLIT_TYPE_N) {
            List<Table> zs = SplitTableUtils.splitByCols(this.table, fixedNumberOfCol, splitTableProperty);
            for (Table table : zs) {
                List<Table> ns = SplitTableUtils.splitByRows(table, fixedNumberOfRow, usableH);
                splits.addAll(ns);
            }
        }
        // 先跨行拆（Z型）
        else {
            List<Table> ns = SplitTableUtils.splitByRows(table, fixedNumberOfRow, usableH);
            for (Table table : ns) {
                List<Table> zs = SplitTableUtils.splitByCols(table, fixedNumberOfCol, splitTableProperty);
                splits.addAll(zs);
            }
        }
        return splits;
    }

    public AbstractElement getTitle() {
        return title;
    }

    public void setTitle(AbstractElement title) {
        this.title = title;
    }

    public void setRemark(AbstractElement remark) {
        this.remark = remark;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getSplitTableProperty() {
        return splitTableProperty;
    }

    public void setSplitTableProperty(String splitTableProperty) {
        this.splitTableProperty = splitTableProperty;
    }

    public int getSplitType() {
        return splitType;
    }

    public void setSplitType(int splitType) {
        this.splitType = splitType;
    }

    public int getFixedNumberOfCol() {
        return fixedNumberOfCol;
    }

    public void setFixedNumberOfCol(int fixedNumberOfCol) {
        this.fixedNumberOfCol = fixedNumberOfCol;
    }

    public int getFixedNumberOfRow() {
        return fixedNumberOfRow;
    }

    public void setFixedNumberOfRow(int fixedNumberOfRow) {
        this.fixedNumberOfRow = fixedNumberOfRow;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
