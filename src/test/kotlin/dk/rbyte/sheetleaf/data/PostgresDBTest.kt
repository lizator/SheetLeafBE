package dk.rbyte.sheetleaf.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PostgresDBTest {

    @Test
    fun initialize() {
        val db = PostgresDB()
        db.initialize()
        assert(db.isInitialized())
        db.close()
        assert(!db.isInitialized())
    }
}