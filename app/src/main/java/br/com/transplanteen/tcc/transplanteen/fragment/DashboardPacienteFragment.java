package br.com.transplanteen.tcc.transplanteen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.activity.IngestaoLiquidoActivity;
import br.com.transplanteen.tcc.transplanteen.activity.PacienteActivity;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.model.Usuario;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardPacienteFragment extends Fragment {

    private Paciente paciente;
    TextView campoNomePaciente, campoPesoPaciente, campoAlturaPaciente, campoStatusAguaPaciente, campoStatusMedicacaoPaciente;
    CardView cardIngestaoLiquido;

    public DashboardPacienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        carregaPaciente();

        View v = inflater.inflate(R.layout.fragment_dashboard_paciente, container, false);
        inicializaComponentes(v);
        inicializaCards();

        return v;
    }

    public void inicializaComponentes(View v){
        campoNomePaciente = v.findViewById(R.id.txtNomePaciente);
        cardIngestaoLiquido = v.findViewById(R.id.cardIngestaoLiquido);
    }

    public void inicializaCards(){
        cardIngestaoLiquido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), IngestaoLiquidoActivity.class);
                i.putExtra("paciente", paciente);
                startActivity(i);
            }
        });
    }


    //carrega o paciente (proveniente do adapter ou do paciente logado)

    public void carregaPaciente(){
        if(paciente == null) {
            Paciente.getPaciente(new SimpleCallback<Paciente>() {
                @Override
                public void callback(Paciente pacienteRetornado) {
                    if(pacienteRetornado != null){
                        paciente = pacienteRetornado;
                    } else {
                        paciente  = (Paciente) getArguments().getSerializable("paciente");
                    }

                    inicializaInfoPaciente();

                }
            });
        }
    }

    public void inicializaInfoPaciente(){
        campoNomePaciente.setText(paciente.getNome());
    }

}
