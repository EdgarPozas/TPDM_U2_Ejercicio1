package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.Toolbar.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import android.database.sqlite.*;
import android.database.*;
import android.widget.AdapterView.*;

public class MainActivity extends Activity 
{
	class Rutina{
		public int id,dia,calorias;
		public String des;
		
		public Rutina(int id,int d,int c,String de){
			this.id=id;
			dia=d;calorias=c;des=de;
		}
	}
	private ArrayList<Rutina> rutinas;
	private ListView ls;
	private BaseDatos bd;
	private EditText dia,cal,des;
	private Rutina rutina_selec;
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		System.out.println("-----------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		bd=new BaseDatos(this);
		rutinas=new ArrayList<Rutina>();
		
		ls=findViewById(R.id.lista);
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener(){  
			@Override
			public void onItemClick(AdapterView<?>adapter,View v, int position,long id){
				rutina_selec=rutinas.get(position);
				mostrar_opciones();
			}
		});
		actualizar_lista();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(R.id.agregar==item.getItemId()){
			
			AlertDialog.Builder alerta=new AlertDialog.Builder(this);
			View vista=getLayoutInflater().inflate(R.layout.agregar,null);
			dia=vista.findViewById(R.id.dias);
			cal=vista.findViewById(R.id.calorias);
			des=vista.findViewById(R.id.descripcion);
			alerta.setView(vista);
			alerta.setTitle("Agregar");
			alerta.setPositiveButton("Guardar",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface d,int id){
						String[] par={dia.getText().toString(),des.getText().toString(),cal.getText().toString()};

						String q="insert into rutina(dias,descripcion,caloriasquemadas) values('"+par[0]+"','"+par[1]+"',"+par[2]+")";

						if(guardar(q))
							mensaje("Guardado");
						else
							mensaje("No se pudo guardar");
						d.dismiss();
						actualizar_lista();
					}
				});

			alerta.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface d,int id){
						d.cancel();
					}
				});
			alerta.show();
		}
		return true;
	}
	private void mostrar_opciones(){
		AlertDialog.Builder alerta=new AlertDialog.Builder(this);
		View vista=getLayoutInflater().inflate(R.layout.agregar,null);
		dia=vista.findViewById(R.id.dias);
		cal=vista.findViewById(R.id.calorias);
		des=vista.findViewById(R.id.descripcion);
		alerta.setView(vista);
		alerta.setTitle("Actualizar/Eliminar");
		dia.setText(rutina_selec.dia+"");
		des.setText(rutina_selec.des);
		cal.setText(rutina_selec.calorias+"");
		alerta.setPositiveButton("Actualizar",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface d,int id){
				String[] par={dia.getText().toString(),des.getText().toString(),cal.getText().toString()};

				String q="update rutina set dias='"+par[0]+"',descripcion='"+par[1]+"',caloriasquemadas="+par[2]+" where id="+rutina_selec.id;

				if(guardar(q))
					mensaje("Actualizado");
				else
					mensaje("No se pudo actualizar");
					
				d.dismiss();
				actualizar_lista();
			}
		});
		
		alerta.setNeutralButton("Borrar",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface d,int id){
				String q="delete from rutina where id="+rutina_selec.id;

				if(guardar(q))
					mensaje("Eliminado");
				else
					mensaje("No se pudo eliminar");
					
				d.dismiss();
				actualizar_lista();
			}
		});
		
		alerta.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface d,int id){
				d.cancel();
			}
		});
		alerta.show();
	}
	
	private void mensaje(String cont){
		Toast.makeText(this,cont,Toast.LENGTH_SHORT).show();
	}
	
	private void actualizar_lista(){
		ls.setAdapter(null);
		ArrayList<String> arr=new ArrayList<String>();
		rutinas.clear();
		try{
			SQLiteDatabase db=bd.getReadableDatabase();
			Cursor c=db.rawQuery("select * from rutina",null);
			if(!c.moveToFirst()){
				mensaje("No hay registros");
				return;
			}
			
			do{
				Rutina r=new Rutina(c.getInt(0),c.getInt(1),c.getInt(3),c.getString(2));
				arr.add(r.id+","+r.dia +","+r.des+","+r.calorias);
				rutinas.add(r);
			}while(c.moveToNext());
			
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
			ls.setAdapter(adapter);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private boolean guardar(String q){
		try{
			SQLiteDatabase db=bd.getWritableDatabase();
			db.execSQL(q);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
}
