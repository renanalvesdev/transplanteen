package br.com.transplanteen.tcc.transplanteen.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoDiaria;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoFiltro;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.DialogUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

public class IngestaoLiquidoDialogDosesFragment extends DialogFragment {

    private DatabaseReference ingestaoLiquidoDosesDiaRef, database;
    private ValueEventListener valueEventListenerDose;
    private AlertDialog dialogProcessando ;
    private TextView porcentagemDia, totalAlcancadoDia, dataIngestao;
    private List<Dose> ingestaoLiquidoDosesDia = new ArrayList<>();
    private IngestaoLiquido ingestaoLiquido;
    private RecyclerView mRecyclerView;
    private AdapterIngestaoLiquidoDiaria adapterIngestaoLiquidoDiaria;
    // this method create view for your Dialog

    public static IngestaoLiquidoDialogDosesFragment newInstance(int title) {
        IngestaoLiquidoDialogDosesFragment frag = new IngestaoLiquidoDialogDosesFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.dialog_ingestao_liquido_doses, container, false);
        dialogProcessando =  DialogUtil.dialogProcessando(v.getContext());
        dialogProcessando.show();

        ingestaoLiquido = (IngestaoLiquido) getArguments().getSerializable("ingestaoLiquido");
        inicializaComponentes(v);

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        ingestaoLiquidoDosesDiaRef = database
                .child("ingestao_liquido_doses")
                .child(ConfiguracaoFirebase.getIdUsuario());

        adapterIngestaoLiquidoDiaria = new AdapterIngestaoLiquidoDiaria(ingestaoLiquidoDosesDia, v.getContext());

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerIngestaoLiquidoDlgDoses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapterIngestaoLiquidoDiaria);
        //get your recycler view and populate it.
        return v;
    }

    public void inicializaComponentes(View v){

        dataIngestao = v.findViewById(R.id.txtDataIngestaoDlg);
        porcentagemDia = v.findViewById(R.id.txtPorcentagemAlcancadaDlg);
        totalAlcancadoDia = v.findViewById(R.id.txtTotalAlcancadoDlg);

        dialogProcessando.show();
        porcentagemDia.setText(ingestaoLiquido.getPorcentagemAlcancada());
        totalAlcancadoDia.setText("( "+ingestaoLiquido.getQuantidadeAlcancada() + " / " + "3.0L )");
    }


    private void recuperaDosesDia() {

        valueEventListenerDose = ingestaoLiquidoDosesDiaRef.child(ingestaoLiquido.getDia()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ingestaoLiquidoDosesDia.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Dose d = ds.getValue(Dose.class);
                            ingestaoLiquidoDosesDia.add(d);
                            adapterIngestaoLiquidoDiaria.notifyDataSetChanged();

                        }

                        catch (Exception e) {
                            MessagesUtil.toastInfo(getContext(), "Erro: " + e.getMessage());
                        }
                    }

                    dialogProcessando.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MessagesUtil.toastInfo(getContext(), "Erro: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaDosesDia();
    }
}
