package com.obsidianlabs.lxpocketbusiness.data.db

import androidx.room.withTransaction
import com.google.gson.Gson
import com.obsidianlabs.lxpocketbusiness.data.model.AppSnapshot
import com.obsidianlabs.lxpocketbusiness.data.model.CashEvent
import com.obsidianlabs.lxpocketbusiness.data.model.CatalogueItem
import com.obsidianlabs.lxpocketbusiness.data.model.Customer
import com.obsidianlabs.lxpocketbusiness.data.model.Debt
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.TaskAction

class RoomWorkspaceStore(private val database: WorkspaceDatabase) {
    private val gson = Gson()
    private val dao = database.workspaceDao()

    suspend fun read(): AppSnapshot? = database.withTransaction {
        val row = dao.workspace() ?: return@withTransaction null
        val metadata = requireNotNull(gson.fromJson(row.metadata, AppSnapshot::class.java))
        require(metadata.version == 1) { "Unsupported workspace version" }
        metadata.copy(
            customers = dao.customers().map { requireNotNull(gson.fromJson(it.payload, Customer::class.java)) },
            catalogueItems = dao.catalogue().map { requireNotNull(gson.fromJson(it.payload, CatalogueItem::class.java)) },
            documents = dao.documents().map { requireNotNull(gson.fromJson(it.payload, Document::class.java)) },
            debts = dao.debts().map { requireNotNull(gson.fromJson(it.payload, Debt::class.java)) },
            cashEvents = dao.cashEvents().map { requireNotNull(gson.fromJson(it.payload, CashEvent::class.java)) },
            tasks = dao.tasks().map { requireNotNull(gson.fromJson(it.payload, TaskAction::class.java)) }
        )
    }

    suspend fun write(snapshot: AppSnapshot) = database.withTransaction {
        val customerIds = snapshot.customers.map { it.id }.toSet()
        val documents = snapshot.documents.filter { it.customerId in customerIds }
        val documentIds = documents.map { it.id }.toSet()
        val debts = snapshot.debts.filter {
            it.customerId in customerIds && (it.invoiceId == null || it.invoiceId in documentIds)
        }
        val debtIds = debts.map { it.id }.toSet()
        val cashEvents = snapshot.cashEvents.map { event ->
            event.copy(
                customerId = event.customerId?.takeIf { it in customerIds },
                debtId = event.debtId?.takeIf { it in debtIds }
            )
        }

        dao.clearCashEvents()
        dao.clearDebts()
        dao.clearDocuments()
        dao.clearCustomers()
        dao.clearCatalogue()
        dao.clearTasks()
        dao.clearWorkspace()
        dao.insertWorkspace(
            WorkspaceRow(
                metadata = gson.toJson(
                    snapshot.copy(
                        customers = emptyList(),
                        catalogueItems = emptyList(),
                        documents = emptyList(),
                        debts = emptyList(),
                        cashEvents = emptyList(),
                        tasks = emptyList()
                    )
                )
            )
        )
        dao.insertCustomers(snapshot.customers.map { CustomerRow(it.id, gson.toJson(it)) })
        dao.insertCatalogue(snapshot.catalogueItems.map { CatalogueRow(it.id, gson.toJson(it)) })
        dao.insertDocuments(documents.map { DocumentRow(it.id, it.customerId, it.number.ifBlank { null }, gson.toJson(it)) })
        dao.insertDebts(debts.map { DebtRow(it.id, it.customerId, it.invoiceId, gson.toJson(it)) })
        dao.insertCashEvents(cashEvents.map {
            CashEventRow(it.id, it.customerId, it.debtId, it.civilDate, gson.toJson(it))
        })
        dao.insertTasks(snapshot.tasks.map { TaskRow(it.id, gson.toJson(it)) })
    }
}
