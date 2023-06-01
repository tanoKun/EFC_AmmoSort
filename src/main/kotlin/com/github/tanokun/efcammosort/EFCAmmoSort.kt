package com.github.tanokun.efcammosort

import com.github.tanokun.efcammosort.command.AmmoCommand
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class EFCAmmoSort : JavaPlugin() {
    override fun onEnable() {
        getCommand("ammosort")?.setExecutor(AmmoCommand)
    }

    override fun onDisable() {
    }
}

fun deleteShulkerFIR(list: List<ItemStack>) {

    for (mono in list) {
        if (mono.type == Material.AIR) continue

        val meta = mono.itemMeta!!
        meta.lore?.let { lore ->
            var l = -1
            var b = false

            for ((i, lore2) in lore.withIndex()) {
                if (lore2.contains("[FIR]")) {l = i; continue }
                if (l != -1) b = true
            }

            if (l != -1) {
                if (!b) lore.removeAt(l)
                else lore[l] = " "
                meta.lore = lore
            }

            mono.itemMeta = meta
        }
    }
}