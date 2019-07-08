package br.com.transplanteen.tcc.transplanteen.model;

public class PrescricaoItem extends ListItem {

    private Prescricao prescricao;

    public Prescricao getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(Prescricao prescricao) {
        this.prescricao = prescricao;
    }
    // here getters and setters
    // for title and so on, built
    // using event

    @Override
    public int getType() {
        return TYPE_EVENT;
    }

}