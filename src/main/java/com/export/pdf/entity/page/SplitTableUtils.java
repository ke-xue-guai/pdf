package com.export.pdf.entity.page;

import com.export.pdf.entity.Table;
import com.export.pdf.entity.TableCol;
import com.export.pdf.entity.TableRow;
import com.export.pdf.style.TableGrid;
import com.export.pdf.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitTableUtils {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");

    /**
     * 可以构建的格式
     * {1,6~10}{11,15~20}
     */
    private static List<SplitTable> buildSplitTables(String property, int fixedNumberOfCol) {
        try {
            List<SplitTable> tables = new ArrayList<>();
            Matcher matcher = PATTERN.matcher(property);
            while (matcher.find()) {
                String match = matcher.group().replaceAll("[{}]?", "");
                String[] numbers = match.split(",");
                SplitTable table = new SplitTable(fixedNumberOfCol);
                for (String number : numbers) {
                    // 包含波浪符号的为取值范围
                    if (number.contains("~")) {
                        String[] radius = number.split("~");
                        int s = Integer.parseInt(radius[0]);
                        int e = Integer.parseInt(radius[1]);
                        for (int i = s; i <= e; i++) {
                            table.addIndex(i);
                        }
                    } else {
                        table.addIndex(Integer.parseInt(number));
                    }
                }
                tables.add(table);
            }
            return tables;
        } catch (Exception e) {
            throw new RuntimeException("分表失败，请检查参数" + property);
        }
    }

    public static List<Table> splitByCols(Table source, int fixedNumberOfCol, String property) {
        List<Table> rst = new ArrayList<>();
        if (StringUtils.isBlank(property)) {
            rst.add(source);
            return rst;
        }
        List<SplitTable> tables = buildSplitTables(property, fixedNumberOfCol);
        List<TableRow> rows = source.getRows();
        for (TableRow row : rows) {
            List<TableCol> cols = row.getCols();
            for (SplitTable table : tables) {
                table.addRow(row.getH());
            }
            int y = 0;
            for (TableCol col : cols) {
                int colspan = col.getColspan();
                // 判断是否是固定列
                if (y <= fixedNumberOfCol) {
                    for (SplitTable table : tables) {
                        table.addCol(col);
                    }
                    y += colspan;
                    continue;
                }
                // 判断是否属于某张表，为了效率，一个单元格只属于一个表
                for (SplitTable table : tables) {
                    if (table.contains(y)) {
                        TableCol clone = new TableCol();
                        clone.setText(col.getText());
                        clone.setValue(col.getValue());
                        clone.setBorders(col.getBorders());
                        clone.setRowspan(col.getRowspan());
                        clone.setColspan(colspan);
                        if (colspan == 1) {
                            table.addCol(clone);
                            y += colspan;
                            colspan = 0;
                            break;
                        } else {
                            int splitIndex = 0;
                            for (int i = 0; i < colspan; i++) {
                                if (!table.contains(y + i)) {
                                    break;
                                }
                                splitIndex = i + 1;
                            }
                            // 是否完全属于当前表
                            if (splitIndex == colspan) {
                                table.addCol(clone);
                                y += colspan;
                                colspan = 0;
                                break;
                            } else {
                                y += splitIndex;
                                clone.setColspan(splitIndex);
                                table.addCol(clone);
                                colspan -= splitIndex;
                            }
                        }
                    }
                }
                y += colspan;
            }
        }
        for (SplitTable split : tables) {
            Table table = split.getTable();
            rst.add(table);
            // 填充
            for (TableGrid grid : source.getTableGrids()) {
                if (split.contains(grid.getIndex())) {
                    split.addTableGrid(grid);
                }
            }
        }
        return rst;
    }

    public static List<Table> splitByRows(Table source, int fixedNumberOfRow, float usableH) {
        List<Table> rst = new ArrayList<>();
        List<TableRow> rows = source.getRows();
        List<TableRow> fixedRows = new ArrayList<>();
        Table table = null;
        for (int i = 0; i < rows.size(); i++) {
            TableRow row = rows.get(i);
            if (i <= fixedNumberOfRow) {
                fixedRows.add(row);
                continue;
            }
            if (table == null || table.getH() + row.getH() > usableH) {
                table = new Table();
                table.setTableGrids(source.getTableGrids());
                table.addRows(fixedRows);
                rst.add(table);
            }
            table.addRow(row);
        }
        return rst;
    }

}
