package com.github.euonmyoji.yysscript.configuration;

import com.github.euonmyoji.yysscript.YysScript;
import com.github.euonmyoji.yysscript.util.Util;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yinyangshi
 */
public class GlobalScriptConfig {
    public static Map<String, String> mappers = new ConcurrentHashMap<>(0);
    public static String global = "";

    public static void init() {
        reload();
    }

    public static void reload() {
        mappers = new ConcurrentHashMap<>(4);
        if (Files.notExists(PluginConfig.globalScriptPath)) {
            try (BufferedWriter out = Files.newBufferedWriter(PluginConfig.globalScriptPath)) {
                out.write(PluginConfig.SETTINGS_SPLIT);
                HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(() -> out).build();
                CommentedConfigurationNode cfg = loader.createEmptyNode();
                CommentedConfigurationNode mappers = cfg.getNode("settings", "mappers");
                mappers.getNode("Sponge").setValue(Sponge.class.getName());
                loader.save(cfg);
                //flush obviously
                out.flush();
            } catch (IOException e) {
                YysScript.logger.warn("create global script file failed", e);
            }
        }

        try {
            String[] s = Util.parseScript(Files.lines(PluginConfig.globalScriptPath).reduce(Util::concatToLines).orElse(""));
            global = s[0];
            if (s.length > 1) {
                CommentedConfigurationNode cfg = Util.loadHoconFromString(null, s[1]);
                cfg.getNode("settings", "mappers").getChildrenMap().forEach((o, o2) -> mappers.put(o.toString(), o2.getString("")));
            }
        } catch (IOException e) {
            YysScript.logger.warn("load global script file failed", e);
        }
    }
}
