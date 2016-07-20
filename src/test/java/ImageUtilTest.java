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
        BufferedImage bi = ImageIO.read(new File("C:\\蓝天草地.jpg"));
        ImageIO.write(ImageUtil.compressJpg(5, bi), "jpg", new File("c:\\1.jpg"));
    }

    @Test
    public void testSplitCircle() throws IOException {
        BufferedImage bi = ImageIO.read(new File("C:\\头像.jpg"));
        ImageIO.write(ImageUtil.splitCircle(bi), "png", new File("c:\\2.png"));
    }
}
