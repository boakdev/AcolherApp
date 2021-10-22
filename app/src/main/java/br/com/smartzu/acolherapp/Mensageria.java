package br.com.smartzu.acolherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import br.com.smartzu.acolherapp.api.NotificacaoService;
import br.com.smartzu.acolherapp.model.Notificacao;
import br.com.smartzu.acolherapp.model.NotificacaoDados;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mensageria extends AppCompatActivity {

    private Retrofit retrofit;
    private String baseUrl;
    private EditText edtTitulo;
    private EditText edtCorpo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensageria);

        edtTitulo = findViewById(R.id.edt_tituloNotif);
        edtCorpo = findViewById(R.id.edt_corpoNotif);

        baseUrl = "https://fcm.googleapis.com/fcm/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FirebaseMessaging.getInstance().subscribeToTopic("allUsers");
    }

    public void enviarNotificacao(View view) {

        String to = "/topics/allUsers";
        String usuario = "";

        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("projetosocialrepartiropao@gmail.com")) {
            usuario = "\n\nenviado por:\nprojetosocialrepartiropao@gmail.com";
        }

        Notificacao notificacao = new Notificacao(edtTitulo.getText().toString(), edtCorpo.getText().toString() + usuario);
        NotificacaoDados notificacaoDados = new NotificacaoDados(to, notificacao);

        NotificacaoService service = retrofit.create(NotificacaoService.class);
        Call<NotificacaoDados> call = service.salvarNotificacao(notificacaoDados);

        call.enqueue(new Callback<NotificacaoDados>() {
            @Override
            public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {


                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Enviado com suceso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificacaoDados> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro no envio... Mensagem não enviada!", Toast.LENGTH_SHORT).show();
            }
        });

        edtTitulo.setText("");
        edtCorpo.setText("");
    }
}
