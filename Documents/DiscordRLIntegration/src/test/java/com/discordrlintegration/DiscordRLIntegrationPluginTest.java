package com.discordrlintegration;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DiscordRLIntegrationPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(DiscordRLIntegrationPlugin.class);
        RuneLite.main(args);
    }
} 