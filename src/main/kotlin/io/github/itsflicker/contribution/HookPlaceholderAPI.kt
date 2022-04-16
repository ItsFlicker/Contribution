package io.github.itsflicker.contribution

import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * @author wlys
 * @since 2022/4/16 13:48
 */
object HookPlaceholderAPI : PlaceholderExpansion{

    override val identifier: String
        get() = "contribution"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return when (args) {
            "amount" -> Contribution.contribution.toString()
            "bar" -> Contribution.bar
            else -> ""
        }
    }
}