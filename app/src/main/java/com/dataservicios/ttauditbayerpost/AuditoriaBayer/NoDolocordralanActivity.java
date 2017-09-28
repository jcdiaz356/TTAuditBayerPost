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
import android.widget.EditText;
import android.widget.Toast;

import com.dataservicios.ttauditbayerpost.Model.PollDetail;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.AuditUtil;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

import java.util.HashMap;

public class NoDolocordralanActivity extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = NoDolocordralanActivity.class.getSimpleName();
    private SessionManager session;


    private Button bt_guardar;

    private String tipo,cadenaruc, fechaRuta, comment1="",  type, region ;

    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id,road_id;
    private EditText etComment1;


    private DatabaseHelper db;

    private ProgressDialog pDialog;

    // private RadioGroup rgOpt1;


    // private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_dolocordralan);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");




        bt_guardar = (Button) findViewById(R.id.btGuardar);

        etComment1 = (EditText) findViewById(R.id.etComment1);

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
        poll_id = GlobalConstant.poll_id[11];

        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;




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

                        comment1 = etComment1.getText().toString();

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(0);
                        pollDetail.setOptions(0);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(0);
                        pollDetail.setComment(1);
                        pollDetail.setResult(0);
                        pollDetail.setLimite("0");
                        pollDetail.setComentario(comment1);
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCategory_product_id(0);
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
                Bundle argRuta = new Bundle();
                argRuta.clear();
                argRuta.putInt("company_id", company_id);
                argRuta.putInt("store_id",store_id);
                argRuta.putString("tipo", tipo);
                argRuta.putString("tipo", tipo);
                argRuta.putString("cadenaruc", cadenaruc);
                argRuta.putString("fechaRuta", fechaRuta);
                argRuta.putInt("audit_id", audit_id);
                argRuta.putInt("rout_id", rout_id);

                Intent intent;
                // intent = new Intent(MyActivity, CambiarApronaxActivity.class);
                intent = new Intent(MyActivity, DoloneurobionPromocionActivity.class);
                intent.putExtras(argRuta);
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

