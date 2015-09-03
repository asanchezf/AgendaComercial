package antonio.ejemplos.agendacomercial.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import antonio.ejemplos.agendacomercial.Beans.Contactos;
import antonio.ejemplos.agendacomercial.modelo.DBhelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class SQLControlador {

	private DBhelper dbhelper;
	private Context ourcontext;
	private SQLiteDatabase db;

	// Definimos constante con el nombre de la tabla y constantes para los campos
	public static final String C_TABLA = "Contactos";
	public static final String C_COLUMNA_ID = "_id";
	public static final String C_COLUMNA_NOMBRE = "Nombre";
	public static final String C_COLUMNA_APELLIDOS = "Apellidos";
	public static final String C_COLUMNA_DIRECCION = "Direccion";
	public static final String C_COLUMNA_TELEFONO = "Telefono";
	public static final String C_COLUMNA_EMAIL = "Email";
	public static final String C_COLUMNA_CATEGORIA = "Id_Categoria";
	public static final String C_COLUMNA_OBSERVACIONES = "Observaciones";

	public static final String IMPORTADO_OBSERVACIONES = "Contacto importado desde la agenda de Android el día: ";
	
	public static final String EMAIL_POR_DEFECTO = "email@gmail.com";
	
	public SQLControlador(Context c) {
		super();
		this.ourcontext = c;
	}


    //ES UTILIZADO PARA CREAR UNA CONEXIÓN EN MODO LECTURA O ESCRITURA
	public SQLControlador abrirBaseDeDatos(int opcion) throws SQLException {

		dbhelper = new DBhelper(ourcontext);

		if (opcion == 1) {
			db = dbhelper.getReadableDatabase();// Abrimos en modo lectura
		} else if (opcion == 2) {
			db = dbhelper.getWritableDatabase();// Abrimos en modo escritura
		}
		return this;
	}


	public SQLControlador abrirBaseDeDatosStatic (int opcion) throws SQLException {

		dbhelper = new DBhelper(ourcontext);

		if (opcion == 1) {
			db = dbhelper.getReadableDatabase();// Abrimos en modo lectura
		} else if (opcion == 2) {
			db = dbhelper.getWritableDatabase();// Abrimos en modo escritura
		}
		return this;
	}



    //ES UTILIZADO PARA CERRAR LAS CONEXIONES CON BB.DD.
	public void cerrar() {
		dbhelper.close();
	}



//========================================ALTA=================
    //ES UTILIZADO PARA DAR DE ALTA LOS USUARIOS
	public void InsertarUsuario(String Nombre, String Apellidos,
			String Direccion, String Telefono, String Email, long Id_Categ,
			String observa) {

        //Utilizando el objeto SQLiteStatement. También se puede hacer el insert utilizando un ContentValues
        // Ya existe un objeto db en la clase... por eso no se pasa por par�metro...


		if(Email.toString().equals("")){
			Email="Email no disponible";
		}

		SQLiteStatement pst = db
				.compileStatement("INSERT INTO Contactos (Nombre, Apellidos, Direccion, Telefono, Email, Id_Categoria, Observaciones) VALUES (?,?,?,?,?,?,?)");
		pst.bindString(1, Nombre);
		pst.bindString(2, Apellidos);
		pst.bindString(3, Direccion);
		pst.bindString(4, Telefono);
		pst.bindString(5, Email);

		pst.bindLong(6, Id_Categ);
		pst.bindString(7, observa);
		pst.execute();

	}


	// ALTA USUARIOS UTILIZANDO CONTENTVALUES. NO ES UTILIZADO DESDE LA APP.PROBAR///////////////
	public void InsertContentAgenda(String Nombre, String Apellidos,
                                    String Direccion, String Telefono, String Email, long Id_Categ,
                                    String observa) {

		ContentValues valores = new ContentValues();

		valores.put(C_COLUMNA_NOMBRE, Nombre);
		 valores.put(C_COLUMNA_APELLIDOS, Apellidos);
		 valores.put(C_COLUMNA_DIRECCION, Direccion);
		valores.put(C_COLUMNA_TELEFONO, Telefono);
		valores.put(C_COLUMNA_EMAIL, Email);
		valores.put(C_COLUMNA_CATEGORIA, Id_Categ);
		valores.put(C_COLUMNA_OBSERVACIONES, observa);
		db.insert(C_TABLA, null, valores);

	}


//===============================MODIFICACIÓN=================
    // ES UTILIZADO PARA MODIFICAR LOS CONTACTOS
    public void ModificarContacto(int id, String Nombre, String Apellidos,
                                  String Direccion, String Telefono, String Email, int Id_Categ,
                                  String Observaciones) {

        // Ya existe un objeto db en la clase... por eso no se pasa por par�metro...

        ContentValues values = new ContentValues();
        // values.put(C_COLUMNA_ID,_id);//Es la Primary key...
        values.put(C_COLUMNA_NOMBRE, Nombre);
        values.put(C_COLUMNA_APELLIDOS, Apellidos);
        values.put(C_COLUMNA_DIRECCION, Direccion);
        values.put(C_COLUMNA_TELEFONO, Telefono);
        values.put(C_COLUMNA_EMAIL, Email);

        values.put(C_COLUMNA_CATEGORIA, Id_Categ);
        values.put(C_COLUMNA_OBSERVACIONES, Observaciones);

        // String where = "_id=?";//Cla�sula where
        String where = C_COLUMNA_ID + " = " + id;

        // String[] args = {"_id" };//Valores adicionales a la cla�sula where...

        Log.i(this.getClass().toString(), "_id" + "UPDATE_2" + id + "where "
                + where);
        // db.update(C_TABLA, values, "_id =" + id, null);
        db.update(C_TABLA, values, where, null);
        // db.update("Contactos", values, "_id=_id", null);
    }


//===================================BÚSQUEDA=================

    // Devuelve un ArrayList de Contactos con los datos que hay en BBDD
    //RELLENA EL LISTVIEW DEL MAIN
    public ArrayList BuscarTodos() {
        Contactos contactos;
        ArrayList<Contactos> arraList = new ArrayList<Contactos>();

        Cursor rs = db
                .rawQuery("Select * from Contactos order by nombre", null);

        if (rs.moveToFirst()) {
            do {

                contactos = new Contactos(rs.getInt(0), rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getString(7));

                arraList.add(contactos);

            } while (rs.moveToNext());

        }
        return arraList;

    }


//=============================BORRADO DE USUARIOS=======================================
    //ES UTILIZADO PARA ELIMINAR UN CONTACTO DE LA AGENDA
    public long delete(long id) {
        // if (db == null)
        // abrir();

        return db.delete(C_TABLA, "_id=" + id, null);
        // return id;
    }

    //ES UTILIZADO PARA ELIMINAR TODOS LOS CONTACTOS DE LA AGENDA
    public void borrarTodos() {
        //SQLiteDatabase db = getWritableDatabase();
        db.delete(C_TABLA, null, null);
        //db.close();
    }

//====================IMPORTAR CONTACTOS DE LA AGENDA ANDROID=================
    //ES UTILIZADO PARA IMPORTAR LOS CONTACTOS DE LA AGENDA DE ANDROID
	public void ImportCollectionContactsContent(ArrayList<Contactos> ArrayListcontactos, String fecha){

		//TRAE LOS CONTACTOS RECOGIDOS EN LA AGENDA DE ANDROID Y LA FECHA DE EJECUCIÓN.
        //COMPARA CON LOS QUE YA EXISTÍAN ANTERIORMENTE EN LA AGENDA E INSERTA LOS QUE NO HUBIERA ANTES Y EVITAMOS DUPLICADOS

		//Fecha de la actualizaci�n
		Contactos contactos;
		ArrayList<Contactos> arraListPorNombre = new ArrayList<Contactos>();
		
		
		ContentValues valores=new ContentValues();
		db.beginTransaction();

		//borrarTodos();
//		borrarImportados();
		
		
		//BORRA LOS QUE HUBIERA IMPORTADO ANTERIORMENTE PARA QUE NO HAYA REGISTROS DUPLICADO
		borrarImportados();
		
		//Busco por nombre y por categor�a 
//		for (int i = 0; i < arraListTodos.size(); i++) {
//			
//			arraListPorNombre=BuscarTodos();
//			
//			String nombre=ArrayListcontactos.get(i).getNombre();
//			
//		}
		
		//RECORRE EL ARRAYLIST DE CONTACTOS TRAIDOS DE LA AGENDA DE ANDROID
		for (int i = 0; i < ArrayListcontactos.size(); i++) {
			
			String nombre=ArrayListcontactos.get(i).getNombre();
			String telefono=ArrayListcontactos.get(i).getTelefono();
			String email=ArrayListcontactos.get(i).getEmail();
			int categoria=ArrayListcontactos.get(i).getId_Categoria();

            //COMPARA CON LOS QUE YA HABÍA EN LA AGENDA DE LA APP Y SOLO INSERTA LOS QUE SEAN DISTINTOS
			String nombrequeexiste=BuscarNombre(nombre);
			
			if(!nombrequeexiste.equals(nombre)){
			
			valores.put(C_COLUMNA_NOMBRE, nombre);
			valores.put(C_COLUMNA_TELEFONO, telefono);
			valores.put(C_COLUMNA_EMAIL, email);
			valores.put(C_COLUMNA_CATEGORIA, categoria);
			valores.put(C_COLUMNA_OBSERVACIONES, IMPORTADO_OBSERVACIONES+fecha);
			db.insert(C_TABLA, null, valores);
			}
			
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

    // ES UTULIZADO PARA LOCALIZAR EL nombre de un contacto al importar contactos de la agenda de android
    // Se utiliza para comparar y decidir si se inserta en la BB.DD, de la app o no para evitar así duplicados.
    public String BuscarNombre(String nombre) {
        //Contactos contactos;
        //ArrayList<Contactos> arraList = new ArrayList<Contactos>();
        // SQLiteDatabase db = this.getReadableDatabase();// Abrimos en modo
        // lectura.
        // Nombre, Apellidos, Direccion, Telefono, Email new String[] { nombre });

        String query="Select * from Contactos where Nombre= ?";
        String[] args=new String[] {nombre};
        Cursor rs = db.rawQuery(query, args);

//		Cursor rs = db
//				.rawQuery("Select Nombre from Contactos where Nombre= ?", new String[] {nombre});

        // Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" +
        // id, null, null, null, null, null);
        // Cursor rs = db.query(true, C_TABLA, arraList, C_COLUMNA_ID + "=" +
        // id, null, null, null, null, null);

        // Nos movemos al primer registro de la consulta
        if (rs != null) {
            //rs.moveToFirst();
            rs.moveToFirst();
        }


        if(rs.getCount()>0){
            return rs.getString(1);
        }
        else{
            return "";
        }

    }



    //SE UTILIZA AL IMPORTAR LOS CONTACTOS DE LA AGENDA DE ANDROID.
    //BORRA TODOS LOS ANTERIORMENTE IMPORTADOS ANTES DE VOLVER A IMPORTAR PARA QUE NO HAYA DUPLICADOS
    public void borrarImportados() {
        //SQLiteDatabase db = getWritableDatabase();
        String where= "Id_Categoria  = ?" ;
        String[] argumentos={"5"};
        db.delete(C_TABLA,where, argumentos);

        //db.close();
    }



//BÚSQUEDA DE USUARIO ANTES DE REALIZAR UN INTENT================================
	// ES UTULIZADO PARA LOCALIZAR EL USUARIO ANTES DE LLAMAR, WSP, EMAIL
	// Devuelve un cursor
	public Cursor CursorBuscarUno(long id) {

		Cursor rs = db
				.rawQuery(
						"Select _id, Nombre, Apellidos, Direccion, Telefono, Email, Id_Categoria, Observaciones from Contactos where _id="
								+ id, null);
		// Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" +
		// id, null, null, null, null, null);

		// Nos movemos al primer registro de la consulta
		if (rs != null) {
			rs.moveToFirst();
		}

		// rs.close();

		return rs;

	}




    /////////////////////////////////////////NO UTILIZADOS///////////////////////////////////////////////////

    //NO SE ESTÁ UTILIZANDO?????????????????????????????????????????????????????????????????????????
    public void ImportaCursorContent(Cursor nombres, Cursor telefonos,
                                     Cursor emails) {

        ContentValues valores = new ContentValues();
        db.beginTransaction();

        // NOMBRES
        while (nombres.moveToNext()) {
            valores.put(C_COLUMNA_NOMBRE, nombres.getInt(0));

            // TEL�FONOS
            while (telefonos.moveToNext()) {
                valores.put(C_COLUMNA_TELEFONO, telefonos.getInt(0));

            }
            telefonos.close();

            // EMAILS
            while (emails.moveToNext()) {

                valores.put(C_COLUMNA_EMAIL, emails.getInt(0));

            }
            emails.close();

        }

        db.insert(C_TABLA, null, valores);

        db.setTransactionSuccessful();
        db.endTransaction();
    }




    //NO SE ESTÁ UTILIZANDO??????????????????????????????
    public void borrarImportados(String nombreantiguo) {
        //SQLiteDatabase db = getWritableDatabase();
        String where= "Id_Categoria  = ? OR Nombre= ?" ;
        String[] argumentos={"5",nombreantiguo};
        db.delete(C_TABLA,where, argumentos);

        //db.close();
    }

    //NO ESTÁ SIENDO UTILIZADO.????????????????????????????????????????????????????
    public ArrayList BuscarPorNombre(String nombre) {
        Contactos contactos;
        ArrayList<Contactos> arraList = new ArrayList<Contactos>();
        // SQLiteDatabase db = this.getReadableDatabase();// Abrimos en modo
        // lectura.
        // Nombre, Apellidos, Direccion, Telefono, Email
        Cursor rs = db
                .rawQuery(
                        "Select Nombre, Apellidos, Direccion, Telefono, Email from Contactos where nombre="
                                + nombre, null);

        // Cursor c = db.query( true, C_TABLA, columnas, C_COLUMNA_ID + "=" +
        // id, null, null, null, null, null);
        // Cursor rs = db.query(true, C_TABLA, arraList, C_COLUMNA_ID + "=" +
        // id, null, null, null, null, null);

        // Nos movemos al primer registro de la consulta
        if (rs != null) {
            rs.moveToFirst();
        }

        return arraList;

    }


    // NO SE ESTÁ USANDO???????????????????????????????????????????????
    public void ImportaColeccionContent(ArrayList<String> nombres,
                                        ArrayList<String> telefonos, ArrayList<String> emails) {
        // public void ImportaColeccionContent(ArrayList<Contactos> datos) {

        // db.beginTransaction();
        // for (entry : listOfEntries)
        // { db.insert(entry); }
        // db.setTransactionSuccessful();
        // db.endTransaction();

        // private void insertTestData() { String sql =
        // "insert into producttable (name, description, price, stock_available) values (?, ?, ?, ?);";
        // dbHandler.getWritableDatabase(); database.beginTransaction();
        // SQLiteStatement stmt = database.compileStatement(sql); for (int i =
        // 0; i < NUMBER_OF_ROWS; i++) { //generate some values
        // stmt.bindString(1, randomName); stmt.bindString(2,
        // randomDescription); stmt.bindDouble(3, randomPrice); stmt.bindLong(4,
        // randomNumber); long entryID = stmt.executeInsert();
        // stmt.clearBindings(); } database.setTransactionSuccessful();
        // database.endTransaction(); dbHandler.close(); }




        ContentValues valores=new ContentValues();
        ContentValues valoresnombres = new ContentValues();
        ContentValues valorestelefonos = new ContentValues();
        ContentValues valoresemails = new ContentValues();

        Iterator iteradorNombres = nombres.iterator();
        Iterator iteradorTelefonos = telefonos.iterator();
        Iterator iteradorEmails = emails.iterator();

        db.beginTransaction();
        String telefono="";
        int contadornombres=0;
        int contadortelefonos=0;
        int contadoremails=0;


        while (iteradorNombres.hasNext()) {
            String nombre = (String) iteradorNombres.next();
            //valores.put(C_COLUMNA_NOMBRE, nombres.get(i));
            valores.put(C_COLUMNA_NOMBRE, nombre);
            //valores.put(C_COLUMNA_TELEFONO, telefono);
            //iteradorNombres.next();
            //db.insert(C_TABLA, null, valoresnombres);
            contadornombres++;


            // TEL�FONOS
            while (iteradorTelefonos.hasNext() && (contadortelefonos== contadornombres-1)) {
                //Integer j = (Integer) iteradorTelefonos.next();
                telefono = (String) iteradorTelefonos.next();
                //valores.put(C_COLUMNA_NOMBRE, nombre);
                //valores.put(C_COLUMNA_TELEFONO, telefono);
                //iteradorTelefonos.next();

                //db.insert(C_TABLA, null, valorestelefonos);
                contadortelefonos++;
            }//Fin tel�fonos



//				while (iteradorEmails.hasNext() && (contadoremails== contadortelefonos-1)) {
//					//Integer z = (Integer) iteradorEmails.next();
//					String email=(String) iteradorEmails.next();
//					valores.put(C_COLUMNA_EMAIL, email);
//					//iteradorEmails.next();
//					//db.insert(C_TABLA, null, valoresemails);
////					db.insert(C_TABLA, null, valores);//MAL trae emails
//					contadoremails++;
//				}

            valores.put(C_COLUMNA_NOMBRE, nombre);
            valores.put(C_COLUMNA_TELEFONO, telefono);
            valores.put(C_COLUMNA_EMAIL, EMAIL_POR_DEFECTO);
            //valores.put(C_COLUMNA_CATEGORIA, 4);
            valores.put(C_COLUMNA_OBSERVACIONES, IMPORTADO_OBSERVACIONES);
            db.insert(C_TABLA, null, valores);

        }//fin todos

        //==============
        //Solo me trae  el �ltimo contacto y el �ltimo correo y el tel�fono mal...!!!
        //db.insert(C_TABLA, null, valores);NONONONONONO
        //==============



        db.setTransactionSuccessful();
        db.endTransaction();


    }



    //NO ESTÁ SIENDO UTILIZADO???????????????????????????????????????????????????????????
    public ArrayList BuscarUno(long id) {
        Contactos contactos;
        ArrayList<Contactos> arraList = new ArrayList<Contactos>();
        // Nombre, Apellidos, Direccion, Telefono, Email
        Cursor rs = db
                .rawQuery(
                        "Select _id, Nombre, Apellidos, Direccion, Telefono, Email, Id_Categoria, Observaciones from Contactos where _id="
                                + id, null);

        // //Nos movemos al primer registro de la consulta
        // if (rs != null) {
        // rs.moveToFirst();
        // }

        // Preguntamos si el Cursor contiene alg�n dato
        if (rs.moveToFirst())
            // Si se puede mover a la primera posici�n ser� porque
            // contenga informaci�n.

            do {

                // Recogemos toda la informaci�n

                contactos = new Contactos(rs.getInt(0), rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getString(7));
                arraList.add(contactos);

                // int _id= rs.getInt(rs.getColumnIndex("_id"));
                // String nombre = rs.getString(rs.getColumnIndex("Nombre"));
                // String apellidos =
                // rs.getString(rs.getColumnIndex("Apellidos"));
                // String direccion =
                // rs.getString(rs.getColumnIndex("Direccion"));
                // String telefono =
                // rs.getString(rs.getColumnIndex("Telefono"));
                // String email = rs.getString(rs.getColumnIndex("Email"));
                //
                // arraList.add(new Contactos(_id,nombre,
                // apellidos,direccion,telefono,email));

            } while (rs.moveToNext());

        return arraList;
    }


    /////////////////////////////////////////NO UTILIZADOS/////////////////////////////


}
