package gabriela.michelle.ticketsgaby

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtUsuario = findViewById<TextView>(R.id.txtLUsuario)
        val txtContrasena = findViewById<TextView>(R.id.txtLContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val btnIrRegistro = findViewById<Button>(R.id.btnIrRegistro)
        btnIrRegistro.setOnClickListener {
            val pantallaRegistro = Intent(this, Registrarse::class.java)
            startActivity(pantallaRegistro)
        }

        btnLogin.setOnClickListener {
            val activity_ticket = Intent(this, MainActivity::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexio()
                val VerUsuer =
                    objConexion?.prepareStatement("select * from usuariooo where nombre = ? AND contrasena = ?")!!
                VerUsuer.setString(1, txtUsuario.text.toString())
                VerUsuer.setString(2, txtContrasena.text.toString())

                //En este caso se va a ejecutar el comando en la base

                val usuarioVerdadero = VerUsuer.executeQuery()

                withContext(Dispatchers.Main) {
                    if (usuarioVerdadero.next()) {
                        startActivity(activity_ticket)

                    } else {
                        println("Error")
                    }


                }


            }


        }
    }
}