package com.test.copynativebinaries

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.CharBuffer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFiles()
    }

    private fun listFiles() {
        val info = baseContext.applicationInfo
        this.doCommand("/system/bin/ls -al " + info.nativeLibraryDir)
    }

    private fun doCommand(command: String, envp: Array<String>? = null, dir: File? = null, wait: Boolean = false) {
        try {
            // Executes the command.
            val process = Runtime.getRuntime().exec(command, envp, dir)

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            val reader = BufferedReader(
                    InputStreamReader(process.inputStream))

            val buffer = CharArray(4096)
            val output = StringBuffer()
            var read = 0
            while ({read = reader.read(buffer); read}() > 0) {
                output.append(buffer, 0, read)
            }
            reader.close()

            if (wait) {
                // Waits for the command to finish.
                process.waitFor()
            }

            // Reads stderr.
            val readerErr = BufferedReader(InputStreamReader(process.errorStream))

            val error = StringBuffer()
            while ({read = readerErr.read(buffer); read}() > 0) {
                error.append(buffer, 0, read)
            }
            reader.close()

            // Send stdout and stderr to the log
            Log.d("Output", output.toString())
            Log.d("Error", error.toString())

        } catch (e: IOException) {
            throw RuntimeException(e);
        } catch (e: InterruptedException) {
            throw RuntimeException(e);
        }
    }
}