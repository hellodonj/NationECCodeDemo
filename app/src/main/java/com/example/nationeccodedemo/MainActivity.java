package com.example.nationeccodedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylz.nativelib.NationEcTrans;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etUrl;
    private EditText etJson;
    private TextView textView;
    private Button btnOpen;

    private EditText etOrgId, etBusinessType, etOperatorId, etOperatorName, etOfficeId, etOfficeName, etDeviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv_show);
        btnOpen = findViewById(R.id.btn_open);
        etUrl = findViewById(R.id.et_url);
        etJson = findViewById(R.id.et_json);
        etOrgId = findViewById(R.id.et_orgId);
        etBusinessType = findViewById(R.id.et_businessType);
        etOperatorId = findViewById(R.id.et_operatorId);
        etOperatorName = findViewById(R.id.et_operatorName);
        etOfficeId = findViewById(R.id.et_officeId);
        etOfficeName = findViewById(R.id.et_officeName);
        etDeviceType = findViewById(R.id.et_deviceType);

        String strUrl = "https://fuwu-test.nhsa.gov.cn/localcfc/api/hsecfc/localQrCodeQuery";
        etUrl.setText(strUrl);
        etJson.setText("{\"data\":{\"businessType\":\"01101\",\"officeId\":\"32760\",\"officeName\":\"消化内科\",\"operatorId\":\"test001\",\"operatorName\":\"admin\",\"orgId\":\"35020319001\",\"deviceType\":\"\"},\"orgId\":\"35020319001\",\"transType\":\"ec.query\"}");

        Log.i("MainActivity", "url=" + etUrl.getText().toString() + "outterJson=" + etJson.getText().toString());


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * {"data":{"businessType":"01101",
                 * "gainWay":"ALL",
                 * "idNo":"",
                 * "infoFrom":"",
                 * "officeId":"32760",
                 * "officeName":"消化内科",
                 * "operatorId":"test001",
                 * "operatorName":"超级管理员",
                 * "orgId":"35020319001",
                 * "outBizNo":"202201111144020046",
                 * "userName":""},
                 * "orgId":"35020319001",
                 * "transType":"iot.gain.ec.qrcode"}
                 */
                JSONObject dataJSON = new JSONObject();
                JSONObject outterJSON = new JSONObject();
                try {
                    dataJSON.put("businessType", etBusinessType.getText().toString());//医院挂号  交易类型编码
                    dataJSON.put("officeId", etOfficeId.getText().toString()); //医保科室编号
                    dataJSON.put("officeName", etOfficeName.getText().toString()); //科室名称
                    dataJSON.put("operatorId", etOperatorId.getText().toString()); //收款人编号
                    dataJSON.put("operatorName", etOperatorName.getText().toString()); //收款人姓名
                    dataJSON.put("orgId", etOrgId.getText().toString()); //机构ID
                    dataJSON.put("deviceType", etDeviceType.getText().toString()); //自助机该字段设为 SelfService，其它情况不用设置

                    outterJSON.put("data", dataJSON);
                    outterJSON.put("orgId", etOrgId.getText().toString());
                    outterJSON.put("transType", "ec.query");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("MainActivity11", "etUrl=" + etUrl.getText().toString() + "outterJson=" + outterJSON.toString());
                etJson.setText(outterJSON.toString());
                SpUtils.getInstance().initSp(MainActivity.this);
                SpUtils.getInstance().putString("outterJSON", outterJSON.toString());
                NationEcTrans.EcQuery(MainActivity.this, etUrl.getText().toString(), etJson.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NationEcTrans.SCAN_REQUEST_CODE && data != null) {
            // 扫码回调结果，json 格式字符串，具体参数请详见说明
            String result = data.getStringExtra("scanResult");
            Log.i("MainActivity", "result=" + result);
            String outterJSON = SpUtils.getInstance().getString("outterJSON", "");
            etJson.setText(outterJSON);
            textView.setText("返回结果:" + result);
        }
    }
}