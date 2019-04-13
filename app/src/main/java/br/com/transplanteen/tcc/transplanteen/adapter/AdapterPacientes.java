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
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class AdapterPacientes extends RecyclerView.Adapter <AdapterPacientes.MyViewHolder>{

    private List<Paciente> pacientes;
    private Context context;

    public AdapterPacientes(List<Paciente> pacientes, Context context) {
        this.pacientes = pacientes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_paciente, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Paciente paciente = pacientes.get(i);
        myViewHolder.dataNascimento.setText(", " + DataUtil.calculaIdade(paciente.getDataNascimento().toString()) + " anos");
        myViewHolder.nome.setText(paciente.getNome());
        myViewHolder.tipoTransplante.setText(paciente.getTipoTransplante());
    }

    @Override
    public int getItemCount() {
        return pacientes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        TextView dataNascimento;
        TextView tipoTransplante;

        public MyViewHolder (View itemView){
            super(itemView);

            nome = itemView.findViewById(R.id.textNomePacienteLista);
            dataNascimento = itemView.findViewById(R.id.textIDataNascimentoPaciente);
            tipoTransplante = itemView.findViewById(R.id.textTipoTransplantePacienteLista);
        }


    }

}
