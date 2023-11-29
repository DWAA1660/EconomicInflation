package me.dwaa.plugins.Managers;

import me.dwaa.plugins.EconomicInflation;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class DataHandler {

    public static void store(String user_received, List<ItemStack> user_gave, EconomicInflation plugin) {
        // Load existing properties or create new ones
        if ("EMERALD".equals(user_received)) {
            Properties properties = loadProperties("selling", plugin);
            for (ItemStack itemStack : user_gave) {
                String item = itemStack.getType().name();
                if (!"AIR".equals(item)) {
                    String value = properties.getProperty(item);

                    if (value == null) {
                        properties.setProperty(item, "1");
                    } else {
                        int new_value = Integer.parseInt(value);
                        new_value += 1;
                        properties.setProperty(item, Integer.toString(new_value));
                    }
                    try (OutputStream outputStream = Files.newOutputStream(Paths.get(plugin.getDataFolder() + File.separator + "selling.data.properties"))) {
                        properties.store(outputStream, "Trade Information");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // buying
            Properties properties = loadProperties("buying", plugin);

            String value = properties.getProperty(user_received);
            if (value == null) {
                properties.setProperty(user_received, "1");
            } else {
                int new_value = Integer.parseInt(value);
                new_value += 1;
                properties.setProperty(user_received, Integer.toString(new_value));
            }
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(plugin.getDataFolder() + File.separator + "buying.data.properties"))) {
                properties.store(outputStream, "Trade Information");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static float get(String item, EconomicInflation plugin) {
        Float inflationRate = (float) plugin.getInflationRate();
        Properties properties = loadProperties("selling", plugin);
        String value = properties.getProperty(item);
        if (value == null) {
            Properties properties2 = loadProperties("buying", plugin);
            value = properties2.getProperty(item);
            if (value == null) {
                return 1.0f;
            } else {
                return (float) (Integer.parseInt(value) / inflationRate + 1);
            }
        } else {
            return (float) (Integer.parseInt(value) / inflationRate + 1);
        }
    }

    private static Properties loadProperties(String method, EconomicInflation plugin) {
        Properties properties = new Properties();

        try {
            String filePath = plugin.getDataFolder() + File.separator + method + ".data.properties";

            // Load existing properties if the file exists
            if (Files.exists(Paths.get(filePath))) {
                try (FileInputStream inputStream = new FileInputStream(filePath)) {
                    properties.load(inputStream);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
