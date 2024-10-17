package com.export.pdf.context;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Font {

    private final PDDocument document;

    private final Map<String, PDFont> cache = new HashMap<>();

    public Font(PDDocument document) {
        this.document = document;
    }

    public void set(String family, InputStream is) {
        try {
            PDFont font = PDType0Font.load(document, is);
            this.cache.put(family, font);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PDFont load(String family) {
        PDFont rst = this.cache.get(family);
        if (rst == null) {
            throw new RuntimeException("字体不存在");
        }
        return this.cache.get(family);
    }


}
