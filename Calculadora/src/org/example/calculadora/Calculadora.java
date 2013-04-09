package org.example.calculadora;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

public class Calculadora extends Activity {
	
	private EditText textoResultado;
	private String operando;
	
	private enum Operacion {
		SUMAR,RESTAR,MULTIPLICAR,DIVIDIR,RESULTADO;
	}
	
	private Operacion operacionSeleccionada;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculadora);
		
		textoResultado = (EditText) findViewById(R.id.textoResultado);
		initButtons();
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.btn0:
					textoResultado.setText(textoResultado.getEditableText() + "0");
					break;
				case R.id.btn1:
					textoResultado.setText(textoResultado.getEditableText() + "1");
					break;
				case R.id.btn2:
					textoResultado.setText(textoResultado.getEditableText() + "2");
					break;
				case R.id.btn3:
					textoResultado.setText(textoResultado.getEditableText() + "3");
					break;
				case R.id.btn4:
					textoResultado.setText(textoResultado.getEditableText() + "4");
					break;
				case R.id.btn5:
					textoResultado.setText(textoResultado.getEditableText() + "5");
					break;
				case R.id.btn6:
					textoResultado.setText(textoResultado.getEditableText() + "6");
					break;
				case R.id.btn7:
					textoResultado.setText(textoResultado.getEditableText() + "7");
					break;
				case R.id.btn8:
					textoResultado.setText(textoResultado.getEditableText() + "8");
					break;
				case R.id.btn9:
					textoResultado.setText(textoResultado.getEditableText() + "9");
					break;
				case R.id.btnPunto:
					String numero = textoResultado.getText().toString();
					if (numero.indexOf('.') == -1) {
						textoResultado.setText(textoResultado.getEditableText() + ".");	
					}
					
					break;
				case R.id.btnSigno:
					break;
					
					
				/* Operaciones */
				case R.id.btnMas:
					operacionSeleccionada = Operacion.SUMAR;
					operando = textoResultado.getText().toString();
					textoResultado.setText("");
					break;
				case R.id.btnDividir:
					operacionSeleccionada = Operacion.DIVIDIR;
					break;
				case R.id.btnMultiplicar:
					operacionSeleccionada = Operacion.MULTIPLICAR;
					break;
				case R.id.btnMenos:
					operacionSeleccionada = Operacion.RESTAR;
					break;
				case R.id.btnIgual:
					calcula(operacionSeleccionada);
					break;
				case R.id.btnBorrar:
					textoResultado.setText("");
					break;
				case R.id.btnParentesis:
					break;
				case R.id.btnBorrarCaracter:
					break;
					
				default:
					break;
					
					
					
			}
			
			textoResultado.setSelection(textoResultado.getText().length());
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculadora, menu);
		return true;
	}
	
	private void initButtons() {
		
		GridLayout grid = (GridLayout) findViewById(R.id.gridBotones);
		
		for(int i = 0;i<grid.getChildCount();i++) {
			Button b = (Button) grid.getChildAt(i);
			b.setOnClickListener(clickListener);
		}
	}
	
	
	private void calcula(Operacion operacion) {
		
		String resultado = "";
		
		String operando2 = textoResultado.getText().toString();
		
		//Convertir a double
		Double op1 = Double.valueOf(operando);
		Double op2 = Double.valueOf(operando2);
		
		switch(operacion) {
		case SUMAR: 
			Double calculado = op1 + op2;
			resultado = calculado.toString();
			break;
		
		}
		
		textoResultado.setText(resultado);
		
		
		
		
	}

}
