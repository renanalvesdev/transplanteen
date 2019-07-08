package br.com.transplanteen.tcc.transplanteen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class AdapterIngestaoLiquidoFiltro extends RecyclerView.Adapter <AdapterIngestaoLiquidoFiltro.MyViewHolder>{



    private List<IngestaoLiquido> listaIngestaoLiquido;
    private Context context;

    public AdapterIngestaoLiquidoFiltro(List<IngestaoLiquido> listaIngestaoLiquido, Context context) {
        this.listaIngestaoLiquido = listaIngestaoLiquido;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_ingestao_liquido_filtro, viewGroup, false);
        return new AdapterIngestaoLiquidoFiltro.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngestaoLiquidoFiltro.MyViewHolder myViewHolder, int i) {
        final IngestaoLiquido ingestaoLiquido = listaIngestaoLiquido.get(i);
        myViewHolder.diaIngestaoLiquido.setText(DataUtil.diaSemana(ingestaoLiquido.getDia()));
        myViewHolder.dataIngestaoLiquido.setText(DataUtil.timestampParaData(ingestaoLiquido.getDia()));
        myViewHolder.totalIngestaoLiquido.setText(ingestaoLiquido.getQuantidadeAlcancada() + " L");
        myViewHolder.iconeStatusIngestaoLiquido.setImageResource(selecionaIconeStatus(ingestaoLiquido.getStatus()));

    }

    @Override
    public int getItemCount() {
        return listaIngestaoLiquido.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView diaIngestaoLiquido, dataIngestaoLiquido, totalIngestaoLiquido;
        ImageView iconeStatusIngestaoLiquido;

        public MyViewHolder (View itemView){
            super(itemView);

            diaIngestaoLiquido = itemView.findViewById(R.id.txtAdapterDiaIngestaoLiquido);
            dataIngestaoLiquido = itemView.findViewById(R.id.txtAdapterDataIngestaoLiquido);
            totalIngestaoLiquido = itemView.findViewById(R.id.txtAdapterTotalIngestaoLiquido);
            iconeStatusIngestaoLiquido= itemView.findViewById(R.id.iconAdapterStatusIngestaoLiquido);

        }


    }

    public int selecionaIconeStatus(String status){
        int iconeStatus = 0;

        if(status.equals("Bom"))
            iconeStatus = R.drawable.ic_ingestao_liquido_bom;
        else if(status.equals("Razoavel"))
            iconeStatus = R.drawable.ic_ingestao_liquido_normal;
        else if(status.equals("Ruim"))
            iconeStatus = R.drawable.ic_ingestao_liquido_ruim;

        return iconeStatus;
    }

}
