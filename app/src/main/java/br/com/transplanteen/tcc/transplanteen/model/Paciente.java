package br.com.transplanteen.tcc.transplanteen.model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Date;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;

public class Paciente implements Serializable {


    private String id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String dataNascimento;

    private String tipoTransplante;
    private String pesoAtual;
    private String alturaAtual;
    private String frequenciaCardiacaAtual;
    private String pressaoAtual;
    private String situacaoMedicacao;
    private String situacaoIngestaoLiquido;
    private boolean status;



    public Paciente() {

        DatabaseReference pacienteRef = ConfiguracaoFirebase.getFirebaseDatabase().child("pacientes");
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pacientes = firebaseRef.child("pacientes").child(id);

        pacientes.setValue(this);
    }

    public void salvarEnfermeiroPaciente() {

        String idEnfermeiro = ConfiguracaoFirebase.getIdUsuario();
        System.out.println("ID DE QUEM ESTÁ LOGADO: " + idEnfermeiro);

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference enfermeiroPaciente = firebaseRef.child("enfermeiro_paciente").child(idEnfermeiro).child(id);

        enfermeiroPaciente.setValue(this);
    }

    public static void getPaciente( final SimpleCallback<Paciente> myCallback) {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pacientesRef = firebaseRef.child("pacientes").child(ConfiguracaoFirebase.getIdUsuario());

        pacientesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Paciente paciente = dataSnapshot.getValue(Paciente.class);
                    myCallback.callback(paciente);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getPesoAtual() {
        return pesoAtual;
    }

    public void setPesoAtual(String pesoAtual) {
        this.pesoAtual = pesoAtual;
    }

    public String getAlturaAtual() {
        return alturaAtual;
    }

    public void setAlturaAtual(String alturaAtual) {
        this.alturaAtual = alturaAtual;
    }

    public String getFrequenciaCardiacaAtual() {
        return frequenciaCardiacaAtual;
    }

    public void setFrequenciaCardiacaAtual(String frequenciaCardiacaAtual) {
        this.frequenciaCardiacaAtual = frequenciaCardiacaAtual;
    }

    public String getPressaoAtual() {
        return pressaoAtual;
    }

    public void setPressaoAtual(String pressaoAtual) {
        this.pressaoAtual = pressaoAtual;
    }

    public String getSituacaoMedicacao() {
        return situacaoMedicacao;
    }

    public void setSituacaoMedicacao(String situacaoMedicacao) {
        this.situacaoMedicacao = situacaoMedicacao;
    }

    public String getSituacaoIngestaoLiquido() {
        return situacaoIngestaoLiquido;
    }

    public void setSituacaoIngestaoLiquido(String situacaoIngestaoLiquido) {
        this.situacaoIngestaoLiquido = situacaoIngestaoLiquido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipoTransplante() {
        return tipoTransplante;
    }

    public void setTipoTransplante(String tipoTransplante) {
        this.tipoTransplante = tipoTransplante;
    }
}
