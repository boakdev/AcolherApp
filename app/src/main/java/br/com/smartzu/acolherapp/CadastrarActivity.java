package br.com.smartzu.acolherapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class CadastrarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ExitDialog.ExitListener, CadastroDialog.CadastroListener {


    private EditText nomeVisitante;
    private EditText telVisitante;
    private EditText emailVisitante;
    private RadioGroup radioGroupGenero;
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4;
    private Spinner spinerDDD;
    private Button btnCadastrar, btnLimpar;
    private Switch swtVisita;
    private ProgressBar progress;
    private RadioButton radio1, radio2;
    private TextView diaVisita;

    private String dataActivity;
    private Visitante visitanteEdit;

    private Calendar c;
    private DatePickerDialog dpd;
    private DatabaseReference reference = ConfiguracaoFirebase.getFirebase();

    Visitante visitante = new Visitante();
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        nomeVisitante = findViewById(R.id.edtext_nome);
        telVisitante = findViewById(R.id.edtext_telefone);
        emailVisitante = findViewById(R.id.edtext_email);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox4 = findViewById(R.id.checkbox4);
        spinerDDD = findViewById(R.id.meuddd);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        btnLimpar = findViewById(R.id.btn_limpar);
        progress = findViewById(R.id.progress_cadastrar);
        swtVisita = findViewById(R.id.swt_visita);
        diaVisita = findViewById(R.id.txt_dia);
        spinerDDD.setOnItemSelectedListener(this);

        progress.setVisibility(View.GONE);
        visitante.setSexoVisitante("");
        swtVisita.setChecked(false);

        visitante.setReceberVisita("Não");
        visitante.setDataVisita("sem data registrada");

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_ddd, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerDDD.setAdapter(adapter);

        visitanteEdit = (Visitante) getIntent().getSerializableExtra("visitanteEdit");

        if (visitanteEdit != null) {
            visitante.setIdVisitante(visitanteEdit.getIdVisitante());
            nomeVisitante.setText(visitanteEdit.getNomeVisitante());
            if (visitanteEdit.getSexoVisitante().equalsIgnoreCase("Masculino")) {
                radio1.setChecked(true);
            }

            btnCadastrar.setText("EDITAR ");
            btnLimpar.setText("CANCELAR");

            btnLimpar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CharSequence c = adapter.getItem(position);
        visitante.setDddVisitante(c.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //MÉTODO RADIOGROUP SEXO VISITANTE
    public void onRadioButtonClick(View view) {
        visitante.setSexoVisitante(((RadioButton) view).getText().toString());
    }

    //MÉTODO SELEÇÃO CHECKBOX
    public void onCheckBoxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.checkbox1:
                if (checked) {
                    //Toast.makeText(this, "Acompanhado do cônjuge", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Cônjude desmarcado", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.checkbox2:
                if (checked) {
                    //Toast.makeText(this, "Acompanhado do(s) pai(s)", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Pai(s) desmarcado", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.checkbox3:
                if (checked) {
                    //Toast.makeText(this, "Acompanhado do(s) filhos(s)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Filho(s) desmarcado", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.checkbox4:
                if (checked) {
                    //Toast.makeText(this, "Acompanhado de outro(s)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Outro(s) desmarcado", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }


    //MÉTODO DE SWITCH DE DATA
    public void swtVisita(View view) {

        if (swtVisita.isChecked()) {
            visitante.setReceberVisita("Sim");
            openDate();
        } else {
            visitante.setReceberVisita("Não");
            visitante.setDataVisita("sem data");
            diaVisita.setText("Dia da visita");
        }
    }

    // MÉTODO GATILHO BOTÃO CADASTRAR
    public void cadastrarV(View view) {
        vibrar();

        if (nomeVisitante.getText().length() < 3) {
            nomeVisitante.setError("Nome deve ter no mínimo 3 caracteres");
        } else if (telVisitante.getText().length() < 8) {
            telVisitante.setError("informe um telefone válido");
            Toast.makeText(this, "Obrigatório informar um telefone!", Toast.LENGTH_LONG).show();

        } else if (nomeVisitante.getText().length() >= 3 && telVisitante.getText().length() >= 8) {
            visitante.setNomeVisitante(nomeVisitante.getText().toString());
            visitante.setTelVisitante(telVisitante.getText().toString());
            visitante.setEmailVisitante(emailVisitante.getText().toString());
            validaCheckBox();
            envioCadastro();
        } else {
            Toast.makeText(this, "Parâmetros de cadastro inválidos!", Toast.LENGTH_LONG).show();
        }
    }


    //MÉTODO GATILHO BOTÃO LIMPAR
    public void limparV(View view) {
        vibrar();
        limparCampos();
    }


    //MÉTODO PARA LIMPAR CAMPOS DO CADASTRO
    public void limparCampos() {

        nomeVisitante.setText("");
        radio1.setChecked(false);
        radio2.setChecked(false);
        checkbox1.setChecked(false);
        checkbox2.setChecked(false);
        checkbox3.setChecked(false);
        checkbox4.setChecked(false);
        spinerDDD.setSelected(false);
        telVisitante.setText("");
        emailVisitante.setText("");
        swtVisita.setChecked(false);
        visitante.setCompanhiaVisitante("");
        diaVisita.setText("Dia da visita");
        visitante.setReceberVisita("Não");
        visitante.setDataVisita("sem data");
    }


    //DESABILITA VIEW PARA PROGRESS BAR
    private void travaViews() {
        nomeVisitante.setEnabled(false);
        radio1.setEnabled(false);
        radio2.setEnabled(false);
        checkbox1.setEnabled(false);
        checkbox2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        spinerDDD.setEnabled(false);
        telVisitante.setEnabled(false);
        emailVisitante.setEnabled(false);
        swtVisita.setEnabled(false);
        btnLimpar.setEnabled(false);
        btnCadastrar.setEnabled(false);
    }


    //HABILITA VIEW APÓS PROGRESS BAR
    private void liberaViews() {
        nomeVisitante.setEnabled(true);
        radio1.setEnabled(true);
        radio2.setEnabled(true);
        checkbox1.setEnabled(true);
        checkbox2.setEnabled(true);
        checkbox3.setEnabled(true);
        checkbox4.setEnabled(true);
        spinerDDD.setEnabled(true);
        telVisitante.setEnabled(true);
        emailVisitante.setEnabled(true);
        swtVisita.setEnabled(true);
        btnLimpar.setEnabled(true);
        btnCadastrar.setEnabled(true);
    }


    //PERSISTÊNCIA DOS DADOS COM PROGRESS BAR
    public void persistirDados() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cadastroObj();
                    SystemClock.sleep(3000);
                } catch (Exception e) {
                    Toast.makeText(CadastrarActivity.this, "Erro de persistêmcia: " + e, Toast.LENGTH_LONG).show();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        liberaViews();
                        Toast toast = Toast.makeText(CadastrarActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }).start();
    }


    public void envioCadastro() {

        travaViews();
        progress.setVisibility(View.VISIBLE);

        persistirDados();
        limparCampos();

        if (visitanteEdit != null) {
            Intent intent = new Intent(this, ListaVisitanteActivity.class);
            startActivity(intent);
        }
    }


    //MÉTODO DE ESCOLHA DE DATA
    public void openDate() {

        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(CadastrarActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int mAno, int mMes, int mDia) {
                String data = mDia + "/" + (mMes + 1) + "/" + mAno;
                diaVisita.setText(mDia + "/" + (mMes + 1) + "/" + mAno);
                visitante.setDataVisita(data);
            }
        }, year, month, day);

        dpd.show();
    }

    //MÉTODO DE ESCOLHA DE HORA
    public void openTime() {
        TimePicker timePicker = new TimePicker();
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onExit() {
        finish();
    }

    @Override
    public void onCadastro() {
        CadastroDialog cadastroDialog = new CadastroDialog();
        cadastroDialog.show(getSupportFragmentManager(), "cadastroDialog");
    }

    public void vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 30;
        rr.vibrate(milliseconds);
    }

    private void cadastroObj() {

        DatabaseReference cadVisitantes = reference;
        cadVisitantes.child(visitante.getIdVisitante()).setValue(visitante);
        visitante = new Visitante();
        visitante.setDddVisitante("DDD");
    }

    private void validaCheckBox() {

        String acompanhantes = "";

        if (checkbox1.isChecked()) {
            acompanhantes += " Cônjuge";
        }

        if (checkbox2.isChecked()) {
            acompanhantes += " Pai(s)";
        }

        if (checkbox3.isChecked()) {
            acompanhantes += " Filho(s)";
        }

        if (checkbox4.isChecked()) {
            acompanhantes += " Outro(s)";
        }

        visitante.setCompanhiaVisitante(acompanhantes);
    }

    @Override
    public void onBackPressed() {

        if (visitanteEdit != null) {
            super.onBackPressed();
        } else {
            finishAffinity();
            Intent intent = new Intent(CadastrarActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
