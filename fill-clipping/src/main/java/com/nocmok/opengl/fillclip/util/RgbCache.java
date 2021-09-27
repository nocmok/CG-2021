package com.nocmok.opengl.fillclip.util;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class RgbCache {
    private static Map<Integer, Color> rgbCache;

    static {
        rgbCache = new HashMap<>();
    }

    private RgbCache() {
    }

    public static int colorToRGB(Color color) {
        int r = ((int) (color.getRed() * 255d)) << 16;
        int g = ((int) (color.getGreen() * 255d)) << 8;
        int b = ((int) (color.getBlue() * 255d));
        return r | g | b;
    }

    public static Color rgbToColor(int rgb) {
        return rgbCache.computeIfAbsent(rgb, rgbKey -> {
            double r = ((rgb & (0x00ff0000)) >>> 16) / 255d;
            double g = ((rgb & (0x0000ff00)) >>> 8) / 255d;
            double b = ((rgb & (0x000000ff))) / 255d;
            return new Color(r, g, b, 1.0);
        });
    }
}
