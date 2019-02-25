package dk.eleknet.tattoodemo.providers

import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.InputStream

interface IFileProvider {

    val offerDir: String
    val invoiceDir: String
    val scanDir: String
    val oldScanDir: String // TODO should be removed eventually

    fun hasInvoicePdf(invoiceGuid: String): Boolean
    fun getInvoicePdf(invoiceGuid: String): File

    fun hasOfferPdf(offerGuid: String): Boolean
    fun getOfferPdf(offerGuid: String): File

    fun removeExtension(fileName: String): String

    fun copyFileFromUri(uri: Uri, fileName: String): File
    fun writeInputStreamToDisk(inputSteam: InputStream, file: File)
    fun writeBitmapToDisk(bitmap: Bitmap, file: File)
}