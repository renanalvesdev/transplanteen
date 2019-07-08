package br.com.transplanteen.tcc.transplanteen.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatusFrequencia{

    public static final int NORMAL= 0;
    public static final int BAIXA= 1;
    public static final int ALTA = 2;

    public StatusFrequencia(@Status int status) {
        System.out.println("Status :" + status);
    }

    @IntDef({NORMAL, BAIXA, ALTA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

}