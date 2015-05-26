package hyuse.how_much;

import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * Created by hwang-gyojun on 2015. 4. 25..
 */
public class MyLabelFormatter implements ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }
}
