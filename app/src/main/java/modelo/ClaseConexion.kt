package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexio():Connection? {

        try {
            val ip = "jdbc:oracle:thin@192.168.1.22:1522:xe"
            val usuario = "SYSTEM"
            val contrasena = "itr2024"

            val connection = DriverManager.getConnection(ip, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("este es el Error: $e")
            return null
        }
    }
}