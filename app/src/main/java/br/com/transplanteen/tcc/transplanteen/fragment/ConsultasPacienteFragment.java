package br.com.transplanteen.tcc.transplanteen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.activity.CadastroEnfermeiroActivity;
import br.com.transplanteen.tcc.transplanteen.activity.ConsultaActivity;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasPacienteFragment extends Fragment {

    FloatingActionButton fab;

    public ConsultasPacienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consultas_paciente, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConsultaActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
