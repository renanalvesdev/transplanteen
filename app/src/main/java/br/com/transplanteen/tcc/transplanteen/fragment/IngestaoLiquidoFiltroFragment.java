package br.com.transplanteen.tcc.transplanteen.fragment;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoDiaria;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoFiltro;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.RecyclerItemClickListener;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngestaoLiquidoFiltroFragment extends Fragment {

    private Spinner campoTipoFiltroIngestaoLiquido;
    private TextView campoIntervaloData;
    private Paciente paciente;
    private RecyclerView recyclerViewFiltroIngestaoLiquido;
    private List<IngestaoLiquido> ingestaoLiquidoList = new ArrayList<>();
    private AdapterIngestaoLiquidoFiltro adapterIngestaoLiquidoFiltro;
    private DatabaseReference ingestaoLiquidoDiaRef, database;

    private ValueEventListener valueEventListenerFiltroIngestaoLiquido;

    public IngestaoLiquidoFiltroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingestao_liquido_filtro, container, false);
        inicializaComponentes(v);
        carregarTipoFiltroIngestaoLiquidoSpinner();

        paciente = (Paciente) getArguments().getSerializable("paciente");

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        ingestaoLiquidoDiaRef = database.child("ingestao_liquido_alcancado").child(paciente.getId());

        //configura adapter
        adapterIngestaoLiquidoFiltro = new AdapterIngestaoLiquidoFiltro(ingestaoLiquidoList, v.getContext());

        //configura recycler view
        recyclerViewFiltroIngestaoLiquido = (RecyclerView) v.findViewById(R.id.recyclerFiltroIngestaoLiquidoDiario);
        recyclerViewFiltroIngestaoLiquido.setHasFixedSize(true);
        recyclerViewFiltroIngestaoLiquido.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFiltroIngestaoLiquido.setAdapter(adapterIngestaoLiquidoFiltro);
        recyclerViewFiltroIngestaoLiquido.addOnItemTouchListener
                (new RecyclerItemClickListener(
                        v.getContext(),
                        recyclerViewFiltroIngestaoLiquido,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                abreDialogoDosesDia(ingestaoLiquidoList.get(position));
                               // MessagesUtil.toastInfo(getContext(),ingestaoLiquidoList.get(position).getPorcentagemAlcancada());
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));


        return v;
    }

   public void abreDialogoDosesDia(IngestaoLiquido ingestaoLiquido) {

       Bundle args = new Bundle();
       args.putSerializable("ingestaoLiquido", ingestaoLiquido);

       DialogFragment dialogDosesDia = IngestaoLiquidoDialogDosesFragment.newInstance(123);
       dialogDosesDia.setArguments(args);
       dialogDosesDia.show(getFragmentManager(), "dialog");

    }

    private void carregarTipoFiltroIngestaoLiquidoSpinner() {
        String[] tipoTransplante = getResources().getStringArray(R.array.tipo_filtro_ingestao_liquido);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                tipoTransplante
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoTipoFiltroIngestaoLiquido.setAdapter(adapter);
    }

    public void inicializaComponentes(View v) {

        campoIntervaloData = v.findViewById(R.id.txtIntervaloData);
        campoTipoFiltroIngestaoLiquido = v.findViewById(R.id.spinnerTipoFiltroIngestaoLiquido);

        //realiza uma acao para cada tipo de filtro clicado
        campoTipoFiltroIngestaoLiquido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateTime dataInicial = DateTime.now(), dataFinal = DateTime.now().minusDays(1);
                switch (position) {
                    case 0:
                        dataInicial = DateTime.now().minusWeeks(1);
                        break;

                    case 1:
                        dataInicial = DateTime.now().minusYears(1);
                        break;
                }

                campoIntervaloData.setText(DataUtil.textoIntervalo(dataInicial, dataFinal));
                recuperaIngestaoLiquidoFiltro(dataInicial.getMillis(), dataFinal.getMillis());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void recuperaIngestaoLiquidoFiltro(long dataInicial, long dataFinal) {

        Query query = ingestaoLiquidoDiaRef.orderByKey().startAt(String.valueOf(dataInicial)).endAt(String.valueOf(dataFinal));

        valueEventListenerFiltroIngestaoLiquido = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ingestaoLiquidoList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            IngestaoLiquido ingestaoLiquido = ds.getValue(IngestaoLiquido.class);
                            ingestaoLiquidoList.add(ingestaoLiquido);
                            adapterIngestaoLiquidoFiltro.notifyDataSetChanged();
                        } catch (Exception e) {
                            MessagesUtil.toastInfo(getContext(), "Erro: " + e.getMessage());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
