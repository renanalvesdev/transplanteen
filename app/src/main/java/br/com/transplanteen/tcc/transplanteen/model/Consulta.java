package br.com.transplanteen.tcc.transplanteen.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Date;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;

public class Consulta implements Serializable {

    private String dataConsulta;
    private String nomeEnfermeiroExibicao;
    private String id;
    private DadosConsulta dadosConsulta;


    public  Consulta (){
        this.setDataConsulta(String.valueOf(new Date().getTime()));
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pacientes = firebaseRef.child("consultas").child(id);

        pacientes.setValue(this);
    }

    public String getNomeEnfermeiroExibicao() {
        return nomeEnfermeiroExibicao;
    }

    public void setNomeEnfermeiroExibicao(String nomeEnfermeiroExibicao) {
        this.nomeEnfermeiroExibicao = nomeEnfermeiroExibicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public DadosConsulta getDadosConsulta() {
        return dadosConsulta;
    }

    public void setDadosConsulta(DadosConsulta dadosConsulta) {
        this.dadosConsulta = dadosConsulta;
    }

}
