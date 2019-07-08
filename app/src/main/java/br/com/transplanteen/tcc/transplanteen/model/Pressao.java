package br.com.transplanteen.tcc.transplanteen.model;

import android.graphics.Color;

import br.com.transplanteen.tcc.transplanteen.constants.StatusPressao;
import br.com.transplanteen.tcc.transplanteen.util.DataUtil;

public class Pressao {

    private String  data;
    private Integer pressaoSistolica;
    private Integer pressaoDiastolica;
    private Integer statusPressao;


    public Pressao() {
    }

    public Pressao(Integer pressaoSistolica, Integer pressaoDiastolica) {
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
        this.data = DataUtil.dataAgora();
        setStatusPressao();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPressaoSistolica() {
        return pressaoSistolica;
    }

    public void setPressaoSistolica(Integer pressaoSistolica) {
        this.pressaoSistolica = pressaoSistolica;
    }

    public Integer getPressaoDiastolica() {
        return pressaoDiastolica;
    }

    public void setPressaoDiastolica(Integer pressaoDiastolica) {
        this.pressaoDiastolica = pressaoDiastolica;
    }

    public Integer getStatusPressao() {
        return statusPressao;
    }

    public void setStatusPressao() {
        Integer retorno = null;

        if(this.pressaoSistolica < 120 && this.pressaoDiastolica < 80)
            retorno = StatusPressao.NORMAL;

        else if((this.pressaoSistolica >= 120 && this.pressaoSistolica < 130)
                && (this.pressaoDiastolica < 80))
            retorno = StatusPressao.ELEVADA;

        else if ( (this.pressaoSistolica >= 130 && this.pressaoSistolica < 140)
                || (this.pressaoDiastolica >= 80 && this.pressaoDiastolica < 90))
            retorno = StatusPressao.HIPERTENSAO_ESTAGIO_1;

        else if( this.pressaoSistolica >= 140 || this.pressaoDiastolica >= 90)
            retorno = StatusPressao.HIPERTENSAO_ESTAGIO_2;

        else if( this.pressaoSistolica >= 180 || this.pressaoDiastolica >= 120)
            retorno = StatusPressao.HIPERTENSAO_ESTAGIO_2;

        this.statusPressao = retorno;
    }

    public String getStatusPressaoString(){
        String retorno = "";
        if(statusPressao == StatusPressao.NORMAL)
            retorno = "Normal";
        else if (statusPressao == StatusPressao.ELEVADA)
            retorno = "Elevada";
        else if (statusPressao == StatusPressao.HIPERTENSAO_ESTAGIO_1)
            retorno = "Hipertensão Estágio 1";
        else if (statusPressao == StatusPressao.HIPERTENSAO_ESTAGIO_2)
            retorno = "Hipertensão Estágio 2";
        else if(statusPressao == StatusPressao.CRISE_HIPERTENSIVA)
            retorno = "Crise Hipertensiva";

        return retorno;
    }

    public int getCor(){
        int cor = -1;

        if(statusPressao == StatusPressao.NORMAL)
            cor = Color.GREEN;
        else
            cor = Color.RED;

        return cor;
    }

    public String getValorPressao(){
        return String.valueOf(pressaoSistolica) + "/" + String.valueOf(pressaoDiastolica);
    }
}
