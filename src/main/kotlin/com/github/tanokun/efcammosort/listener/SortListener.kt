package com.github.tanokun.efcammosort.listener

import com.github.tanokun.efcammosort.command.sort
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SortListener : Listener {
    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (player.inventory.itemInMainHand.type == Material.AIR) return
        if (!player.inventory.itemInMainHand.itemMeta.displayName.contains("アモケース")) return
        if (!player.isSneaking) return

        sort(e.player)
        player.sendMessage("ソートが完了しました")
    }
}