package com.export.pdf.entity.abs;


import com.export.pdf.entity.page.PageFooter;
import com.export.pdf.entity.page.PointerFactory;

public abstract class AbstractPage extends AbstractElement {

    protected PageFooter footer;

    protected PointerFactory pointerFactory;

    public void setPointerFactory(PointerFactory pointerFactory) {
        this.pointerFactory = pointerFactory;
    }

    public PageFooter getFooter() {
        return footer;
    }

    public void setFooter(PageFooter footer) {
        this.footer = footer;
    }

}
