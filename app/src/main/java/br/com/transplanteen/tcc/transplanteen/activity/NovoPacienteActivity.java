package br.com.transplanteen.tcc.transplanteen.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import dmax.dialog.SpotsDialog;

public class NovoPacienteActivity extends AppCompatActivity {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate;
    private EditText campoNomePaciente, campoDataNascimento, campoCpfPaciente, campoEmail, campoSenha;
    private Spinner campoTipoTransplante;
    private AlertDialog dialog;
    private Button botaoCadastrarPaciente;

    FirebaseAuth mAuth1, mAuth2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_paciente);

        inicializaComponentes();
        carregarDadosSpinner();

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NovoPacienteActivity.this,
                        android.R.style.Theme_Material_Dialog,
                        mDateSetListener,
                        ano, mes, dia);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String data = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(data);
            }
        };


        botaoCadastrarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarPaciente();
            }
        });


        //Configurações de Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Paciente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void inicializaComponentes() {

        mDisplayDate = findViewById(R.id.textViewDataNascimento);
        campoEmail = findViewById(R.id.editEmailPaciente);
        campoSenha = findViewById(R.id.editSenhaPaciente);
        campoCpfPaciente = findViewById(R.id.editCpfPaciente);
        campoNomePaciente = findViewById(R.id.editNomePaciente);
        campoTipoTransplante = findViewById(R.id.spinnerTipoTransplantePaciente);
        botaoCadastrarPaciente = findViewById(R.id.btnCadastrarPaciente);
    }


    private void carregarDadosSpinner() {
        String[] estados = getResources().getStringArray(R.array.tipo_transplante);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoTipoTransplante.setAdapter(adapter);
    }

    public void salvarPaciente() {

        //salva no banco de dados
        Paciente paciente = inicializaPaciente();
        if (paciente != null) {
            cadastrarPaciente(paciente);
        }
    }


    public void cadastrarPaciente(final Paciente paciente) {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Salvando paciente").setCancelable(false).build();

        mAuth1 = ConfiguracaoFirebase.getFirebaseAutenticacao();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://transplant-care.firebaseio.com/")
                .setApiKey("AIzaSyCn5WNKT31b6PhbkEv0lgQ8WJ-FkRwlrGM")
                .setApplicationId("1:918579338158:android:fb048201fcd7bfb1").build();


        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
            MessagesUtil.toastInfo(getApplicationContext(), e.toString());
        }

        mAuth2.createUserWithEmailAndPassword(
                paciente.getEmail(),
                paciente.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.show();
                if (task.isSuccessful()) {

                    String idPaciente = task.getResult().getUser().getUid();
                    System.out.println("ID DO USUÁRIO CRIADO AGORA: " + idPaciente);
                    mAuth2.signOut();

                    Usuario usuario = new Usuario();
                    //salva usuario
                    usuario.setId(idPaciente);
                    usuario.setEmail(paciente.getEmail());
                    usuario.setSenha(paciente.getSenha());
                    usuario.setTipo("P");
                    usuario.salvar();

                    //salva paciente
                    paciente.setId(idPaciente);
                    paciente.salvar();

                    //cria relacao de paciente com enfermeiro
                    paciente.salvarEnfermeiroPaciente();
                    dialog.dismiss();
                    MessagesUtil.toastInfo(getApplicationContext(), "Sucesso ao cadastrar usuário !");
                    finish();
                } else {
                    MessagesUtil.toastInfo(getApplicationContext(), task.getException().toString());
                    dialog.dismiss();
                }
            }
        });


    }


    public Paciente inicializaPaciente() {
        String cpfPaciente = campoCpfPaciente.getText().toString();
        String nomePaciente = campoNomePaciente.getText().toString();
        String tipoTransplantePaciente = campoTipoTransplante.getSelectedItem().toString();
        String dataNascimentoPaciente = mDisplayDate.getText().toString();
        String emailPaciente = campoEmail.getText().toString();
        String senhaPaciente = campoSenha.getText().toString();


        Paciente paciente = new Paciente();
        paciente.setCpf(cpfPaciente);
        paciente.setNome(nomePaciente);
        paciente.setTipoTransplante(tipoTransplantePaciente);
        paciente.setDataNascimento(DataUtil.stringParaTimestamp(dataNascimentoPaciente));
        paciente.setEmail(emailPaciente);
        paciente.setSenha(senhaPaciente);
        paciente.setStatus(false);

        if (validaPaciente(paciente)) {
            return paciente;
        }

        return null;
    }

    public boolean validaPaciente(Paciente paciente) {

        if (!paciente.getCpf().equals("")) {
            if (!paciente.getNome().equals("")) {
                if (!paciente.getTipoTransplante().equals("Tipo de transplante ...")) {
                    if (!paciente.getDataNascimento().equals("")) {
                        if (!paciente.getEmail().equals("")) {
                            if (!paciente.getSenha().equals("")) {
                                return true;
                            } else {
                                MessagesUtil.toastInfo(this, "Insira a senha");
                            }
                        } else {
                            MessagesUtil.toastInfo(this, "Insira o e-mail do paciente");
                        }
                    }

                    MessagesUtil.toastInfo(this, "Insira a data de nascimento");
                } else
                    MessagesUtil.toastInfo(this, "Insira o Tipo de Transplante");
            } else {
                MessagesUtil.toastInfo(this, "Insira o Nome");
            }
        } else {
            MessagesUtil.toastInfo(this, "Insira o CPF");
        }

        return false;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
