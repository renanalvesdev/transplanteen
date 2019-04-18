package br.com.transplanteen.tcc.transplanteen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.activity.CadastroEnfermeiroActivity;
import br.com.transplanteen.tcc.transplanteen.activity.ConsultaActivity;
import br.com.transplanteen.tcc.transplanteen.activity.PacienteActivity;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterConsultasPaciente;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterPacientes;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.RecyclerItemClickListener;
import br.com.transplanteen.tcc.transplanteen.model.Consulta;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasPacienteFragment extends Fragment {

    FloatingActionButton fab;
    private RecyclerView recyclerViewConsultasPaciente;
    private List<Consulta> consultas = new ArrayList<>();
    private AdapterConsultasPaciente adapterConsultasPaciente;

    private DatabaseReference consultasPacienteRef, consultasRef, database;
    private ValueEventListener valueEventListenerConsultasPaciente;
    private ValueEventListener valueEventListenerConsultas;

    private Intent intentConsulta;
    Paciente paciente;

    public ConsultasPacienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consultas_paciente, container, false);
        paciente = (Paciente) getArguments().getSerializable("paciente");


        //preparando intent de consulta
        intentConsulta = new Intent(getActivity(), ConsultaActivity.class);
        intentConsulta.putExtra("paciente", paciente);

        //ação do floatting action button
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentConsulta);
            }
        });

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        consultasPacienteRef = database.child("paciente_consulta").child(paciente.getId());

        //configura adapter
        adapterConsultasPaciente= new AdapterConsultasPaciente(consultas , v.getContext());

        //configura recycler view
        recyclerViewConsultasPaciente = (RecyclerView) v.findViewById(R.id.recyclerConsultas);
        recyclerViewConsultasPaciente.setHasFixedSize(true);
        recyclerViewConsultasPaciente.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewConsultasPaciente.setAdapter(adapterConsultasPaciente);

        //configurar evento de clique no recycler view
        recyclerViewConsultasPaciente.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewConsultasPaciente,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Consulta consulta = consultas.get(position);
                        intentConsulta.putExtra("consultaPaciente",  consulta);
                        startActivity(intentConsulta);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        return v;
    }


    private void recuperarConsultasPaciente(){

       valueEventListenerConsultasPaciente =  consultasPacienteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                consultas.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    recuperarConsultas(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void recuperarConsultas(String idConsulta ){

        consultasRef = database.child("consultas").child(idConsulta);

       valueEventListenerConsultas = consultasRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Consulta consulta = dataSnapshot.getValue(Consulta.class);
                   consultas.add(consulta);
                   adapterConsultasPaciente.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       })  ;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConsultasPaciente();

    }

    @Override
    public void onStop() {
        super.onStop();
        consultasPacienteRef.removeEventListener(valueEventListenerConsultasPaciente);
        if(!consultas.isEmpty())
            consultasRef.removeEventListener(valueEventListenerConsultas);
    }
}
