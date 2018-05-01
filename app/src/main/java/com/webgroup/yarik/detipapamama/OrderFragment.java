package com.webgroup.yarik.detipapamama;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class OrderFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private String productID;
    private EditText order_name,order_phone,order_email,order_comment;
    private Button orderBtn;
    private ImageView closeBtn;

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

        order_name = (EditText) v.findViewById(R.id.order_name);

        order_email = (EditText) v.findViewById(R.id.order_email);

        order_phone = (EditText) v.findViewById(R.id.order_phone);

        order_comment = (EditText) v.findViewById(R.id.order_comment);

        orderBtn = (Button) v.findViewById(R.id.orderBtn);

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

}
