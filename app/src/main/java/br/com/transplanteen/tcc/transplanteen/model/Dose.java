package br.com.transplanteen.tcc.transplanteen.model;

import java.io.Serializable;
import java.util.Date;

public class Dose implements Serializable {

    private Double dose;
    private String descricao;
    private long data;
    private String id;

    public Dose() {
    }

    public Dose(Double dose, String descricao) {
        this.dose = dose;
        this.descricao = descricao;
        this.data = new Date().getTime();
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
