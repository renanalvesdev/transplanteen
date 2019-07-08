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
import br.com.transplanteen.tcc.transplanteen.model.Consulta;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class AdapterConsultasPaciente extends RecyclerView.Adapter <AdapterConsultasPaciente.MyViewHolder>{

    private List<Consulta> consultasPaciente;
    private Context context;

    public AdapterConsultasPaciente(List<Consulta> consultasPaciente, Context context) {
        this.consultasPaciente = consultasPaciente;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_consulta, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Consulta consulta = consultasPaciente.get(i);

        myViewHolder.altura.setText(consulta.getDadosConsulta().getAltura());
        myViewHolder.peso.setText(consulta.getDadosConsulta().getPeso());
        myViewHolder.frequencia.setText(consulta.getDadosConsulta().getFrequenciaCardiaca());
        myViewHolder.pressao.setText(consulta.getDadosConsulta().getPressao());
        myViewHolder.nomeEnfermeiro.setText(consulta.getNomeEnfermeiroExibicao());
    /*    myViewHolder.nomeEnfermeiro.setText(consulta.getNomeEnfermeiro());*/
        myViewHolder.dataConsulta.setText(DataUtil.timestampParaData(String.valueOf(consulta.getDataConsulta())));
    }

    @Override
    public int getItemCount() {
        return consultasPaciente.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView altura, peso, frequencia, pressao, nomeEnfermeiro, dataConsulta;

        public MyViewHolder (View itemView){
            super(itemView);

            altura= itemView.findViewById(R.id.txtAlturaPacienteConsulta);
            peso= itemView.findViewById(R.id.txtPesoPacienteConsulta);
            frequencia = itemView.findViewById(R.id.txtFrequenciaPacienteConsulta);
            pressao = itemView.findViewById(R.id.txtPressaoPacienteConsulta);
            nomeEnfermeiro = itemView.findViewById(R.id.txtNomeEnfermeiro);
          /*  nomeEnfermeiro = itemView.findViewById(R.id.txtNomeEnfermeiroConsulta);*/
            dataConsulta = itemView.findViewById(R.id.txtDataConsulta);

        }


    }

}
