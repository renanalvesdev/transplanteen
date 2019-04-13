package br.com.transplanteen.tcc.transplanteen.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.TipoUsuario;
import br.com.transplanteen.tcc.transplanteen.fragment.AgendaFragment;
import br.com.transplanteen.tcc.transplanteen.fragment.PacientesFragment;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import dmax.dialog.SpotsDialog;

public class EnfermeiroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private TextView textoNome, textoEmail, textoCirculo;
    private AlertDialog dialogProcessando ;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private String idUsuario = ConfiguracaoFirebase.getIdUsuario();
    private Usuario usuarioAtual;


    private final DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermeiro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        //carrega tela principal
        AgendaFragment agendaFragment = new AgendaFragment();
        FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.frameContainer, agendaFragment);
        fragment.commit();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        textoCirculo = (TextView) headerView.findViewById(R.id.txtHeaderCircle);
        textoEmail = (TextView) headerView.findViewById(R.id.txtHeaderEmail);
        textoNome = (TextView) headerView.findViewById(R.id.txtHeaderNome);

        textoCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                abrirPerfilUsuario();
            }
        });

        dialogProcessando = new SpotsDialog.Builder().setContext(this).setMessage("Carregando").setCancelable(false).build();
        carregaUsuarioAtual();
        //recuperaDadosUsuario();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(EnfermeiroActivity.this, "Sucesso ao logar", Toast.LENGTH_SHORT).show();
    }


    //carrega o usuario atual logado no firebase
    public void carregaUsuarioAtual(){
        dialogProcessando.show();
        ConfiguracaoFirebase.getUsuarioAtual(new SimpleCallback<Usuario>() {
            @Override
            public void callback(Usuario usuario) {
                if(usuario != null){
                    usuarioAtual = usuario;
                    exibeItemMenu();
                    recuperaDadosUsuario();
                };
            }
        });
    }

    public void exibeItemMenu(){
        Menu nav_menu = navigationView.getMenu();
        if(usuarioAtual.getTipo().equals(TipoUsuario.ENFERMEIRO.toString())){
            nav_menu.findItem(R.id.nav_meu_diario).setVisible(false);
            nav_menu.findItem(R.id.nav_ingestao_liquido).setVisible(false);
            nav_menu.findItem(R.id.nav_meu_amigo_enfermeiro).setVisible(false);
            nav_menu.findItem(R.id.nav_visitas_ao_hospital).setVisible(false);
        }

        else if(usuarioAtual.getTipo().equals(TipoUsuario.PACIENTE.toString())){
            nav_menu.findItem(R.id.nav_agenda).setVisible(false);
            nav_menu.findItem(R.id.nav_pacientes).setVisible(false);
        }
        
    }

    //direciona o usuario para seu perfil (paciente ou enfermeiro)
    public void abrirPerfilUsuario() {

        String tipo = usuarioAtual.getTipo();

        if (tipo.equals(TipoUsuario.ENFERMEIRO.toString()))
            startActivity(new Intent(getApplicationContext(), CadastroEnfermeiroActivity.class));
        else if (tipo.equals(TipoUsuario.PACIENTE.toString()))
            startActivity(new Intent(getApplicationContext(), NovoPacienteActivity.class));

    }

    //carrega dados do usuario no nav_header (titulo, subtitulo e circulo com primeira letra )
    private void recuperaDadosUsuario() {

        if(usuarioAtual != null){
            String txtCirculo = String.valueOf(usuarioAtual.getNome().charAt(0));
            textoNome.setText(usuarioAtual.getNome());
            textoEmail.setText(usuarioAtual.getEmail());
            textoCirculo.setText(txtCirculo);
        }

        dialogProcessando.dismiss();
    }

    private void deslogar() {
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null) {
            autenticacao.signOut();
            startActivity(new Intent(getApplicationContext(), AutenticacaoActivity.class));
            Toast.makeText(EnfermeiroActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enfermeiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sair) {
            deslogar();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_agenda) {
            AgendaFragment agendaFragment = new AgendaFragment();
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.frameContainer, agendaFragment);
            fragment.commit();
        } else if (id == R.id.nav_pacientes) {
            PacientesFragment pacientesFragment = new PacientesFragment();
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.frameContainer, pacientesFragment);
            fragment.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
