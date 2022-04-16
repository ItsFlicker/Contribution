package io.github.itsflicker.contribution

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.Coerce
import taboolib.expansion.createHelper

/**
 * @author wlys
 * @since 2022/4/16 13:19
 */
@CommandHeader(name = "contribution", aliases = ["cpp"],  description = "贡献值", permission = "contribution.access")
object ContributionCommand {

    @CommandBody(optional = true)
    val give = subCommand {
        dynamic("player") {
            suggestion<CommandSender> { _, _ ->
                onlinePlayers().map { it.name }
            }
            dynamic("amount") {
                restrict<CommandSender> { _, _, argument ->
                    Coerce.asInteger(argument).isPresent
                }
                execute<CommandSender> { _, content, argument ->
                    val player = Bukkit.getPlayer(content.argument(-1)) ?: return@execute
                    val amount = argument.toInt()
                    Contribution.ppAPI.give(player.uniqueId, amount)
                    Contribution.addContribution(amount)
                }
            }
        }
    }

    @CommandBody
    val main = mainCommand {
        createHelper()
    }
}