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
import android.widget.Toast;

import com.dataservicios.ttauditbayerpost.Model.PollDetail;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.AuditUtil;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

import java.util.HashMap;

public class MarcaCambiarActivity extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = MarcaCambiarActivity.class.getSimpleName();
    private SessionManager session;


    private Button bt_guardar;
    private EditText etCommentOption;

    private LinearLayout lyContent;



    private String tipo,cadenaruc, fechaRuta, comentario="", type, region , commentOptions="";

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
        setContentView(R.layout.activity_marca_cambiar);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");


        lyContent = (LinearLayout) findViewById(R.id.lyContent);

        bt_guardar = (Button) findViewById(R.id.btGuardar);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etCommentOption = (EditText) findViewById(R.id.etCommentOption);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        product_id =bundle.getInt("product_id");

        //poll_id = 562;
        poll_id = GlobalConstant.poll_id[8];


        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        checkBoxArray = new CheckBox[19];

        checkBoxArray[0] = new CheckBox(this);
        checkBoxArray[1] = new CheckBox(this);
        checkBoxArray[2] = new CheckBox(this);
        checkBoxArray[3] = new CheckBox(this);
        checkBoxArray[4] = new CheckBox(this);
        checkBoxArray[5] = new CheckBox(this);
        checkBoxArray[6] = new CheckBox(this);
        checkBoxArray[7] = new CheckBox(this);
        checkBoxArray[8] = new CheckBox(this);
        checkBoxArray[9] = new CheckBox(this);
        checkBoxArray[10] = new CheckBox(this);
        checkBoxArray[11] = new CheckBox(this);
        checkBoxArray[12] = new CheckBox(this);
        checkBoxArray[13] = new CheckBox(this);
        checkBoxArray[14] = new CheckBox(this);
        checkBoxArray[15] = new CheckBox(this);
        checkBoxArray[16] = new CheckBox(this);
        checkBoxArray[17] = new CheckBox(this);
        checkBoxArray[18] = new CheckBox(this);

        checkBoxArray[0].setText("Aflamax");
        checkBoxArray[1].setText("Breflex");
        checkBoxArray[2].setText("Dioxaflex");
        checkBoxArray[3].setText("Dolgramin");
        checkBoxArray[4].setText("Doloaproxol");
        checkBoxArray[5].setText("Dolocordralan Extra Fuerte");
        checkBoxArray[6].setText("Doloflam Extra Fuerte");
        checkBoxArray[7].setText("Dologyna");
        checkBoxArray[8].setText("Dolomuskqlar");
        checkBoxArray[9].setText("Flogodisten");
        checkBoxArray[10].setText("Iraxen");
        checkBoxArray[11].setText("Maxiflam Forte");
        checkBoxArray[12].setText("Miodel");
        checkBoxArray[13].setText("Miofedrol");
        checkBoxArray[14].setText("Miopress Forte");
        checkBoxArray[15].setText("Naproxeno");
        checkBoxArray[16].setText("Paldolor");
        checkBoxArray[17].setText("Redex");
        checkBoxArray[18].setText("Otros");

        checkBoxArray[0].setTag("a");
        checkBoxArray[1].setTag("b");
        checkBoxArray[2].setTag("c");
        checkBoxArray[3].setTag("d");
        checkBoxArray[4].setTag("e");
        checkBoxArray[5].setTag("f");
        checkBoxArray[6].setTag("g");
        checkBoxArray[7].setTag("h");
        checkBoxArray[8].setTag("i");
        checkBoxArray[9].setTag("j");
        checkBoxArray[10].setTag("k");
        checkBoxArray[11].setTag("l");
        checkBoxArray[12].setTag("m");
        checkBoxArray[13].setTag("n");
        checkBoxArray[14].setTag("o");
        checkBoxArray[15].setTag("p");
        checkBoxArray[16].setTag("q");
        checkBoxArray[17].setTag("r");
        checkBoxArray[18].setTag("s");

        lyContent.addView(checkBoxArray[0]);
        lyContent.addView(checkBoxArray[1]);
        lyContent.addView(checkBoxArray[2]);
        lyContent.addView(checkBoxArray[3]);
        lyContent.addView(checkBoxArray[4]);
        lyContent.addView(checkBoxArray[5]);
        lyContent.addView(checkBoxArray[6]);
        lyContent.addView(checkBoxArray[7]);
        lyContent.addView(checkBoxArray[8]);
        lyContent.addView(checkBoxArray[9]);
        lyContent.addView(checkBoxArray[10]);
        lyContent.addView(checkBoxArray[11]);
        lyContent.addView(checkBoxArray[12]);
        lyContent.addView(checkBoxArray[13]);
        lyContent.addView(checkBoxArray[14]);
        lyContent.addView(checkBoxArray[15]);
        lyContent.addView(checkBoxArray[16]);
        lyContent.addView(checkBoxArray[17]);
        lyContent.addView(checkBoxArray[18]);

        etCommentOption.setHint("Comentario");
        etCommentOption.setVisibility(View.GONE);
        checkBoxArray[18].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {


                    etCommentOption.setVisibility(View.VISIBLE);
                }
                else
                {
                    etCommentOption.setVisibility(View.GONE);
                }
            }
        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opt1 = "" ;
                commentOptions="";
                int contador = 0;
                for (int x = 0; x < checkBoxArray.length; x++) {
                    if(checkBoxArray[x].isChecked()) contador ++;
                }

                if (contador == 0){
                    Toast.makeText(MyActivity,"Seleccionar una opción " , Toast.LENGTH_LONG).show();
                    return;
                } else{
//                    for (int x = 0; x < checkBoxArray.length; x++) {
//                        if(checkBoxArray[x].isChecked())  {
//                            opt1 = opt1 + poll_id.toString() + checkBoxArray[x].getTag() + "|";
//                        }
//                    }

                    for (int x = 0; x < checkBoxArray.length; x++) {
                        if(checkBoxArray[x].isChecked())  {
                            opt1 =  poll_id.toString() + checkBoxArray[x].getTag() + "|" + opt1 ;


                        }
                    }

                    for (int x = 0; x < checkBoxArray.length; x++) {
                        if(checkBoxArray[x].isChecked() ) {
                            if(x==18) {
                                commentOptions = etCommentOption.getText().toString() + "|" + commentOptions;
                                //commentOptions = commentOptions + etCommentOption.getText().toString() + "|"  ;

                            }else {
                                commentOptions =   commentOptions + "|" ;
                            }
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

                if(tipo.equals("HORIZONTAL") ||  tipo.equals("SUB DISTRIBUIDOR") ||  tipo.equals("DETALLISTA")) {
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
                    intent = new Intent(MyActivity, TieneCanestenActivity.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                }
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

