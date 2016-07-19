package util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * Created by Cabeza on 2016/7/19.
 */
public class ImageUtil {
    private static void compress(int compression, File file) throws IOException {
        BufferedImage bi = ImageIO.read(file);
        Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter jpegWriter = i.next();
        ImageWriteParam param = jpegWriter.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.1f * compression);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(255);
        ImageOutputStream out = ImageIO.createImageOutputStream(bos);
        jpegWriter.setOutput(out);
        jpegWriter.write(null, new IIOImage(bi, null, null), param);
        jpegWriter.dispose();
        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
        ImageIO.write(imag, "jpg", new File(file.getParent() + "\\" + file.getName().replaceAll("\\.jpg", "") + compression + ".jpg"));
        out.close();
    }
}
