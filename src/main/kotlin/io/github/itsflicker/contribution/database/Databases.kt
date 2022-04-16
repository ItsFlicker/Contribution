package io.github.itsflicker.contribution.database

import io.github.itsflicker.contribution.Contribution
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule

/**
 * @author wlys
 * @since 2022/4/16 18:43
 */
object Databases {

    lateinit var database: Database
        private set

    fun init() {
        database = when (val type = Contribution.CONF.getString("Database.Method")!!.uppercase()) {
            "SQLITE" -> DatabaseSQLite()
            "SQL" -> DatabaseSQL()
            else -> error("Unsupported database type: $type")
        }
    }

    @Schedule(delay = 100, period = 20 * 60 * 5, async = true)
    @Awake(LifeCycle.DISABLE)
    fun save() {
        database.push()
    }
}