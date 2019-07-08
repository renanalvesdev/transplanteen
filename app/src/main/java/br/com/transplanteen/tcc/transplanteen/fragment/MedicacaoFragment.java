package br.com.transplanteen.tcc.transplanteen.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.adapter.AdapterPrescricao;
import br.com.transplanteen.tcc.transplanteen.constants.StatusPrescricao;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.RecyclerItemClickListener;
import br.com.transplanteen.tcc.transplanteen.model.HeaderItem;
import br.com.transplanteen.tcc.transplanteen.model.ListItem;
import br.com.transplanteen.tcc.transplanteen.model.Medicamento;
import br.com.transplanteen.tcc.transplanteen.model.Paciente;
import br.com.transplanteen.tcc.transplanteen.model.Prescricao;
import br.com.transplanteen.tcc.transplanteen.model.PrescricaoItem;
import br.com.transplanteen.tcc.transplanteen.notifications.Client;
import br.com.transplanteen.tcc.transplanteen.notifications.Data;
import br.com.transplanteen.tcc.transplanteen.notifications.MyResponse;
import br.com.transplanteen.tcc.transplanteen.notifications.Sender;
import br.com.transplanteen.tcc.transplanteen.notifications.Token;
import br.com.transplanteen.tcc.transplanteen.util.MessagesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicacaoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Paciente paciente = (Paciente) getArguments().getSerializable("paciente");

    private DatabaseReference medicamentosRef, prescricaoPacienteRef, database;
    private ValueEventListener valueEventListenerMedicamentos, valueEventListenerPrescricoes;
    private List<String> medicamentos = new ArrayList<>();
    private List<ListItem> prescricaoList = new ArrayList<>();
    private AdapterPrescricao adapterPrescricao;
    private RecyclerView recyclerViewPrescricao;

    Paciente paciente = new Paciente();

    String userId;

    APIService apiService;

    TextView editDataInicial, editDataFinal, editHoraInicialMedicacao, editIntervaloMedicacao, txtDataInicial, txtIntervaloHoras;

    Button btnSalvarPrescricao;

    Spinner spinnerMedicacao;

    boolean notify = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Paciente pacienteArgs;

    /*  private OnFragmentInteractionListener mListener;
     */
    public MedicacaoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MedicacaoFragment.
     */
  /*  // TODO: Rename and change types and number of parameters
    public static MedicacaoFragment newInstance(String param1, String param2) {
        MedicacaoFragment fragment = new MedicacaoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            pacienteArgs = (Paciente) getArguments().getSerializable("paciente");

        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        if(pacienteArgs != null){
            paciente = pacienteArgs;
        }
        else {
            paciente.setId(ConfiguracaoFirebase.getIdUsuario());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        medicamentosRef = database.child("medicamentos");
        prescricaoPacienteRef = database.child("prescricao_paciente").child(paciente.getId());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_medicacao, container, false);


        //configura adapter
        adapterPrescricao = new AdapterPrescricao(prescricaoList, v.getContext());
        //configura recycler view
        recyclerViewPrescricao = (RecyclerView) v.findViewById(R.id.recyclerPrescricao);
        recyclerViewPrescricao.setHasFixedSize(true);
        recyclerViewPrescricao.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPrescricao.setAdapter(adapterPrescricao);
        recyclerViewPrescricao.addOnItemTouchListener (new RecyclerItemClickListener(
                v.getContext(),
                recyclerViewPrescricao,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                      //  confirmaPrescricao(prescricaoList.get(position));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));


        FloatingActionButton fab = v.findViewById(R.id.fabMedicacao);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreDialogoMedicacao();
            }
        });

        return v;
    }

    public void confirmaPrescricao(Prescricao prescricao){

        String nomeMedicamento = "Medicamento: " + prescricao.getMedicamento();
        String cicloMedicamento= "Ciclo: " + prescricao.getIntervaloDosesString();
        String dataInicio = "Primeira dose: " + prescricao.getDataInicio() + " às " + prescricao.getHoraInicio();
        String dataFinal = "Tomar até o dia: " + prescricao.getDataFim();

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmar Prescrição ?")
                .setMessage(nomeMedicamento + "\n\n" + cicloMedicamento
                + "\n\n" + dataInicio + "\n\n" + dataFinal)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MessagesUtil.toastInfo(getContext(), "Medicamento adicionado ...");
                    }
                })
                .setNegativeButton("Não", null)
                .show();

    }


 /*   // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    public void abreDialogoMedicacao() {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_medicacao, null);

        //inicializa componentes do dialogo de medicação
        spinnerMedicacao = mView.findViewById(R.id.spinnerMedicamento);
        editDataInicial = mView.findViewById(R.id.editDataInicialMedicacao);
        editDataFinal = mView.findViewById(R.id.editDataFinalMedicacao);
        editHoraInicialMedicacao = mView.findViewById(R.id.editHoraInicialMedicacao);
        editIntervaloMedicacao = mView.findViewById(R.id.editIntervaloMedicacao);

        txtDataInicial = mView.findViewById(R.id.txtDataInicialMedicamento);
        txtIntervaloHoras = mView.findViewById(R.id.txtIntervaloHorasMedicacao);

        Calendar calendar = Calendar.getInstance();
        final int ano = calendar.get(Calendar.YEAR);
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);

        editDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editDataInicial.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });


        editDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editDataFinal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        editHoraInicialMedicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                editHoraInicialMedicacao.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        editIntervaloMedicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intervaloPickerDialog();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, medicamentos);
        spinnerMedicacao.setAdapter(adapter);


        builder.setView(mView);
        builder.setPositiveButton("SALVAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Prescricao prescricao = new Prescricao();
                String dataInicio = editDataInicial.getText().toString();
                String dataFim = editDataFinal.getText().toString();
                String hora = editHoraInicialMedicacao.getText().toString();
                String medicamento = spinnerMedicacao.getSelectedItem().toString();
                String intervalo = editIntervaloMedicacao.getText().toString();

                prescricao.setDataInicio(dataInicio);
                prescricao.setDataFim(dataFim);
                prescricao.setHoraInicio(hora);
                prescricao.setDataProximaDose(dataInicio);
                prescricao.setHoraProximaDose(hora);
                prescricao.setMedicamento(medicamento);
                prescricao.setIntervalo(Integer.valueOf(intervalo));

                salvaPrescricao(prescricao);
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        dialog = builder.create();
        dialog.show();

        //  Button btnFecharDetalhesDiario= mView.findViewById(R.id.buttonFecharDetalhesDiario);
    }

    public void intervaloPickerDialog() {
        NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(12);

        NumberPicker.OnValueChangeListener changeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String valor = String.valueOf(newVal);
                txtIntervaloHoras.setText("De " + valor + " em " + valor + " horas ");
            }
        };

        numberPicker.setOnValueChangedListener(changeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(numberPicker);
        builder.setTitle("Intervalo entre as medicações (horas)");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        builder.show();
    }

    //medicamentos do spinner
    public void recuperaMedicamentos() {
        valueEventListenerMedicamentos = medicamentosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicamentos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Medicamento medicamento = ds.getValue(Medicamento.class);
                    medicamentos.add(medicamento.getDescricao());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void recuperaPrescricoes() {
        Query query = prescricaoPacienteRef.orderByChild("statusPrescricao");
        valueEventListenerPrescricoes = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    prescricaoList.clear();
                    Integer status = -1;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Prescricao prescricao = ds.getValue(Prescricao.class);
                        if(prescricao.getStatusPrescricao() != status){
                            status = prescricao.getStatusPrescricao();
                            HeaderItem header = new HeaderItem();
                            header.setStatusPrescricao(prescricao.getStatusPrescricao());
                            prescricaoList.add(header);
                        }

                        PrescricaoItem item = new PrescricaoItem();
                        item.setPrescricao(prescricao);
                        prescricaoList.add(item);

                        adapterPrescricao.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void salvaPrescricao(Prescricao prescricao) {

        notify = true;
        String idPrescricao = prescricaoPacienteRef.push().getKey();
        prescricao.setIdPrescricao(idPrescricao);
        prescricao.setStatusPrescricao(StatusPrescricao.ESPERANDO_CONFIRMACAO);

        prescricaoPacienteRef.child(idPrescricao).setValue(prescricao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MessagesUtil.toastInfo(getContext(), "Prescrição salva !");
                    //updateToken(FirebaseInstanceId.getInstance().getToken());
                    sendNotification(paciente.getId() , "Confirme em 'Minhas Medicações' ", "Nova(s) Medicação(ões) Adicionada(s)");
                } else {
                    MessagesUtil.toastInfo(getContext(), "Não foi possível salvar a prescrição !");
                }
            }
        });
    }


    private void sendNotification(String receptor, final String descricao, final String mensagem) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receptor);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(ConfiguracaoFirebase.getIdUsuario(), R.drawable.logo_transplanteen , descricao , mensagem, paciente.getId());
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code() == 200){
                                if(response.body().sucess == 1){
                                    MessagesUtil.toastInfo(getContext(), "Falhou");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(ConfiguracaoFirebase.getIdUsuario()).setValue(token1);
    }


    @Override
    public void onStart() {
        super.onStart();
        recuperaMedicamentos();
        recuperaPrescricoes();
    }

    /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
  /*  @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
