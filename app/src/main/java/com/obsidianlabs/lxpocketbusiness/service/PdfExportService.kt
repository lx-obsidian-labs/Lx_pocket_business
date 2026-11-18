package com.obsidianlabs.lxpocketbusiness.service

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentType
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

object PdfExportService {

    fun generateDocumentPdf(context: Context, document: Document, business: Business): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.DEFAULT
        }

        var y = 50f

        // Header Branding
        paint.color = Color.parseColor("#1B241B")
        canvas.drawRect(0f, 0f, 595f, 90f, paint)

        textPaint.color = Color.parseColor("#C7F400")
        textPaint.textSize = 22f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(business.name, 30f, 45f, textPaint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.DEFAULT
        val docTitle = when (document.type) {
            DocumentType.QUOTE -> "QUOTATION"
            DocumentType.INVOICE -> if (business.vatNumber.isNotEmpty()) "TAX INVOICE" else "INVOICE"
            DocumentType.CREDIT_NOTE -> "CREDIT NOTE"
        }
        canvas.drawText(docTitle, 30f, 68f, textPaint)

        textPaint.color = Color.WHITE
        textPaint.textSize = 14f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(document.number.ifEmpty { "DRAFT" }, 450f, 48f, textPaint)

        y = 120f
        textPaint.color = Color.BLACK

        // Issuer Info (Left)
        textPaint.textSize = 10f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("FROM:", 30f, y, textPaint)
        y += 15f
        textPaint.typeface = Typeface.DEFAULT
        canvas.drawText(business.name, 30f, y, textPaint)
        if (business.phone.isNotEmpty()) {
            y += 14f
            canvas.drawText("Tel: ${business.phone}", 30f, y, textPaint)
        }
        if (business.email.isNotEmpty()) {
            y += 14f
            canvas.drawText("Email: ${business.email}", 30f, y, textPaint)
        }
        if (business.vatNumber.isNotEmpty()) {
            y += 14f
            canvas.drawText("VAT Reg No: ${business.vatNumber}", 30f, y, textPaint)
        }

        // Recipient Info (Right)
        var rightY = 120f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("FOR / BILL TO:", 320f, rightY, textPaint)
        rightY += 15f
        textPaint.typeface = Typeface.DEFAULT
        canvas.drawText(document.customerName, 320f, rightY, textPaint)
        if (document.customerPhone.isNotEmpty()) {
            rightY += 14f
            canvas.drawText("Tel: ${document.customerPhone}", 320f, rightY, textPaint)
        }
        rightY += 14f
        canvas.drawText("Issue Date: ${document.issueDate}", 320f, rightY, textPaint)
        if (!document.dueDate.isNullOrEmpty()) {
            rightY += 14f
            canvas.drawText("Due Date: ${document.dueDate}", 320f, rightY, textPaint)
        }

        y = Math.max(y, rightY) + 30f

        // Table Header
        paint.color = Color.parseColor("#F1F5F1")
        canvas.drawRect(30f, y, 565f, y + 25f, paint)

        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textSize = 10f
        canvas.drawText("DESCRIPTION", 40f, y + 16f, textPaint)
        canvas.drawText("QTY", 320f, y + 16f, textPaint)
        canvas.drawText("UNIT PRICE", 390f, y + 16f, textPaint)
        canvas.drawText("AMOUNT (ZAR)", 470f, y + 16f, textPaint)

        y += 35f
        textPaint.typeface = Typeface.DEFAULT

        // Table Lines
        for (line in document.lines) {
            canvas.drawText(line.description, 40f, y, textPaint)
            canvas.drawText(line.quantity.toString(), 320f, y, textPaint)
            canvas.drawText("R${String.format(Locale.US, "%.2f", line.unitPriceMinor / 100.0)}", 390f, y, textPaint)
            canvas.drawText("R${String.format(Locale.US, "%.2f", line.grossMinor / 100.0)}", 470f, y, textPaint)

            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawLine(30f, y + 8f, 565f, y + 8f, paint)
            y += 24f
        }

        y += 20f

        // Totals Box
        paint.color = Color.parseColor("#F8FAFC")
        canvas.drawRect(350f, y, 565f, y + 70f, paint)

        textPaint.textSize = 10f
        canvas.drawText("Net Subtotal:", 360f, y + 20f, textPaint)
        canvas.drawText("R${String.format(Locale.US, "%.2f", document.totalNetMinor / 100.0)}", 470f, y + 20f, textPaint)

        canvas.drawText("VAT (15%):", 360f, y + 40f, textPaint)
        canvas.drawText("R${String.format(Locale.US, "%.2f", document.totalTaxMinor / 100.0)}", 470f, y + 40f, textPaint)

        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textSize = 11f
        canvas.drawText("TOTAL DUE:", 360f, y + 60f, textPaint)
        canvas.drawText(document.formattedTotalGross, 470f, y + 60f, textPaint)

        y += 100f

        // Payment Instructions & Terms
        textPaint.textSize = 9f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("PAYMENT INSTRUCTIONS:", 30f, y, textPaint)
        y += 14f
        textPaint.typeface = Typeface.DEFAULT
        business.paymentInstructions.split("\n").forEach { line ->
            canvas.drawText(line, 30f, y, textPaint)
            y += 12f
        }

        y += 10f
        textPaint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("TERMS & CONDITIONS:", 30f, y, textPaint)
        y += 14f
        textPaint.typeface = Typeface.DEFAULT
        canvas.drawText(business.defaultTerms, 30f, y, textPaint)

        // Footer branding
        textPaint.textSize = 8f
        textPaint.color = Color.GRAY
        canvas.drawText("Generated by LX Pocket Business — South African Entrepreneur Assistant", 150f, 810f, textPaint)

        pdfDocument.finishPage(page)

        // Save PDF file to private cache directory
        val fileName = "${document.type.name}_${document.number.ifEmpty { "DRAFT" }}_${System.currentTimeMillis()}.pdf"
        val pdfFile = File(context.cacheDir, fileName)
        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()
        return pdfFile
    }
}
