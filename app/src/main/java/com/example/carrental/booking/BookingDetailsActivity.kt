package com.example.carrental.booking

import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.carrental.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val bookingId = intent.getStringExtra("bookingId") ?: ""
        val userId = intent.getIntExtra("userId", -1)
        val model = intent.getStringExtra("model") ?: ""
        val pickupDate = intent.getStringExtra("pickupDate") ?: ""
        val duration = intent.getStringExtra("duration")?.toInt() ?: 0
        val returnDate = intent.getStringExtra("returnDate") ?: ""
        val extra1 = intent.getStringExtra("extra1") ?: ""
        val extra2 = intent.getStringExtra("extra2") ?: ""


        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(pickupDate)!!
        calendar.add(Calendar.DAY_OF_YEAR, duration)


        val receiptText = findViewById<TextView>(R.id.receipt_text)
        receiptText.text = """
    Booking ID: $bookingId
    User ID: $userId
    Car Model: $model
    Pickup Date: $pickupDate
    Return Date: $returnDate
    Duration: $duration
    ${if (extra1.isNotEmpty()) "Insurance: $extra1\n" else ""}
    ${if (extra2.isNotEmpty()) "GPS: $extra2\n" else ""}
""".trimIndent()

        val downloadButton = findViewById<Button>(R.id.download_receipt_button)
        downloadButton.setOnClickListener {
            downloadReceipt(receiptText.text.toString(), bookingId)
        }
    }

    private fun downloadReceipt(receiptContent: String, bookingId: String) {
        val fileName = "Receipt_$bookingId.pdf"

        val paint = Paint()
        paint.textSize = 14f

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        canvas.drawText(receiptContent, 10f, 50f, paint)

        pdfDocument.finishPage(page)

        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        pdfDocument.close()

        val fileUri = FileProvider.getUriForFile(
            this,
            "com.example.carrental.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "application/pdf"
        }

        startActivity(Intent.createChooser(intent, "Share Receipt"))
    }
}
