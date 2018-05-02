package com.webgroup.yarik.detipapamama;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OrderFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private String productID;
    private EditText order_name,order_phone,order_email,order_comment;
    private Button orderBtn;
    private ImageView closeBtn;
    private ProgressBar download_progress;
    private ConstraintLayout form_block, form_result;
    private TextView result_text, result_hint;

    public static OrderFragment newInstance(String id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID, id);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String product_id = (String) getArguments().getSerializable(ARG_PRODUCT_ID);
        productID = product_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        form_block = (ConstraintLayout) v.findViewById(R.id.form_block);
        form_result = (ConstraintLayout) v.findViewById(R.id.form_result);
        download_progress = (ProgressBar) v.findViewById(R.id.download_progress);
        result_text = (TextView) v.findViewById(R.id.result_text);
        result_hint = (TextView) v.findViewById(R.id.result_hint);

        order_name = (EditText) v.findViewById(R.id.order_name);
        order_email = (EditText) v.findViewById(R.id.order_email);
        order_phone = (EditText) v.findViewById(R.id.order_phone);
        order_comment = (EditText) v.findViewById(R.id.order_comment);

        orderBtn = (Button) v.findViewById(R.id.order_button);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                new FetchTask().execute();
            }
        });

        closeBtn = (ImageView) v.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
            }
        });

        return v;
    }

    private class FetchTask extends AsyncTask<Void,Void,OrderResult> {

        @Override
        protected OrderResult doInBackground(Void... params) {
            String name = order_name.getText().toString();
            String email = order_email.getText().toString();
            String phone = order_phone.getText().toString();
            String message = order_comment.getText().toString();
            return new Fetchr().orderRequest(name, email, phone, message, productID);
        }

        @Override
        protected void onPostExecute(OrderResult orderResult) {
            showResult(orderResult);
        }
    }


    private void showProgressBar(){
        if(form_block != null)
            form_block.setVisibility(View.INVISIBLE);

        if(download_progress != null)
            download_progress.setVisibility(View.VISIBLE);

    }

    private void showResult(OrderResult orderResult){
        if(form_result != null) {
            if(orderResult.getResultCode().equals("0")) {
                result_text.setTextColor(Color.RED);
                result_hint.setTextColor(Color.RED);
            } else {
                result_text.setTextColor(Color.GREEN);
                result_hint.setTextColor(Color.RED);
            }
            result_text.setText(orderResult.getResultMessage());
            result_hint.setText(orderResult.getResultHint());
            form_result.setVisibility(View.VISIBLE);
        }

        if(download_progress != null)
            download_progress.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar(){
        if(form_block != null)
            form_block.setVisibility(View.VISIBLE);

        if(download_progress != null)
            download_progress.setVisibility(View.INVISIBLE);
    }
}
