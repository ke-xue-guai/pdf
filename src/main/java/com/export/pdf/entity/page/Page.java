package com.export.pdf.entity.page;

import com.export.pdf.context.Context;
import com.export.pdf.context.Pointer;
import com.export.pdf.entity.abs.AbstractElement;
import com.export.pdf.entity.abs.AbstractPage;

import java.util.ArrayList;
import java.util.List;

public class Page extends AbstractPage {

    private final List<AbstractElement> elements = new ArrayList<>();

    private boolean skipPage = false;

    public void isSkipPage() {
        this.skipPage = true;
    }

    public void addElement(AbstractElement element) {
        this.elements.add(element);
        if (element instanceof Page) {
            throw new RuntimeException("单页内不能包含Page");
        }
    }

    @Override
    public void draw(Context context, float x, float y) {
        Pointer pointer = this.pointerFactory.createPointer();
        context.setPointer(pointer);
        float s = y;
        for (AbstractElement element : this.elements) {
            element.draw(context, x, s);
            s -= element.getH();
        }
        if (this.footer != null) {
            this.footer.draw(context, this, 0, 0);
        }
        pointer.close();
        if (!skipPage) {
            // 下一页
            context.nextPage();
        }
    }

}
