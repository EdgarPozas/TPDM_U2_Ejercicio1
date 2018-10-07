package com.mycompany.myapp;
import android.database.sqlite.*;
import android.content.*;

public class BaseDatos extends SQLiteOpenHelper
{
	public BaseDatos(Context c){
		super(c,"base",null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase p1)
	{
		String c1="create table rutina(id integer primary key,dias varchar(200),descripcion varchar(500),caloriasquemadas integer)";
		p1.execSQL(c1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3)
	{
		// TODO: Implement this method
	}
}
