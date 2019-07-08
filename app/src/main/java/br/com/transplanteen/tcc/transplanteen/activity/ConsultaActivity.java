package br.com.transplanteen.tcc.transplanteen.activity;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;
import br.com.transplanteen.tcc.transplanteen.model.Consulta;
import br.com.transplanteen.tcc.transplanteen.model.DadosConsulta;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import dmax.dialog.SpotsDialog;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class ConsultaActivity extends AppCompatActivity {


    private TextInputLayout campoAlturaConsulta, campoPesoConsulta, campoFrequenciaConsulta, campoPressaoConsulta;
    DatabaseReference ref;
    AlertDialog dialogProcessando;

    private Paciente paciente;
    private Consulta consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        ref = ConfiguracaoFirebase.getFirebaseDatabase();
        Bundle dados = getIntent().getExtras();
        paciente = (Paciente) dados.getSerializable("paciente");
        consulta = (Consulta) dados.getSerializable("consultaPaciente") ;

        inicializarComponentes();


        setSupportActionBar(criaToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public Toolbar criaToolbar(){

        String tituloToolbar = "";
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(paciente.getNome());
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);

        if(consulta == null)
            tituloToolbar = "Nova Consulta";
        else
            tituloToolbar = "Consulta " + "("+DataUtil.timestampParaData(consulta.getDataConsulta())+")";

        toolbar.setTitle(tituloToolbar);

        return toolbar;

    }


    public void inicializarComponentes() {

        dialogProcessando = new SpotsDialog
        .Builder()
        .setContext(this)
        .setMessage("Carregando")
        .setCancelable(false).build();

        campoAlturaConsulta = findViewById(R.id.editAlturaConsulta);
        campoPesoConsulta = findViewById(R.id.editPesoConsulta);
        campoFrequenciaConsulta = findViewById(R.id.editFrequenciaCardiacaConsulta);
        campoPressaoConsulta = findViewById(R.id.editPressaoConsulta);

        if(consulta != null){
            populaCampos(consulta.getDadosConsulta());
        }
    }

    public void populaCampos(DadosConsulta dadosConsulta){
        campoAlturaConsulta.getEditText().setText(dadosConsulta.getAltura());
        campoPesoConsulta.getEditText().setText(dadosConsulta.getPeso());
        campoFrequenciaConsulta.getEditText().setText(dadosConsulta.getFrequenciaCardiaca());
        campoPressaoConsulta.getEditText().setText(dadosConsulta.getPressao());
    }

    public void salvarConsulta() {
        if (validarCampos()) {

            final Consulta consulta = preparaConsulta();
            final String idEnfermeiro = ConfiguracaoFirebase.getIdUsuario();
            final String idPaciente = paciente.getId();

            ConfiguracaoFirebase.getUsuarioAtual(new SimpleCallback<Usuario>() {
                @Override
                public void callback(Usuario usuario) {
                    if(usuario != null){
                        consulta.setNomeEnfermeiroExibicao(usuario.getNome());
                        escreveConsulta(idEnfermeiro, idPaciente, consulta);
                    }
                }
            });


        }

    }

    public Consulta preparaConsulta(){

        String idConsulta = this.consulta.getId() != null ? this.consulta.getId() : ref.child("consultas").push().getKey();
        Consulta consulta = new Consulta();
        DadosConsulta dadosConsulta = new DadosConsulta();
        consulta.setId(idConsulta);

        //pegando valores inseridos
        String altura = campoAlturaConsulta.getEditText().getText().toString();
        String peso = campoPesoConsulta.getEditText().getText().toString();
        String frequencia = campoFrequenciaConsulta.getEditText().getText().toString();
        String pressao = campoPressaoConsulta.getEditText().getText().toString();

        //definindo valores da consulta
        dadosConsulta.setAltura(altura);
        dadosConsulta.setPeso(peso);
        dadosConsulta.setFrequenciaCardiaca(frequencia);
        dadosConsulta.setPressao(pressao);

        //adicionando cada valor no histórico do paciente
        consulta.setDadosConsulta(dadosConsulta);

        return consulta;

    }

    public void escreveConsulta(String idEnfermeiro, String idPaciente, Consulta consulta){

        Map<String, Object> childUpdates = new HashMap<>();
        String idConsulta = consulta.getId();

        childUpdates.put("/consultas/" + idConsulta, consulta);
        childUpdates.put("/paciente_consulta/" + idPaciente + "/" + idConsulta, true);
        childUpdates.put("/enfermeiro_consulta/" + idEnfermeiro + "/" + idConsulta,true);
        dialogProcessando.show();

        ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MessagesUtil.toastInfo(getApplicationContext(), "Consulta salva !");
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MessagesUtil.toastInfo(getApplicationContext(), "Erro:" + e.toString());
                dialogProcessando.dismiss();
            }
        });

    }



    public boolean validarCampos() {

        String mensagemPadrao = "Por favor, preencha o campo ";
        boolean alturaVazio = campoAlturaConsulta.getEditText().getText().toString().trim().isEmpty();
        boolean pesoVazio = campoPesoConsulta.getEditText().getText().toString().trim().isEmpty();
        boolean frequenciaVazio = campoFrequenciaConsulta.getEditText().getText().toString().trim().isEmpty();
        boolean pressaoVazio = campoPressaoConsulta.getEditText().getText().toString().trim().isEmpty();


        if (!alturaVazio) {
            campoAlturaConsulta.setError(null);
            if (!pesoVazio) {
                campoPesoConsulta.setError(null);
                if (!frequenciaVazio) {
                    campoFrequenciaConsulta.setError(null);
                    if (!pressaoVazio) {
                        campoPressaoConsulta.setError(null);
                        return true;
                    } else {
                        campoPressaoConsulta.setError(mensagemPadrao + "Pressão");
                    }
                } else {
                    campoFrequenciaConsulta.setError(mensagemPadrao + "Frequência");
                }

            } else {
                campoPesoConsulta.setError(mensagemPadrao + "Peso");
            }
        } else {
            campoAlturaConsulta.setError(mensagemPadrao + "Altura");
        }

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCheck:
                salvarConsulta();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_consulta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
