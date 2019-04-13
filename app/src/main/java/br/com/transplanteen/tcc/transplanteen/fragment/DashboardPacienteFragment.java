package br.com.transplanteen.tcc.transplanteen.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.activity.PacienteActivity;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardPacienteFragment extends Fragment {

    private Paciente paciente;
    TextView campoNomePaciente, campoPesoPaciente, campoAlturaPaciente, campoStatusAguaPaciente, campoStatusMedicacaoPaciente;

    public DashboardPacienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        paciente = (Paciente) getArguments().getSerializable("paciente");

        View v = inflater.inflate(R.layout.fragment_dashboard_paciente, container, false);
        inicializaComponentes(v);

        campoNomePaciente.setText(paciente.getNome());

        return v;
    }

    public void inicializaComponentes(View v){
        campoNomePaciente = v.findViewById(R.id.txtNomePaciente);
    }

}
