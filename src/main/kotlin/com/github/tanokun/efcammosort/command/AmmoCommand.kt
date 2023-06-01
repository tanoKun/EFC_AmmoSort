package com.github.tanokun.efcammosort.command

import com.github.tanokun.efcammosort.AmmoType
import com.github.tanokun.efcammosort.deleteShulkerFIR
import org.bukkit.Material
import org.bukkit.block.ShulkerBox
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta

object AmmoCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cプレイヤーのみ実行可能です")
            return false
        }

        if (sender.inventory.itemInMainHand.type == Material.AIR) {
                sender.sendMessage("§c手にアモケースを持ってください")
                return false
        }

        if (!sender.inventory.itemInMainHand.itemMeta.displayName.contains("アモケース")) {
            sender.sendMessage("§c手にアモケースを持ってください")
            return false
        }

        val meta = (sender.inventory.itemInMainHand.itemMeta as BlockStateMeta)
        val shulker = meta.blockState as ShulkerBox

        val copy = HashMap<AmmoType, ItemStack>()

        val items = shulker.inventory.filterNotNull().map {
            val name = it.itemMeta.displayName
                .replace("§e", "A_")
                .replace(".", "_")
                .replace("x", "X")
                .replace(" ", "_")
                .replace("-", "_")
                .uppercase()

            val ammoType = AmmoType.valueOf(name)
            copy[ammoType] = it

            return@map Pair(ammoType, it.amount)
        }

        val putItems = HashMap<AmmoType, Int>()

        items.forEach {
            putItems[it.first] = (putItems[it.first] ?: 0) + it.second
        }

        val sortedItems = ArrayList<ItemStack>()

        putItems
            .toSortedMap { o1, o2 -> return@toSortedMap if (o1.han < o2.han) -1 else 1 }
            .forEach { en ->
                val stack = en.value / en.key.byMaterial.max
                val fraction = en.value % en.key.byMaterial.max
                for (i in 1..stack)
                    sortedItems.add(copy.getOrDefault(en.key, ItemStack(Material.AIR)).clone().apply { amount = en.key.byMaterial.max })
                if (fraction != 0) sortedItems.add(copy.getOrDefault(en.key, ItemStack(Material.AIR)).clone().apply { amount = fraction })
            }

        shulker.inventory.clear()
        deleteShulkerFIR(sortedItems)
        sortedItems.forEachIndexed { index, ammo -> shulker.inventory.setItem(index, ammo) }
        meta.blockState = shulker
        sender.inventory.itemInMainHand.itemMeta = meta

        /**
         *     val items = arrayListOf(
        Pair("A_5_56X45M_M995", 10),
        Pair("A_5_7X28M_SS190", 40),
        Pair("A_45_ACP_MATCH_FMJ", 20),
        Pair("A_45_ACP_AP", 20),
        Pair("A_45_ACP_AP", 40),
        Pair("A_5_45X39M_PPBS", 40)
        )

        val putItems = HashMap<AmmoType, Int>()

        items.forEach {
        putItems[AmmoType.valueOf(it.first)] = (putItems[AmmoType.valueOf(it.first)] ?: 0) + it.second
        }

        val sortedItems = ArrayList<Pair<AmmoType, Int>>()

        putItems
        .toSortedMap { o1, o2 -> return@toSortedMap if (o1.han < o2.han) -1 else 1 }
        .forEach { en ->
        val stack = en.value / en.key.byMaterial.max
        val fraction = en.value % en.key.byMaterial.max
        for (i in 1..stack)
        sortedItems.add(Pair(en.key, en.key.byMaterial.max))
        if (fraction != 0) sortedItems.add(Pair(en.key, fraction))
        }
         */

        return false
    }
}