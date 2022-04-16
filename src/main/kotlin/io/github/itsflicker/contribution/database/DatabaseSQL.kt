package io.github.itsflicker.contribution.database

import io.github.itsflicker.contribution.Contribution
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost

class DatabaseSQL : Database() {

    val host = Contribution.CONF.getHost("Database.SQL")

    val table = Table(Contribution.CONF.getString("Database.SQL.table")!!, host) {
        add { id() }
        add {
            name("data")
            type(ColumnTypeSQL.VARCHAR, 128)
        }
    }

    val dataSource = host.createDataSource()
    var cache: Configuration? = null

    init {
        table.workspace(dataSource) { createTable(true) }.run()
    }

    override fun pull(): ConfigurationSection {
        if (cache == null) {
            cache = table.select(dataSource) {
                limit(1)
            }.firstOrNull {
                Configuration.loadFromString(getString("data"))
            } ?: Configuration.empty(Type.YAML)
        }
        return cache!!
    }

    override fun push() {
        val file = cache ?: return
        if (table.select(dataSource) { limit(1) }.find()) {
            table.workspace(dataSource) {
                update {
                    set("data", file.saveToString())
                }
            }.run()
        } else {
            table.workspace(dataSource) {
                insert("id", "data") {
                    value(1, file.saveToString())
                }
            }.run()
        }
    }

    override fun release() {
        cache = null
    }
}