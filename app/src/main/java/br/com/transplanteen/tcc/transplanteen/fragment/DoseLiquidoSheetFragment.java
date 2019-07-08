package br.com.transplanteen.tcc.transplanteen.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.lang.ref.ReferenceQueue;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.DialogUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoseLiquidoSheetFragment extends BottomSheetDialogFragment {

    boolean retornoDialogo = false;
    android.app.AlertDialog dialogProcessando;
    DatabaseReference database, ingestaoLiquidoDosesDiaRef;



    private IngestaoLiquido ingestaoLiquido;
    private String idUsuario = ConfiguracaoFirebase.getIdUsuario();

    static final Double DOSE_COPO = 250.0;
    static final Double DOSE_GARRAFA_PEQUENA = 500.0;
    static final Double DOSE_GARRAFA_GRANDE = 1000.0;

    public DoseLiquidoSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.bottom_sheet_ingestao_liquido, container, false);

        ingestaoLiquido = (IngestaoLiquido) getArguments().getSerializable("ingestaoLiquido");
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        ingestaoLiquidoDosesDiaRef = database.child("ingestao_liquido_doses").child(ConfiguracaoFirebase.getIdUsuario()).child(DataUtil.hojeTimestamp());

        //obs: colocar num metodo
        LinearLayout layoutDoseCopo = v.findViewById(R.id.layoutDoseCopo);
        LinearLayout layoutDoseGarrafaPequena = v.findViewById(R.id.layoutDoseGarrafaPequena);
        LinearLayout layoutDoseGarrafaGrande= v.findViewById(R.id.layoutDoseGarrafaGrande);

        inicializarComponentes();
        layoutDoseCopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copo
                Dose copo = new Dose(DOSE_COPO, getString(R.string.copo));
                adicionarDose(v.getContext(), copo);
            }
        });

        layoutDoseGarrafaPequena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copo
                Dose garrafaPequena = new Dose(DOSE_GARRAFA_PEQUENA, getString(R.string.garrafaPequena));
                adicionarDose(v.getContext(), garrafaPequena);
            }
        });

        layoutDoseGarrafaGrande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copo
                Dose garrafaGrande = new Dose(DOSE_GARRAFA_GRANDE, getString(R.string.garrafaGrande));
                adicionarDose(v.getContext(), garrafaGrande);
            }
        });

        return v;
    }



    public void adicionarDose(Context context, final Dose tipoDose) {

        String mensagem = "Adicionar " + tipoDose.getDescricao() + " ?";
        final String msgSucesso = tipoDose.getDescricao() + " Adicionado !";

        new AlertDialog.Builder(context)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MessagesUtil.toastInfo(getContext(), msgSucesso );
                        escreveDose(tipoDose);
                    }
                })
                .setNegativeButton("Não", null)
                .show();

    }

    public void escreveDose(Dose tipoDose){

        dialogProcessando.show();

        String doseId = ingestaoLiquidoDosesDiaRef.push().getKey();
        tipoDose.setId(doseId);

        ingestaoLiquidoDosesDiaRef.child(doseId).setValue(tipoDose).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //atualizaIngestaoDiaria();
                dialogProcessando.dismiss();
                dismiss();
            }
        });
    }


    public void inicializarComponentes(){
        dialogProcessando = new SpotsDialog
                .Builder()
                .setContext(getContext())
                .setMessage("Carregando")
                .setCancelable(false).build();
    }

}
