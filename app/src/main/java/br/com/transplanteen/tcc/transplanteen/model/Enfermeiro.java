package br.com.transplanteen.tcc.transplanteen.model;

import com.google.firebase.database.DatabaseReference;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;

public class Enfermeiro {

    private String id;
    private String numeroInscricao;
    private String tipoInscricao;
    private String situacaoInscricao;
    private String tipoRegistro;
    private String nome;
    private String email;
    private String senha;

    public Enfermeiro() {
        DatabaseReference enfermeirosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("enfermeiros");

    }

    public void salvar(){

        id = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference enfermeiros = firebaseRef.child("enfermeiros").child(id);

        enfermeiros.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroInscricao() {
        return numeroInscricao;
    }

    public void setNumeroInscricao(String numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public String getTipoInscricao() {
        return tipoInscricao;
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public String getSituacaoInscricao() {
        return situacaoInscricao;
    }

    public void setSituacaoInscricao(String situacaoInscricao) {
        this.situacaoInscricao = situacaoInscricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}
