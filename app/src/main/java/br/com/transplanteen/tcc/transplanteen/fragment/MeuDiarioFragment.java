package br.com.transplanteen.tcc.transplanteen.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterIngestaoLiquidoDiaria;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterMeuDiario;
import br.com.transplanteen.tcc.transplanteen.constants.StatusFrequencia;
import br.com.transplanteen.tcc.transplanteen.constants.StatusPressao;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.RecyclerItemClickListener;
import br.com.transplanteen.tcc.transplanteen.model.DadosDiario;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.Frequencia;
import br.com.transplanteen.tcc.transplanteen.model.Peso;
import br.com.transplanteen.tcc.transplanteen.model.Pressao;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.DialogUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import br.com.transplanteen.tcc.transplanteen.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeuDiarioFragment extends Fragment {

    private LinearLayout linearLayoutResumo;
    private RecyclerView recyclerViewMeuDiario;
    private List<DadosDiario> meuDiarioList = new ArrayList<>();
    private AdapterMeuDiario adapterMeuDiario;

    private DatabaseReference meuDiarioRef, meuDiarioTodosRef, database;
    private ValueEventListener valueEventListenerMeuDiarioTodos;
    private DadosDiario dadosDiario = new DadosDiario();

    private Spinner spinnerTipoFiltroMeuDiario;
    public View shadowView;

    //layouts visiveis caso existam dados
    private LinearLayout layoutDadosPeso, layoutDadosPressao, layoutDadosFrequencia,
            layoutDiarioCompleto, layoutDiarioIncompleto, layoutDiarioVazio, layoutDadosDiario;

    //campos dos dialogos
    public TextView campoPeso, campoPressaoSis, campoPressaoDia, campoFrequencia;

    //labels de exibiao
    public TextView txtPeso, txtPressao, txtFrequencia, txtIntervaloDataFiltroMeuDiario,
    txtResumoPesoFiltro, txtResumoPressaoFiltro, txtResumoFrequenciaFiltro;


    public FloatingActionsMenu fab_actions;
    private AlertDialog dialog;


    public MeuDiarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inicializando referencias do banco de dados
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        meuDiarioRef = database.child("meu_diario").child(ConfiguracaoFirebase.getIdUsuario()).child(DataUtil.hojeTimestamp());
        meuDiarioTodosRef = database.child("meu_diario").child(ConfiguracaoFirebase.getIdUsuario());

        View v = inflater.inflate(R.layout.fragment_meu_diario, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //configura adapter
        adapterMeuDiario = new AdapterMeuDiario(meuDiarioList, v.getContext());
        //configura recycler view
        recyclerViewMeuDiario = (RecyclerView) v.findViewById(R.id.recyclerMeuDiario);
        recyclerViewMeuDiario.setHasFixedSize(true);
        recyclerViewMeuDiario.setLayoutManager(mLayoutManager);
        recyclerViewMeuDiario.setAdapter(adapterMeuDiario);
        recyclerViewMeuDiario.addOnItemTouchListener
                (new RecyclerItemClickListener(
                        v.getContext(),
                        recyclerViewMeuDiario,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                abreDialogoDetalhesDiario(meuDiarioList.get(position));

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

        inicializaComponentes(v);
        carregarTipoFiltroMeuDiarioSpinner();


        fab_actions = v.findViewById(R.id.fab_actions);
        fab_actions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                shadowView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                shadowView.setVisibility(View.GONE);
            }
        });

        FloatingActionButton fab_peso = v.findViewById(R.id.fab_peso);
        fab_peso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreDialogoPeso();
            }
        });

        FloatingActionButton fab_pressao = v.findViewById(R.id.fab_pressao);
        fab_pressao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreDialogoPressao();
            }
        });


        FloatingActionButton fab_frequencia = v.findViewById(R.id.fab_frequencia);
        fab_frequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreDialogoFrequencia();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    public void abreDialogoPeso() {

        MessagesUtil.toastInfo(this.getContext(), "Dialogo aberto");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_cadastro_diario, null);
        Button btnSalvarPeso = mView.findViewById(R.id.buttonSalvarDiario);

        campoPeso = mView.findViewById(R.id.editPesoDialog);
        builder.setView(mView);
        dialog = builder.create();

        btnSalvarPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.campoVazio(campoPeso.getText().toString())) {
                    MessagesUtil.toastInfo(getContext(), "Preencha o campo !");
                } else {
                    Peso peso = new Peso(Double.valueOf(campoPeso.getText().toString()));
                    dadosDiario.setPeso(peso);
                    salvarMeuDiario(dadosDiario);
                }
            }
        });

        dialog.show();
        fab_actions.collapse();
    }

    public void abreDialogoFrequencia() {

        MessagesUtil.toastInfo(this.getContext(), "Dialogo aberto");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_cadastro_frequencia, null);
        builder.setView(mView);
        dialog = builder.create();

        Button btnSalvarFrequencia = mView.findViewById(R.id.buttonSalvarFrequencia);
        campoFrequencia = mView.findViewById(R.id.editFrequenciaCardiacaDialog);

        btnSalvarFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.campoVazio(campoFrequencia.getText().toString())) {
                    MessagesUtil.toastInfo(getContext(), "Preencha o campo !");
                } else {
                    Frequencia frequencia = new Frequencia(Integer.valueOf(campoFrequencia.getText().toString()));
                    dadosDiario.setFrequencia(frequencia);
                    salvarMeuDiario(dadosDiario);
                }
            }
        });

        dialog.show();
        fab_actions.collapse();
    }

    public void abreDialogoPressao() {

        MessagesUtil.toastInfo(this.getContext(), "Dialogo aberto");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_cadastro_pressao, null);
        Button btnSalvarFrequencia = mView.findViewById(R.id.buttonSalvarPressao);

        campoPressaoSis = mView.findViewById(R.id.editPressaoSistolicaDialog);
        campoPressaoDia = mView.findViewById(R.id.editPressaoDiastolicaDialog);


        builder.setView(mView);
        dialog = builder.create();

        btnSalvarFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.campoVazio(campoPressaoSis.getText().toString()) || StringUtil.campoVazio(campoPressaoDia.getText().toString())) {
                    MessagesUtil.toastInfo(getContext(), "Preencha o campo !");
                } else {
                    Pressao pressao = new Pressao(Integer.valueOf(campoPressaoSis.getText().toString()),
                            Integer.valueOf(campoPressaoDia.getText().toString()));
                    dadosDiario.setPressao(pressao);

                    salvarMeuDiario(dadosDiario);
                }
            }
        });

        dialog.show();
        fab_actions.collapse();
    }

    public void abreDialogoDetalhesDiario(DadosDiario dadosDiario){

        AlertDialog dialog ;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_detalhes_diario, null);
      //  Button btnFecharDetalhesDiario= mView.findViewById(R.id.buttonFecharDetalhesDiario);

        TextView dataDetalheDiario = mView.findViewById(R.id.dataDetalheDiario);
        TextView valorPesoDetalheDiario = mView.findViewById(R.id.valorPesoDetalheDiario);
        TextView valorFrequenciaDetalheDiario = mView.findViewById(R.id.valorFrequenciaDetalheDiario );
        TextView valorPressaoDetalheDiario = mView.findViewById(R.id.valorPressaoDetalheDiario );

        TextView statusPressaoDetalheDiario = mView.findViewById(R.id.statusPressaoDetalheDiario);
        TextView statusFrequenciaDetalheDiario = mView.findViewById(R.id.statusFrequenciaDetalheDiario);
       /* btnFecharDetalhesDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dataDetalheDiario.setText(dadosDiario.getDataFormatada());

        if(dadosDiario.getPressao() != null){
            Pressao pressao = dadosDiario.getPressao();
            valorPressaoDetalheDiario.setText(pressao.getValorPressao());
            valorPressaoDetalheDiario.setTextColor(pressao.getCor());
            statusPressaoDetalheDiario.setTextColor(pressao.getCor());
            statusPressaoDetalheDiario.setText(pressao.getStatusPressaoString());
        }

        if(dadosDiario.getFrequencia() != null){
            Frequencia frequencia = dadosDiario.getFrequencia();
            valorFrequenciaDetalheDiario.setText(frequencia.getValorString());
            valorFrequenciaDetalheDiario.setTextColor(frequencia.getCor());
            statusFrequenciaDetalheDiario.setTextColor(frequencia.getCor());
            statusFrequenciaDetalheDiario.setText(frequencia.getStatusFrequenciaString());
        }

        builder.setView(mView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog = builder.create();
        dialog.show();

    }

    public void inicializaComponentes(View v) {

        linearLayoutResumo = v.findViewById(R.id.linearLayoutResumo);
        layoutDiarioCompleto = v.findViewById(R.id.layoutDiarioCompleto);
        layoutDiarioIncompleto = v.findViewById(R.id.layoutDiarioIncompleto);
        layoutDiarioVazio = v.findViewById(R.id.layoutDiarioVazio);
        layoutDadosDiario = v.findViewById(R.id.layoutDadosDiario);

        spinnerTipoFiltroMeuDiario = v.findViewById(R.id.spinnerTipoFiltroMeuDiario);
        shadowView = v.findViewById(R.id.shadowView);

        //layouts visiveis caso existam dados de peso, pressao e frequencia
        layoutDadosPeso = v.findViewById(R.id.layoutDadosPeso);
        layoutDadosPressao = v.findViewById(R.id.layoutDadosPressao);
        layoutDadosFrequencia = v.findViewById(R.id.layoutDadosFrequencia);


        //labels de exibicao
        txtPeso = v.findViewById(R.id.txtPesoDiario);
        txtPressao = v.findViewById(R.id.txtPressaoDiario);
        txtFrequencia = v.findViewById(R.id.txtFrequenciaDiario);
        txtIntervaloDataFiltroMeuDiario = v.findViewById(R.id.txtIntervaloDataFiltroMeuDiario);
        txtResumoPesoFiltro= v.findViewById(R.id.txtResumoPesoFiltro);
        txtResumoPressaoFiltro= v.findViewById(R.id.txtResumoPressaoFiltro);
        txtResumoFrequenciaFiltro = v.findViewById(R.id.txtResumoFrequenciaFiltro);


        linearLayoutResumo.setVisibility(View.GONE);
        recyclerViewMeuDiario.setVisibility(View.GONE);

        //realiza uma acao para cada tipo de filtro clicado
        spinnerTipoFiltroMeuDiario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateTime dataInicial = DateTime.now(),
                        dataFinal = DateTime.now().minusDays(1);
                switch (position) {
                    case 0:
                        dataInicial = DateTime.now().minusWeeks(1);
                        break;

                    case 1:
                        dataInicial = DateTime.now().minusYears(1);
                        break;
                }

                txtIntervaloDataFiltroMeuDiario.setText(DataUtil.textoIntervalo(dataInicial, dataFinal));
                recuperaMeuDiarioFiltro(dataInicial.getMillis(), dataFinal.getMillis());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void carregarTipoFiltroMeuDiarioSpinner() {
        String[] tipoFiltroMeuDiario = getResources().getStringArray(R.array.tipo_filtro_ingestao_liquido);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item,
                tipoFiltroMeuDiario
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTipoFiltroMeuDiario.setAdapter(adapter);
    }


    public void salvarMeuDiario(DadosDiario dadosDiario) {

        dadosDiario.setData(DataUtil.hojeTimestamp());

        final AlertDialog processando = DialogUtil.dialogProcessando(getContext());
        processando.show();

        meuDiarioRef.setValue(dadosDiario).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                processando.dismiss();
                dialog.dismiss();
                MessagesUtil.toastInfo(getContext(), "Salvo!");
            }
        });
    }

    public void recuperaDiario() {
        meuDiarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    layoutDadosDiario.setVisibility(View.VISIBLE);
                    layoutDiarioVazio.setVisibility(View.GONE);

                    dadosDiario = dataSnapshot.getValue(DadosDiario.class);
                    mostraDadosDiario(dadosDiario);
                }

                else {

                    layoutDadosDiario.setVisibility(View.GONE);
                    layoutDiarioVazio.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void mostraDadosDiario(DadosDiario dadosDiario) {

        int mostraLayoutDiarioCompleto = View.VISIBLE, mostraLayoutDiarioIncompleto= View.GONE,
        mostraLayoutDadosPeso = View.VISIBLE, mostraLayoutDadosPressao= View.VISIBLE, mostraLayoutDadosFrequencia = View.VISIBLE;

        String peso = dadosDiario.getPeso() != null ? String.valueOf(dadosDiario.getPeso().getValor()) : " ";
        String pressao = dadosDiario.getPressao() != null ? String.valueOf(dadosDiario.getPressao().getPressaoSistolica()) +
                "/" + String.valueOf(dadosDiario.getPressao().getPressaoDiastolica()) : " ";
        String frequencia =  dadosDiario.getFrequencia() != null ? String.valueOf(dadosDiario.getFrequencia().getValor()) : " ";


        if(dadosDiario.getPeso() == null || dadosDiario.getFrequencia() == null || dadosDiario.getPressao() == null ){

            mostraLayoutDiarioCompleto = View.GONE;
            mostraLayoutDiarioIncompleto = View.VISIBLE;

            //checa peso
            if (dadosDiario.getPeso() == null)
                mostraLayoutDadosPeso = View.GONE;

            //checa frequencia
            if (dadosDiario.getFrequencia() == null)
                mostraLayoutDadosFrequencia = View.GONE;

            //pressao
            if (dadosDiario.getPressao() == null ){
                mostraLayoutDadosPressao= View.GONE;
            }

        }

        txtPeso.setText(peso);
        txtFrequencia.setText(frequencia);
        txtPressao.setText(pressao);

        layoutDadosPressao.setVisibility(mostraLayoutDadosPressao);
        layoutDadosFrequencia.setVisibility(mostraLayoutDadosFrequencia);
        layoutDadosPeso.setVisibility(mostraLayoutDadosPeso);
        layoutDiarioCompleto.setVisibility(mostraLayoutDiarioCompleto);
        layoutDiarioIncompleto.setVisibility(mostraLayoutDiarioIncompleto);

    }

    public void recuperaMeuDiarioFiltro(long dataInicial, long dataFinal) {

        Query query = meuDiarioTodosRef.orderByKey().startAt(String.valueOf(dataInicial)).endAt(String.valueOf(dataFinal));
        valueEventListenerMeuDiarioTodos = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    meuDiarioList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        DadosDiario dadosDiario = ds.getValue(DadosDiario.class);
                        meuDiarioList.add(dadosDiario);
                        adapterMeuDiario.notifyDataSetChanged();
                    }

                    linearLayoutResumo.setVisibility(View.VISIBLE);
                    recyclerViewMeuDiario.setVisibility(View.VISIBLE);
                    resumoDiarioFiltro(meuDiarioList);

                } else {
                    linearLayoutResumo.setVisibility(View.GONE);
                    recyclerViewMeuDiario.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void resumoDiarioFiltro(List<DadosDiario> listaDiario){


        double totalFrequenciaNormal = 0, totalFrequencia = 0,  totalPressaoNormal = 0, totalPressao = 0;

        if(listaDiario.size() > 1){
            DadosDiario ultimoElemento = listaDiario.get(listaDiario.size() - 1);
            DadosDiario primeiroElemento = listaDiario.get(0);

            Peso pesoUltimoElemento = ultimoElemento.getPeso();
            Peso pesoPrimeiroElemento = primeiroElemento.getPeso();

            if(pesoUltimoElemento != null && pesoPrimeiroElemento != null){
                int corVariacaoPeso ;
                String sinalVariacao;

                Double variacaoPeso = pesoUltimoElemento.getValor() - pesoPrimeiroElemento.getValor();
                String variacaoPesoString = String.valueOf(variacaoPeso);
                if(variacaoPeso < 0.0){
                    sinalVariacao = " - ";
                    corVariacaoPeso = Color.RED;
                }

                else {
                    sinalVariacao = " + ";
                    corVariacaoPeso = Color.GREEN;
                }

                txtResumoPesoFiltro.setText(sinalVariacao + variacaoPesoString);
                txtResumoPesoFiltro.setTextColor(corVariacaoPeso);
            }
        }


        for(DadosDiario dd : listaDiario){

            //resumo frequencia
            if(dd.getFrequencia() != null){
                totalFrequencia ++;

                if(dd.getFrequencia().getStatusFrequencia() == StatusFrequencia.NORMAL)
                    totalFrequenciaNormal ++;
            }

            //resumo pressao
            if(dd.getPressao() != null){
                totalPressao ++;

                if(dd.getPressao().getStatusPressao() == StatusPressao.NORMAL)
                    totalPressaoNormal ++;
            }
        }

        if(totalFrequencia > 0){
            int corPorcentagemFrequencia;

            double porcentagemFrequencia = (totalFrequenciaNormal/totalFrequencia) * 100;
            if(porcentagemFrequencia < 50)
                corPorcentagemFrequencia = Color.RED;
            else if (porcentagemFrequencia < 80)
                corPorcentagemFrequencia = Color.YELLOW;
            else
                corPorcentagemFrequencia = Color.GREEN;

            txtResumoFrequenciaFiltro.setText(String.valueOf(porcentagemFrequencia) + "%");
            txtResumoFrequenciaFiltro.setTextColor(corPorcentagemFrequencia);
        }

        if(totalPressao > 0){
            int corPorcentagemPressao;

            double porcentagemPressao= (totalPressaoNormal/totalPressao) * 100;
            if(porcentagemPressao< 50)
                corPorcentagemPressao= Color.RED;
            else if (porcentagemPressao< 80)
                corPorcentagemPressao = Color.YELLOW;
            else
                corPorcentagemPressao= Color.GREEN;

            txtResumoPressaoFiltro.setText(String.valueOf(porcentagemPressao) + "%");
            txtResumoPressaoFiltro.setTextColor(corPorcentagemPressao);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaDiario();
        //  recuperaMeuDiarioTodos();
    }
}
