package br.com.transplanteen.tcc.transplanteen.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.activity.CadastroEnfermeiroActivity;
import br.com.transplanteen.tcc.transplanteen.activity.EnfermeiroActivity;
import br.com.transplanteen.tcc.transplanteen.activity.NovoPacienteActivity;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterPacientes;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;

/**
 * A simple {@link Fragment} subclass.
 */
public class PacientesFragment extends Fragment {

    private RecyclerView recyclerViewPacientes;
    private List<Paciente> pacientes = new ArrayList<>();
    private AdapterPacientes adapterPacientes;
    private DatabaseReference pacientesRef;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    //private MaterialSearchView searchView;
    public PacientesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicializarComponentes();
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_enfermeiro, menu);

        //configurar botao de pesquisa
        MenuItem searchItem = menu.findItem(R.id.menuPesquisa);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Ex: João Veloso");


            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuAdicionar){
            Intent intent = new Intent(getActivity(), NovoPacienteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pacientes, container, false);

        pacientesRef = ConfiguracaoFirebase.getFirebaseDatabase().child("enfermeiro_paciente").child(ConfiguracaoFirebase.getIdUsuario());


        recyclerViewPacientes = (RecyclerView) view.findViewById(R.id.recyclerPacientes);
        recyclerViewPacientes.setHasFixedSize(true);
        recyclerViewPacientes.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterPacientes = new AdapterPacientes(pacientes, view.getContext());
        recyclerViewPacientes.setAdapter(adapterPacientes);

        //Recupera pacientes
        recuperarPacientes();

        return view;
    }

    private void recuperarPacientes(){
        pacientesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pacientes.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    pacientes.add(ds.getValue(Paciente.class));
                }

                Collections.reverse(pacientes);
                adapterPacientes.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
