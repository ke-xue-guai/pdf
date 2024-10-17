package com.export.pdf.entity;

import com.export.pdf.context.Context;
import com.export.pdf.context.Font;
import com.export.pdf.context.Pointer;
import com.export.pdf.entity.abs.AbstractPage;
import com.export.pdf.style.Padding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDImmutableRectangle;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Document {

    private Padding padding = new Padding(20, 30);

    /**
     * user space units per inch
     */
    private static final float POINTS_PER_INCH = 72;

    /**
     * user space units per millimeter
     */
    private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    /**
     * 纵向
     */
    public static final int ORIENTATION_PORTRAIT = 0;

    /**
     * 横向
     */
    public static final int ORIENTATION_LANDSCAPE = 1;

    /**
     * 纸张大小A3
     */
    public static final int SIZE_A3 = 0;

    /**
     * 纸张大小A4
     */
    public static final int SIZE_A4 = 1;

    /**
     * 纸张大小B4
     */
    public static final int SIZE_B4 = 2;

    private final PDDocument document = new PDDocument();
    private final Font font;
    private final List<AbstractPage> pages = new ArrayList<>();

    private final float w;
    private final float h;

    public Document() {
        this(ORIENTATION_PORTRAIT, SIZE_A4);
    }

    public Document(int orientation, int size) {
        if (orientation == ORIENTATION_PORTRAIT) {
            if (size == SIZE_A4) {
                this.w = 210 * POINTS_PER_MM;
                this.h = 297 * POINTS_PER_MM;
            } else if (size == SIZE_A3) {
                this.w = 297 * POINTS_PER_MM;
                this.h = 420 * POINTS_PER_MM;
            } else {
                this.w = 257 * POINTS_PER_MM;
                this.h = 364 * POINTS_PER_MM;
            }
        } else {
            if (size == SIZE_A4) {
                this.h = 210 * POINTS_PER_MM;
                this.w = 297 * POINTS_PER_MM;
            } else if (size == SIZE_A3) {
                this.h = 297 * POINTS_PER_MM;
                this.w = 420 * POINTS_PER_MM;
            } else {
                this.h = 257 * POINTS_PER_MM;
                this.w = 364 * POINTS_PER_MM;
            }
        }

        this.font = new Font(this.document);
    }

    public void addPage(AbstractPage page) {
        this.pages.add(page);
    }

    public Pointer createPointer() {
        PDRectangle rectangle = new PDImmutableRectangle(this.w, this.h);
        PDPage page = new PDPage(rectangle);
        this.document.addPage(page);
        try {
            return new Pointer(new PDPageContentStream(this.document, page), this.font);
        } catch (IOException e) {
            throw new RuntimeException("PDF导出，创建页面失败");
        }
    }

    public void draw() {
        Context context = new Context(this.padding, this.w, this.h);
        for (AbstractPage page : this.pages) {
            page.setPointerFactory(this::createPointer);
            page.draw(context, 0 + this.padding.getLeft(), this.h - this.padding.getTop());
        }
    }

    public void save(OutputStream output) {
        try {
            this.document.save(output);
        } catch (IOException e) {
            throw new RuntimeException("PDF导出，文件保存失败");
        }
    }

    /**
     * 设置字体
     *
     * @param family 字体名称
     * @param is     字体文件流
     */
    public void setFont(String family, InputStream is) {
        this.font.set(family, is);
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

}
