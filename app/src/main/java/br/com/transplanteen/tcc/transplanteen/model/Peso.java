package br.com.transplanteen.tcc.transplanteen.model;

import br.com.transplanteen.tcc.transplanteen.constants.StatusPeso;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class Peso {

    private Double valor;
    private String data;
    private StatusPeso statusPeso;

    public Peso(Double valor) {
        this.valor = valor;
        this.data = DataUtil.dataAgora();

    }

    public Peso() {
    }

    public Double getValor() {
        return valor;
    }

    public String getValorString(){
        return String.valueOf(valor);
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public StatusPeso getStatusPeso() {

        return statusPeso;
    }

    public void setStatusPeso(Double peso) {
        this.statusPeso = statusPeso;
    }
}
