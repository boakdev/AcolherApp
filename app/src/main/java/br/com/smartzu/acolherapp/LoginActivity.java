package br.com.smartzu.acolherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLogin;
    private EditText edtSenha;
    private Button btnAcessar;
    private TextView txtLembraSenha;
    private ProgressBar progress;
    private BroadcastReceiver broadcastReceiver;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuarios = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = findViewById(R.id.edt_login);
        edtSenha = findViewById(R.id.edt_senha);
        btnAcessar = findViewById(R.id.btn_acessar);
        txtLembraSenha = findViewById(R.id.txt_lembra_senha);
        progress = findViewById(R.id.progress_login);

        progress.setVisibility(View.GONE);
        travaViews(false);

        btnAcessar.setOnClickListener(this);
        txtLembraSenha.setOnClickListener(this);

        if (usuarios.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        checkInternet();
    }


    @Override
    public void onClick(View v) {

        if (v == btnAcessar) {
            vibrar();

            if (edtLogin.getText().length() == 0) {
                edtLogin.setError("Login não pode estar vazio");

            } else if (edtSenha.getText().length() < 6) {
                edtSenha.setError("Senha deve ter no mínino 6 números");

            } else if (edtLogin.getText().length() > 0 && edtSenha.getText().length() >= 6) {

                progress.setVisibility(View.VISIBLE);
                travaViews(true);


                usuarios.signInWithEmailAndPassword(edtLogin.getText().toString(), edtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            progress.setVisibility(View.GONE);
                            travaViews(false);
                            startActivity(intent);

                        } else {
                            progress.setVisibility(View.GONE);
                            travaViews(false);
                            Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorreto(s)", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                progress.setVisibility(View.GONE);
                travaViews(false);
                Toast.makeText(LoginActivity.this, "Parâmetros de login inválidos", Toast.LENGTH_LONG).show();
            }


        } else if (v == txtLembraSenha) {

            if (edtLogin.getText().length() == 0) {
                edtLogin.setError("Login não pode estar vazio");
            } else {

                String emailAddress = edtLogin.getText().toString();
                usuarios.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Redefinição de senha enviado no email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Erro em redefinir senha por email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }


        } else {
            throw new RuntimeException("Erro de implementação de View na LoginActivity");
        }
    }

    private void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;
        rr.vibrate(milliseconds);
    }


    public void limparCampos() {
        edtLogin.setText("");
        edtSenha.setText("");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
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
                    snackbar = Snackbar.make(findViewById(R.id.loginActivity), "Conectado", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(context.getColor(R.color.alertOnSnackbar));
                    snackbar.show();
                    travaViews(false);

                } else {

                    snackbar = Snackbar.make(findViewById(R.id.loginActivity), "sem conexão com a internet...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(context.getColor(R.color.alertOffSnackbar));
                    snackbar.show();
                    travaViews(true);
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

    //MÉTODO DE DESABILITAÇÃO DE TODOS OS COMPONENTES DA VIEW
    private void travaViews(Boolean trava) {

        if (trava == true) {
            edtLogin.setEnabled(false);
            edtSenha.setEnabled(false);
            btnAcessar.setEnabled(false);
            txtLembraSenha.setEnabled(false);
            //txtCriarUser.setEnabled(false);

        } else if (trava == false) {
            edtLogin.setEnabled(true);
            edtSenha.setEnabled(true);
            btnAcessar.setEnabled(true);
            txtLembraSenha.setEnabled(true);
            //txtCriarUser.setEnabled(true);
        } else {
            Toast.makeText(this, "Erro no método TravaViews", Toast.LENGTH_SHORT).show();
        }
    }
}
