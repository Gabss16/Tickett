package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexio():Connection? {

        try {
            val ip = "jdbc:oracle:thin@10.10.1.110:1521:xe"
            val usuario = "system"
            val contrasena = "desarrollo"

            val connection = DriverManager.getConnection(ip, usuario, contrasena)
            return connection
        } catch (e: Exception) {
            println("este es el Error: $e")
            return null
        }
    }
}