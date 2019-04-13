package com.github.euonmyoji.yysscript.util;

import com.github.euonmyoji.yysscript.configuration.PluginConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author yinyangshi
 */
public class Util {
    public static String[] parseScript(String raw) {
        String[] ss = raw.split(PluginConfig.SETTINGS_SPLIT, 2);
        ss[0] += "\n";
        return ss;
    }

    public static String concatToLines(String s1, String s2) {
        return s1 + "\n" + s2;
    }

    public static CommentedConfigurationNode loadHoconFromString(@Nullable HoconConfigurationLoader loader, String s) throws IOException {
        try (StringReader stringReader = new StringReader(s); BufferedReader bufferedReader = new BufferedReader(stringReader)) {
            if (loader == null) {
                loader = HoconConfigurationLoader.builder().setSource(() -> bufferedReader).build();
            }
            return loader.load();
        }
    }
}
