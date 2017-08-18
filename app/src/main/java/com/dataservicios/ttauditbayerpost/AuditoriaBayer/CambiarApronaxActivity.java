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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditbayerpost.Model.Audit;
import com.dataservicios.ttauditbayerpost.Model.PollDetail;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.AuditUtil;
import com.dataservicios.ttauditbayerpost.util.GPSTracker;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CambiarApronaxActivity extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = CambiarApronaxActivity.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo  ;
    private Button bt_guardar;
    private EditText etComent;
    private TextView tv_Pregunta;
    private String tipo,cadenaruc, fechaRuta, comentario="";
    private Integer user_id, company_id,store_id,rout_id,road_id,audit_id, product_id, poll_id;
    int  is_sino=0;
    private DatabaseHelper db;

    private ProgressDialog pDialog;

    private PollDetail pollDetail;
    private Audit mAudit;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_apronax);


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");

        swSiNo = (Switch) findViewById(R.id.swSiNo);
        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        bt_guardar = (Button) findViewById(R.id.btGuardar);
        //bt_photo = (Button) findViewById(R.id.btPhoto);

        //etComent = (EditText) findViewById(R.id.etComent);

        gpsTracker = new GPSTracker(MyActivity);

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

        poll_id = GlobalConstant.poll_id[7]; //¿Se encuentra abierto el establecimiento?

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;



        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    is_sino = 1;

                } else {
                    is_sino = 0;

                }
            }
        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // comentario = String.valueOf(etComent.getText()) ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(1);
                        pollDetail.setOptions(0);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(1);
                        pollDetail.setComment(0);
                        pollDetail.setResult(is_sino);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario(comentario);
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions("");
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }



    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class loadPoll extends AsyncTask<Void, Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //cargaTipoPedido();
            if(is_sino == 1) {
                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
            } else{

                String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());

                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
                //if(!AuditUtil.closeAuditRoadStore(mAudit)) return false;
                // if(!AuditUtil.closeAuditRoadAll(mAudit)) return false;
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();
                if(is_sino==1) {
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("company_id", company_id);
                    argRuta.putInt("store_id",store_id);
                    argRuta.putString("tipo", tipo);
                    argRuta.putString("tipo", tipo);
                    argRuta.putString("cadenaruc", cadenaruc);
                    argRuta.putString("fechaRuta", fechaRuta);
                    argRuta.putInt("idAuditoria", audit_id);
                    argRuta.putInt("rout_id", rout_id);

                    Intent intent;
                    intent = new Intent(MyActivity, MarcaCambiarActivity.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();
                } else if(is_sino==0){

                    if(tipo.equals("HORIZONTAL") ||  tipo.equals("SUB DISTRIBUIDOR")  ||  tipo.equals("DETALLISTA")) {
                        Bundle argRuta = new Bundle();
                        argRuta.clear();
                        argRuta.putInt("company_id", company_id);
                        argRuta.putInt("store_id",store_id);
                        argRuta.putString("tipo", tipo);
                        argRuta.putString("tipo", tipo);
                        argRuta.putString("cadenaruc", cadenaruc);
                        argRuta.putString("fechaRuta", fechaRuta);
                        argRuta.putInt("idAuditoria", audit_id);
                        argRuta.putInt("rout_id", rout_id);
                        Intent intent;
                        intent = new Intent(MyActivity, TieneCanestenActivity.class);
                        intent.putExtras(argRuta);
                        startActivity(intent);
                    }
                    finish();
                }


            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }

            hidepDialog();
        }
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

}
