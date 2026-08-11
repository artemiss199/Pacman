import java.awt.Font;
import java.io.File;

public class FontManager {
    private static Font customFont;
    public static Font getFont(float size) {
        if (customFont == null) {
            try {
                File fontFile = new File("Assets/fonts/Emulogic-zrEw.ttf");
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            } catch (Exception e) {
                System.out.println("🚨 Failed to load retro font! Using fallback.");
                customFont = new Font("Arial", Font.BOLD, 12);
            }
        }
        return customFont.deriveFont(size);
    }
}