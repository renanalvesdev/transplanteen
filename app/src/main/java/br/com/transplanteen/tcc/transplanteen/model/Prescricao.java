package br.com.transplanteen.tcc.transplanteen.model;

import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class Prescricao {

    private String idPrescricao;
    private String medicamento;
    private String dataInicio;
    private String dataFim;
    private String horaInicio;
    private String dataProximaDose;
    private String horaProximaDose;
    private Integer statusPrescricao;
    private Integer intervalo;

    public Integer getStatusPrescricao() {
        return statusPrescricao;
    }

    public void setStatusPrescricao(Integer statusPrescricao) {
        this.statusPrescricao = statusPrescricao;
    }

    public String getDataProximaDose() {
        return dataProximaDose;
    }

    public void setDataProximaDose(String dataProximaDose) {
        this.dataProximaDose = dataProximaDose;
    }

    public String getHoraProximaDose() {
        return horaProximaDose;
    }

    public void setHoraProximaDose(String horaProximaDose) {
        this.horaProximaDose = horaProximaDose;
    }

    public String intervaloDatasInicioFim(){
        return  dataInicio + " - " + dataFim;//DataUtil.timestampParaData(dataInicio) + " - " + DataUtil.timestampParaData(dataFim);
    }

    public String getIntervaloDosesString(){
        return intervalo + " em " + intervalo + " horas";
    }

    public String getIdPrescricao() {
        return idPrescricao;
    }

    public void setIdPrescricao(String idPrescricao) {
        this.idPrescricao = idPrescricao;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim()
    {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim =  dataFim;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }
}
