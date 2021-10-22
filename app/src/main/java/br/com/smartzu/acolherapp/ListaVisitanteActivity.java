package br.com.smartzu.acolherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaVisitanteActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerVisitantes;
    private AdapterVisitantes adapterVisitantes;
    private List<Visitante> visitantes = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_view);

        //Configurações iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();


        //Configura recyclerview
        recyclerVisitantes.setLayoutManager(new LinearLayoutManager(this));
        recyclerVisitantes.setHasFixedSize(true);
        adapterVisitantes = new AdapterVisitantes(visitantes, this);
        recyclerVisitantes.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerVisitantes.setAdapter(adapterVisitantes);

        //Recupera visitantes da lista
        recuperarVisitantes();

        //Adiciona evento de clique no recyclerview
        recyclerVisitantes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerVisitantes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                vibrar();
                                Intent intent = new Intent(ListaVisitanteActivity.this, VisitanteSelecionadoActivity.class);
                                intent.putExtra("visitanteSelec", visitantes.get(position));
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Visitante visitanteSelecionado = visitantes.get(position);
                                DatabaseReference reference = firebaseRef;

                                String key = reference.child(visitanteSelecionado.getIdVisitante()).getKey();
                                String nome = visitanteSelecionado.getNomeVisitante();

                                new AlertDialog.Builder(ListaVisitanteActivity.this)
                                        .setTitle("Exclusão de visitante")
                                        .setMessage("Confirma exclusão de " + nome + "?")
                                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                reference.child(key).removeValue();
                                                Toast.makeText(getApplicationContext(), nome + " excluído(a)!", Toast.LENGTH_LONG).show();
                                            }
                                        }).setNegativeButton("NÃO", null).show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaVisitanteActivity.this, CadastrarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void recuperarVisitantes() {

        DatabaseReference produtosRef = firebaseRef;
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                visitantes.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    visitantes.add(ds.getValue(Visitante.class));
                }

                adapterVisitantes.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
        recyclerVisitantes = findViewById(R.id.recyclerView);
        progress = findViewById(R.id.progress_lista);
        progress.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(ListaVisitanteActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;//'30' é o tempo em milissegundos, é basicamente o tempo de duração da vibração. portanto, quanto maior este numero, mais tempo de vibração você irá ter
        rr.vibrate(milliseconds);
    }

}