package gabriela.michelle.ticketsgaby

import RecyclerView.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTickets
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // madar a llamar elementos
        val txtNum = findViewById<EditText>(R.id.txtNum)
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtAutor = findViewById<EditText>(R.id.txtautor)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val txtFechaC = findViewById<EditText>(R.id.txtFCreacion)
        val txtFechaF = findViewById<EditText>(R.id.txtFFinal)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        //Asignarle un layout al RecicleView
        rcvTickets.layoutManager = LinearLayoutManager(this)
//Funcion para mostar Datos

        fun obtenerDatos():List<tbTickets>{
            //creo un objeto d la clase conexion
            val objConexion = ClaseConexion().cadenaConexio()

            //creo uun statement
            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from tickett")!!
            val tickets = mutableListOf<tbTickets>()

            while (resulSet.next()){
                val UUIDticket= resulSet.getString("UUIDticket")
                val numTicket= resulSet.getString("numTicket")
                val titulo= resulSet.getString("titulo")
                val descripcion= resulSet.getString("descripcion")
                val autor= resulSet.getString("autor")
                val email= resulSet.getString("email")
                val estado= resulSet.getString("estado")
                val fechaCreacion= resulSet.getString("fechaCreacion")
                val fechaFinalizacion= resulSet.getString("fechaFinalizacion")

                val ticket = tbTickets(UUIDticket,numTicket,titulo,descripcion,autor,email, estado, fechaCreacion, fechaFinalizacion)

                tickets.add(ticket)
            }
            return tickets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticketsDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(ticketsDB)
                rcvTickets.adapter = adapter
            }
        }


        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //1-creo un objeto de la clse conexion
                val objConexion = ClaseConexion().cadenaConexio()

                val addTicket = objConexion?.prepareStatement("insert into tickett (UUIDticket, numTicket, titulo, descripcion, autor, email, estado, fechaCreacion, fechaFinalizacion) values (? ,?, ?, ?, ?, ?, ?, ?, ?)")!!
                addTicket.setString(1,UUID.randomUUID().toString())
                addTicket.setString(2,txtNum.text.toString())
                addTicket.setString(3,txtTitulo.text.toString())
                addTicket.setString(4,txtDescripcion.text.toString())
                addTicket.setString(5,txtAutor.text.toString())
                addTicket.setString(6,txtEmail.text.toString())
                addTicket.setString(7,txtEstado.text.toString())
                addTicket.setString(8,txtFechaC.text.toString())
                addTicket.setString(9,txtFechaF.text.toString())
                addTicket.executeUpdate()

                val nuevaMascota = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvTickets.adapter as? Adaptador)?.actualizarLista(nuevaMascota)
                }
            }




        }


    }
}