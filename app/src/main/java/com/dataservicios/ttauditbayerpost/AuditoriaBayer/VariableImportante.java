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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import com.dataservicios.ttauditbayerpost.Model.PollDetail;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.AuditUtil;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

/**
 * Created by Jaime on 27/09/2016.
 */
public class VariableImportante extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = VariableImportante.class.getSimpleName();
    private SessionManager session;


    private Button bt_guardar;
    private EditText et_Comentario,etComent1;

    private LinearLayout lyContent;



    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;

    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id2;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    // private RadioGroup rgOpt1;
    private String opt1="";

    //private RadioButton[] radioButton1Array;
    private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.variable_importante);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Variable importante");


        lyContent = (LinearLayout) findViewById(R.id.lyContent);

        bt_guardar = (Button) findViewById(R.id.btGuardar);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComent1 = (EditText) findViewById(R.id.etComent1);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        //product_id =bundle.getInt("product_id");

        //poll_id = 562;
        poll_id = GlobalConstant.poll_id[6];


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        if(tipo.equals("CADENA")) {
            checkBoxArray = new CheckBox[5];

            checkBoxArray[0] = new CheckBox(this);
            checkBoxArray[0].setText("INCENTIVOS / REGALOS");
            checkBoxArray[0].setTag("a");
            lyContent.addView(checkBoxArray[0]);

            checkBoxArray[1] = new CheckBox(this);
            checkBoxArray[1].setText("MARCA CONOCIDA / PRESTIGIO / EFECTIVIDAD");
            checkBoxArray[1].setTag("b");
            lyContent.addView(checkBoxArray[1]);

            checkBoxArray[2] = new CheckBox(this);
            checkBoxArray[2].setText("PRECIO AL PUBLICO ACCESIBLE");
            checkBoxArray[2].setTag("c");
            lyContent.addView(checkBoxArray[2]);

            checkBoxArray[3] = new CheckBox(this);
            checkBoxArray[3].setText("ALTO STOCK");
            checkBoxArray[3].setTag("d");
            lyContent.addView(checkBoxArray[3]);

            checkBoxArray[4] = new CheckBox(this);
            checkBoxArray[4].setText("PUBLICIDAD");
            checkBoxArray[4].setTag("e");
            lyContent.addView(checkBoxArray[4]);



        }  else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA") || tipo.equals("MINI CADENAS")  || tipo.equals("SUB DISTRIBUIDOR")) {

            checkBoxArray = new CheckBox[6];

            checkBoxArray[0] = new CheckBox(this);
            checkBoxArray[0].setText("EFECTIVIDAD / MARCA CONOCIDA / PRESTIGIO");
            checkBoxArray[0].setTag("f");
            lyContent.addView(checkBoxArray[0]);

            checkBoxArray[1] = new CheckBox(this);
            checkBoxArray[1].setText("ALTO MARGEN DE GANANCIA");
            checkBoxArray[1].setTag("g");
            lyContent.addView(checkBoxArray[1]);

            checkBoxArray[2] = new CheckBox(this);
            checkBoxArray[2].setText("INCENTIVOS / REGALOS");
            checkBoxArray[2].setTag("h");
            lyContent.addView(checkBoxArray[2]);

            checkBoxArray[3] = new CheckBox(this);
            checkBoxArray[3].setText("PRECIO AL PUBLICO ACCESIBLE");
            checkBoxArray[3].setTag("c");
            lyContent.addView(checkBoxArray[3]);

            checkBoxArray[4] = new CheckBox(this);
            checkBoxArray[4].setText("ALTO STOCK");
            checkBoxArray[4].setTag("d");
            lyContent.addView(checkBoxArray[4]);

            checkBoxArray[5] = new CheckBox(this);
            checkBoxArray[5].setText("PUBLICIDAD");
            checkBoxArray[5].setTag("e");
            lyContent.addView(checkBoxArray[5]);
        }



        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                opt1 = "" ;
                int contador = 0;
                for (int x = 0; x < checkBoxArray.length; x++) {
                    if(checkBoxArray[x].isChecked()) contador ++;
                }

                if (contador == 0){
                    Toast.makeText(MyActivity,"Seleccionar una opción " , Toast.LENGTH_LONG).show();
                    return;
                } else{
                    for (int x = 0; x < checkBoxArray.length; x++) {
                        if(checkBoxArray[x].isChecked())  {
                            opt1 = opt1 + poll_id.toString() + checkBoxArray[x].getTag() + "|";

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
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions(opt1);
                        pollDetail.setSelectedOtionsComment("");
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

                Bundle argPDV = new Bundle();
                argPDV.putInt("store_id", Integer.valueOf(store_id));
                argPDV.putInt("road_id", Integer.valueOf(rout_id));
                Intent intent = new Intent(MyActivity, EncuestaRepLegalActivity.class);
                intent.putExtras(argPDV);
                startActivity(intent);
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

