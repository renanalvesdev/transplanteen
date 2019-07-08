package br.com.transplanteen.tcc.transplanteen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.model.HeaderItem;
import br.com.transplanteen.tcc.transplanteen.model.ListItem;
import br.com.transplanteen.tcc.transplanteen.model.Prescricao;
import br.com.transplanteen.tcc.transplanteen.model.PrescricaoItem;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class AdapterPrescricao extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private List<ListItem> listaPrescricao;
    private Context context;

    public AdapterPrescricao(List<ListItem> listaPrescricao, Context context) {
        this.listaPrescricao = listaPrescricao;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == ListItem.TYPE_HEADER){
            View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_medicacoes, viewGroup, false);
            return new AdapterPrescricao.HeaderViewHolder(item);
        }

        else if(i == ListItem.TYPE_EVENT){
            View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_prescricao, viewGroup, false);
            return new AdapterPrescricao.MyViewHolder(item);
        }

        else
            throw new IllegalStateException("unsupported item type");
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = getItemViewType(i);

        switch(viewType){
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) listaPrescricao.get(i);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
                holder.descricaoHeader.setText(header.getStatusPrescricao());
                break;
            }

            case ListItem.TYPE_EVENT: {
                PrescricaoItem prescricaoItem = (PrescricaoItem) listaPrescricao.get(i);
                MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
                Prescricao prescricao = prescricaoItem.getPrescricao();

                // your logic here
                myViewHolder.medicamento.setText(prescricao.getMedicamento());
                myViewHolder.intervalo.setText(prescricao.getIntervaloDosesString());
                myViewHolder.periodo.setText(prescricao.intervaloDatasInicioFim());
                myViewHolder.dataProximaDose.setText(prescricao.getDataProximaDose());
                //myViewHolder.dataProximaDose.setText(DataUtil.timestampParaData(prescricao.getDataProximaDose()));
                myViewHolder.horaProximaDose.setText(prescricao.getHoraProximaDose());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listaPrescricao.get(position).getType();
    }

/*    @Override
    public void onBindViewHolder(@NonNull AdapterPrescricao.MyViewHolder myViewHolder, int i) {
        Prescricao prescricao = listaPrescricao.get(i);



    }*/

    @Override
    public int getItemCount() {
        return listaPrescricao.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medicamento, intervalo, periodo, horaProximaDose, dataProximaDose;

        public MyViewHolder (View itemView){
            super(itemView);

            medicamento = itemView.findViewById(R.id.txtAdapterMedicamento);
            intervalo = itemView.findViewById(R.id.txtAdapterIntervaloPrescricao);
            periodo = itemView.findViewById(R.id.txtAdapterPeriodoPrescricao);
            horaProximaDose = itemView.findViewById(R.id.txtAdapterHoraProximaDosePrescricao);
            dataProximaDose = itemView.findViewById(R.id.txtAdapterDataProximaDosePrescricao);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoHeader;

        public HeaderViewHolder (View itemView){
            super(itemView);
            descricaoHeader = itemView.findViewById(R.id.headerTxt);
        }
    }
}
