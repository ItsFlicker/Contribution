package io.github.itsflicker.contribution.database

import taboolib.library.configuration.ConfigurationSection

/**
 * @author sky
 * @since 2020-08-14 14:38
 */
abstract class Database {

    abstract fun pull(): ConfigurationSection

    abstract fun push()

    abstract fun release()

}