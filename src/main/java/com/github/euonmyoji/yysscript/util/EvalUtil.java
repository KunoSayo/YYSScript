package com.github.euonmyoji.yysscript.util;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Objects;

/**
 * @author yinyangshi
 */
public class EvalUtil {
    public static String eval(ScriptEngine se, String s) throws ScriptException {
        return Objects.toString(se.eval(s));
    }
}
