package RecyclerView

import android.app.AlertDialog
import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import modelo.tbTickets
import androidx.recyclerview.widget.RecyclerView
import gabriela.michelle.ticketsgaby.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion


class Adaptador(var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<tbTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualizarPantalla (uuid: String, nuevoEstado: String){
        val index = Datos.indexOfFirst { it.UUIDticket == uuid }
        Datos[index].titulo= nuevoEstado
        notifyDataSetChanged()
    }

    fun actualizarDatos(nuevoTitulo: String, uuid:String){
        GlobalScope.launch(Dispatchers.IO){

            ///1 - creo un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexio()

            //2 - Creo una variable que tenga un prepareStatement
            val updateTicket = objConexion?.prepareStatement("Update Ticket set titulo = ? where UUID_Ticket = ?")!!
            updateTicket.setString(1, nuevoTitulo)
            updateTicket.setString(2, uuid)
            updateTicket.executeUpdate()
            withContext(Dispatchers.Main) {
                actualizarPantalla(uuid, nuevoTitulo)
            }


        }
    }

    fun EliminarDatos(estado: String, Posicion: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(Posicion)
        GlobalScope.launch(Dispatchers.IO) {
            // creamos un objeto de la clase conexion

            val objConexion = ClaseConexion().cadenaConexio()

            // 2- Crear una variable que contenga un preparestatement (donde se mete el código de sqlserver
            val deleteTicket =
                objConexion?.prepareStatement("delete from tickett where titulo = ?")!!
            deleteTicket.setString(1, estado)
            deleteTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        //Notificar el adaptador sobre los cambios
        notifyItemRemoved(Posicion)
        notifyDataSetChanged()


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtNombreCard.text = item.titulo

        holder.imgBorrar.setOnClickListener {

            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Elimnar")
            builder.setMessage("¿Está seguro que quiere eliminar la mascota?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                EliminarDatos(item.titulo, position)
            }
            builder.setNegativeButton("no") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar")
            builder.setMessage("Estas seguro que quieres editar?")

//Agregar un cuadro de texto para que el usuario escriba el nuevo nombre.

            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(item.titulo)
            builder.setView(cuadroTexto)

            builder.setPositiveButton("Actualizar ") { dialog, which ->
                actualizarDatos(cuadroTexto.text.toString(), item.UUIDticket)
            }
            builder.setNegativeButton("no") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }
    }

