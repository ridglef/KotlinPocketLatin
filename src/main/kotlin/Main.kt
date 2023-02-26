package org.example

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.awt.Color
import java.awt.Dimension
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.swing.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        MainFrame()
    }

    fun MainFrame() {
        val frame = JFrame()
        val panel = JPanel()
        val field = JTextField("")
        field.preferredSize = Dimension(740, 20)
        val LTE = JButton("Translate Latin to English")
        val ETL = JButton("Translate English to Latin")
        val translation = JLabel("null")
        val Toggle = JButton("Toggle Theme")
        panel.add(field)
        panel.add(Toggle)
        panel.add(LTE)
        panel.add(ETL)
        panel.add(translation)
        frame.add(panel)
        frame.setSize(800, 200)
        frame.isResizable = true
        frame.isVisible = true
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        ETL.addActionListener {
            try {
                translation.text = translate(field.text, "etl")
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
            translation.update(translation.graphics)
        }
        LTE.addActionListener {
            try {
                translation.text = translate(field.text, "lte")
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
            translation.update(translation.graphics)
        }
        Toggle.addActionListener {
            if (m == true) {
                panel.background = Color(0, 0, 0)
                translation.foreground = Color(255, 255, 255)
                m = false
            } else {
                panel.background = Color(255, 255, 255)
                translation.foreground = Color(0, 0, 0)
                m = true
            }
        }
    }

    var m = true
    @Throws(Exception::class)
    fun translate(text: String, mode: String): String? {
        println("Translating $text using $mode")
        var response: String? = "fail"
        var url = ""
        if (mode == "etl") {
            var bruhl = text
            bruhl = bruhl.replace(" ", "%20")
            url = "https://bw-trans.vercel.app/la/$bruhl"
            response = ez(extractor(url), "text")
        }
        if (mode == "lte") {
            var bruh = text
            bruh = bruh.replace(" ", "%20")
            url = "https://bw-trans.vercel.app/en/$bruh"
            response = ez(extractor(url), "text")
        }
        return response
    }

    fun extractor(url: String?): String {
        return try {
            BufferedReader(InputStreamReader(URL(url).openStream())).readLine().toString()
        } catch (ignored: Exception) {
            println(url)
            "Failed to translate."
        }
    }

    fun ez(json: String?, key: String?): String? {
        val gson = Gson()
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        return jsonObject?.get(key)?.asString
    }
}