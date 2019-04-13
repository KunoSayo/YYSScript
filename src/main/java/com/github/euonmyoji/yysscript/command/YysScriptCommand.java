package com.github.euonmyoji.yysscript.command;

import com.github.euonmyoji.yysscript.YysScript;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;

import static org.spongepowered.api.text.Text.of;

/**
 * @author yinyangshi
 */
public final class YysScriptCommand {
    private static final CommandSpec RELOAD = CommandSpec.builder()
            .permission("yysscript.admin.command.reload")
            .executor((src, args) -> {
                long start = System.currentTimeMillis();
                YysScript.plugin.reload();
                long end = System.currentTimeMillis();
                src.sendMessage(of("[YYSScript]reload successfully in " + (end - start) + " ms"));
                return CommandResult.success();
            })
            .build();

    public static final CommandSpec SCRIPT = CommandSpec.builder()
            .permission("yysscript.command.yysscript")
            .executor((src, args) -> {
                src.sendMessage(of("\nYYSScript Version: " + YysScript.VERSION));
                src.sendMessage(of("YYSScript Author: yinyangshi"));
                src.sendMessage(of("YYSScript Url: https://github.com/euOnmyoji/YYSScript"));
                return CommandResult.success();
            })
            .child(EvaluateCommand.EVALUATE, "evaluate", "eval")
            .child(RELOAD, "reload")
            .build();
}
