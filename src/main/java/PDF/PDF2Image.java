package PDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.*;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringJoiner;

public class PDF2Image {
    String filename;
    String fname;

    public PDF2Image(String filename) {
        this.filename = filename;
        this.fname = filename.substring(0, filename.lastIndexOf('.'));
        convert();
    }

    private String getDir() {
        String lim = System.getProperty("file.separator");
        StringJoiner s = new StringJoiner(lim, "", lim);
        s.add(System.getProperty("user.home"));
        s.add(".NAME_GESTIONALE");
        if (new File(s.toString()).mkdirs()) {
            System.out.printf("Directory %s created...\n", s);
        }
        s.add(fname);
        if (new File(s.toString()).mkdirs()) {
            System.out.printf("Directory %s created...\n", s);
        }
        return s.toString();
    }

    private void convert() {
        try {
            PDDocument document = PDDocument.load(new File(this.filename));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            String dir_name = getDir();
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                String name = String.format("%s%d.jpg", dir_name, page + 1);
                System.out.println(name);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, name, 300);
            }
            document.close();
        } catch (Exception ignored) {
            System.out.println("Conversion error PFD to Image");
        }
    }

    public int getSize() {
        try (PDDocument document = PDDocument.load(new File(this.filename))) {
            return document.getNumberOfPages();
        } catch (IOException ignored) {
            return 0;
        }
    }

    public static void main(String[] args) {
        PDF2Image p = new PDF2Image("test.pdf");
        System.out.println(p.getSize());
    }
}
