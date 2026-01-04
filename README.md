# Paper Plugin Template

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Atrimilan/PaperPluginTemplate/release.yml?branch=master&event=workflow_dispatch&style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/actions/workflows/release.yml)
[![GitHub Tag](https://img.shields.io/github/v/tag/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/releases)
[![GitHub License](https://img.shields.io/github/license/Atrimilan/PaperPluginTemplate?style=flat-square)](https://github.com/Atrimilan/PaperPluginTemplate/blob/master/LICENSE)

A template to get started with Paper plugin development.

> [!NOTE]
> This template was made following the official [PaperMC documentation](https://docs.papermc.io/paper/)
([archive](https://web.archive.org/web/20251213104318/https://docs.papermc.io/paper/)),
> head there to explore more about plugin development on Paper.

## Using the template

Since this is just an example project, you will need to follow the official documentation to learn how to modify
[build.gradle.kts](build.gradle.kts) and [plugin.yml](src/main/resources/plugin.yml) to suit your needs.

You can then use this project as a starting point to create your own plugins, or modify the architecture if you prefer.

Here are the main Gradle task you will need: 
* Build the plugin and run a local Paper server that includes it:
  ```sh
  ./gradlew runServer
  ```
  This will automatically start a local server with your plugin a `localhost:25565`.

* Build a JAR file:
  ```sh
  ./gradlew build
  ```
  Or if you don't want to run unit tests:
  ```sh
  ./gradlew assemble
  ```

## Example features

This template shows some examples of what can be done when creating plugins, such as adding custom commands with permissions,
reading file values, and listening to in-game events to perform actions.

Overridden event:
* When a player joins the server, the welcome message is customized (formatted with [MiniMessage](https://docs.papermc.io/adventure/minimessage/)).

Available commands:
* `/flyspeed <speed>`
  * Set the flight speed of the player executing the command
  * Permission: `ppt.flyspeed.self`
* `/flyspeed <player> <speed>`
  * Set the flight speed of any player
  * Permission: `ppt.flyspeed.others`
* `/read-config <ultimate-answer|pangram|boolean|player>`
  * Read [config.yml](src/main/resources/config.yml) values
  * Permission: `ppt.read-config`

> [!TIP]
> If you want to manage player's permissions, I recommend using [LuckPerms](https://luckperms.net/).
