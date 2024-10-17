package com.export.pdf.entity.page;

import com.export.pdf.context.Context;
import com.export.pdf.entity.abs.AbstractPage;
import com.export.pdf.entity.abs.IPageFooterFormat;

public class PageFooterFormat implements IPageFooterFormat {

    @Override
    public String format(Context context, AbstractPage parent) {
        return String.format("- %d -", context.getPageIndex());
    }

}
