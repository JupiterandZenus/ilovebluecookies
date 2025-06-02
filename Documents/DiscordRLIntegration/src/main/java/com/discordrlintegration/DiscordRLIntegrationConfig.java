package com.discordrlintegration;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("discordrlintegration")
public interface DiscordRLIntegrationConfig extends Config
{
    @ConfigSection(
        name = "Discord Settings",
        description = "Discord webhook configuration",
        position = 0
    )
    String discordSection = "discord";

    @ConfigItem(
        keyName = "webhookUrl",
        name = "Discord Webhook URL",
        description = "The Discord webhook URL to send notifications to",
        section = discordSection,
        position = 1
    )
    default String webhookUrl()
    {
        return "";
    }

    @ConfigItem(
        keyName = "enableLevelUps",
        name = "Level Up Notifications",
        description = "Send notifications when you level up",
        section = discordSection,
        position = 2
    )
    default boolean enableLevelUps()
    {
        return true;
    }

    @ConfigItem(
        keyName = "enableQuestComplete",
        name = "Quest Complete Notifications",
        description = "Send notifications when you complete a quest",
        section = discordSection,
        position = 3
    )
    default boolean enableQuestComplete()
    {
        return true;
    }

    @ConfigItem(
        keyName = "enableAchievementDiary",
        name = "Achievement Diary Notifications",
        description = "Send notifications when you complete achievement diary tasks",
        section = discordSection,
        position = 4
    )
    default boolean enableAchievementDiary()
    {
        return true;
    }

    @ConfigItem(
        keyName = "enableDrops",
        name = "Rare Drop Notifications",
        description = "Send notifications for rare drops",
        section = discordSection,
        position = 5
    )
    default boolean enableDrops()
    {
        return false;
    }

    @ConfigItem(
        keyName = "minimumDropValue",
        name = "Minimum Drop Value (GP)",
        description = "Minimum value for drop notifications (in GP)",
        section = discordSection,
        position = 6
    )
    default int minimumDropValue()
    {
        return 100000;
    }

    @ConfigItem(
        keyName = "customMessage",
        name = "Custom Message Template",
        description = "Custom message template. Use {player}, {skill}, {level}, {totalLevel} as placeholders",
        section = discordSection,
        position = 7
    )
    default String customMessage()
    {
        return "{player} just reached level {level} {skill}! Total level: {totalLevel}";
    }
} 