package com.github.tanokun.efcammosort.command

import com.github.tanokun.efcammosort.Ammo
import com.github.tanokun.efcammosort.ammo
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

        val copy = HashMap<Ammo, ItemStack>()

        val items = shulker.inventory.filterNotNull().map {
            val name = it.itemMeta.displayName
                .replace("§e", "A_")
                .replace(".", "_")
                .replace("x", "X")
                .replace(" ", "_")
                .replace("-", "_")
                .uppercase()

            val ammo = ammo[name] ?: Ammo("null", 0, 0)
            copy[ammo] = it

            return@map Pair(ammo, it.amount)
        }

        val putItems = HashMap<Ammo, Int>()

        items.forEach {
            putItems[it.first] = (putItems[it.first] ?: 0) + it.second
        }

        val sortedItems = ArrayList<ItemStack>()

        putItems
            .toSortedMap { o1, o2 -> return@toSortedMap if (o1.handle < o2.handle) -1 else 1 }
            .forEach { en ->
                val stack = en.value / en.key.max
                val fraction = en.value % en.key.max
                for (i in 1..stack)
                    sortedItems.add(copy.getOrDefault(en.key, ItemStack(Material.AIR)).clone().apply { amount = en.key.max })
                if (fraction != 0) sortedItems.add(copy.getOrDefault(en.key, ItemStack(Material.AIR)).clone().apply { amount = fraction })
            }

        shulker.inventory.clear()
        deleteShulkerFIR(sortedItems)
        sortedItems.forEachIndexed { index, ammo -> shulker.inventory.setItem(index, ammo) }
        meta.blockState = shulker
        sender.inventory.itemInMainHand.itemMeta = meta

        sender.sendMessage("ソートが完了しました")

        return false
    }
}