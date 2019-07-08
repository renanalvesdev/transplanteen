package br.com.transplanteen.tcc.transplanteen.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.fragment.ConsultasPacienteFragment;
import br.com.transplanteen.tcc.transplanteen.fragment.DashboardPacienteFragment;
import br.com.transplanteen.tcc.transplanteen.fragment.MedicacaoFragment;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;

public class PacienteActivity extends AppCompatActivity {

    Paciente paciente = new Paciente();
    TextView textoNomePaciente, textoTipoTransplantePaciente, textoPesoPaciente, textoAlturaPaciente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        Bundle bundle = getIntent().getExtras();
        DashboardPacienteFragment dashboardPacienteFragment = new DashboardPacienteFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ((Paciente) bundle.getSerializable("paciente") == null) {
            paciente.setId(ConfiguracaoFirebase.getIdUsuario());

        } else {
            paciente = (Paciente) bundle.getSerializable("paciente");
            toolbar.setTitle(paciente.getNome());
        }


        bundle.putSerializable("paciente", paciente);
        //Configurar abas (passando dados do paciente como bundle)
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Dashboard", DashboardPacienteFragment.class, bundle)
                        .add("Consultas", ConsultasPacienteFragment.class, bundle)
                        .add("Medicação", MedicacaoFragment.class, bundle).create()
        );

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        /*Intent notifyIntent = getIntent();

        String extras = getIntent().getStringExtra("From");
        ;
        if (extras != null && extras.equals("notifyFragment")) {
            viewPager.setCurrentItem(2);
        }*/


        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);


    }


}
