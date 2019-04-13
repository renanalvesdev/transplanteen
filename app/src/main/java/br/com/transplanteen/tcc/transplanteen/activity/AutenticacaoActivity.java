package br.com.transplanteen.tcc.transplanteen.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import dmax.dialog.SpotsDialog;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcessar, botaoConsultar;
    private EditText campoIdentificacao, campoSenha, campoInscricao;
    private TextView textoCadastro;
    private Switch tipoAcesso;
    private ProgressBar progressBar;
    private android.app.AlertDialog dialog;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        inicializaComponentes();


        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int textoVisivel = isChecked ? View.VISIBLE : View.GONE;
                textoCadastro.setVisibility(textoVisivel);
            }
        });

        textoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutenticacaoActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_consulta_enfermeiro, null);
                campoInscricao = mView.findViewById(R.id.editNumeroInscricao);
                botaoConsultar = mView.findViewById(R.id.buttonConsultar);

                builder.setView(mView);
                final AlertDialog dialogConsultaCoren = builder.create();
                dialogConsultaCoren.show();

                botaoConsultar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog dialog = new SpotsDialog.Builder().setContext(AutenticacaoActivity.this).setMessage("Salvando paciente").setCancelable(false).build();
                        dialog.show();
                        Enfermeiro enfermeiro = consultaEnfermeiro(campoInscricao.getText().toString());

                        if (enfermeiro != null) {
                            Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiroActivity.class);

                            //Passando dados
                            intent.putExtra("nome", enfermeiro.getNome());
                            intent.putExtra("inscricao", enfermeiro.getNumeroInscricao());
                            intent.putExtra("tipoRegistro", enfermeiro.getTipoRegistro());
                            intent.putExtra("situacao", enfermeiro.getSituacaoInscricao());

                            dialogConsultaCoren.dismiss();
                            startActivity(intent);
                            //toastAux("Nome: " + enfermeiro.getNome());
                        }

                        dialog.dismiss();
                    }
                });


            }
        });


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //deslogar
        autenticacao.signOut();
        //verifica usuario logado
        //verificaUsuarioLogado();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoIdentificacao.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        carregando(true);
                        autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    String usuarioId = task.getResult().getUser().getUid();
                                    recuperaUsuario(usuarioId);
                                } else {
                                    Toast.makeText(AutenticacaoActivity.this, "Erro ao logar", Toast.LENGTH_SHORT).show();
                                    carregando(false);
                                }
                            }
                        });

                    } else {
                        Toast.makeText(AutenticacaoActivity.this, "Preencha a senha", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(AutenticacaoActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void carregando(boolean status){
        int statusProgress, statusBotao;
        statusProgress = status ? View.VISIBLE :  View.GONE;
        statusBotao = statusProgress == View.VISIBLE ? View.GONE : View.VISIBLE;

        progressBar.setVisibility(statusProgress);
        botaoAcessar.setVisibility(statusBotao);

    }

    private void toastAux(String texto) {
        Toast.makeText(AutenticacaoActivity.this, texto, Toast.LENGTH_LONG).show();
    }


    private void verificaUsuarioLogado() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null) {
            abrirTelaPrincipal(usuarioAtual.getUid());
        }
    }

    private void abrirTelaPrincipal(String usuarioId) {
        if (usuarioId != null) {

            startActivity(new Intent(getApplicationContext(), EnfermeiroActivity.class));
            carregando(false);
            finish();
        }
    }

    private void recuperaUsuario(String usuarioId) {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(usuarioId);
        String tipoUsuario = "";
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Usuario usuarioAux = dataSnapshot.getValue(Usuario.class);
                    abrirTelaPrincipal(usuarioAux.getTipo());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inicializaComponentes() {

        textoCadastro = findViewById(R.id.txtCadastro);
        textoCadastro.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        campoIdentificacao = findViewById(R.id.editIdentificacao);
        campoSenha = findViewById(R.id.editSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);


    }


    //teste parse xml
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    private Enfermeiro consultaEnfermeiro(String numeroInscricao) {

        Enfermeiro enfermeiro = null;
        RecuperaDadosXML recuperaDadosXML = new RecuperaDadosXML();
        String teste = "";
        try {
            teste = recuperaDadosXML.execute("\n" +
                    "https://app3.incorpnet.com.br/appincorpnet2/incorpnet.dll/" +
                    "controller?conselho=corence&json={\"Commands\":[{\"Command\":\"LocalizarCadastro\"," +
                    "\"params\":{\"campos\":[\"inscricaoFormatada\",\"nome\",\"sobrenome\",\"cpf\",\"cgc\",\"codSituacao\",\"codTipoRegistro\"]," +
                    "\"condicoes\":[\"\",\"\",\"\",\"=\",\"=\",\"=\",\"=\"]," +
                    "\"valores\":[\"" + numeroInscricao + "\",\"\",\"\",\"%27%27\",\"%27%27\",\"\",\"\"],\"top\":10}}]}").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(teste);
            jsonObject = jsonObject.getJSONArray("Commands").getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("result").getJSONObject("resultSet");

            JSONArray jsonArray = jsonObject.getJSONArray("values");

            if (jsonArray.length() == 1) {
                enfermeiro = new Enfermeiro();

                jsonObject = jsonObject.getJSONArray("values").getJSONObject(0);
                enfermeiro.setNome(jsonObject.getString("nome"));
                enfermeiro.setNumeroInscricao(jsonObject.getString("inscricaoformatada"));
                enfermeiro.setSituacaoInscricao(jsonObject.getString("situacao"));
                enfermeiro.setTipoRegistro(jsonObject.getString("tiporegistro"));

            } else if (jsonArray.length() == 0) {
                toastAux("Nenhum resultado encontrado");
            } else
                toastAux("Mais de um resultado encontrado");

            campoInscricao.setText("");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return enfermeiro;
    }
}

class RecuperaDadosXML extends AsyncTask<String, Void, String> {

    private Exception exception;

    public String doInBackground(String... urls) {
        StringBuilder bufferString = new StringBuilder("");
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            URL url = new URL(urls[0]);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            http.connect();

            InputStream is = http.getInputStream();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("linha")) {
                            // create a new instance of employee
                            System.out.println("Inicio da tag linha ...");
                            //bufferString.append("inicio da tag ..");

                        }
                        break;

                    case XmlPullParser.TEXT:
                        bufferString.append(parser.getText());
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("linha")) {
                            System.out.println("Fim da tag linha ...");
                            //bufferString.append("fim da tag ...");
                        }

                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(bufferString.toString());
        return bufferString.toString();
    }


}