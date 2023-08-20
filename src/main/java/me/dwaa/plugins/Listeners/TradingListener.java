package me.dwaa.plugins.Listeners;

import io.papermc.paper.event.player.PlayerTradeEvent;
import me.dwaa.plugins.EconomicInflation;
import me.dwaa.plugins.Managers.DataHandler;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;
public class TradingListener implements Listener {
    private final Map<UUID, Merchant> interactingVillagers = new HashMap<>();
    private final Map<UUID, List<MerchantRecipe>> originalRecipes = new HashMap<>();

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent event){
        DataHandler.store(event.getTrade().getResult().getType().name(), event.getTrade().getIngredients(), EconomicInflation.getPlugin(EconomicInflation.class));
    }
    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Merchant) {
            Merchant villager = (Merchant) event.getRightClicked();
            UUID playerUUID = event.getPlayer().getUniqueId();

            if (!originalRecipes.containsKey(playerUUID)) {
                originalRecipes.put(playerUUID, copyRecipes(villager.getRecipes()));
            }
            interactingVillagers.put(playerUUID, villager);

            List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes()); // Make a copy of the recipes
            for (int i = 0; i < recipes.size(); i++) {
                recipes.get(i).setIgnoreDiscounts(true);
                List<ItemStack> current_ingredients = recipes.get(i).getIngredients();
                for (int i2 = 0; i2 < current_ingredients.size(); i2++) {
                    if (!current_ingredients.get(i2).getType().name().equals("AIR")) {
                        Float res = DataHandler.get(current_ingredients.get(i2).getType().name(), EconomicInflation.getPlugin(EconomicInflation.class));
                        if(res == 1.0){
                            Float res2 = DataHandler.get(recipes.get(i).getResult().getType().name(), EconomicInflation.getPlugin(EconomicInflation.class));
                            if(res2 == 1.0){
                                res = 1.0F;
                            }
                            else{
                                res = res2;
                            }
                        }
                        int new_price = Math.round(current_ingredients.get(i2).getAmount() * res);
                        if (new_price > 64) {
                            new_price = 64;
                        }
                        current_ingredients.get(i2).setAmount(new_price);
                    }
                }
                recipes.get(i).setIngredients(current_ingredients);
            }
            villager.setRecipes(recipes);

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (interactingVillagers.containsKey(playerUUID)) {
            Merchant villager = interactingVillagers.get(playerUUID);

            if (originalRecipes.containsKey(playerUUID)) {
                List<MerchantRecipe> originalVillagerRecipes = originalRecipes.get(playerUUID);

                // Make a copy of the original recipes to restore
                List<MerchantRecipe> recipesToRestore = copyRecipes(originalVillagerRecipes);

                // Restore original recipes
                villager.setRecipes(recipesToRestore);

                // Remove the player's original recipes from the map
                originalRecipes.remove(playerUUID);
            }

            // Remove the player from the interactingVillagers map
            interactingVillagers.remove(playerUUID);
        }
    }


    // Make a deep copy of a list of recipes
    private List<MerchantRecipe> copyRecipes(List<MerchantRecipe> original) {
        List<MerchantRecipe> copy = new ArrayList<>();
        for (MerchantRecipe recipe : original) {
            MerchantRecipe recipeCopy = new MerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
            recipeCopy.setIngredients(new ArrayList<>(recipe.getIngredients()));
            copy.add(recipeCopy);
        }
        return copy;
    }
}