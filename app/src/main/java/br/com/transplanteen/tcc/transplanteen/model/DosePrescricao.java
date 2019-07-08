package br.com.transplanteen.tcc.transplanteen.model;

public class DosePrescricao {

    private String idDoseMedicamento;
    private String data;
    private String hora;
    private Integer status;

    public String getIdDoseMedicamento() {
        return idDoseMedicamento;
    }

    public void setIdDoseMedicamento(String idDoseMedicamento) {
        this.idDoseMedicamento = idDoseMedicamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
