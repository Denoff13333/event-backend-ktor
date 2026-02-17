package com.example.plugins

import com.example.models.tables.Events
import com.example.models.tables.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val cfg = environment.config

    val hikari = HikariConfig().apply {
        driverClassName = cfg.property("db.driver").getString()
        jdbcUrl = cfg.property("db.jdbcUrl").getString()
        username = cfg.property("db.user").getString()
        password = cfg.property("db.password").getString()
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    val dataSource = HikariDataSource(hikari)
    Database.connect(dataSource)

    // Minimal schema setup
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users, Events)
    }
}
