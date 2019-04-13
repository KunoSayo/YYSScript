package com.github.euonmyoji.yysscript.task;

import com.github.euonmyoji.yysscript.YysScript;
import com.github.euonmyoji.yysscript.configuration.PluginConfig;
import com.github.euonmyoji.yysscript.util.Util;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author yinyangshi
 */
public class TaskManager {

    public static void reload() {
        try {
            Files.walk(PluginConfig.cfgDir.resolve("script")).filter(Files::isReadable)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            String s = Files.lines(path).reduce(Util::concatToLines).orElse("");
                        } catch (IOException e) {
                            YysScript.logger.info("read file failed: " + e.getMessage());
                            YysScript.logger.debug("read file failed:", e);
                        }
                    });
        } catch (IOException e) {
            YysScript.logger.warn("ioe", e);
        }
    }
}
