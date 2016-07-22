import org.junit.Test;
import util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cabeza on 2016/7/20.
 */
public class ImageUtilTest {

    @Test
    public void testCompress() throws IOException {
        BufferedImage bi = ImageIO.read(new File("E:\\merchantShow\\154572115184582656.jpg"));
        ImageIO.write(ImageUtil.compressJpg(5, bi), "jpg", new File("c:\\1.jpg"));
    }

    @Test
    public void testSplitCircle() throws IOException {
        BufferedImage bi = ImageIO.read(new File("C:\\头像.jpg"));
        ImageIO.write(ImageUtil.splitCircle(bi), "png", new File("c:\\2.png"));
    }

    @Test
    public void testIsJpg(){
        System.out.println(ImageUtil.isJpg("166435962073776128.jpg"));
    }

    @Test
    public void testCompressBatch() throws IOException {
        ImageUtil.compressBatch("E:\\merchantShow","E:\\compress_result");
    }

    @Test
    public void testGetImageSize() throws IOException {
       int size= ImageUtil.getImageSize(ImageIO.read(new File("E:\\merchantShow\\154572141344456704.jpg")),"jpg");
        System.out.println(size);
    }
}
