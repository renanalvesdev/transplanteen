package br.com.transplanteen.tcc.transplanteen.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.helper.SimpleCallback;

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

    public static void getEnfermeiro( final SimpleCallback<Enfermeiro> myCallback) {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference enfermeirosRef = firebaseRef.child("enfermeiros").child(ConfiguracaoFirebase.getIdUsuario());

        enfermeirosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Enfermeiro enfermeiro = dataSnapshot.getValue(Enfermeiro.class);
                    myCallback.callback(enfermeiro);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
