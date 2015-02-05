package com.izv.android.proyectopaintfinal;

import java.util.UUID;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {

	private Vista vista;
	private ImageButton colorActual, btBorrar, btNuevo, btGuardar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		vista = (Vista)findViewById(R.id.lienzo);

		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.colores);
		colorActual = (ImageButton)paintLayout.getChildAt(0);

		vista.settamPincel(10);

		btBorrar = (ImageButton)findViewById(R.id.btBorrar);
		btBorrar.setOnClickListener(this);

		btNuevo = (ImageButton)findViewById(R.id.btNuevo);
		btNuevo.setOnClickListener(this);

		btGuardar = (ImageButton)findViewById(R.id.btGuardar);
		btGuardar.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	public void colorSelect(View view){
		vista.setborrar(false);
		vista.settamPincel(10);

		if(view!=colorActual){
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			vista.setColor(color);
			colorActual=(ImageButton)view;
		}
	}

	@Override
	public void onClick(View view) {

        if (view.getId() == R.id.btNuevo) {
            AlertDialog.Builder nuevoDibu = new AlertDialog.Builder(this);
            nuevoDibu.setTitle("Nuevo dibujo");
            nuevoDibu.setMessage("Se perderán los cambios si no han sido guardados ¿Continuar?");
            nuevoDibu.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    vista.nuevo();
                    dialog.dismiss();
                }
            });
            nuevoDibu.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            nuevoDibu.show();
        } else if (view.getId() == R.id.btGuardar) {
            AlertDialog.Builder guardar = new AlertDialog.Builder(this);
            guardar.setTitle("Guardar");
            guardar.setMessage("¿Desea guardar los cambios?");
            guardar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    vista.setDrawingCacheEnabled(true);
                    MediaStore.Images.Media.insertImage(
                            getContentResolver(), vista.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "dibujo");

                    Toast.makeText(getApplicationContext(), "Se ha guardado correctamente.", Toast.LENGTH_SHORT).show();

                    vista.destroyDrawingCache();
                }
            });
            guardar.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            guardar.show();
        } else if (view.getId() == R.id.btBorrar) {
            vista.setborrar(true);
        }
    }

    public void selColor(View v){
        vista.cambiarColor();
    }
}
