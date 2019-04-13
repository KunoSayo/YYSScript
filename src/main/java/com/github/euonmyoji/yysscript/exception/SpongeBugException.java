package com.github.euonmyoji.yysscript.exception;

/**
 * If this exception threw, it means sponge has bug or YYSScript has bug
 * Most of the time the exception threw, it means sponge has.
 *
 * @author yinyangshi
 */
public final class SpongeBugException extends RuntimeException {
    public SpongeBugException() {
        super("The sponge (or yysscript?) has bug!");
    }
}
