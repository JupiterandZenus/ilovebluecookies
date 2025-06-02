package com.discordrlintegration;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import net.runelite.http.api.RuneLiteAPI;

import java.io.IOException;
import java.awt.Color;

@Slf4j
public class DiscordWebhook
{
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient httpClient = RuneLiteAPI.CLIENT;

    public static void sendMessage(String webhookUrl, String playerName, String message, Color color)
    {
        if (webhookUrl == null || webhookUrl.trim().isEmpty())
        {
            log.debug("No webhook URL configured, skipping Discord notification");
            return;
        }

        try
        {
            String jsonPayload = createEmbedPayload(playerName, message, color);
            RequestBody body = RequestBody.create(JSON, jsonPayload);
            Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

            httpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    log.warn("Failed to send Discord webhook", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if (response.isSuccessful())
                    {
                        log.debug("Successfully sent Discord notification");
                    }
                    else
                    {
                        log.warn("Discord webhook returned error: {}", response.code());
                    }
                    response.close();
                }
            });
        }
        catch (Exception e)
        {
            log.warn("Error sending Discord webhook", e);
        }
    }

    private static String createEmbedPayload(String playerName, String message, Color color)
    {
        int colorValue = color != null ? color.getRGB() & 0xFFFFFF : 0x00FF00;
        
        return String.format(
            "{\"embeds\":[{" +
                "\"title\":\"RuneScape Achievement\"," +
                "\"description\":\"%s\"," +
                "\"color\":%d," +
                "\"footer\":{\"text\":\"RuneLite Discord Integration\"}," +
                "\"timestamp\":\"%s\"" +
            "}]}",
            message.replace("\"", "\\\""),
            colorValue,
            java.time.Instant.now().toString()
        );
    }

    public static void sendSimpleMessage(String webhookUrl, String message)
    {
        if (webhookUrl == null || webhookUrl.trim().isEmpty())
        {
            log.debug("No webhook URL configured, skipping Discord notification");
            return;
        }

        try
        {
            String jsonPayload = String.format("{\"content\":\"%s\"}", message.replace("\"", "\\\""));
            RequestBody body = RequestBody.create(JSON, jsonPayload);
            Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

            httpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    log.warn("Failed to send Discord webhook", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if (response.isSuccessful())
                    {
                        log.debug("Successfully sent Discord notification");
                    }
                    else
                    {
                        log.warn("Discord webhook returned error: {}", response.code());
                    }
                    response.close();
                }
            });
        }
        catch (Exception e)
        {
            log.warn("Error sending Discord webhook", e);
        }
    }
} 