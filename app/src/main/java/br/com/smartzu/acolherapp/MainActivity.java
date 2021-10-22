package br.com.smartzu.acolherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    private ImageButton imgCadastrar;
    private ImageButton imgListar;
    private ImageButton imgSair;
    private ImageButton imgCompartilhar;
    private Toolbar toolbar;
    private TextView emailLogin;
    private TextView criarUser;
    private TextView enviarMensagem;
    private BroadcastReceiver broadcastReceiver;
    private String userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);
        setActionBar(toolbar);

        imgCadastrar = findViewById(R.id.img_btn_cadastrar);
        imgListar = findViewById(R.id.img_btn_listar);
        imgSair = findViewById(R.id.img_btn_exit_app);
        imgCompartilhar = findViewById(R.id.img_btn_compartilhar);
        emailLogin = findViewById(R.id.txt_login_email);
        criarUser = findViewById(R.id.txt_criar_usuario);
        enviarMensagem = findViewById(R.id.txt_enviar_mensagem);

        imgCadastrar.setOnClickListener(this);
        imgListar.setOnClickListener(this);
        imgSair.setOnClickListener(this);
        imgCompartilhar.setOnClickListener(this);
        criarUser.setOnClickListener(this);
        enviarMensagem.setOnClickListener(this);

        userLogado = usuario.getCurrentUser().getEmail();
        emailLogin.setText(userLogado);

        checkInternet();
        userAdmin();
    }

    // MÉTODO DE CHAMADA DO CLICK DOS BUTTON
    @Override
    public void onClick(View v) {

        if (v == imgCadastrar) {
            vibrar();
            Intent intent = new Intent(MainActivity.this, CadastrarActivity.class);
            startActivity(intent);

        } else if (v == imgListar) {
            vibrar();
            Intent intent = new Intent(MainActivity.this, ListaVisitanteActivity.class);
            startActivity(intent);


        } else if (v == imgSair) {
            vibrar();
            new AlertDialog.Builder(this)
                    .setTitle("Desconectando usuário")
                    .setMessage("Tem certeza que deseja desconectar?")
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_activity), "desconectando...", Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            usuario.signOut();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }

                    }).setNegativeButton("NÃO", null).show();


        } else if (v == imgCompartilhar) {
            vibrar();
            enviaZapApp();
            //Toast.makeText(this, "Compartilhando APP...", Toast.LENGTH_LONG).show();


        } else if (v == criarUser) {
            vibrar();
            Intent intent = new Intent(this, NovoUsuarioActivity.class);
            startActivity(intent);

        } else if (v == enviarMensagem) {
            vibrar();
            Intent intent = new Intent(this, Mensageria.class);
            startActivity(intent);

        } else {
            throw new RuntimeException("Erro de implementação da view em MainActivity");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuConfig:
                Toast.makeText(this, "Configurando...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuSincro:
                Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuAjuda:
                Toast.makeText(this, "Ajudando...", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;//'30' é o tempo em milissegundos, é basicamente o tempo de duração da vibração. portanto, quanto maior este numero, mais tempo de vibração você irá ter
        rr.vibrate(milliseconds);
    }


    //MÉTODO DE VEIRIFICAÇÃO DE INTERNET ATIVA
    private void checkInternet() {


        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                Snackbar snackbar;

                //Primeiro verifica se tem internet ativa
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    snackbar = Snackbar.make(findViewById(R.id.main_activity), "Conectado", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(context.getColor(R.color.alertOnSnackbar));
                    snackbar.show();

                } else {

                    snackbar = Snackbar.make(findViewById(R.id.main_activity), "sem conexão com a internet...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(context.getColor(R.color.alertOffSnackbar));
                    snackbar.show();
                }
            }
        };

        //register receiver
        registerReceiver(broadcastReceiver, intentFilter);

    }
    //FIM DO MÉTODO


    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        super.onDestroy();
    }

    private void userAdmin() {

        if (userLogado.equalsIgnoreCase("boakdev@gmail.com")) {
            criarUser.setEnabled(true);
            enviarMensagem.setEnabled(true);

        } else if (userLogado.equalsIgnoreCase("projetosocialrepartiropao@gmail.com")) {
            criarUser.setEnabled(true);
        } else {
            criarUser.setEnabled(false);
            enviarMensagem.setEnabled(false);
        }
    }

    public void enviaZapApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,

                "*------------------------------------------*" +
                        "\n" + "*--- \uD83D\uDE4F AcolherApp IBCR \uD83D\uDE4F ---*" +
                        "\n" + "aplicativo de gestão de visitantes" +
                        "\n" + "" +
                        "\n" + "https://play.google.com/store/apps/details?id=br.com.smartzu.acolherapp" +
                        "\n*------------------------------------------*");

        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        startActivity(intent);

    }
}
