package com.export.pdf.entity;


import com.export.pdf.context.Context;
import com.export.pdf.context.Pointer;
import com.export.pdf.entity.abs.AbstractChildValueElement;
import com.export.pdf.style.Padding;
import com.export.pdf.utils.StringUtils;

public class Text extends AbstractChildValueElement {

    /**
     * 垂直对其方式
     */
    public static final int ALIGN_TOP = 0;
    public static final int ALIGN_MIDDLE = 1;
    public static final int ALIGN_BOTTOM = 2;
    /**
     * 水平对齐方式
     */
    public static final int VERTICAL_ALIGN_LEFT = 0;
    public static final int VERTICAL_ALIGN_CENTER = 1;
    public static final int VERTICAL_ALIGN_RIGHT = 2;

    private float fontSize = 6;

    private String family;

    private int align = ALIGN_MIDDLE;

    private int verticalAlign = VERTICAL_ALIGN_LEFT;

    private float familyFontHeight = 0;

    /**
     * 内边距
     */
    private Padding padding = new Padding(1);

    @Override
    public void draw(Context context, float x, float y) {
        Pointer pointer = context.getPointer();
        if (this.familyFontHeight == 0) {
            this.familyFontHeight = pointer.fontHeight(this.family);
        }
        String value = this.parent.getValue();
        if (StringUtils.isBlank(value)) {
            return;
        }
        // 替换掉换行/回车
        value = value.replaceAll("[\r\n]", "");
        float contentH = this.parent.getH();
        float contentW = this.parent.getW();
        float usableContentW = this.parent.getW() - this.padding.getLeft() - this.padding.getRight();

        float familyFontWidth = pointer.stringWidth(value, this.family);
        // 值的宽度
        float valueWidth = familyFontWidth * this.fontSize;
        // 内容的宽度大于可用宽度，需要换行或者缩小字体
        if (valueWidth > usableContentW) {
            // 如果字号的高度小于可用高度的三分之一，则可以换行
            if (this.fontSize < contentH / 3) {
                // 先计算内容宽度是可用宽度的倍数，几倍就要拆成几行
                int bei = (int) Math.ceil(valueWidth / usableContentW);
                int len = value.length();
                int[] split = new int[bei];
                int total = 0;
                for (int i = 0; i < bei; i++) {
                    // 最后的长度是剩余的长度
                    if (i + 1 == bei) {
                        split[i] = len - total;
                    } else {
                        split[i] = len / bei;
                        total += split[i];
                    }
                }
                int index = value.length();
                // 行高
                float fontHeight = this.familyFontHeight * this.fontSize;
                // 行间距
                float lineSpacing = fontHeight * 0.4f;
                // 检查拆分的每个字符串是否附和条件
                for (int i = bei - 1; i >= 0; i--) {
                    index -= split[i];
                    int checkLen = checkLen(pointer, value, index, split[i], usableContentW);
                    // 判断是否需要将字符“借”给上级
                    if (checkLen > 0 && i > 0) {
                        index += checkLen;
                        split[i] -= checkLen;
                        split[i - 1] += checkLen;
                    }
                    float offsetY = 0;
                    switch (this.align) {
                        case ALIGN_TOP:
                            offsetY = fontHeight * (i + 1) + lineSpacing * i + this.padding.getTop();
                            break;
                        case ALIGN_MIDDLE:
                            offsetY = (contentH + fontHeight * bei + lineSpacing * (bei - 1)) / 2 - (bei - i - 1) * (fontHeight + lineSpacing);
                            break;
                        case ALIGN_BOTTOM:
                            offsetY = contentH - fontHeight * i - lineSpacing * i - this.padding.getBottom();
                            break;
                    }
                    String current = value.substring(index, index + split[i]);
                    float currentFamilyFontWidth = pointer.stringWidth(current, this.family);
                    writeX(pointer, contentW, x, y - offsetY, currentFamilyFontWidth, this.fontSize, current);
                }
            }
            // 缩放
            else {
                // 求出比率值，估算要缩放的比例
                this.write(pointer, contentH, contentW, x, y, this.familyFontHeight, familyFontWidth, usableContentW / valueWidth * this.fontSize, value);
            }
        } else {
            this.write(pointer, contentH, contentW, x, y, this.familyFontHeight, familyFontWidth, this.fontSize, value);
        }
    }

    private int checkLen(Pointer pointer, String value, int index, int len, float w) {
        String current = value.substring(index, index + len);
        // 检查长度是否满足，如果不满足，则减一个字符给上一行
        float splitFamilyFontWidth = pointer.stringWidth(current, this.family);
        if (this.fontSize * splitFamilyFontWidth > w) {
            return 1 + checkLen(pointer, value, index + 1, len - 1, w);
        }
        return 0;
    }

    private void write(Pointer pointer,
                       float h, float w,
                       float x, float y,
                       float familyFontHeight, float familyFontWidth,
                       float fontSize, String value) {
        float offsetY = 0;
        switch (this.align) {
            case ALIGN_TOP:
                offsetY = familyFontHeight * fontSize + this.padding.getTop();
                break;
            case ALIGN_MIDDLE:
                offsetY = (h + familyFontHeight * fontSize) / 2;
                break;
            case ALIGN_BOTTOM:
                offsetY = h - this.padding.getBottom();
                break;
        }
        writeX(pointer, w, x, y - offsetY + 0.5f, familyFontWidth, fontSize, value);
    }

    private void writeX(Pointer pointer,
                        float w,
                        float x, float y,
                        float familyFontWidth,
                        float fontSize, String value) {
        float offsetX = 0;
        switch (this.verticalAlign) {
            case VERTICAL_ALIGN_LEFT:
                offsetX = 0 + this.padding.getLeft();
                break;
            case VERTICAL_ALIGN_CENTER:
                offsetX = (w - familyFontWidth * fontSize) / 2;
                break;
            case VERTICAL_ALIGN_RIGHT:
                offsetX = w - familyFontWidth * fontSize - this.padding.getRight();
                break;
        }
        pointer.text(x + offsetX, y, value, this.family, fontSize);
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        this.familyFontHeight = 0;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
        this.familyFontHeight = 0;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(int verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

}
