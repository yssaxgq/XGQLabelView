package com.xgq.a123.xgqlabelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] mFirstContents = new String[]{"快快快...进来摆！进来摆！", "算逑咯", "那我就先走了。"};

    private EditText edit;
    private Button btn_add;
    private LabelView label_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = findViewById(R.id.edit);
        btn_add = findViewById(R.id.btn_add);
        label_view = findViewById(R.id.label_view);

        label_view.addViewWithStrings(mFirstContents);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String label = edit.getText().toString().trim();
                if (!TextUtils.isEmpty(label)) {
                    label_view.addViewWithString(label);
                }
                break;
            default:
                break;
        }
    }
}
