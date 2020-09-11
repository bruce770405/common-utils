package tw.com.bruce.common.utils;

import com.jhlabs.image.NoiseFilter;
import com.jhlabs.image.ScratchFilter;
import com.jhlabs.image.SmearFilter;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class CaptchaUtils {

    private static final Random WEAK_JVM_RANDOM = new Random();
    private static final int CAPTCHA_IMG_WIDTH = 152; // 圖片寬
    private static final int CAPTCHA_IMG_HEIGHT = 60; // 圖片高
    private static final int CAPTCHA_LENGTH = 4; // 字數
    private static final int[][] COLOR_BOX = {{0x25, 0xA2, 0xB6}}; // font color
    private static final int OFFSET_X = 35; // 文字間隔
    private static final int START_POSITION_X = 8; // 首字x軸位置
    private static final int START_POSITION_Y = 50; // 首字Y軸位置
    private static final String[] UNITS = {"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final int NOISE_AMOUNT = 50; // 背景雜訊程度
    private static final float TEXT_NOISE_AMOUNT = 0.1f; // 文字模糊程度
    private static final boolean LINE_NOISE_AMOUNT = true;
    private static SecureRandom secureRandom = new SecureRandom();

    /**
     * 返回一組生成的captcha
     */
    public static Map<String, String> getCaptcha() throws Exception {

        Map<String, String> resultMap = new HashMap<>();
        String captchaNum = getCaptchaNum();

        if (StringUtilsEx.isNotBlank(captchaNum)) {

            String captchaImg = getCaptchaImg(captchaNum);

            // 回傳 base64 Image
            resultMap.put("captchaImg", "data:image/jpeg;base64," + captchaImg);

            // Captcha放入Session Scope, for 後續驗證
            resultMap.put("captchaNum", captchaNum);
        }

        return resultMap;
    }

    /**
     * 隨機產生4碼數字
     */
    private static String getCaptchaNum() {
        String num = "";
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            int idx = WEAK_JVM_RANDOM.nextInt(UNITS.length - 1);
            num += UNITS[idx];
        }
        return num;
    }

    /**
     * 產生base64 image
     *
     * @throws IOException
     */
    private static String getCaptchaImg(String num) throws IOException {

        BufferedImage backgroundImage;
        BufferedImage filteredImage = null;

        backgroundImage = new BufferedImage(CAPTCHA_IMG_WIDTH, CAPTCHA_IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2DBackground = backgroundImage.createGraphics();

        graphics2DBackground.setBackground(Color.white);
        graphics2DBackground.clearRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());

        // 整張圖片傾斜
        Font font = new Font("Courier", Font.PLAIN, 50);
        graphics2DBackground.setFont(font);

        int startPosition = RandomUtils.nextInt(0, START_POSITION_X);
        while (startPosition < 3) {
            startPosition = RandomUtils.nextInt(0, START_POSITION_X);
        }

        int offsetXIn = 0;
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            int pickedColorLocation = WEAK_JVM_RANDOM.nextInt(COLOR_BOX.length);
            graphics2DBackground.setColor(new Color(COLOR_BOX[pickedColorLocation][0] + pickedColorLocation, COLOR_BOX[pickedColorLocation][1] + pickedColorLocation, COLOR_BOX[pickedColorLocation][2] + pickedColorLocation));

            // 單一文字傾斜
            String n = num.substring(i, i + 1);
            graphics2DBackground.drawString(n, startPosition + offsetXIn, START_POSITION_Y);

            // 文字間隔處理
            if ("W".equalsIgnoreCase(n) || "M".equalsIgnoreCase(n) || "Q".equalsIgnoreCase(n) || "G".equalsIgnoreCase(n)) {
                offsetXIn += (OFFSET_X + 4);
            } else if ("F".equalsIgnoreCase(n) || "J".equalsIgnoreCase(n)) {
                offsetXIn += (OFFSET_X - 3);
            } else if (NumberUtilsEx.isWholeNumber(n)) {
                offsetXIn += (OFFSET_X - 2);
            } else {
                offsetXIn += OFFSET_X;
            }
        }

        // 背景雜訊
        NoiseFilter filter = new NoiseFilter();
        filter.setAmount(NOISE_AMOUNT);
        filteredImage = filter.filter(backgroundImage, filteredImage);

        // 文字模糊
        SmearFilter filter2 = new SmearFilter();
        filter2.setDensity(TEXT_NOISE_AMOUNT);
        filter2.filter(filteredImage, filteredImage);

        // 背景線條
        if (LINE_NOISE_AMOUNT) {
            ScratchFilter filter3 = new ScratchFilter();
            filter3.setSeed(secureRandom.nextInt(7));
            filter3.setLength(0.3f);
            filter3.setColor(0); // black
            filter3.filter(filteredImage, filteredImage);
        }

        graphics2DBackground.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(filteredImage, "jpeg", outputStream);
        outputStream.close();

        return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
    }


}
