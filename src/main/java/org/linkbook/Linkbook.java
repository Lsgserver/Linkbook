package org.linkbook;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Linkbook extends JavaPlugin {

    private static final int MAX_CHARS_PER_PAGE = 240; // ca. 240 Zeichen pro Seite

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerBookCommands();
    }

    private void registerBookCommands() {
        FileConfiguration config = getConfig();
        ConfigurationSection booksSection = config.getConfigurationSection("books");
        if (booksSection == null) {
            getLogger().warning("Keine Bücher in der config.yml gefunden!");
            return;
        }

        for (String bookKey : booksSection.getKeys(false)) {
            String command = config.getString("books." + bookKey + ".command");
            String title = config.getString("books." + bookKey + ".title", "Buch");
            String author = config.getString("books." + bookKey + ".author", "Server");
            List<Map<?, ?>> entries = config.getMapList("books." + bookKey + ".entries");

            if (command == null || command.isEmpty()) continue;

            BukkitCommand dynamicCommand = new BukkitCommand(command) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(Component.text("Nur Spieler können dieses Kommando ausführen."));
                        return true;
                    }

                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta meta = (BookMeta) book.getItemMeta();
                    meta.setTitle(title);
                    meta.setAuthor(author);

                    List<Component> pages = createBookPages(entries, MAX_CHARS_PER_PAGE);
                    meta.pages(pages);
                    book.setItemMeta(meta);

                    player.openBook(book);
                    return true;
                }
            };

            // Paper-spezifisch: Command dynamisch registrieren
            getServer().getCommandMap().register(getDescription().getName(), dynamicCommand);
            getLogger().info("Registriere Befehl /" + command + " für Buch '" + title + "'");
        }
    }

    // Automatische Seitentrennung, nur blauer Name, mit zusätzlichem Zeilenabstand
    private List<Component> createBookPages(List<Map<?, ?>> entries, int maxCharsPerPage) {
        List<Component> pages = new ArrayList<>();
        int currentLength = 0;
        List<Component> currentPageComponents = new ArrayList<>();

        for (Map<?, ?> entry : entries) {
            String type = (String) entry.get("type");
            String name = (String) entry.get("name");
            Component comp = Component.text(name).color(NamedTextColor.BLUE);

            if ("url".equals(type)) {
                String link = (String) entry.get("link");
                comp = comp.clickEvent(ClickEvent.openUrl(link));
            } else if ("command".equals(type)) {
                String cmd = (String) entry.get("command");
                comp = comp.clickEvent(ClickEvent.runCommand(cmd));
            }
            // Zeilenabstand: ein Leerzeichen + Zeilenumbruch nach jedem Eintrag
            comp = comp.append(Component.text("\n\n"));

            int entryLength = name.length() + 2; // +2 für zwei Zeilenumbrüche
            if (currentLength + entryLength > maxCharsPerPage) {
                pages.add(componentsToComponent(currentPageComponents));
                currentPageComponents.clear();
                currentLength = 0;
            }
            currentPageComponents.add(comp);
            currentLength += entryLength;
        }
        if (!currentPageComponents.isEmpty()) {
            pages.add(componentsToComponent(currentPageComponents));
        }
        if (pages.isEmpty()) {
            pages.add(Component.text("Keine Einträge vorhanden."));
        }
        return pages;
    }

    // Hilfsfunktion: Component-Liste zu einer Seite zusammenfügen
    private Component componentsToComponent(List<Component> comps) {
        Component page = Component.text("");
        for (Component c : comps) {
            page = page.append(c);
        }
        return page;
    }
}