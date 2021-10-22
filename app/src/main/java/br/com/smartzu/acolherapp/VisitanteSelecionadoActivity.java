package br.com.smartzu.acolherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class VisitanteSelecionadoActivity extends AppCompatActivity {

    TextView nome;
    TextView dataVisita;
    TextView ddd;
    TextView telefone;
    TextView sexo;
    TextView acompanhante;
    TextView recebeVisita;
    TextView email;
    ImageButton imageButtonEdit;
    ImageButton imageButtonDelete;
    ImageButton imageButtonZap;
    Visitante visitante;
    String id;

    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitante_selecionado);

        nome = findViewById(R.id.text_vnome);
        dataVisita = findViewById(R.id.text_dia_vvisita);
        ddd = findViewById(R.id.txt_vddd);
        telefone = findViewById(R.id.text_vtel);
        sexo = findViewById(R.id.txt_vsexo);
        acompanhante = findViewById(R.id.txt_vcompanhia);
        recebeVisita = findViewById(R.id.text_vrecebe_visita);
        email = findViewById(R.id.text_vemail);
        imageButtonEdit = findViewById(R.id.btn_edit_visitante);
        imageButtonDelete = findViewById(R.id.btn_delete_visitante);
        imageButtonZap = findViewById(R.id.img_btn_zap);

        visitante = (Visitante) getIntent().getSerializableExtra("visitanteSelec");

        firebaseRef = ConfiguracaoFirebase.getFirebase();

        nome.setText(visitante.getNomeVisitante());
        dataVisita.setText(visitante.getDataVisita());
        ddd.setText("DDD: " + visitante.getDddVisitante());
        telefone.setText("TEL: " + visitante.getTelVisitante());
        sexo.setText("SEXO: " + visitante.getSexoVisitante());
        acompanhante.setText("COMPANHIA: " + visitante.getCompanhiaVisitante());
        recebeVisita.setText("RECEBER VISITA: " + visitante.getReceberVisita());
        email.setText("EMAIL: " + visitante.getEmailVisitante());
    }

    public void enviaZap(View view) {
        vibrar();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,

                "*-------------------------------------*" +
                        "\n" + "*--- \uD83D\uDE4F AcolherApp IBCR \uD83D\uDE4F ---*" +
                        "\n" + "App de gestão de visitantes" +
                        "\n" + "*------------------------------------*" +
                        "\n" + "*" + nome.getText() + "*" +
                        "\n" + sexo.getText() +
                        "\n" + ddd.getText() + " - " + telefone.getText() +
                        "\n" + email.getText() +
                        "\n" + recebeVisita.getText() +
                        "\nQuando: " + dataVisita.getText() +
                        "\n" + acompanhante.getText() +
                        "\n*-------------------------------------*");

        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }

    public void editVisitante(View view) {
        vibrar();
        Intent intent = new Intent(this, CadastrarActivity.class);
        intent.putExtra("visitanteEdit", visitante);
        startActivity(intent);
    }

    public void deleteVisitante(View view) {
        vibrar();

        DatabaseReference reference = firebaseRef;
        id = reference.child(visitante.getIdVisitante()).getKey();

        new AlertDialog.Builder(this)
                .setTitle("Exclusão de visitante")
                .setMessage("Confirma exclusão de " + nome.getText() + "?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        reference.child(id).removeValue();
                        Toast.makeText(getApplicationContext(), nome.getText() + " excluído(a)!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(VisitanteSelecionadoActivity.this, ListaVisitanteActivity.class);
                        startActivity(intent);

                    }
                }).setNegativeButton("NÃO", null).show();


    }

    private void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;//'30' é o tempo em milissegundos, é basicamente o tempo de duração da vibração. portanto, quanto maior este numero, mais tempo de vibração você irá ter
        rr.vibrate(milliseconds);
    }
}
