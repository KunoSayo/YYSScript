package com.github.euonmyoji.yysscript.configuration;

import com.github.euonmyoji.yysscript.YysScript;
import com.github.euonmyoji.yysscript.listener.ChatListener;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yinyangshi
 */
public class PluginConfig {
    public static final String SETTINGS_SPLIT = "#Script Hocon Settings";
    private static final String DATA_DIR = "data-dir-path";

    public static Path defaultCfgDir;
    public static Path cfgDir;
    static Path globalScriptPath;
    private static HoconConfigurationLoader loader;
    private static CommentedConfigurationNode cfg;
    private static CommentedConfigurationNode generalNode;

    public static void init() {
        Path cfgFilePath = defaultCfgDir.resolve("config.conf");
        loader = HoconConfigurationLoader.builder().setPath(cfgFilePath).build();
        reload();
        if (Files.notExists(cfgFilePath)) {
            save();
        }
    }

    public static void reload() {
        loadNode();
        String path = generalNode.getNode(DATA_DIR).getString("default");
        cfgDir = "default".equals(path) ? defaultCfgDir : Paths.get(path);
        path = generalNode.getNode("global-script-path").getString("default");
        globalScriptPath = "default".equals(path) ? cfgDir.resolve("globalScript.js") : Paths.get(path);
        YysScript.logger.info("using data dir path:" + cfgDir);
        ChatListener.setup(cfg.getNode("listen-chat"));
    }

    private static void loadNode() {
        try {
            cfg = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
        } catch (IOException e) {
            YysScript.logger.error("load config failed", e);
        }
        generalNode = cfg.getNode("general");

    }

    private static void save() {
        try {
            loader.save(cfg);
        } catch (IOException e) {
            YysScript.logger.error("save config failed", e);
        }
    }
}
