# LinkBook - A Paper Minecraft Plugin

LinkBook is a Paper Minecraft plugin that lets you add custom links to a config file and display them in a book GUI via custom commands. This makes it easy for server admins to share information, website URLs, or any links with players in a neat, interactive way.

## Features

- Add unlimited custom links in `config.yml`
- Assign each link to a custom command
- Display links inside an in-game book GUI
- Easy-to-use and configure

## Installation

1. Download the latest version of LinkBook from [Releases](https://github.com/Lsgserver/Linkbook/tree/main/target).
2. I recommend to Download the lastest version of Linkbook.
3. Place the `LinkBook.jar` file into your server’s `plugins` folder.
4. Restart your server. The plugin will generate a `config.yml` file in a Linkbook folder.

## Configuration

Open `plugins/LinkBook/config.yml` to configure your links.

```yaml
links:
  website:
    command: "website"
    title: "Server Website"
    url: "https://yourwebsite.com"
  discord:
    command: "discord"
    title: "Join our Discord"
    url: "https://discord.gg/yourinvite"
```

- **command**: The command players will use (e.g., `/website`)
- **title**: The title shown in the book GUI
- **url**: The link shown in the book GUI

## Usage

- Use the configured commands (e.g., `/website`, `/discord`) in-game.
- A book GUI will open, displaying the link and title.
- Players can click the link to copy or open it.

## Permissions

- `linkbook.use.<command>`: Allows players to use the specific link command (replace `<command>` with your configured command).

## Example

1. Add a new entry in `config.yml`:
    ```yaml
    links:
      wiki:
        command: "wiki"
        title: "Server Wiki"
        url: "https://wiki.yourserver.com"
    ```
2. Reload or restart your server.
3. Players can now use `/wiki` to open a book with the server wiki link.

## Support

For issues, suggestions, or contributions, please open an issue or pull request on [GitHub](https://github.com/YOUR_GITHUB_REPO).

---

**Made for Paper Minecraft servers.**
