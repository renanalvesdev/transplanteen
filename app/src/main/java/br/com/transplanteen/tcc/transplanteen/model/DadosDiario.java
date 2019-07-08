package br.com.transplanteen.tcc.transplanteen.model;

import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class DadosDiario {


    private Peso peso;
    private Pressao pressao;
    private Frequencia frequencia;
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataFormatada(){
        return DataUtil.timestampParaData(data);
    }

    public String getDiaFormatado(){
       return DataUtil.diaSemana(data);
    }


    public Peso getPeso() {
        return peso;
    }

    public void setPeso(Peso peso) {
        this.peso = peso;
    }

    public Pressao getPressao() {
        return pressao;
    }

    public void setPressao(Pressao pressao) {
        this.pressao = pressao;
    }

    public Frequencia getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Frequencia frequencia) {
        this.frequencia = frequencia;
    }
}
