package antonio.ejemplos.agendacomercial.activitys;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import antonio.ejemplos.agendacomercial.Beans.Contactos;
import antonio.ejemplos.agendacomercial.R;
import antonio.ejemplos.agendacomercial.controlador.SQLControlador;

import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends ListActivity {// -EXTENDS DE LISTACTIVITY---MODIFICACION-1..UPV

	private ListView lista;// OBJETO LISTVIEW
	private SQLControlador dbConnection;//CONTIENE LAS CONEXIONES A BBDD (CREADA EN DBHELPER.CLASS) Y LOS M�TODOS INSERT, UPDATE, DELETE, BUSCAR....
	private ArrayList<Contactos> contactos;//COLECCION DE TIPO CONTACTOS (BEAN CON LA MISMA ESTRUTURA DE CAMPOS QUE LA BBDD)

	//ADAPTADORES....===========================================================
	ArrayAdapter<Contactos> adaptador;//Primer adaptador utilizado. EJEMPLO 1
	//private ContactosAdapter contactosAdapter;// Segundo adaptador utilizado.Ejemplo sin im�genes..
	//private ContactosAdapter_old contactosAdapter_Jarroba;// Ejemplo con im�genes.
	private ContactosAdapter_Imagenes contactosAdapter_imagenes;
	//FIN ADAPTADORES....======================================================
	
	
	//CONSTANTES PARA EL MODO FORMULARIO Y PARA LOS TIPOS DE LLAMADA.============================
	public static final String C_MODO = "modo";
	public static final int C_VISUALIZAR = 551;
	public static final int C_CREAR = 552;
	public static final int C_EDITAR = 553;
	public static final int C_ELIMINAR = 554;
	public static final int C_CALL = 555;
	//FIN CONSTANTES==============================================================================
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);//--MODIFICACION
		setContentView(R.layout.inicio);//Contiene un Listview y una caja d texto para la salida sin datos. UTILIZADO EN UPV

				
		lista = (ListView) findViewById(android.R.id.list);// -----MODIFICACION-2
		// lista = (ListView) findViewById(R.id.list);
		
		

		// Permite al ListView mostrar un men� contextual
		registerForContextMenu(this.getListView());// -----MODIFICACION-3 VER...
		// registerForContextMenu(lista);No funciona si la Activity no extends de ListActivity 

		
//==============RESUMEN DE LOS DISTINTOS ADAPTADORES QUE SE HAN PROBADO===========================
			
