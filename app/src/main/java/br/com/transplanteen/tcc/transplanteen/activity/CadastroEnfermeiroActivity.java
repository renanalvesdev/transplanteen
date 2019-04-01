package br.com.transplanteen.tcc.transplanteen.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;

public class CadastroEnfermeiroActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editRepeteSenha;
    private TextView textNome, textNumInscricao, textTipoRegistro, textSituacao;
    private Button botaoCadastrarEnfermeiro;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro);

        //inicializa componentes
        inicializarComponentes();

        //Recuperando dados enviados
        Bundle dados = getIntent().getExtras();
        carregaDadosEnfermeiro(dados);

        //Configurações de Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Enfermeiro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Botao de cadastro
        botaoCadastrarEnfermeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              validarCadastroUsuario(v);
            }
        });

    }

    public void validarCadastroUsuario(View view) {

        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        String repeteSenha = editRepeteSenha.getText().toString();


        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                if (!repeteSenha.isEmpty()) {
                    if(senha.toLowerCase().equals(repeteSenha.toLowerCase())){
                        Usuario usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        usuario.setTipo("E");

                        cadastrarUsuario(usuario);
                    }

                    else
                        exibirMensagem("Senhas distintas");


                } else {
                    exibirMensagem("Confirme a senha");
                }
            } else {
                exibirMensagem("Digite uma senha");
            }
        } else {
            exibirMensagem("Digite seu email");
        }
    }


    public void cadastrarUsuario(final Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idUsuario = task.getResult().getUser().getUid();
                    Enfermeiro enfermeiro = new Enfermeiro();

                    //salva usuario
                    usuario.setId(idUsuario);
                    usuario.salvar();
                    enfermeiro.salvar();

                    abrirTelaEnfermeiro();

                    exibirMensagem("Sucesso ao cadastrar usuário !");
                }
            }
        });


    }

    private void abrirTelaEnfermeiro() {
        startActivity(new Intent(getApplicationContext(), EnfermeiroActivity.class));
    }

    private void carregaDadosEnfermeiro(Bundle dados) {
        String nome = dados.getString("nome");
        String numeroInscricao = dados.getString("inscricao");
        String tipoRegistro = dados.getString("tipoRegistro");
        String situacao = dados.getString("situacao");

        textNome.setText(nome);
        textNumInscricao.setText(numeroInscricao);
        textTipoRegistro.setText(tipoRegistro);
        textSituacao.setText(situacao);

    }

    private void inicializarComponentes() {

        textNumInscricao = findViewById(R.id.txtNumInscricao);
        textTipoRegistro = findViewById(R.id.txtTipoRegistro);
        textSituacao = findViewById(R.id.txtSituacao);
        textNome = findViewById(R.id.txtNome);
        botaoCadastrarEnfermeiro = findViewById(R.id.buttonCadastrarEnfermeiro);


        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        editRepeteSenha = findViewById(R.id.editRepitaNovaSenha);

    }



    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
