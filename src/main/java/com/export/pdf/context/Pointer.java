package com.export.pdf.context;


import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;

/**
 * 上下文
 */
public class Pointer {

    private final PDPageContentStream stream;

    private final com.export.pdf.context.Font font;


    public Pointer(PDPageContentStream stream, Font font) {
        this.stream = stream;
        this.font = font;
    }

    public void line(float[] p1, float[] p2, float[] p3, float[] p4, Color color) {
        try {
            this.stream.setNonStrokingColor(color);
            this.stream.moveTo(p1[0], p1[1]);
            this.stream.lineTo(p2[0], p2[1]);
            this.stream.lineTo(p3[0], p3[1]);
            this.stream.lineTo(p4[0], p4[1]);
            this.stream.fill();
        } catch (Exception e) {
            throw new RuntimeException("PDF Painter 绘制线条失败");
        }
    }

    public float stringWidth(String value, String family) {
        PDFont font = this.font.load(family);
        try {
            return font.getStringWidth(value) / 1000;
        } catch (Exception e) {
            throw new RuntimeException("PDF Painter 获取字符宽度失效 str：" + value);
        }
    }

    public float fontHeight(String family) {
        PDFont font = this.font.load(family);
        return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000f * 0.865f;
    }

    public void text(float x, float y, String value, String family, float fontSize) {
        try {
            this.stream.setFont(this.font.load(family), fontSize);
            this.stream.beginText();
            this.stream.newLineAtOffset(x, y);
            this.stream.showText(value);
            this.stream.endText();
        } catch (Exception e) {
            throw new RuntimeException("PDF Painter 文本渲染失败");
        }
    }

    public void close() {
        try {
            this.stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
