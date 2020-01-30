package com.example.sync.Manager

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.*


private var waiting = false
private val tcpPort = 3333
private var wifi: WifiManager? = null

//同一Wi-fiに接続している全端末に対してブロードキャスト送信を行う
fun sendBroadcast(context: Context) {
    sample_WifiConnection(context)
    val sharedPreferences = context.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE);
    val udpPort = sharedPreferences.getInt("port", 8080)
    val myIpAddress: String = getIpAddress() ?: ""
    val sendJsonData = JSONObject()
    sendJsonData.put("ip", myIpAddress)
    sendJsonData.put("device", Build.BRAND + " " + Build.MODEL)
    val sendMsg = sendJsonData.toString()

    waiting = true
    Thread {
        var count = 0
        //送信回数を5回に制限する
        while (count < 5 && waiting) {
            try {
                val udpSocket = DatagramSocket(udpPort)
                udpSocket.broadcast = true  // broadcast ON
                val packet = DatagramPacket(
                    sendMsg.toByteArray(),
                    sendMsg.length,
                    getBroadcastAddress(),
                    udpPort
                )
                udpSocket.send(packet)
                udpSocket.close()
            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //5秒待って再送信を行う
            try {
                Thread.sleep(2000)
                count++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }.start()
}


/*コンストラクタ*/
fun sample_WifiConnection(context: Context) {
    wifi = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
}

//IPアドレスの取得
fun getIpAddress(): String? {
    val ipAddress_int = wifi!!.connectionInfo.ipAddress
    if (ipAddress_int == 0) {
        return null
    } else {
        Log.d("{ip-address}", "address: %s".format((ipAddress_int and 0xFF).toString() + "." + (ipAddress_int shr 8 and 0xFF) + "." + (ipAddress_int shr 16 and 0xFF) + "." + (ipAddress_int shr 24 and 0xFF)))
        return (ipAddress_int and 0xFF).toString() + "." + (ipAddress_int shr 8 and 0xFF) + "." + (ipAddress_int shr 16 and 0xFF) + "." + (ipAddress_int shr 24 and 0xFF)
    }
}

//ブロードキャストアドレスの取得
fun getBroadcastAddress(): InetAddress? {
    val dhcpInfo = wifi!!.dhcpInfo
    val broadcast = dhcpInfo.ipAddress and dhcpInfo.netmask or dhcpInfo.netmask.inv()
    val quads = ByteArray(4)
    for (i in 0..3) {
        quads[i] = (broadcast shr i * 8 and 0xFF).toByte()
    }
    return try {
        Log.d("{Broadcast}", "address: %s".format(InetAddress.getByAddress(quads).toString()))
        InetAddress.getByAddress(quads)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}




//ホストからTCPでIPアドレスが返ってきたときに受け取るメソッド
fun receivedHostIp(resolve: (JSONObject) -> Unit, reject: (e: Exception) -> Unit = {}) {
    Thread {
        try {
            val serverSocket = ServerSocket(tcpPort)
            serverSocket.soTimeout = 20000
            val connectedSocket = serverSocket.accept()
            val receivedReader = BufferedReader(InputStreamReader(connectedSocket.getInputStream()))
            val receivedJsonData = JSONObject(receivedReader.readLine())
            Log.d("receivedHostIp", "receive: %s".format(receivedJsonData.toString()))
            resolve(receivedJsonData)
            serverSocket.close()
            connectedSocket.close()

        } catch (e: SocketTimeoutException) {
            reject(e)
            e.printStackTrace()
        } catch (e: Exception) {
            reject(e)
            e.printStackTrace()
        } finally {
            waiting = false
        }
    }.start()
}
