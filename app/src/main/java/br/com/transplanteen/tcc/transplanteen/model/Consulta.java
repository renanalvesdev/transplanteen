package br.com.transplanteen.tcc.transplanteen.model;

public class Consulta {

    private long dataConsulta;
    private String idEnfermeiro;
    private Paciente dadosPaciente;
    

    public long getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(long dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getIdEnfermeiro() {
        return idEnfermeiro;
    }

    public void setIdEnfermeiro(String idEnfermeiro) {
        this.idEnfermeiro = idEnfermeiro;
    }

    public Paciente getDadosPaciente() {
        return dadosPaciente;
    }

    public void setDadosPaciente(Paciente dadosPaciente) {
        this.dadosPaciente = dadosPaciente;
    }
}
