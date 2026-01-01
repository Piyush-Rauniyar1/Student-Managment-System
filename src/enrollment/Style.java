
package enrollment;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for defining UI styles.
 * Contains constants for colors, fonts, and dimensions to ensure design
 * consistency.
 */
public class Style {
    // Colors from Tailwind config
    public static final Color PRIMARY = new Color(54, 126, 226); // #367ee2
    public static final Color BACKGROUND_LIGHT = new Color(246, 247, 248); // #f6f7f8
    public static final Color SURFACE_LIGHT = new Color(255, 255, 255); // #ffffff
    public static final Color BORDER_LIGHT = new Color(232, 236, 243); // #e8ecf3
    public static final Color TEXT_MAIN = new Color(14, 19, 27); // #0e131b
    public static final Color TEXT_SECONDARY = new Color(80, 109, 149); // #506d95

    // Fonts
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BODY_BOLD = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_ICON = new Font("SansSerif", Font.PLAIN, 16); // Fallback for icons

    // Dimensions
    public static final int SIDEBAR_WIDTH = 250;
    public static final int HEADER_HEIGHT = 60;
}
