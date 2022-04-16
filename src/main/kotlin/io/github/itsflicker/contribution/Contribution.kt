package io.github.itsflicker.contribution

import io.github.itsflicker.contribution.database.Databases
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import java.util.*

object Contribution : Plugin() {

    var contribution = 0
        private set

    val ppAPI: PlayerPointsAPI by lazy {
        PlayerPoints.getInstance().api
    }

    var bar = ""
        private set

    @Config
    lateinit var CONF: Configuration
        private set

    @ConfigNode
    lateinit var segmentation: List<Int>
        private set

    @ConfigNode
    lateinit var empty: String
        private set

    @ConfigNode
    lateinit var fill: List<String>
        private set

    @ConfigNode
    var scale = 10
        private set

    override fun onEnable() {
        ppAPI
        Databases.init()

        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        if (Databases.database.pull().getInt("month", 0) != month) {
            Databases.database.pull()["month"] = month
            Databases.database.pull()["contribution"] = 0
        }

        contribution = Databases.database.pull().getInt("contribution")
        bar = createBar()
    }

    fun addContribution(amount: Int) {
        contribution += amount
        Databases.database.pull()["contribution"] = contribution
        bar = createBar()
    }

    fun createBar(): String {
        val string = "$contribution/${segmentation.last()} "
        val segmentation = segmentation.map { it / scale }
        val contribution = contribution / scale
        return string + (1..segmentation.last()).joinToString("") {
            if (contribution >= it) {
                var current = ""
                for (i in 0 until segmentation.size - 1) {
                    if (it in segmentation[i]..segmentation[i + 1]) {
                        current = fill[i]
                        break
                    }
                }
                current
            } else {
                empty
            }
        }
    }
}