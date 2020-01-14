package com.example.sync.Manager

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.*


class IpPortManager(val context: Context) {
    val sharedPref = context.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
    private var ip: String = sharedPref.getString("ip", "192.168.100.2") ?: "192.168.100.2"
    private var port: Int = sharedPref.getInt("port", 8080)

    fun getIP(): String = ip

    fun getPort(): Int = port

    fun reloadAddressInfo() {
        this.ip = sharedPref.getString("ip", "192.168.100.2") ?: "192.168.100.2"
        this.port = sharedPref.getInt("port", 8080)
    }
}


fun runSocket(ip: String, port: Int, jsonObj: JSONObject) {
    try {
        val socket: DatagramSocket = DatagramSocket(port)
        val address = InetAddress.getByName(ip)

        Thread {
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
    } catch (e: Exception) {
        Log.e("on Socket", e.message)
    }


}