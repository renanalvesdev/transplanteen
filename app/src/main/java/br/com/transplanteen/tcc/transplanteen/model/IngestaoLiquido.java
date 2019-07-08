package br.com.transplanteen.tcc.transplanteen.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;

public class IngestaoLiquido  implements Serializable {

    private String porcentagemAlcancada;
    private String quantidadeAlcancada;
    private String status;
    private String dia;

    public String getPorcentagemAlcancada() {
        return porcentagemAlcancada;
    }

    public void setPorcentagemAlcancada(String porcentagemAlcancada) {
        this.porcentagemAlcancada = porcentagemAlcancada;
    }

    public String getQuantidadeAlcancada() {
        return quantidadeAlcancada;
    }

    public void setQuantidadeAlcancada(String quantidadeAlcancada) {
        this.quantidadeAlcancada = quantidadeAlcancada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
