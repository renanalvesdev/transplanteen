package br.com.transplanteen.tcc.transplanteen.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;

public class Paciente {


    private String id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String dataNascimento;
    private String tipoTransplante;
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
