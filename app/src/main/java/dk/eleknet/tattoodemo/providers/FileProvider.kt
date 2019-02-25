package dk.eleknet.tattoodemo.providers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import timber.log.Timber
import java.io.*

class FileProvider(private val context: Context) : IFileProvider {

    override val offerDir: String
        get() = "${context.filesDir.path}/offers"
    override val invoiceDir: String
        get() = "${context.filesDir.path}/invoices"
    override val scanDir: String
        get() = "${context.filesDir.path}/scans"
    override val oldScanDir: String
        get() = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absolutePath.orEmpty()

    init {
        createDirectory(offerDir)
        createDirectory(invoiceDir)
        createDirectory(scanDir)
    }

    private fun createDirectory(path: String) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    override fun hasInvoicePdf(invoiceGuid: String): Boolean {
        return File(invoiceDir, "$invoiceGuid.pdf").exists()
    }

    override fun getInvoicePdf(invoiceGuid: String): File {
        return File(invoiceDir, "$invoiceGuid.pdf")
    }

    override fun hasOfferPdf(offerGuid: String): Boolean {
        return File(offerDir, "$offerGuid.pdf").exists()
    }

    override fun getOfferPdf(offerGuid: String): File {
        return File(offerDir, "$offerGuid.pdf")
    }

    override fun removeExtension(fileName: String): String {
        return fileName.replaceFirst("[.][^.]+$", "")
    }

    @Throws(IOException::class)
    override fun copyFileFromUri(uri: Uri, fileName: String): File {
        var inputStream: InputStream? = null
        val outputFile = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(outputFile)
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val buffer = ByteArray(inputStream.available())
                while (inputStream.read(buffer) != -1) {
                    outputStream.write(buffer)
                }
                outputStream.flush()
            } catch (e: Exception) {
                throw IOException("Error copying file")
            } finally {
                inputStream?.close()
                outputStream.close()
            }
        }
        return outputFile
    }

    override fun writeInputStreamToDisk(inputSteam: InputStream, file: File) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            do {
                len = inputSteam.read(buf)
                out.write(buf, 0, len)
            } while (len > 0)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // close steams
            out?.close()
            inputSteam.close()
        }
    }

    override fun writeBitmapToDisk(bitmap: Bitmap, file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }
        var fBitmapOutputStream: FileOutputStream? = null
        try {
            fBitmapOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fBitmapOutputStream)
            fBitmapOutputStream.flush()
        } catch (e: FileNotFoundException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        } finally {
            fBitmapOutputStream?.close()
        }
    }
}