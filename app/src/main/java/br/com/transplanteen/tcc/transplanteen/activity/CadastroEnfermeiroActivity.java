package br.com.transplanteen.tcc.transplanteen.activity;

import android.app.AlertDialog;
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
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import dmax.dialog.SpotsDialog;

public class CadastroEnfermeiroActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editRepeteSenha;
    private TextView textNome, textNumInscricao, textTipoRegistro, textSituacao;
    private Button botaoCadastrarEnfermeiro;
    private Usuario usuarioAtual = new Usuario();
    private FirebaseAuth autenticacao;
    private Enfermeiro enfermeiro = new Enfermeiro();
    private String idUsuario = ConfiguracaoFirebase.getIdUsuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro);

        //inicializa componentes
        inicializarComponentes();

        if(idUsuario != null ){
            carregaUsuarioAtual();
            somenteLeitura();
        }

        else{
            carregaDadosEnfermeiroApi();
            populaComponentes();
        }



       // populaComponentes();
        //Configurações de Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de Enfermeiro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Botao de cadastro
        botaoCadastrarEnfermeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarEnfermeiro();
            }
        });

    }

    public void somenteLeitura(){
        editEmail.setFocusable(false);
        editSenha.setVisibility(View.GONE);
        editRepeteSenha.setVisibility(View.GONE);
        botaoCadastrarEnfermeiro.setVisibility(View.GONE);

    }

    //carrega o usuario atual logado no firebase
    public void carregaUsuarioAtual(){
        ConfiguracaoFirebase.getUsuarioAtual(new SimpleCallback<Usuario>() {
            @Override
            public void callback(Usuario usuario) {
                if(usuario != null){
                    usuarioAtual.setNome(usuario.getNome());
                    usuarioAtual.setEmail(usuario.getEmail());
                    usuarioAtual.setTipo(usuario.getTipo());
                    carregaDadosEnfermeiroFirebase();
                };
            }
        });
    }

    public void carregaDadosEnfermeiroFirebase(){
        Enfermeiro.getEnfermeiro(new SimpleCallback<Enfermeiro>() {
            @Override
            public void callback(Enfermeiro enfermeiroRetorno) {
                if(enfermeiroRetorno != null){
                    enfermeiro.setNome(enfermeiroRetorno.getNome());
                    enfermeiro.setNumeroInscricao(enfermeiroRetorno.getNumeroInscricao());
                    enfermeiro.setTipoRegistro(enfermeiroRetorno.getTipoRegistro());
                    enfermeiro.setSituacaoInscricao(enfermeiroRetorno.getSituacaoInscricao());
                    populaComponentes();
                };
            }
        });
    }


    public void cadastrarEnfermeiro() {

        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        String nome = textNome.getText().toString();

        final Usuario usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setNome(nome);
        usuario.setTipo("E");

        if(validarCadastroUsuario(usuario)){
            final android.app.AlertDialog dialog = new SpotsDialog
                    .Builder()
                    .setContext(CadastroEnfermeiroActivity.this)
                    .setMessage("Salvando enfermeiro")
                    .setCancelable(false)
                    .build();

            dialog.show();
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.createUserWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String idUsuario = task.getResult().getUser().getUid();

                        //salva usuario
                        usuario.setId(idUsuario);
                        usuario.salvar();

                        Enfermeiro enfermeiro = new Enfermeiro();
                        enfermeiro.setId(idUsuario);
                        enfermeiro.setNome(textNome.getText().toString());
                        enfermeiro.setSituacaoInscricao(textSituacao.getText().toString());
                        enfermeiro.setNumeroInscricao(textNumInscricao.getText().toString());
                        enfermeiro.setTipoRegistro(textTipoRegistro.getText().toString());
                        enfermeiro.salvar();
                        dialog.dismiss();

                        finish();


                        exibirMensagem("Sucesso ao cadastrar enfermeiro !");
                    }

                    else{
                        dialog.dismiss();
                    }
                }

            });
        }



    }

    public boolean validarCadastroUsuario(Usuario usuario) {
        boolean retorno = false;
        String repeteSenha = editRepeteSenha.getText().toString();

        if (!usuario.getEmail().isEmpty()) {
            if (!usuario.getSenha().isEmpty()) {
                if (!repeteSenha.isEmpty()) {
                    if (usuario.getSenha().toLowerCase().equals(repeteSenha.toLowerCase())) {
                        retorno = true;
                    } else
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

        return retorno;
    }


    private void abrirTelaEnfermeiro() {
        startActivity(new Intent(getApplicationContext(), EnfermeiroActivity.class));
    }

    private void carregaDadosEnfermeiroApi() {

            Bundle dados = getIntent().getExtras();
            enfermeiro.setNome(dados.getString("nome"));
            enfermeiro.setNumeroInscricao(dados.getString("inscricao"));
            enfermeiro.setTipoRegistro(dados.getString("tipoRegistro"));
            enfermeiro.setSituacaoInscricao(dados.getString("situacao"));
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


    private void populaComponentes(){
        textNome.setText(enfermeiro.getNome());
        textNumInscricao.setText(enfermeiro.getNumeroInscricao());
        textTipoRegistro.setText(enfermeiro.getTipoRegistro());
        textSituacao.setText(enfermeiro.getSituacaoInscricao());
        editEmail.setText(usuarioAtual.getEmail());
        editSenha.setText(usuarioAtual.getSenha());
        editRepeteSenha.setText("");

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
