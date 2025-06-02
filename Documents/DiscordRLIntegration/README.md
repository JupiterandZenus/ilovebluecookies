# Discord RuneLite Integration Plugin

A RuneLite plugin that sends your RuneScape achievements and progress to Discord via webhooks. Share your level ups, quest completions, and rare drops with your Discord community!

## Features

- **Level Up Notifications**: Get notified when you level up any skill
- **Quest Completion**: Notifications when you complete quests
- **Achievement Diary Progress**: Notifications for diary completions
- **Rare Drop Alerts**: Configure value thresholds for drop notifications
- **Customizable Messages**: Use templates with placeholders for personalized messages
- **Rich Embeds**: Beautiful Discord embeds with colors matching each skill
- **Easy Configuration**: Simple setup through RuneLite's plugin configuration

## Setup Instructions

### 1. Create a Discord Webhook

1. Go to your Discord server
2. Navigate to Server Settings → Integrations → Webhooks
3. Click "Create Webhook"
4. Choose the channel where you want notifications to appear
5. Copy the webhook URL

### 2. Install the Plugin

1. Place the compiled JAR file in your RuneLite plugins directory
2. Restart RuneLite
3. Enable the "Discord RL Integration" plugin in the plugin hub

### 3. Configure the Plugin

1. Go to RuneLite Settings → Discord RL Integration
2. Paste your Discord webhook URL
3. Configure which notifications you want to receive:
   - Level Up Notifications
   - Quest Complete Notifications
   - Achievement Diary Notifications
   - Rare Drop Notifications
4. Set your minimum drop value for rare drop alerts (default: 100,000 GP)
5. Customize your message template using placeholders:
   - `{player}` - Your player name
   - `{skill}` - The skill name
   - `{level}` - The new level
   - `{totalLevel}` - Your total level

## Message Customization

The default message template is:
```
{player} just reached level {level} {skill}! Total level: {totalLevel}
```

You can customize this to your liking. For example:
```
🎉 {player} is now level {level} in {skill}! Total: {totalLevel} 🎉
```

## Building from Source

### Prerequisites
- Java 11 or higher
- Gradle

### Build Steps
```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## Configuration Options

| Setting | Description | Default |
|---------|-------------|---------|
| Discord Webhook URL | Your Discord webhook URL | (empty) |
| Level Up Notifications | Send notifications for level ups | Enabled |
| Quest Complete Notifications | Send notifications for quest completions | Enabled |
| Achievement Diary Notifications | Send notifications for diary completions | Enabled |
| Rare Drop Notifications | Send notifications for valuable drops | Disabled |
| Minimum Drop Value | Minimum GP value for drop notifications | 100,000 |
| Custom Message Template | Template for level up messages | See above |

## Supported Events

- **Skill Level Ups**: All skills except Overall
- **Quest Completions**: Detected via game messages
- **Achievement Diary Tasks**: Basic detection for diary completions
- **Loot Drops**: Configurable value threshold for notifications

## Troubleshooting

### Messages Not Sending
1. Verify your webhook URL is correct
2. Check that the Discord channel still exists
3. Ensure the webhook hasn't been deleted
4. Check RuneLite logs for error messages

### Level Ups Not Detected
1. Make sure "Level Up Notifications" is enabled in settings
2. Verify you're logged into the game
3. The plugin only detects real level increases, not temporary boosts

### Wrong Player Name
- The plugin uses your in-game display name
- Make sure you're logged in when the events occur

## Security

- Your webhook URL is stored locally in RuneLite's configuration
- The plugin only sends achievement data, no sensitive information
- All communication uses HTTPS

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This plugin is open source and available under the MIT License.

## Disclaimer

This plugin is not affiliated with Jagex Ltd. RuneScape is a trademark of Jagex Ltd. 