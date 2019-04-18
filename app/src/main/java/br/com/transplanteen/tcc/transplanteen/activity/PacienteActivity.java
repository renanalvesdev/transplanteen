package br.com.transplanteen.tcc.transplanteen.activity;

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
import br.com.transplanteen.tcc.transplanteen.model.Paciente;

public class PacienteActivity extends AppCompatActivity {

    Paciente paciente;
    TextView textoNomePaciente, textoTipoTransplantePaciente, textoPesoPaciente, textoAlturaPaciente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        Bundle bundle = getIntent().getExtras();
        DashboardPacienteFragment dashboardPacienteFragment = new DashboardPacienteFragment();

        paciente = (Paciente) bundle.getSerializable("paciente");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(paciente.getNome());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configurar abas (passando dados do paciente como bundle)
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Dashboard", DashboardPacienteFragment.class, bundle)
                        .add("Consultas", ConsultasPacienteFragment.class, bundle).create()
        );

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);


    }


}
