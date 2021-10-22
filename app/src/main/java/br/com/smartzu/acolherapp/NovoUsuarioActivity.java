package br.com.smartzu.acolherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NovoUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth usuarios = FirebaseAuth.getInstance();

    private EditText login;
    private EditText senha;
    private Button btnNovoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        login = findViewById(R.id.edt_login_new_user);
        senha = findViewById(R.id.edt_senha_new_user);
        btnNovoUser = findViewById(R.id.btn_criar_new_user);

        btnNovoUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // MÉTODO DE CRIAÇÃO DE USUÁRIO
        if (login.getText().length() == 0) {
            login.setError("Login não pode estar vazio");

        } else if (senha.getText().length() < 6) {
            senha.setError("Senha deve ter no mínino 6 números");

        } else if (login.getText().length() > 0 && senha.getText().length() >= 6) {

            usuarios.createUserWithEmailAndPassword(login.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NovoUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(NovoUsuarioActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(NovoUsuarioActivity.this, "ERRO ao cadastrar usuário", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Parâmetros de login inválidos", Toast.LENGTH_LONG).show();
        }
    }
}
