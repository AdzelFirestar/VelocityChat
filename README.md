# VelocityChat

Simple, lightweight **staff chat toggle** plugin for **Velocity** networks.

VelocityChat lets staff members quickly enter a private staff-chat mode using `/sc`, talk to other staff across the proxy, and toggle back to normal chat when done.

## Features

- Toggle staff chat mode with a single command (`/sc`)
- Staff messages are delivered to all online players with staff-chat permission
- Messages are formatted with a configurable prefix
- Clean default config that is easy to customize
- Built for Velocity with Maven

## Command

- `/sc`  
  Toggles staff chat mode on/off for the player.

## Permission

- `velocitychat.sc`  
  Required to:
  - Use `/sc`
  - Receive staff chat messages

## How It Works

- A staff member runs `/sc` to enable staff chat mode.
- While mode is enabled, their normal chat messages are sent to staff chat.
- Running `/sc` again disables staff chat mode and restores normal chat behavior.
- Staff chat is always visible to players with `velocitychat.sc`, regardless of whether they currently have mode enabled.

## Installation

1. Build the plugin:
   - `mvn package`
2. Copy the generated JAR from `target/velocitychat-0.1.jar` into your Velocity `plugins` folder.
3. Start or restart the proxy.
4. Edit the generated config file if needed:
   - `plugins/velocitychat/config.yml`

## Configuration

Default `config.yml`:

```yml
# Plugin Version - DO NOT CHANGE
Version: 0.1

#Start of Configuration
Prefix: "&a[Staff] &r"
```

### Config Notes

- `Version` is managed by the plugin and should not be manually changed.
- `Prefix` supports `&` color codes and can be customized freely.
- Default prefix `&a[Staff] &r` displays `[Staff]` in lime green, then resets formatting.

## Example Usage

- Staff member types `/sc` -> receives: staff chat mode enabled.
- Staff member types `Check player reports in survival` in chat.
- All online staff with `velocitychat.sc` see the message with the configured staff prefix.
- Staff member types `/sc` again -> staff chat mode disabled.

## Requirements

- Java 17+
- Velocity proxy

## Project Info

- **Author:** Adzel
- **Version:** 0.1
- **Build Tool:** Maven
