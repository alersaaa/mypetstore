package org.csu.mypetstorebyssm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class FormatUtil {
    public static String valueFormat(double value){
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        String xs = df.format(new BigDecimal(value));
        return xs;
    }

    public static String timeFormat(Date time){
        DecimalFormat df = new DecimalFormat("yyyy/MM/dd hh:mm:ss");
        String xs = df.format(time);
        return xs;
    }
}
