package com.obsidianlabs.lxpocketbusiness.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "workspace")
data class WorkspaceRow(@PrimaryKey val id: Int = 1, val metadata: String)

@Entity(tableName = "customers")
data class CustomerRow(@PrimaryKey val id: String, val payload: String)

@Entity(tableName = "catalogue")
data class CatalogueRow(@PrimaryKey val id: String, val payload: String)

@Entity(tableName = "documents", indices = [Index(value = ["number"], unique = true), Index("customerId")],
    foreignKeys = [ForeignKey(entity = CustomerRow::class, parentColumns = ["id"], childColumns = ["customerId"])])
data class DocumentRow(@PrimaryKey val id: String, val customerId: String, val number: String?, val payload: String)

@Entity(tableName = "debts", indices = [Index("customerId"), Index(value = ["invoiceId"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = CustomerRow::class, parentColumns = ["id"], childColumns = ["customerId"]),
        ForeignKey(entity = DocumentRow::class, parentColumns = ["id"], childColumns = ["invoiceId"])
    ])
data class DebtRow(@PrimaryKey val id: String, val customerId: String, val invoiceId: String?, val payload: String)

@Entity(tableName = "cash_events", indices = [Index("customerId"), Index("debtId"), Index("civilDate")],
    foreignKeys = [
        ForeignKey(entity = CustomerRow::class, parentColumns = ["id"], childColumns = ["customerId"]),
        ForeignKey(entity = DebtRow::class, parentColumns = ["id"], childColumns = ["debtId"])
    ])
data class CashEventRow(@PrimaryKey val id: String, val customerId: String?, val debtId: String?, val civilDate: String, val payload: String)

@Entity(tableName = "tasks")
data class TaskRow(@PrimaryKey val id: String, val payload: String)

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM workspace WHERE id = 1") suspend fun workspace(): WorkspaceRow?
    @Query("SELECT * FROM customers ORDER BY rowid") suspend fun customers(): List<CustomerRow>
    @Query("SELECT * FROM catalogue ORDER BY rowid") suspend fun catalogue(): List<CatalogueRow>
    @Query("SELECT * FROM documents ORDER BY rowid") suspend fun documents(): List<DocumentRow>
    @Query("SELECT * FROM debts ORDER BY rowid") suspend fun debts(): List<DebtRow>
    @Query("SELECT * FROM cash_events ORDER BY rowid") suspend fun cashEvents(): List<CashEventRow>
    @Query("SELECT * FROM tasks ORDER BY rowid") suspend fun tasks(): List<TaskRow>

    @Insert suspend fun insertWorkspace(row: WorkspaceRow)
    @Insert suspend fun insertCustomers(rows: List<CustomerRow>)
    @Insert suspend fun insertCatalogue(rows: List<CatalogueRow>)
    @Insert suspend fun insertDocuments(rows: List<DocumentRow>)
    @Insert suspend fun insertDebts(rows: List<DebtRow>)
    @Insert suspend fun insertCashEvents(rows: List<CashEventRow>)
    @Insert suspend fun insertTasks(rows: List<TaskRow>)

    @Query("DELETE FROM cash_events") suspend fun clearCashEvents()
    @Query("DELETE FROM debts") suspend fun clearDebts()
    @Query("DELETE FROM documents") suspend fun clearDocuments()
    @Query("DELETE FROM customers") suspend fun clearCustomers()
    @Query("DELETE FROM catalogue") suspend fun clearCatalogue()
    @Query("DELETE FROM tasks") suspend fun clearTasks()
    @Query("DELETE FROM workspace") suspend fun clearWorkspace()
}

@Database(entities = [WorkspaceRow::class, CustomerRow::class, CatalogueRow::class,
    DocumentRow::class, DebtRow::class, CashEventRow::class, TaskRow::class], version = 1, exportSchema = true)
abstract class WorkspaceDatabase : RoomDatabase() {
    abstract fun workspaceDao(): WorkspaceDao
}
