package antonio.ejemplos.agendacomercial.Beans;

public class Contactos {
	
	
	/*
	 * Id_Categoria
	 * 1-Familia
	 * 2-Amigos
	 * 3-Compa�eros
	 * 4-Otros
	 * 
	 * */
	
	private long _id;
	private String Nombre;
	private String Apellidos;
	private String Direccion;
	private String Telefono;
	private String Email;
	private int Id_Categoria;
	private String Observaciones;
	public Contactos(long _id, String nombre, String apellidos,
					 String direccion, String telefono, String email, int id_Categoria,
					 String observaciones) {
		super();
		this._id = _id;
		Nombre = nombre;
		Apellidos = apellidos;
		Direccion = direccion;
		Telefono = telefono;
		Email = email;
		Id_Categoria = id_Categoria;
		Observaciones = observaciones;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getApellidos() {
		return Apellidos;
	}
	public void setApellidos(String apellidos) {
		Apellidos = apellidos;
	}
	public String getDireccion() {
		return Direccion;
	}
	public void setDireccion(String direccion) {
		Direccion = direccion;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public int getId_Categoria() {
		return Id_Categoria;
	}
	public void setId_Categoria(int id_Categoria) {
		Id_Categoria = id_Categoria;
	}
	public String getObservaciones() {
		return Observaciones;
	}
	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}
	public long get_id() {
		return _id;
	}
	
	
	
	
	
}