//==============FIN RESUMEN DE LOS DISTINTOS ADAPTADORES QUE SE HAN PROBADO=======================================
		
		
		
		consultar();
		lista.setTextFilterEnabled(true);
		
		// Seleccionamos un elemento de la lista y lo vemos completo en otra Activity
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView adapterView, View view,int posicion, long id) {// int posicion, long id

				// Solo hace falta si llamamos al m�todo que devuelve una Colleccion
				
				try {

					dbConnection = new SQLControlador(getApplicationContext());
					dbConnection.abrirBaseDeDatos(1);// Lectura. Solo para ver

					// Devuelve una Colecci�n.Problema en el orden de recogida
					// de los campos
					// Se cambia por otro m�todo que devuelve un cursor...
					Cursor c = dbConnection.CursorBuscarUno(id);// Devuelve un Cursor

					int _id= c.getInt(c.getColumnIndex("_id"));
					
					
					String nombre = c.getString(c.getColumnIndex("Nombre"));
					String apellidos = c.getString(c
							.getColumnIndex("Apellidos"));
					String direccion = c.getString(c
							.getColumnIndex("Direccion"));
					String telefono = c.getString(c.getColumnIndex("Telefono"));
					String email = c.getString(c.getColumnIndex("Email"));
					
					int categoria=c.getInt(c.getColumnIndex("Id_Categoria"));
					String observ = c.getString(c.getColumnIndex("Observaciones"));

					// txtCaja2.setText("" + posicion + " posicion");

					dbConnection.cerrar();

					// Pasamos datos al formulario en modo visualizar
					Intent i = new Intent(MainActivity.this,ModificarUsuarios.class);
					i.putExtra("Nombre", nombre);
					i.putExtra("Apellidos", apellidos);
					i.putExtra("Direccion", direccion);
					i.putExtra("Telefono", telefono);
					i.putExtra("Email", email);

					i.putExtra("Id_Categoria", categoria);
					i.putExtra("Observaciones", observ);
					
					i.putExtra(C_MODO, C_VISUALIZAR);
					startActivityForResult(i, C_VISUALIZAR);

					// startActivity(i);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
	}
	@Override
	protected void onStart() {
    	/*
    	 * Nos indica que la actividad est� a punto de ser mostrada al usuario.*/
    	   super.onStart();
    	   //Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    	   
    	 }

    	 

    	@Override
		protected void onResume() {
    		/*
    		 Se llama cuando la actividad va a comenzar a interactuar con el usuario. 
    		 Es un buen lugar para lanzar las animaciones y la m�sica.
    		 * 
    		 * */
    		
    		super.onResume();
    	   
    	   //mp.start();
    	   
    		//Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    	 }

    	 

    	@Override
		protected void onPause() {
    	/*
    	 * Indica que la actividad est� a punto de ser lanzada a segundo plano, normalmente porque otra actividad es lanzada. 
    	 * Es el lugar adecuado para detener animaciones, m�sica o almacenar los datos que estaban en edici�n.
    	 * 
    	 * */
    		//mp.stop();
    		
    		//Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    		super.onPause();
    	 }

    	 

    	@Override
		protected void onStop() {
    		/*
    		 *  La actividad ya no va a ser visible para el usuario. Ojo si hay muy poca memoria, 
    		 *  es posible que la actividad se destruya sin llamar a este m�todo
    		 * */
    		super.onStop();
    		//Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show(); 
    	 }

    	 

    	@Override
		protected void onRestart() {
    		/*
    		 *  Indica que la actividad va a volver a ser representada despu�s de haber pasado por onStop().*/
    		
    	   super.onRestart();
    	   //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    	   
    	   consultar();
    	   
    	 }

    	 

    	@Override
		protected void onDestroy() {
    		/*
    		 * Se llama antes de que la actividad sea totalmente destruida. Por ejemplo, cuando el usuario pulsa 
    		 * el bot�n <volver> o cuando se llama al m�todo finish(). Ojo si hay muy poca memoria, es posible 
    		 * que la actividad se destruya sin llamar a este m�todo.
    		 * 
    		 * */
    		
    		super.onDestroy();
    		//Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();     
    	 }

    
    
	private void visualizar(long id) throws SQLException {
		dbConnection = new SQLControlador(getApplicationContext());
		dbConnection.abrirBaseDeDatos(1);// Lectura. Solo para ver

		Cursor c = dbConnection.CursorBuscarUno(id);// Devuelve un Cursor

		// int _id= c.getInt(c.getColumnIndex("_id"));
		String nombre = c.getString(c.getColumnIndex("Nombre"));
		String apellidos = c.getString(c.getColumnIndex("Apellidos"));
		String direccion = c.getString(c.getColumnIndex("Direccion"));
		String telefono = c.getString(c.getColumnIndex("Telefono"));
		String email = c.getString(c.getColumnIndex("Email"));
		
		int categoria=c.getInt(c.getColumnIndex("Id_Categoria"));
		String observ = c.getString(c.getColumnIndex("Observaciones"));
		
		// txtCaja2.setText("" + posicion + " posicion");
		dbConnection.cerrar();

		// Pasamos datos al formulario en modo visualizar
		Intent i = new Intent(MainActivity.this, ModificarUsuarios.class);
		i.putExtra("Nombre", nombre);
		i.putExtra("Apellidos", apellidos);
		i.putExtra("Direccion", direccion);
		i.putExtra("Telefono", telefono);
		i.putExtra("Email", email);
		
		i.putExtra("Id_Categoria", categoria);
		i.putExtra("Observaciones", observ);
		
		i.putExtra(C_MODO, C_VISUALIZAR);
		startActivityForResult(i, C_VISUALIZAR);


		//startActivity(i);

	}
	
	
	private void call(long id) throws SQLException {
		dbConnection = new SQLControlador(getApplicationContext());
		dbConnection.abrirBaseDeDatos(1);// Lectura. Solo para ver

		Cursor c = dbConnection.CursorBuscarUno(id);// Devuelve un Cursor

		// int _id= c.getInt(c.getColumnIndex("_id"));
		String nombre = c.getString(c.getColumnIndex("Nombre"));
		String apellidos = c.getString(c.getColumnIndex("Apellidos"));
		String direccion = c.getString(c.getColumnIndex("Direccion"));
		String telefono = c.getString(c.getColumnIndex("Telefono"));
		String email = c.getString(c.getColumnIndex("Email"));

		// txtCaja2.setText("" + posicion + " posicion");
		dbConnection.cerrar();

		
		//ACTION_DIAL NO necesita permisos en el Manifiest. Marca y sale ventana de confirmaci�n...
		//Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + telefono));
		
		//ACTION_CALL necesita permisos en el Manifiest. Marca directamente...
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono));
			startActivity(intent);
		
	}
	

	private void borrar(final long id) {
		/*
		 * Borramos el registro y refrescamos la lista
		 */
		AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

		dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
		dialogEliminar.setTitle(getResources().getString(
				R.string.agenda_eliminar_titulo));
		dialogEliminar.setMessage(getResources().getString(
				R.string.agenda_eliminar_mensaje));
		dialogEliminar.setCancelable(false);

		dialogEliminar.setPositiveButton(
				getResources().getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int boton) {
						// dbAdapter.delete(id);

						dbConnection = new SQLControlador(
								getApplicationContext());
						try {
							dbConnection.abrirBaseDeDatos(2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}// Escritura. Borrar

						dbConnection.delete(id);

						Toast.makeText(MainActivity.this,
								R.string.agenda_eliminar_confirmacion,
								Toast.LENGTH_SHORT).show();
						dbConnection.cerrar();	
						consultar();
					}
				});

		dialogEliminar.setNegativeButton(android.R.string.no, null);

		dialogEliminar.show();
	}
	
	
	private void borrarTodos() {
		/*
		 * Borramos el registro y refrescamos la lista
		 */
		AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

		dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
		dialogEliminar.setTitle(getResources().getString(
				R.string.agenda_eliminar_todos_titulo));
		dialogEliminar.setMessage(getResources().getString(
				R.string.agenda_eliminar_todos_mensaje));
		dialogEliminar.setCancelable(false);

		dialogEliminar.setPositiveButton(
				getResources().getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int boton) {
						// dbAdapter.delete(id);

						dbConnection = new SQLControlador(
								getApplicationContext());
						try {
							dbConnection.abrirBaseDeDatos(2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}// Escritura. Borrar

						dbConnection.borrarTodos();

						Toast.makeText(MainActivity.this,
								R.string.agenda_eliminar_todos_confirmacion,
								Toast.LENGTH_SHORT).show();
						dbConnection.cerrar();	
						consultar();
					}
				});

		dialogEliminar.setNegativeButton(android.R.string.no, null);

		dialogEliminar.show();
	}

	private void consultar()  {
		

		dbConnection = new SQLControlador(getApplicationContext());

		try {

			// Lectura. Solo para ver
			dbConnection.abrirBaseDeDatos(1);//Modo lectura
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// llamamos a BuscarTodos() que devuelve un arraylist de contactos...
		contactos = dbConnection.BuscarTodos();
		contactosAdapter_imagenes = new ContactosAdapter_Imagenes(this,contactos);
		
		//int total=contactos.size();

		lista.setAdapter(contactosAdapter_imagenes);
		dbConnection.cerrar();
	}

	private void editar(long id) throws SQLException {
		dbConnection = new SQLControlador(getApplicationContext());
		dbConnection.abrirBaseDeDatos(1);// Lectura. Solo para ver

		Cursor c = dbConnection.CursorBuscarUno(id);// Devuelve un Cursor

		int idenviado= c.getInt(c.getColumnIndex("_id"));
		String nombre = c.getString(c.getColumnIndex("Nombre"));
		String apellidos = c.getString(c.getColumnIndex("Apellidos"));
		String direccion = c.getString(c.getColumnIndex("Direccion"));
		String telefono = c.getString(c.getColumnIndex("Telefono"));
		String email = c.getString(c.getColumnIndex("Email"));
		
		int Id_Categ=c.getInt(c.getColumnIndex("Id_Categoria"));
		String observ = c.getString(c.getColumnIndex("Observaciones"));

		// txtCaja2.setText("" + posicion + " posicion");
		dbConnection.cerrar();

		// Pasamos datos al formulario en modo visualizar
		Intent i = new Intent(MainActivity.this, ModificarUsuarios.class);
		i.putExtra("_id", idenviado);
		i.putExtra("Nombre", nombre);
		i.putExtra("Apellidos", apellidos);
		i.putExtra("Direccion", direccion);
		i.putExtra("Telefono", telefono);
		i.putExtra("Email", email);

		i.putExtra("Id_Categoria", Id_Categ);
		i.putExtra("Observaciones", observ);
		
		i.putExtra(C_MODO, C_EDITAR);
		startActivityForResult(i, C_EDITAR);
		//finish();
		//consultar();Se va a llamar desde Onrestart();
		
		
	}
	
	public void importarDatos(){
	
		
		
		
	}
	
	
	// Men� contextual....
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// menu.setHeaderTitle(hipotecasAdapter.getItem(((AdapterContextMenuInfo)
		// menuInfo).position).getNombre());

		// menu.setHeaderTitle(contactos.getItem(((AdapterContextMenuInfo)
		// menuInfo).position).getNombre());//--MODIFICACION 6=

		menu.setHeaderTitle(contactosAdapter_imagenes.getItem(
				((AdapterContextMenuInfo) menuInfo).position).getNombre());
		menu.add(Menu.NONE, C_VISUALIZAR, Menu.NONE, R.string.menu_visualizar);
		menu.add(Menu.NONE, C_EDITAR, Menu.NONE, R.string.menu_editar);
		menu.add(Menu.NONE, C_ELIMINAR, Menu.NONE, R.string.menu_eliminar);
		
		menu.add(Menu.NONE, C_CALL, Menu.NONE, R.string.menu_llamar);
		
		
	}

	// Men� contextual....
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent i;

		switch (item.getItemId()) {
		
		case C_ELIMINAR:
			borrar(info.id);
			return true;

		case C_VISUALIZAR:
			try {
				visualizar(info.id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		case C_EDITAR:
			try {
				editar(info.id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
			
		case C_CALL:
			try {
				call(info.id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;	
			
		}
		return super.onContextItemSelected(item);
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_crear) {

			Intent i = new Intent(this, AltaUsuarios.class);
			 startActivityForResult(i,C_CREAR);
			 
//			 setResult(RESULT_OK);
//				finish();
//			//startActivity(i);
			 //consultar();No hace nada. Se va a llamar desde Onrestart():
			 //finish();
			return true;
		}
		
		if (id == R.id.menu_traer_contactos) {
		//MIGRAR DATOS DE LA AGENDA DE ANDROID=========================
			Intent i = new Intent(this, ImportarContactos.class);
			 //startActivityForResult(i,C_CREAR);
			 startActivity(i);
			 
//			 setResult(RESULT_OK);
//				finish();
//			//startActivity(i);
			 //consultar();No hace nada. Se va a llamar desde Onrestart():
			 //finish();
			return true;
		}
		
		
		if (id == R.id.menu_borrar_todos) {
			//MIGRAR DATOS DE LA AGENDA DE ANDROID=========================
//				Intent i = new Intent(this, ImportarContactos.class);
//				 //startActivityForResult(i,C_CREAR);
//				 startActivity(i);
				 
//				 setResult(RESULT_OK);
//					finish();
//				//startActivity(i);
				 //consultar();No hace nada. Se va a llamar desde Onrestart():
				 //finish();
			borrarTodos();
				return true;
			}
		
		
		
		
//		if (id == R.id.menu_editar) {
//
//			Intent i = new Intent(this, ModificarUsuarios.class);
//		startActivityForResult(i, C_EDITAR);
		//consultar();No hace nada. Se va a llamar desde Onrestart():
		
			//startActivity(i);
//		setResult(RESULT_OK);
//		finish();
		//finish();
			//return true;
		//}
		
		
		return super.onOptionsItemSelected(item);
	}

	// Men�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		
		getMenuInflater().inflate(R.menu.main, menu);
	
		return true;
	}

	// Men�
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i;

		switch (item.getItemId()) {
		case R.id.menu_crear:
			i = new Intent(MainActivity.this, AltaUsuarios.class);
			 i.putExtra(C_MODO, C_CREAR);
			 startActivityForResult(i, C_CREAR);
			 //consultar();No hace nada. Se va a llamar desde Onrestart():
			 
			//startActivity(i);
//			 setResult(RESULT_OK);
			//finish();
			return true;

			
		//case R.id.menu_editar:
			//i = new Intent(MainActivity.this, ModificarUsuarios.class);
			// i.putExtra(C_MODO, C_CREAR);
			// startActivityForResult(i, C_CREAR);
			//editar(info.id);
			
			//startActivity(i);
			//return true;	
			
			// case R.id.menu_preferencias:
			// i = new Intent(HipotecaActivity.this, Configuracion.class);
			// startActivityForResult(i, C_CONFIGURAR);
			// return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}



}
