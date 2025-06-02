package com.discordrlintegration;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
    name = "Discord RL Integration",
    description = "Send RuneScape achievements to Discord via webhooks",
    tags = {"discord", "webhook", "notifications", "levels", "achievements"}
)
public class DiscordRLIntegrationPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private DiscordRLIntegrationConfig config;

    @Inject
    private ItemManager itemManager;

    private final Map<Skill, Integer> previousLevels = new EnumMap<>(Skill.class);

    @Override
    protected void startUp() throws Exception
    {
        log.info("Discord RL Integration started!");
        
        // Initialize previous levels
        if (client.getGameState() == GameState.LOGGED_IN)
        {
            initializeLevels();
        }
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("Discord RL Integration stopped!");
        previousLevels.clear();
    }

    private void initializeLevels()
    {
        for (Skill skill : Skill.values())
        {
            if (skill != Skill.OVERALL)
            {
                int level = client.getRealSkillLevel(skill);
                previousLevels.put(skill, level);
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
        {
            initializeLevels();
        }
        else if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN)
        {
            previousLevels.clear();
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged)
    {
        if (!config.enableLevelUps() || client.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }

        Skill skill = statChanged.getSkill();
        if (skill == Skill.OVERALL)
        {
            return;
        }

        int currentLevel = statChanged.getLevel();
        Integer previousLevel = previousLevels.get(skill);

        if (previousLevel != null && currentLevel > previousLevel)
        {
            // Level up detected!
            String playerName = client.getLocalPlayer().getName();
            int totalLevel = client.getTotalLevel();
            
            String message = formatMessage(config.customMessage(), playerName, skill, currentLevel, totalLevel);
            Color skillColor = getSkillColor(skill);
            
            DiscordWebhook.sendMessage(config.webhookUrl(), playerName, message, skillColor);
            
            log.info("Level up detected: {} {} -> {}", skill.getName(), previousLevel, currentLevel);
        }

        previousLevels.put(skill, currentLevel);
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded)
    {
        // Quest completion detection
        if (config.enableQuestComplete() && widgetLoaded.getGroupId() == 153) // Quest completion interface
        {
            client.runScript(ScriptID.MESSAGE_LAYER_CLOSE, 0, 0, 0);
            String playerName = client.getLocalPlayer().getName();
            String message = String.format("%s just completed a quest!", playerName);
            DiscordWebhook.sendMessage(config.webhookUrl(), playerName, message, Color.YELLOW);
        }
        
        // Achievement diary completion detection
        if (config.enableAchievementDiary() && widgetLoaded.getGroupId() == 153)
        {
            // This would need more specific detection for achievement diaries
            // For now, we'll keep it simple
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage)
    {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE)
        {
            return;
        }

        String message = Text.removeTags(chatMessage.getMessage());
        String playerName = client.getLocalPlayer().getName();

        // Quest completion detection via chat
        if (config.enableQuestComplete() && message.contains("Congratulations, you have completed"))
        {
            String questName = extractQuestName(message);
            String discordMessage = String.format("%s just completed the quest: %s!", playerName, questName);
            DiscordWebhook.sendMessage(config.webhookUrl(), playerName, discordMessage, Color.YELLOW);
        }

        // Achievement diary completion
        if (config.enableAchievementDiary() && (message.contains("Congratulations, you have completed") || message.contains("Achievement Diary")))
        {
            String discordMessage = String.format("%s completed an Achievement Diary task!", playerName);
            DiscordWebhook.sendMessage(config.webhookUrl(), playerName, discordMessage, Color.ORANGE);
        }
    }

    @Subscribe
    public void onLootReceived(LootReceived lootReceived)
    {
        if (!config.enableDrops())
        {
            return;
        }

        String playerName = client.getLocalPlayer().getName();
        long totalValue = 0;

        for (ItemStack item : lootReceived.getItems())
        {
            int price = itemManager.getItemPrice(item.getId());
            totalValue += (long) price * item.getQuantity();
        }

        if (totalValue >= config.minimumDropValue())
        {
            String itemNames = lootReceived.getItems().stream()
                .map(item -> itemManager.getItemComposition(item.getId()).getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Unknown items");

            String message = String.format("%s received a valuable drop worth %,d GP: %s", 
                playerName, totalValue, itemNames);
            
            DiscordWebhook.sendMessage(config.webhookUrl(), playerName, message, Color.GREEN);
        }
    }

    private String formatMessage(String template, String playerName, Skill skill, int level, int totalLevel)
    {
        return template
            .replace("{player}", playerName)
            .replace("{skill}", skill.getName())
            .replace("{level}", String.valueOf(level))
            .replace("{totalLevel}", String.valueOf(totalLevel));
    }

    private String extractQuestName(String message)
    {
        // Extract quest name from completion message
        String[] parts = message.split(":");
        if (parts.length > 1)
        {
            return parts[1].trim().replaceAll("\\.$", "");
        }
        return "Unknown Quest";
    }

    private Color getSkillColor(Skill skill)
    {
        switch (skill)
        {
            case ATTACK:
            case STRENGTH:
            case DEFENCE:
            case HITPOINTS:
                return new Color(139, 69, 19); // Brown for combat
            case RANGED:
                return new Color(0, 128, 0); // Green
            case PRAYER:
                return new Color(255, 215, 0); // Gold
            case MAGIC:
                return new Color(0, 0, 255); // Blue
            case COOKING:
            case FIREMAKING:
                return new Color(255, 69, 0); // Red/Orange
            case WOODCUTTING:
            case FARMING:
            case HERBLORE:
                return new Color(34, 139, 34); // Forest Green
            case FISHING:
                return new Color(30, 144, 255); // Dodger Blue
            case MINING:
            case SMITHING:
                return new Color(105, 105, 105); // Dim Gray
            case CRAFTING:
            case FLETCHING:
                return new Color(160, 82, 45); // Saddle Brown
            case AGILITY:
            case THIEVING:
                return new Color(75, 0, 130); // Indigo
            case SLAYER:
                return new Color(25, 25, 112); // Midnight Blue
            case RUNECRAFT:
                return new Color(128, 0, 128); // Purple
            case HUNTER:
                return new Color(139, 69, 19); // Saddle Brown
            case CONSTRUCTION:
                return new Color(255, 140, 0); // Dark Orange
            default:
                return new Color(0, 255, 0); // Default green
        }
    }

    @Provides
    DiscordRLIntegrationConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DiscordRLIntegrationConfig.class);
    }
} 