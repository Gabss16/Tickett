package modelo

data class tbTickets(
    val UUIDticket: String,
    val numTicket: String,
    var titulo: String,
    val descripcion: String,
    val autor: String,
    val email: String,
    val estado: String,
    val fechaCreacion: String,
    val fechaFinalizacion: String
)
