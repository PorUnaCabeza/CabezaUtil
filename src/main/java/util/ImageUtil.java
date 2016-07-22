package util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Iterator;

/**
 * Created by Cabeza on 2016/7/19.
 */
public class ImageUtil {

    public static BufferedImage compressJpg(int compression, BufferedImage image) throws IOException {
        int originSize = getImageSize(image,"jpg");
        Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter jpegWriter = i.next();
        ImageWriteParam param = jpegWriter.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.1f * compression);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(255);
        ImageOutputStream out = ImageIO.createImageOutputStream(bos);
        jpegWriter.setOutput(out);
        jpegWriter.write(null, new IIOImage(image, null, null), param);
        jpegWriter.dispose();
        BufferedImage resultImage = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
        int afterSize = getImageSize(resultImage,"jpg");
        return originSize > afterSize ? resultImage : image;
    }

    public static BufferedImage splitCircle(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage convertedImage = null;
        Graphics2D g2D = null;
        convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2D = (Graphics2D) convertedImage.getGraphics();
        g2D.drawImage(image, 0, 0, null);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++) {
                if (isOutOfCircle(i, j, width / 2))
                    convertedImage.setRGB(i, j, 0);
            }
        return convertedImage;
    }

    public static boolean isOutOfCircle(int x, int y, int r) {
        if ((x - r) * (x - r) + (y - r) * (y - r) > r * r)
            return true;
        return false;
    }

    public static void saveAsPng(BufferedImage image, String fileName) throws IOException {
        ImageIO.write(image, "png", new File(fileName));
        byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
    }

    public static void saveAsJpg(BufferedImage image, String fileName) throws IOException {
        ImageIO.write(image, "jpg", new File(fileName));
    }

    public static int getImageSize(BufferedImage image,String format) throws IOException {
        if(image==null)
            return 0;
        ByteArrayOutputStream bos=new ByteArrayOutputStream(255);
        ImageIO.write(image,format,bos);
        return bos.toByteArray().length;
    }

    public static boolean isJpg(String fileName){
        return fileName.indexOf(".jpg")>0;
    }

    public static void compressBatch(String originPath, String newPath) throws IOException {
        File originDir = new File(originPath);
        File newDir = new File(newPath);
        if (!originDir.exists() && !originDir.isDirectory()) {
            System.out.println("路径不对");
            return;
        }
        if (!newDir.exists() && !newDir.isDirectory()) {
            System.out.println("路径不存在，现创建");
            newDir.mkdir();
        }
        File[] files = originDir.listFiles();
        for (File file : files) {
            BufferedImage image=ImageIO.read(file);
            if(isJpg(file.getName())&&getImageSize(image,"jpg")>0){
                String fileName=file.getName();
                System.out.println("压缩"+fileName);
                saveAsJpg(compressJpg(6, image), newPath + "\\" + fileName);
            }
        }
    }
}