package com.export.pdf.entity.abs;


import com.export.pdf.context.Context;

public interface IPageFooterFormat {

    String format(Context context, AbstractPage parent);

}
