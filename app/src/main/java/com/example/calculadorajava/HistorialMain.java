package com.example.calculadorajava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HistorialMain extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistorialAdapter historialAdapter;
    private List<Calculo> calculosList; // Declaración de la lista de cálculos
    private FirebaseFirestore db;
    private Button btnVolver; // Declarar el botón

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_activity); // Asegúrate de que este es el layout correcto

        recyclerView = findViewById(R.id.recyclerViewHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista de cálculos
        calculosList = new ArrayList<>();

        historialAdapter = new HistorialAdapter(calculosList);
        recyclerView.setAdapter(historialAdapter);

        db = FirebaseFirestore.getInstance(); // Inicializar Firestore
        cargarHistorial(); // Cargar historial desde Firestore

        // Inicializar el botón de volver
        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a la actividad principal
                Intent intent = new Intent(HistorialMain.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });
    }

    private void cargarHistorial() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Consulta a Firestore para obtener el historial del usuario
            db.collection("historial")
                    .whereEqualTo("usuarioId", userId) // Filtrar por ID de usuario
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@NonNull QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                // Manejar el error
                                return;
                            }

                            calculosList.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (QueryDocumentSnapshot snapshot : value) {
                                Calculo calculo = snapshot.toObject(Calculo.class);
                                calculosList.add(calculo); // Agregar cada cálculo a la lista
                            }
                            historialAdapter.notifyDataSetChanged(); // Actualizar el RecyclerView
                        }
                    });
        }
    }
}
