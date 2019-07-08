package br.com.transplanteen.tcc.transplanteen.model;

import android.graphics.Color;

import br.com.transplanteen.tcc.transplanteen.constants.StatusFrequencia;
import br.com.transplanteen.tcc.transplanteen.constants.StatusPressao;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class Frequencia {

    private String data;
    private Integer statusFrequencia;
    private Integer valor;

    public Frequencia() {
    }

    public Frequencia(Integer valor) {
        this.valor = valor;
        this.data = DataUtil.dataAgora();
        setStatusFrequencia();

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatusFrequencia() {
        return statusFrequencia;
    }

    public void setStatusFrequencia() {
        Integer retorno = StatusFrequencia.NORMAL;

        if(this.valor < 60)
            retorno = StatusFrequencia.BAIXA;

        else if(this.valor > 100)
            retorno = StatusFrequencia.ALTA;

        this.statusFrequencia = retorno;
    }

    public String getStatusFrequenciaString(){
        if(statusFrequencia == StatusFrequencia.BAIXA)
            return "Baixa";
        else if (statusFrequencia == StatusFrequencia.ALTA)
            return "Alta";
        else
            return "Normal";
    }

    public String getValorString(){
        return String.valueOf(valor);
    }

    public int getCor(){
        int cor = -1;

        if(statusFrequencia == StatusFrequencia.NORMAL)
            cor = Color.GREEN;
        else
            cor = Color.RED;

        return cor;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
