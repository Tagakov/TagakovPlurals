package com.tagakov.tagakovplurals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tagakov.Plural;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Тагаков Владимир on 01.02.2015.
 */
public class ConverterFragment extends Fragment {

    private EditText input;
    private TextView output;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.converter_fragment, container, false);
        input = (EditText) view.findViewById(R.id.input_edit_text);
        output = (TextView) view.findViewById(R.id.output_text_view);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString().replaceAll(",", "");
                if (TextUtils.isEmpty(number) || number.equals("-")) {
                    output.setText("");
                    return;
                }
                if (number.length() > 27)
                    output.setText("Ну что же вы, число должно быть меньше октиллиона.");
                else
                    output.setText(new Plural(number).getNumberAsText());
                DecimalFormat decimalFormat = new DecimalFormat();
                DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
                decimalFormatSymbol.setGroupingSeparator(',');
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);
                input.removeTextChangedListener(this);
                String formattedNumber = decimalFormat.format(new BigInteger(number));
                input.setText(formattedNumber);
                input.setSelection(formattedNumber.length());
                input.addTextChangedListener(this);
            }
        });

        return view;
    }
}
