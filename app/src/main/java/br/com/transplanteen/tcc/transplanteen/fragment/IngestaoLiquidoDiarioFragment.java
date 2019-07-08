package br.com.transplanteen.tcc.transplanteen.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterConsultasPaciente;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoDiaria;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterPacientes;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Consulta;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngestaoLiquidoDiarioFragment extends Fragment {

    private RecyclerView recyclerViewIngestaoLiquido;
    private List<Dose> ingestaoLiquidoList = new ArrayList<>();
    private AdapterIngestaoLiquidoDiaria adapterIngestaoLiquidoDiaria;
    private Paciente paciente;
    private IngestaoLiquido ingestaoLiquido;
    private DatabaseReference ingestaoLiquidoAlcancadoDiaRef, ingestaoLiquidoDosesDiaRef, database;
    private ValueEventListener valueEventListenerDose, valueEventListenerIngestaoDia;
    private TextView campoPorcentagemAlcancada, campoAlcalcadoTotal, campoDataAtual;
    private String idUsuario = ConfiguracaoFirebase.getIdUsuario();

    private final String DATA_ATUAL = DataUtil.dataAtual();
    private final String LIMITE_DIARIO = "3 L";

    FloatingActionButton fab;

    public IngestaoLiquidoDiarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingestao_liquido_diario, container, false);
        inicializaComponentes(v);
        paciente = (Paciente) getArguments().getSerializable("paciente");

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoseLiquidoSheetFragment doseBottomSheet = new DoseLiquidoSheetFragment();
                ingestaoLiquido = ingestaoLiquido != null ? ingestaoLiquido : new IngestaoLiquido();

                Bundle b = new Bundle();
                b.putSerializable("ingestaoLiquido", (Serializable) ingestaoLiquido);
                doseBottomSheet.setArguments(b);
                doseBottomSheet.show(getFragmentManager(), doseBottomSheet.getTag());
            }
        });

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        ingestaoLiquidoDosesDiaRef = database.child("ingestao_liquido_doses").child(paciente.getId()).child(DataUtil.hojeTimestamp());
        ingestaoLiquidoAlcancadoDiaRef = database.child("ingestao_liquido_alcancado").child(paciente.getId()).child(DataUtil.hojeTimestamp());

        //configura adapter
        adapterIngestaoLiquidoDiaria = new AdapterIngestaoLiquidoDiaria(ingestaoLiquidoList, v.getContext());

        //configura recycler view
        recyclerViewIngestaoLiquido = (RecyclerView) v.findViewById(R.id.recyclerIngestaoLiquidoDiario);
        recyclerViewIngestaoLiquido.setHasFixedSize(true);
        recyclerViewIngestaoLiquido.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewIngestaoLiquido.setAdapter(adapterIngestaoLiquidoDiaria);

        return v;
    }


    private void recuperaDosesDia() {


        valueEventListenerDose = ingestaoLiquidoDosesDiaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    double totalAlcancado = 0.0;
                    ingestaoLiquidoList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Dose d = ds.getValue(Dose.class);

                            totalAlcancado += d.getDose();
                            ingestaoLiquidoList.add(d);
                            adapterIngestaoLiquidoDiaria.notifyDataSetChanged();

                        } catch (Exception e) {
                            MessagesUtil.toastInfo(getContext(), "Erro: " + e.getMessage());
                        }
                    }

                    atualizaIngestaoDiaria(totalAlcancado);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MessagesUtil.toastInfo(getContext(), "Erro: " + databaseError.getMessage());
            }
        });
    }

    public void recuperaIngestaoLiquidoAlcancadoDia(){

        valueEventListenerIngestaoDia =  ingestaoLiquidoAlcancadoDiaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ingestaoLiquido = dataSnapshot.getValue(IngestaoLiquido.class);
                    populaDadosIngestaoDiaria(ingestaoLiquido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        recuperaDosesDia();
        recuperaIngestaoLiquidoAlcancadoDia();

    }

    public void atualizaIngestaoDiaria(double totalAlcancado) {

        String status;
        double totalAlcancadoLitros = totalAlcancado / 1000;
        double porcentagemAlcancada = (totalAlcancado / 3000.0) * 100;
        String porcentagemFormatada = String.format("%.2f", porcentagemAlcancada) + "%";

        IngestaoLiquido ingestaoLiquido = new IngestaoLiquido();
        ingestaoLiquido.setQuantidadeAlcancada(String.valueOf(totalAlcancadoLitros));
        ingestaoLiquido.setPorcentagemAlcancada(porcentagemFormatada);
        ingestaoLiquido.setStatus(calculaStatus(porcentagemAlcancada));
        ingestaoLiquido.setDia(DataUtil.hojeTimestamp());

        ingestaoLiquidoAlcancadoDiaRef.setValue(ingestaoLiquido);

    }

    public String calculaStatus(double porcentagemAlcancada) {

        porcentagemAlcancada = Math.round(porcentagemAlcancada * 100.0) / 100.0;
        String status = "";

        if (porcentagemAlcancada >= 100)
            status = "Bom";
        else if (porcentagemAlcancada >= 50)
            status = "Razoavel";
        else
            status = "Ruim";

        return status;
    }

    public void inicializaComponentes(View v) {
        campoPorcentagemAlcancada = v.findViewById(R.id.txtPorcentagemAlcancada);
        campoAlcalcadoTotal = v.findViewById(R.id.txtAlcancadoTotal);
        campoDataAtual = v.findViewById(R.id.txtDataAtual);

        campoDataAtual.setText("Hoje " + " (" + DATA_ATUAL + ")");
    }

    public void populaDadosIngestaoDiaria(IngestaoLiquido ingestaoLiquido) {
        campoPorcentagemAlcancada.setText(ingestaoLiquido.getPorcentagemAlcancada());
        campoAlcalcadoTotal.setText(ingestaoLiquido.getQuantidadeAlcancada() + " / " + LIMITE_DIARIO);

    }

}
