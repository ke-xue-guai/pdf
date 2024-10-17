package com.export.pdf.entity.page;

import com.export.pdf.context.Context;
import com.export.pdf.entity.Text;
import com.export.pdf.entity.abs.AbstractChildElement;
import com.export.pdf.entity.abs.AbstractPage;
import com.export.pdf.entity.abs.IPageFooterFormat;
import com.export.pdf.entity.abs.IValueElement;
import com.export.pdf.utils.StringUtils;

public class PageFooter extends AbstractChildElement implements IValueElement {

    /**
     * 格式化
     */
    private IPageFooterFormat format = new PageFooterFormat();
    /**
     * Y坐标偏移量
     */
    private float offsetY = 0f;
    /**
     * 页脚的样式
     */
    private Text text;
    /**
     * 页码
     */
    private String value;

    @Override
    public void draw(Context context, float x, float y) {
        this.value = this.format.format(context, (AbstractPage) this.parent);
        if (StringUtils.isNotBlank(this.value) && this.text != null) {
            this.setW(context.getPageW());
            this.setH(context.getPagePadding().getBottom());
            this.text.draw(context, this, x, y + this.offsetY + this.h);
        }
    }

    public IPageFooterFormat getFormat() {
        return format;
    }

    public void setFormat(IPageFooterFormat format) {
        this.format = format;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
