# Economic Inflation Minecraft Plugin

Economic Inflation is a Paper/Purpur plugin for Minecraft that introduces economic inflation mechanics to the game's trading system. This plugin modifies the prices of items traded with villagers based on a configurable inflation rate.

## Features

- Trade Price Adjustment: Adjusts the prices of items traded with villagers based on inflation rates.
- Configurable Inflation Rate: Server administrators can configure the inflation rate in the plugin's configuration.
- Trade Data Tracking: Tracks the number of times each item has been traded (bought or sold) with villagers.
- Event Listeners: Utilizes event listeners to intercept player interactions with villagers and modify trade prices.

- Inflation Rate: The rate of inflation is synced across all of server but you can still get cheaper items with different villagers, the current max price is 64 (subject to change) 

## Configuration

To configure the EconomicInflation plugin, follow these steps:

1. Place the `EconomicInflation.jar` file in the `plugins` folder of your Bukkit/Spigot server.
2. Start the server to generate the `config.yml` file inside the `plugins/EconomicInflation` directory.
3. Open the `config.yml` file in a text editor.

Edit the following configuration options:

- `inflation_rate`: the higher the number the lower it is, it is calculated by (amount_of_times_traded / infaltion_rate + normal price)

Save the changes to the `config.yml` file.

## Usage

1. Install and configure the plugin as described in the Configuration section.
2. Start your Paper/Purpur server.
3. Interact with villagers in the game to observe the economic inflation mechanics in action. Prices of traded items will be adjusted based on the configured inflation rate.
4. The plugin will automatically track trade data, including the number of times each item has been traded with villagers.

## Known Issues

- None at the moment.

## Contributing

If you have improvements or new features to suggest, feel free to message me on discord `dwaa`.


---

For support or inquiries, please contact `dwatnip123@gmail.com` or `dwaa` on discord.
