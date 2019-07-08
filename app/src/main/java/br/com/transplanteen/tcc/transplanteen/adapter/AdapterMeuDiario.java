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
import br.com.transplanteen.tcc.transplanteen.model.DadosDiario;
import br.com.transplanteen.tcc.transplanteen.model.Peso;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class AdapterMeuDiario extends RecyclerView.Adapter <AdapterMeuDiario.MyViewHolder> {

    private List<DadosDiario> listaDadosMeuDiario;
    private Context context;

    public AdapterMeuDiario(List<DadosDiario> listaDadosMeuDiario, Context context) {
        this.listaDadosMeuDiario = listaDadosMeuDiario;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_meu_diario, viewGroup, false);
        return new MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        DadosDiario dadosDiario = listaDadosMeuDiario.get(i);
        String pesoValor  = dadosDiario.getPeso().getValorString();
        String frequenciaValor  = dadosDiario.getFrequencia().getValorString();
        String pressao = dadosDiario.getPressao().getValorPressao();
        String data = dadosDiario.getDataFormatada();
        String diaSemana =  dadosDiario.getDiaFormatado();


        myViewHolder.peso.setText(pesoValor);
        myViewHolder.pressao.setText(pressao);
        myViewHolder.frequencia.setText(frequenciaValor);
        myViewHolder.data.setText(data);
        myViewHolder.diaSemana.setText(diaSemana);
    }

    @Override
    public int getItemCount() {
        return listaDadosMeuDiario.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView peso, pressao, frequencia, diaSemana, data;

        public MyViewHolder (View itemView){
            super(itemView);

            peso = itemView.findViewById(R.id.txtPesoAdapterDiario);
            pressao = itemView.findViewById(R.id.txtPressaoAdapterDiario);
            frequencia = itemView.findViewById(R.id.txtFrequenciaAdapterDiario);
            diaSemana = itemView.findViewById(R.id.txtDiaSemanaAdapterDiario);
            data = itemView.findViewById(R.id.txtDataAdapterDiario);
        }
    }
}
