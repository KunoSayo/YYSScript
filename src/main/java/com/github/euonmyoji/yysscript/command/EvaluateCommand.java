package com.github.euonmyoji.yysscript.command;

import com.github.euonmyoji.yysscript.YysScript;
import com.github.euonmyoji.yysscript.configuration.GlobalScriptConfig;
import com.github.euonmyoji.yysscript.exception.SpongeBugException;
import com.github.euonmyoji.yysscript.util.EvalUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandFlags;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

import static org.spongepowered.api.command.args.GenericArguments.none;

/**
 * @author yinyangshi
 */
final class EvaluateCommand {
    private static HashMap<String, ScriptEngine> ses = new HashMap<>();
    static final CommandSpec EVALUATE = CommandSpec.builder()
            .permission("yysscript.command.evaluate")
            .arguments(GenericArguments.flags()
                            .valueFlag(GenericArguments.string(Text.of("ScriptEngineName")), "-SE", "-ScriptEngine")
                            .setUnknownLongFlagBehavior(CommandFlags.UnknownFlagBehavior.ACCEPT_NONVALUE)
                            .setUnknownShortFlagBehavior(CommandFlags.UnknownFlagBehavior.ACCEPT_NONVALUE)
                            .flag("-clear", "-c")
                            .flag("-no-result")
                            .flag("-use-Global")
                            .flag("-use-Mappers")
                            .flag("-async").buildWith(none()),
                    GenericArguments.remainingJoinedStrings(Text.of("str")))
            .executor(EvaluateCommand::execute)
            .build();

    @Nonnull
    private static CommandResult execute(CommandSource src, CommandContext args) {
        Runnable r = () -> {
            String scriptStr = args.<String>getOne(Text.of("str")).orElseThrow(SpongeBugException::new);

            if (args.hasAny("use-Global")) {
                scriptStr = GlobalScriptConfig.global + scriptStr;
            }

            if (args.hasAny("use-Mappers")) {
                for (Map.Entry<String, String> entry : GlobalScriptConfig.mappers.entrySet()) {
                    scriptStr = scriptStr.replaceAll(entry.getKey(), entry.getValue());
                }
            }

            String seName = args.<String>getOne(Text.of("ScriptEngineName")).orElse("js");
            ScriptEngine engine = getEngine(seName, args.hasAny("clear"));
            boolean noResult = args.hasAny("no-result");

            try {
                String result = EvalUtil.eval(engine, scriptStr);
                if (!noResult) {
                    src.sendMessage(Text.of(result));
                }
            } catch (ScriptException e) {
                src.sendMessage(Text.of(e.toString()));
            }
        };
        if (args.hasAny("async")) {
            Task.builder().async().execute(r).submit(YysScript.plugin);
        } else {
            r.run();
        }
        return CommandResult.success();
    }

    private static ScriptEngine getEngine(String name, boolean clear) {
        ScriptEngine se;
        if (clear) {
            se = YysScript.SCRIPT_ENGINE_MANAGER.getEngineByName(name);
            ses.put(name, se);
        } else {
            se = ses.get(name);
            if (se == null) {
                se = YysScript.SCRIPT_ENGINE_MANAGER.getEngineByName(name);
                ses.put(name, se);
            }
        }
        return se;
    }
}
