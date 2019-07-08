package br.com.transplanteen.tcc.transplanteen.model;

import br.com.transplanteen.tcc.transplanteen.constants.StatusPrescricao;

public class HeaderItem extends ListItem {

    private Integer statusPrescricao;

    public String getStatusPrescricao() {
        switch (statusPrescricao){
            case StatusPrescricao.ESPERANDO_CONFIRMACAO:{
                return "Esperando Confirmação";
            }

            case StatusPrescricao.TOMANDO:{
                return "Tomando";
            }

            case StatusPrescricao.FINALIZADO:{
                return "Finalizado";
            }

            default: return "";
        }
    }

    public void setStatusPrescricao(Integer statusPrescricao) {
        this.statusPrescricao = statusPrescricao;
    }

    // here getters and setters
    // for title and so on, built
    // using date

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

}