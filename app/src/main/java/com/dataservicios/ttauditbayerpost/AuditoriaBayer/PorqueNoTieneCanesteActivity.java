package com.dataservicios.ttauditbayerpost.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dataservicios.ttauditbayerpost.Model.PollDetail;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.AuditUtil;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

import java.util.HashMap;

public class PorqueNoTieneCanesteActivity extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = MarcaCambiarActivity.class.getSimpleName();
    private SessionManager session;


    private Button bt_guardar;
    private EditText etCommentOption;

    private LinearLayout lyContent,lyOptionComment;



    private String tipo,cadenaruc, fechaRuta, comentario="", type, region , commentOptions="";

    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id2,road_id;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    // private RadioGroup rgOpt1;
    private String opt1="";

    private RadioButton[] radioButtonArray;
    private RadioGroup  radioGroup;
   // private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porque_no_tiene_caneste);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");


        lyContent = (LinearLayout) findViewById(R.id.lyContent);
        lyOptionComment = (LinearLayout) findViewById(R.id.lyOptionComment);

        bt_guardar = (Button) findViewById(R.id.btGuardar);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        //etCommentOption = (EditText) findViewById(R.id.etCommentOption);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        road_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        product_id =bundle.getInt("product_id");
        //product_id =bundle.getInt("product_id");

        //poll_id = 562;
        poll_id = GlobalConstant.poll_id[10];


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        radioGroup = new RadioGroup(MyActivity);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        radioButtonArray = new RadioButton[6];

        radioButtonArray[0] = new RadioButton(this);
        radioButtonArray[1] = new RadioButton(this);
        radioButtonArray[2] = new RadioButton(this);
        radioButtonArray[3] = new RadioButton(this);
        radioButtonArray[4] = new RadioButton(this);
        radioButtonArray[5] = new RadioButton(this);

        radioButtonArray[0].setText("No me lo piden");
        radioButtonArray[1].setText("No es efectivo, hay productos mejores.");
        radioButtonArray[2].setText("No me hace ganar plata");
        radioButtonArray[3].setText("No me lo ofrecen");
        radioButtonArray[4].setText("No lo conozco");
        radioButtonArray[5].setText("Otros");

        radioButtonArray[0].setTag("a");
        radioButtonArray[1].setTag("b");
        radioButtonArray[2].setTag("c");
        radioButtonArray[3].setTag("d");
        radioButtonArray[4].setTag("e");
        radioButtonArray[5].setTag("f");

        radioGroup.addView(radioButtonArray[0]);
        radioGroup.addView(radioButtonArray[1]);
        radioGroup.addView(radioButtonArray[2]);
        radioGroup.addView(radioButtonArray[3]);
        radioGroup.addView(radioButtonArray[4]);
        radioGroup.addView(radioButtonArray[5]);

        lyContent.addView(radioGroup);

        etCommentOption     = new EditText(MyActivity);
        radioButtonArray[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    lyOptionComment.removeAllViews();
                    etCommentOption.setHint("Comentario");
                    etCommentOption.setText("");
                    lyOptionComment.addView(etCommentOption);
                }
                else
                {
                    etCommentOption.setText("");
                    lyOptionComment.removeAllViews();
                }
            }
        });


        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opt1 = "" ;
                commentOptions="";
                int contador = 0;
                for (int x = 0; x < radioButtonArray.length; x++) {
                    if(radioButtonArray[x].isChecked()) contador ++;
                }

                if (contador == 0){
                    Toast.makeText(MyActivity,"Seleccionar una opción " , Toast.LENGTH_LONG).show();
                    return;
                } else{
                    for (int x = 0; x < radioButtonArray.length; x++) {
                        if(radioButtonArray[x].isChecked())  {
                            opt1 = opt1 + poll_id.toString() + radioButtonArray[x].getTag() ;
                        }
                    }
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(radioButtonArray[5].isChecked()) commentOptions = etCommentOption.getText().toString(); else comentario="" ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(0);
                        pollDetail.setOptions(1);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(0);
                        pollDetail.setComment(0);
                        pollDetail.setResult(0);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setCommentOptions(1);
                        pollDetail.setSelectdOptions(opt1);
                        pollDetail.setSelectedOtionsComment(commentOptions);
                        pollDetail.setPriority("0");

                        new loadPoll().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);

            }
        });

    }



    class loadPoll extends AsyncTask<Void, Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {

            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if(!AuditUtil.insertPollDetail(pollDetail)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

