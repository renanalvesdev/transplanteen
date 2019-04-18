package br.com.transplanteen.tcc.transplanteen.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Date;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;

public class Consulta implements Serializable {

    private String dataConsulta;
    private String id;
    private DadosConsulta dadosConsulta;
    private String idPressao, idPeso, idAltura, idFrequenciaCardiaca;


    public  Consulta (){
        this.setDataConsulta(String.valueOf(new Date().getTime()));
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference pacientes = firebaseRef.child("consultas").child(id);

        pacientes.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPressao() {
        return idPressao;
    }

    public void setIdPressao(String idPressao) {
        this.idPressao = idPressao;
    }

    public String getIdPeso() {
        return idPeso;
    }

    public void setIdPeso(String idPeso) {
        this.idPeso = idPeso;
    }

    public String getIdAltura() {
        return idAltura;
    }

    public void setIdAltura(String idAltura) {
        this.idAltura = idAltura;
    }

    public String getIdFrequenciaCardiaca() {
        return idFrequenciaCardiaca;
    }

    public void setIdFrequenciaCardiaca(String idFrequenciaCardiaca) {
        this.idFrequenciaCardiaca = idFrequenciaCardiaca;
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
