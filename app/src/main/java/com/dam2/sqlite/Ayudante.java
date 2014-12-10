package com.dam2.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "futbol.sqlite";
    public static final int DATABASE_VERSION = 2;

    public Ayudante (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Contrato.TablaJugador.TABLA + " (" +
                Contrato.TablaJugador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contrato.TablaJugador.NOMBRE + " TEXT UNIQUE, " +
                Contrato.TablaJugador.TELEFONO + " TEXT, " +
                Contrato.TablaJugador.FNAC + " TEXT)";

        db.execSQL(query);

        query = "CREATE TABLE " + Contrato.TablaPartido.TABLA + " (" +
                Contrato.TablaPartido._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contrato.TablaPartido.IDJUGADOR + " INTEGER, " +
                Contrato.TablaPartido.VALORACION + " FLOAT, " +
                Contrato.TablaPartido.CONTRINCANTE + " TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 1 Tablas de respaldo para pasar datos
        String query = "CREATE TABLE RESPALDO" + Contrato.TablaJugador.TABLA + " (" +
                Contrato.TablaJugador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contrato.TablaJugador.NOMBRE + " TEXT UNIQUE, " +
                Contrato.TablaJugador.TELEFONO + " TEXT, " +
                Contrato.TablaPartido.VALORACION + " FLOAT, " +
                Contrato.TablaJugador.FNAC + " TEXT)";
        db.execSQL(query);



        // 2 - copiamos de las originales a la de respaldo
        query = "INSERT INTO RESPALDO" + Contrato.TablaJugador.TABLA + " SELECT * FROM " + Contrato.TablaJugador.TABLA;
        db.execSQL(query);

        // 3 delete
        query = "DROP TABLE " + Contrato.TablaJugador.TABLA;
        db.execSQL(query);

//***


        // 4 - Crear tablas nuevas
        onCreate(db);

        // 5 - Copiamos de las tablas de respaldo a las nuevas
        query = "INSERT INTO " + Contrato.TablaJugador.TABLA + " (" +
                Contrato.TablaJugador.NOMBRE + "," +
                Contrato.TablaJugador.TELEFONO + "," +
                Contrato.TablaJugador.FNAC + ") SELECT " +
                Contrato.TablaJugador.NOMBRE + "," +
                Contrato.TablaJugador.TELEFONO + "," +
                Contrato.TablaJugador.FNAC + " FROM RESPALDO";
        db.execSQL(query);

        query = "INSERT INTO " + Contrato.TablaPartido.TABLA + " (" +
                Contrato.TablaPartido.VALORACION + "," +
                Contrato.TablaPartido.IDJUGADOR + ") SELECT " +
                Contrato.TablaPartido.VALORACION + "," +
                Contrato.TablaJugador._ID + " FROM RESPALDO R INNER JOIN " +
                Contrato.TablaJugador.TABLA + " J WHERE " +
                "R."+ Contrato.TablaJugador.NOMBRE + "=J." + Contrato.TablaJugador.NOMBRE +
                " AND R." + Contrato.TablaJugador.TELEFONO + "=J." + Contrato.TablaJugador.TELEFONO +
                " AND R." + Contrato.TablaJugador.FNAC + "=J." + Contrato.TablaJugador.FNAC;
        db.execSQL(query);

        query = "UPDATE " + Contrato.TablaPartido.TABLA + " SET " + Contrato.TablaPartido.CONTRINCANTE + "=''";
        db.execSQL(query);

        // 6 Por ultimo borramos la tablas de respaldo
        query = "DROP TABLE RESPALDO";
        db.execSQL(query);
    }
    // 7...
}