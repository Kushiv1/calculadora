package com.example.calculadorajava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText num1, num2, palabra1, palabra2;
    private Spinner operadorSpinner;
    private TextView resultadoTextView;
    private Button calcularButton, historialButton;

    // Referencia a Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Inicializar Firestore

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
            return;
        }

        // Inicializar los elementos de la UI
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        palabra1 = findViewById(R.id.P1);
        palabra2 = findViewById(R.id.P2);
        operadorSpinner = findViewById(R.id.spinner1);
        resultadoTextView = findViewById(R.id.result);
        calcularButton = findViewById(R.id.button1);
        historialButton = findViewById(R.id.btnHistorial);

        // Poblar el Spinner con datos
        String[] operadores = {"+", "-", "*", "/"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operadores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operadorSpinner.setAdapter(adapter);

        // Configurar el botón de calcular
        calcularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarCalculo(currentUser.getUid()); // Pasar el ID del usuario
            }
        });

        // Configurar el botón de historial
        historialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistorialMain.class);
                startActivity(intent);
            }
        });
    }

    private void realizarCalculo(String userId) {
        // Obtener los valores de los campos de texto
        String strNum1 = num1.getText().toString();
        String strNum2 = num2.getText().toString();
        String palabra1Input = palabra1.getText().toString();
        String palabra2Input = palabra2.getText().toString();

        if (strNum1.isEmpty() || strNum2.isEmpty()) {
            Toast.makeText(this, "Introduce dos números", Toast.LENGTH_SHORT).show();
            return;
        }

        if (palabra1Input.isEmpty() || palabra2Input.isEmpty()) {
            Toast.makeText(this, "Introduce las palabras para la verificación", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de las palabras
        if (!validarPalabras(palabra1Input, palabra2Input)) {
            Toast.makeText(this, "Las palabras no son válidas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizar la operación matemática
        double numero1 = Double.parseDouble(strNum1);
        double numero2 = Double.parseDouble(strNum2);
        String operador = operadorSpinner.getSelectedItem().toString();
        double resultado = calcularOperacion(numero1, numero2, operador);

        // Mostrar el resultado
        resultadoTextView.setText("Resultado: " + resultado);

        // Guardar el cálculo en Firestore
        long timestamp = System.currentTimeMillis(); // Marca de tiempo actual
        Calculo calculo = new Calculo(numero1, numero2, operador, resultado, userId, timestamp);

        db.collection("historial")
                .add(calculo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "Cálculo guardado en el historial", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al guardar el cálculo", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validarPalabras(String palabra1, String palabra2) {
        return palabra1.length() == palabra2.length() && !palabra1.equals(palabra2);
    }

    private double calcularOperacion(double num1, double num2, String operador) {
        switch (operador) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    Toast.makeText(this, "No se puede dividir entre 0", Toast.LENGTH_SHORT).show();
                    return 0;
                }
            default:
                Toast.makeText(this, "Operador no válido", Toast.LENGTH_SHORT).show();
                return 0;
        }
    }
}
