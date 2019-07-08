package br.com.transplanteen.tcc.transplanteen.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusPeso{

    public static final int NORMAL= 0;
    public static final int ABAIXO= 1;
    public static final int ACIMA = 2;

    public StatusPeso(@Status int status) {
        System.out.println("Status :" + status);
    }

    @IntDef({NORMAL, ABAIXO, ACIMA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

}