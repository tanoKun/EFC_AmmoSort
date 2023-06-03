package com.github.tanokun.efcammosort

import com.github.tanokun.efcammosort.command.AmmoCommand
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

lateinit var ammoConfig: FileConfiguration private set

val ammo = HashMap<String, Ammo>()

class EFCAmmoSort : JavaPlugin() {
    override fun onEnable() {
        getCommand("ammosort")?.setExecutor(AmmoCommand)

        saveDefaultConfig()
        ammoConfig = config

        config.getConfigurationSection("ammo")?.getKeys(false)?.forEachIndexed { index, str ->
            println(str)
            ammo[str] = Ammo(str, index, config.getInt("ammo.$str", 4))
        }
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