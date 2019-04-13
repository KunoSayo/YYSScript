package com.github.euonmyoji.yysscript;

import com.github.euonmyoji.yysscript.command.YysScriptCommand;
import com.github.euonmyoji.yysscript.configuration.GlobalScriptConfig;
import com.github.euonmyoji.yysscript.configuration.PluginConfig;
import com.github.euonmyoji.yysscript.task.TaskManager;
import com.google.inject.Inject;
import org.bstats.sponge.Metrics2;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author yinyangshi
 */
@Plugin(id = "yysscript", name = "YYS Script", version = YysScript.VERSION, description = "Run script", authors = "yinyangshi")
public class YysScript {
    public static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    public static final String VERSION = "@spongeVersion@";
    public static Logger logger;
    public static YysScript plugin;
    private final Metrics2 metrics;

    @Inject
    public YysScript(@ConfigDir(sharedRoot = false) Path cfgDir, Logger logger, Metrics2 metrics) {
        plugin = this;
        PluginConfig.defaultCfgDir = cfgDir;
        try {
            Files.createDirectories(cfgDir);
        } catch (IOException e) {
            logger.warn("create dir failed", e);
        }

        YysScript.logger = logger;
        this.metrics = metrics;
        PluginConfig.init();
        GlobalScriptConfig.init();
    }

    @Listener
    public void onStarted(GameStartedServerEvent event) {
        Sponge.getCommandManager().register(this, YysScriptCommand.SCRIPT, "yysscript", "yyss", "script");
        try {
            if (!Sponge.getMetricsConfigManager().areMetricsEnabled(this)) {
                Sponge.getServer().getConsole()
                        .sendMessage(Text.of("[YYS Script]If you like to support this plugin, enable metrics will help a lot! thanks!"));
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            //do not spam the server (ignore)
            metrics.cancel();
            Task.builder().delayTicks(60 * 20).execute(metrics::cancel).submit(this);
            logger.debug("NoMetricsManagerClassDefFound, try canceling the metrics");
        }
        TaskManager.reload();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        reload();
    }

    public void reload() {
        PluginConfig.reload();
        GlobalScriptConfig.reload();
        TaskManager.reload();
    }

}
