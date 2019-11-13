package com.example.sync

import org.json.JSONObject
import java.io.IOException
import java.net.*

fun runSocket(jsonObj: JSONObject) {
    Thread {
        var socket: DatagramSocket = DatagramSocket(8080)
        val address = InetAddress.getByName("192.168.43.148")
//        val socket = Socket()
//        val inetSocketAddress = InetSocketAddress("192.168.43.148", 8080)
        try {
//            // ソケットにデータ投入
//            socket.connect(inetSocketAddress)
//            val socketOS = socket.getOutputStream()
//            socketOS.write(jsonObj.toString().toByteArray())
//            socketOS.close()

            val packet = DatagramPacket(jsonObj.toString().toByteArray(), jsonObj.toString().toByteArray().size, address, 8080)
            socket.send(packet)
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }.start()
}