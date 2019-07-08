package br.com.transplanteen.tcc.transplanteen.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusPrescricao{

    public static final int ESPERANDO_CONFIRMACAO= 0;
    public static final int TOMANDO= 1;
    public static final int FINALIZADO= 2;

    public StatusPrescricao(@Status int status) {
        System.out.println("Status :" + status);
    }

    @IntDef({ESPERANDO_CONFIRMACAO, TOMANDO, FINALIZADO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

}