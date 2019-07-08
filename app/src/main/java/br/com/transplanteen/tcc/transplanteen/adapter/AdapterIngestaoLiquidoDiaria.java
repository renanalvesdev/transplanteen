package br.com.transplanteen.tcc.transplanteen.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Dose;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

public class AdapterIngestaoLiquidoDiaria extends RecyclerView.Adapter <AdapterIngestaoLiquidoDiaria.MyViewHolder>{

    private List<Dose> listaDose;
    private Context context;

    private DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference ingestaoLiquidoDosesDiaRef =
            database.child("ingestao_liquido_doses")
            .child(ConfiguracaoFirebase.getIdUsuario())
            .child(DataUtil.hojeTimestamp());

    public AdapterIngestaoLiquidoDiaria(List<Dose> listaIngestaoLiquido, Context context) {
        this.listaDose = listaIngestaoLiquido;
        this.context = context;
    }

    @NonNull
    @Override
    public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_ingestao_liquido_diaria, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Dose dose = listaDose.get(i);
        myViewHolder.horario.setText(DataUtil.somenteHoras(dose.getData()));
        myViewHolder.tipoDose.setText(dose.getDescricao());
        myViewHolder.iconDeletaDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeletarDose(v.getContext(), dose);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listaDose.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView horario, tipoDose;
        ImageView iconDeletaDose;

        public MyViewHolder (View itemView){
            super(itemView);

            horario = itemView.findViewById(R.id.txtHorario);
            tipoDose = itemView.findViewById(R.id.txtTipoDose);
            iconDeletaDose = itemView.findViewById(R.id.iconDeletaDose);

        }
    }


    public void dialogDeletarDose(final Context context, final Dose tipoDose) {

        String mensagem = "Remover " + tipoDose.getDescricao() + " ?";
        final String msgSucesso = tipoDose.getDescricao() + " Removida!";

        new AlertDialog.Builder(context)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletaDose( context ,tipoDose.getId());
                    }
                })
                .setNegativeButton("Não", null)
                .show();

    }

    public void deletaDose(final Context context, String id){

        ingestaoLiquidoDosesDiaRef.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MessagesUtil.toastInfo(context, "Dose removida com sucesso !");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MessagesUtil.toastInfo(context, "Erro:!"+e.getMessage());
            }
        });


    }
}
