package com.github.euonmyoji.yysscript.listener;

import com.github.euonmyoji.yysscript.YysScript;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinyangshi
 */
public class ChatListener {
    private static final ChatListener INSTANCE = new ChatListener();
    private static Map<String, String> chatMappers;
    private static boolean registered = false;
    private static boolean async = false;

    private ChatListener() {
    }

    public static void setup(CommentedConfigurationNode listenChatCfg) {
        chatMappers = null;
        if (listenChatCfg.getNode("enable").getBoolean(false)) {
            register();
            chatMappers = new HashMap<>(4);
            listenChatCfg.getNode("mappers").getChildrenMap().forEach((o, o2) -> chatMappers.put(o.toString(), o2.getString("")));
            async = listenChatCfg.getNode("async").getBoolean(false);
        } else {
            unregister();
        }
    }

    private static synchronized void register() {
        if (!registered) {
            registered = true;
            Sponge.getEventManager().registerListeners(YysScript.plugin, INSTANCE);
        }
    }

    private static synchronized void unregister() {
        if (registered) {
            registered = false;
            Sponge.getEventManager().unregisterListeners(INSTANCE);
        }
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event) {
        String s = event.getRawMessage().toPlain();
    }
}
