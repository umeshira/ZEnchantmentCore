package io.zivoric.enchantmentcore;

import io.zivoric.enchantmentcore.utils.EnchantmentInformation;
import io.zivoric.enchantmentcore.utils.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Event listener responsible for applying the custom enchantment generator to enchantment tables and anvils.
 */
public class ItemEnchantListener implements Listener {
    private final EnchantmentGenerator generator;
    private final EnchantmentCore core;

    public ItemEnchantListener(EnchantmentCore c, EnchantmentGenerator generator) {
        core = c;
        this.generator = generator;
    }

    @EventHandler
    public void onPrepare(PrepareItemEnchantEvent event) {
        if (core.getConfig().getBoolean("enable-custom-generator")) {
            List<EnchantmentInformation> offers = generator.getOffers(event.getItem(), event.getEnchanter(), event.getEnchantmentBonus(), EnchantmentUtils.generatorSettings().getDouble("enchantment-amount-multiplier"), EnchantmentUtils.generatorSettings().getBoolean("allow-stacked-books"), EnchantmentUtils.generatorSettings().getBoolean("allow-treasure-enchants"), EnchantmentUtils.generatorSettings().getBoolean("treat-axes-as-weapons"));
            for (EnchantmentInformation offer : offers) {
                if (offer == null) continue;
                Enchantment firstEnch = offer.getEnchs().get(0);
                int firstLevel = offer.getLevels().get(0);
                int cost = offer.getCost();
                EnchantmentOffer newOffer = new EnchantmentOffer(firstEnch, firstLevel, cost);
                event.getOffers()[offers.indexOf(offer)] = newOffer;
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if (core.getConfig().getBoolean("enable-custom-generator")) {
            EnchantmentInformation enchants = generator.getOffers(event.getItem(), event.getEnchanter(), generator.getBonusNumber(event.getEnchantBlock()), EnchantmentUtils.generatorSettings().getDouble("enchantment-amount-multiplier"), EnchantmentUtils.generatorSettings().getBoolean("allow-stacked-books"), EnchantmentUtils.generatorSettings().getBoolean("allow-treasure-enchants"), EnchantmentUtils.generatorSettings().getBoolean("treat-axes-as-weapons")).get(event.whichButton());
            generator.updateXpSeed(event.getEnchanter());
            event.setExpLevelCost(enchants.getCost());
            event.getEnchantsToAdd().clear();
            for (Enchantment e : enchants.getEnchs()) {
                event.getEnchantsToAdd().put(e, enchants.getLevels().get(enchants.getEnchs().indexOf(e)));
            }
        }
        ItemStack item = event.getItem();
        Bukkit.getScheduler().scheduleSyncDelayedTask(core, () -> {
            ItemMeta iMeta = item.getItemMeta();
            if (iMeta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta eMeta = (EnchantmentStorageMeta) iMeta;
                event.getEnchantsToAdd().forEach((ench, lvl) -> {
                    if (ench instanceof CustomEnch) {
                        eMeta.addStoredEnchant(ench, lvl, true);
                    }
                });
            }
            EnchantmentUtils.updateItemLore(iMeta);
            item.setItemMeta(iMeta);
        }); // schedule needed since Minecraft will not apply custom enchantments to books (for some reason)
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        boolean canApply = false;
        AnvilInventory inv = event.getInventory();
        GameMode gm = event.getViewers().get(0).getGameMode();
        ItemStack firstSlot = inv.getItem(0);
        ItemStack secondSlot = inv.getItem(1);
        if (firstSlot == null || firstSlot.getType() == Material.AIR)
            return;
        ItemStack result = firstSlot.clone();
        ItemMeta firstMeta = result.getItemMeta();
        int repair = EnchantmentUtils.getAnvilCost(firstSlot);
        if (secondSlot != null && secondSlot.getType() != Material.AIR) {
            ItemMeta secondMeta = secondSlot.getItemMeta();
            Map<Enchantment, Integer> firstEnchantments;
            if (firstMeta instanceof EnchantmentStorageMeta)
                firstEnchantments = ((EnchantmentStorageMeta) firstMeta).getStoredEnchants();
            else
                firstEnchantments = firstSlot.getEnchantments();
            Map<Enchantment, Integer> secondEnchantments;
            if (secondMeta instanceof EnchantmentStorageMeta)
                secondEnchantments = ((EnchantmentStorageMeta) secondMeta).getStoredEnchants();
            else
                secondEnchantments = secondSlot.getEnchantments();
            int repairAmount = EnchantmentUtils.getRepairAmount(firstSlot, secondSlot);
            if (repairAmount > 0) {
                Damageable dFirst = (Damageable) firstMeta;
                int initDamage = dFirst.getDamage();
                dFirst.setDamage(Math.max(initDamage - repairAmount, 0));
                int unitRepairNumber = EnchantmentUtils.getUnitRepairNumber(firstSlot, secondSlot);
                if (unitRepairNumber > -1)
                    repair += Math.min(unitRepairNumber, secondSlot.getAmount());
                else {
                    repair += EnchantmentUtils.getAnvilCost(secondSlot);
                }
                if (firstSlot.getType() == secondSlot.getType() && initDamage > 0)
                    repair += 2;
                if (initDamage != dFirst.getDamage())
                    canApply = true;
            }
            if (firstSlot.getType() == secondSlot.getType() || secondMeta instanceof EnchantmentStorageMeta) {
                boolean allowIllegal;
                boolean allowOverMaxLevel = EnchantmentUtils.generatorSettings().getBoolean("allow-over-max-level");
                if (gm == GameMode.SURVIVAL || gm == GameMode.ADVENTURE)
                    allowIllegal = EnchantmentUtils.generatorSettings().getBoolean("allow-survival-illegal-enchants");
                else
                    allowIllegal = EnchantmentUtils.generatorSettings().getBoolean("allow-creative-illegal-enchants");
                Map<Enchantment, Integer> combinedEnchs;
                Map<Enchantment, Integer> validSacEnchants = EnchantmentUtils.getValidSacrificeEnchants(result, allowIllegal, firstEnchantments, secondEnchantments);
                repair += secondEnchantments.size() - validSacEnchants.size();
                combinedEnchs = EnchantmentUtils.combineEnchants(result, allowIllegal, allowOverMaxLevel, firstEnchantments, secondEnchantments);
                for (Entry<Enchantment, Integer> e : validSacEnchants.entrySet())
                    repair += (secondMeta instanceof EnchantmentStorageMeta ? EnchantmentUtils.getAnvilBookMultiplier(e.getKey()) : EnchantmentUtils.getAnvilItemMultiplier(e.getKey())) * combinedEnchs.get(e.getKey());
                if (!combinedEnchs.equals(firstEnchantments) && validSacEnchants.size() != 0) {
                    EnchantmentUtils.removeAllEnchantments(firstMeta);
                    EnchantmentUtils.addEnchantments(firstMeta, combinedEnchs, EnchantmentUtils.generatorSettings().getBoolean("ignore-level-restrictions"));
                    EnchantmentUtils.updateItemLore(firstMeta);
                    canApply = true;
                }
            }
        }
        if (!Objects.equals(inv.getRenameText(), firstMeta.getDisplayName())) {
            firstMeta.setDisplayName(inv.getRenameText());
            repair += 1;
            canApply = true;
        }
        result.setItemMeta(firstMeta);
        if (!canApply) {
            event.setResult(new ItemStack(Material.AIR));
            return;
        }
        final int repairCost;
        repairCost = repair;
        result = EnchantmentUtils.setAnvilUses(result, Math.max(EnchantmentUtils.getAnvilUses(firstSlot), EnchantmentUtils.getAnvilUses(secondSlot)) + 1);
        event.setResult(result);
        core.getServer().getScheduler().runTask(core, () -> {
            event.getInventory().setRepairCost(repairCost);
            for (HumanEntity viewer : event.getInventory().getViewers()) {
                if (viewer instanceof Player)
                    viewer.setWindowProperty(InventoryView.Property.REPAIR_COST, repairCost);
            }
        });
    }
}
