package com.dataservicios.ttauditbayerpost.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dataservicios.ttauditbayerpost.AndroidCustomGalleryActivity;
import com.dataservicios.ttauditbayerpost.Model.PollProductStore;
import com.dataservicios.ttauditbayerpost.Model.ProductScore;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.JSONParser;
import com.dataservicios.ttauditbayerpost.util.JSONParserX;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

/**
 * Created by Jaime on 12/08/2016.
 */
public class BeroccaSupradyn extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = Canesten.class.getSimpleName();
    private SessionManager session;

    private Switch sw_recomienda, sw_stock ;
    private LinearLayout ly_stock,ly_productos;

    private Button bt_photo, bt_guardar;
    private EditText et_Comentario, et_ComentarioOtros;
    private TextView tv_ComentarioOtros;
    private TextView tv_Pregunta, tvStock;


    private String tipo,cadenaruc,fechaRuta, comentario="" ,comentarioOtros="";
    private Integer user_id, company_id,store_id,rout_id,audit_id, product_id, poll_id, poll_id_2,poll_id_3;

    int  is_exhibidor=0, is_recomieda =0 , stock=0 ,is_priority=0;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    String totalOption="";
    int totalValores ;
    int vTienda=0,vProducto=0,vA=0,vB=0,vC=0,vD;
    String oTienda="",oProducto="",oA="",oB="",oC="",oD="";
    String pTienda="",pProducto="",pA="",pB="",pC="",pD="";

    private EditText[] editTextArray;
    private CheckBox[] checkBoxArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.berocca_supradyn);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Berocca Supradyn");


        sw_recomienda = (Switch) findViewById(R.id.swRecomienda);
        sw_stock = (Switch) findViewById(R.id.swStock);
        tvStock = (TextView) findViewById(R.id.tvStock);


        ly_stock = (LinearLayout) findViewById(R.id.lyStock);
        ly_productos = (LinearLayout) findViewById(R.id.lyProductos);

        et_Comentario = (EditText) findViewById(R.id.etComentario);

        et_ComentarioOtros = (EditText) findViewById(R.id.etComentarioOtros);
        tv_ComentarioOtros = (TextView) findViewById(R.id.tvComentarioOtros);

        tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);

        bt_guardar = (Button) findViewById(R.id.btGuardar);

        et_Comentario = (EditText) findViewById(R.id.etComentario);

        editTextArray = new EditText[] {
                (EditText) findViewById(R.id.etA),
                (EditText) findViewById(R.id.etB),
                (EditText) findViewById(R.id.etC),
                (EditText) findViewById(R.id.etD),
                (EditText) findViewById(R.id.etE),
                (EditText) findViewById(R.id.etF),
                (EditText) findViewById(R.id.etG),
                (EditText) findViewById(R.id.etH),
                (EditText) findViewById(R.id.etI),
                (EditText) findViewById(R.id.etJ),
                (EditText) findViewById(R.id.etK),
                (EditText) findViewById(R.id.etL),
                (EditText) findViewById(R.id.etM),
                (EditText) findViewById(R.id.etN),
                (EditText) findViewById(R.id.etO),
                (EditText) findViewById(R.id.etP),
                (EditText) findViewById(R.id.etQ),
                (EditText) findViewById(R.id.etR),
                (EditText) findViewById(R.id.etS),
                (EditText) findViewById(R.id.etT),
                (EditText) findViewById(R.id.etU),
                (EditText) findViewById(R.id.etW),
                (EditText) findViewById(R.id.etX),
                (EditText) findViewById(R.id.etY),

        };
        checkBoxArray = new CheckBox[] {
                (CheckBox) findViewById(R.id.cbA),
                (CheckBox) findViewById(R.id.cbB),
                (CheckBox) findViewById(R.id.cbC),
                (CheckBox) findViewById(R.id.cbD),
                (CheckBox) findViewById(R.id.cbE),
                (CheckBox) findViewById(R.id.cbF),
                (CheckBox) findViewById(R.id.cbG),
                (CheckBox) findViewById(R.id.cbH),
                (CheckBox) findViewById(R.id.cbI),
                (CheckBox) findViewById(R.id.cbJ),
                (CheckBox) findViewById(R.id.cbK),
                (CheckBox) findViewById(R.id.cbL),
                (CheckBox) findViewById(R.id.cbM),
                (CheckBox) findViewById(R.id.cbN),
                (CheckBox) findViewById(R.id.cbO),
                (CheckBox) findViewById(R.id.cbP),
                (CheckBox) findViewById(R.id.cbQ),
                (CheckBox) findViewById(R.id.cbR),
                (CheckBox) findViewById(R.id.cbS),
                (CheckBox) findViewById(R.id.cbT),
                (CheckBox) findViewById(R.id.cbU),
                (CheckBox) findViewById(R.id.cbW),
                (CheckBox) findViewById(R.id.cbX),
                (CheckBox) findViewById(R.id.cbY),
        };

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("store_id");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("rout_id");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("audit_id");
        product_id =bundle.getInt("product_id");

//        poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        poll_id = GlobalConstant.poll_id[2]; //SE RECOMIENDA EL PRODUCTO
        poll_id_2 = GlobalConstant.poll_id[3]; //QUE PRODUCTO RECOMENDO
        poll_id_3 = GlobalConstant.poll_id[4]; //STOcK



        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        db = new DatabaseHelper(getApplicationContext());
        PollProductStore pps = new PollProductStore();

        pps = db.getPollProductStore(product_id, tipo);
        tv_Pregunta.setText(pps.getQuestion());

        ly_stock.setEnabled(true);
        ly_stock.setVisibility(View.VISIBLE);



        checkBoxArray[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[0].setText("1");
                    editTextArray[0].setEnabled(true);
                    editTextArray[0].requestFocus();
                    tvStock.setVisibility(View.INVISIBLE);
                    sw_stock.setEnabled(false);
                    sw_stock.setVisibility(View.INVISIBLE);
                } else{
                    editTextArray[0].setText("0");
                    editTextArray[0].setEnabled(false);

                    tvStock.setVisibility(View.VISIBLE);
                    sw_stock.setEnabled(true);
                    sw_stock.setVisibility(View.VISIBLE);
                }
            }
        });

        loadActionControl();
        checkBoxArray[23].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[23].setText("1");
                    editTextArray[23].setEnabled(true);
                    editTextArray[23].requestFocus();
                    // perform logic
                    tv_ComentarioOtros.setVisibility(View.VISIBLE);
                    et_ComentarioOtros.setEnabled(true);
                    et_ComentarioOtros.setVisibility(View.VISIBLE);
                } else{
                    editTextArray[23].setText("0");
                    editTextArray[23].setEnabled(false);
                    tv_ComentarioOtros.setVisibility(View.INVISIBLE);
                    et_ComentarioOtros.setEnabled(false);
                    et_ComentarioOtros.setVisibility(View.INVISIBLE);
                }

            }
        });



        sw_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stock = 1;

                } else {
                    stock = 0;

                }
            }
        });



        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------Verificando que haya seleccionado 1 elemento o max 3
                int contado_control=0;
                for (int x = 0; x < checkBoxArray.length; x++) {
                    if (checkBoxArray[x].isChecked()) contado_control++;
                }
                if (contado_control > 3) {
                    Toast.makeText(MyActivity, R.string.message_product_max_options  , Toast.LENGTH_LONG).show();
                    return;
                }
                if (contado_control < 1) {
                    Toast.makeText(MyActivity, R.string.message_product_min_options , Toast.LENGTH_LONG).show();
                    return;
                }
                //-------------------------------------------------------

                //--------------------- Verificando que ingrese valores iguales ------------------------
                for (int x = 0; x < checkBoxArray.length; x++) {

                    if(checkBoxArray[x].isChecked()) {
                        int valor;
                        if( editTextArray[x].getText().equals(""))  valor = 0 ; else valor = Integer.valueOf(String.valueOf(editTextArray[x].getText())) ;
                        if (valor > 3) {
                            Toast.makeText(MyActivity, R.string.message_priority_rank , Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (valor < 1) {
                            Toast.makeText(MyActivity, R.string.message_priority_initial , Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i = 0; i < editTextArray.length; i++) {

                            if(x != i){
                                int nuevo_valor;
                                nuevo_valor = Integer.valueOf(String.valueOf(editTextArray[i].getText()));
                                if(valor == nuevo_valor  ){
                                    Toast.makeText(MyActivity, R.string.message_priority_no_equal , Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                        }
                    }
                }


                totalValores = 0;
                totalOption = "";

                if (checkBoxArray[0].isChecked()) {
                    int prioridad=0;

                    prioridad = Integer.valueOf(editTextArray[0].getText().toString());
                    if(editTextArray[0].getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, R.string.message_select_options , Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    } else if (prioridad == 1 ||prioridad == 2 || prioridad == 3) {
                        is_recomieda=1;
                        is_priority=prioridad;
                        totalValores = totalValores + 1 ;
                        totalOption = String.valueOf(poll_id_2)  + checkBoxArray[0].getTag().toString() + "-" + editTextArray[0].getText().toString()  + "|" + totalOption;  //Apronax
                        //vProducto = 1;
                        //oProducto = String.valueOf(poll_id_2) + "," + product_id + checkBoxArray[0].getTag().toString() + "-" + editTextArray[0].getText().toString();  //Apronax

                    }

                }

                for (int x = 1; x < checkBoxArray.length - 1; x++) {
                    if (checkBoxArray[x].isChecked()) {
                        if(editTextArray[x].getText().equals("")){
                            Toast toast;
                            toast = Toast.makeText(MyActivity, R.string.message_priority_value_numeric, Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }else  {
//                            vA = 1;
//                            oA = String.valueOf(poll_id_2) + "f" + "-" + editTextArray[x].getText().toString(); //Dolocordralan Extra Fuerte
                            totalValores = totalValores + 1 ;
                            totalOption = String.valueOf(poll_id_2) + checkBoxArray[x].getTag().toString() + "-" + editTextArray[x].getText().toString()  + "|" + totalOption;
                        }
                    }

                }

                if (checkBoxArray[23].isChecked()) {
                    if(editTextArray[23].getText().equals("")){
                        Toast toast;
                        toast = Toast.makeText(MyActivity, R.string.message_priority_value_numeric , Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }else  {
//                            vA = 1;
//                            oA = String.valueOf(poll_id_2) + "f" + "-" + editTextArray[x].getText().toString(); //Dolocordralan Extra Fuerte
                        totalValores = totalValores + 1 ;
                        totalOption = String.valueOf(poll_id_2) +  checkBoxArray[23].getTag().toString() + "-" + editTextArray[23].getText().toString()  + "|" + totalOption;
                    }
                }


//                totalValores = vProducto  + vA + vB   ;
//                totalOption = oProducto + "|"  + oA + "|" + oB + "|"  ;

                if(is_recomieda==0){
                    if(totalValores==0){

                        Toast toast;
                        toast = Toast.makeText(MyActivity, R.string.message_priority_value_numeric , Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle(R.string.save);
                builder.setMessage(R.string.saveInformation);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        comentario = String.valueOf(et_Comentario.getText()) ;
                        comentarioOtros = String.valueOf(et_ComentarioOtros.getText()) ;
                        new loadPoll71().execute();
                        db.updateProductActive(product_id, 1);


                        List<ProductScore> listProductScore = new ArrayList<ProductScore>();
                        listProductScore = db.getAllProductsScore();
                        Log.d(LOG_TAG, String.valueOf(listProductScore));

                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
//            onBackPressed();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void onBackPressed() {
        //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class loadPoll71 extends AsyncTask<Void, Integer , Boolean> {
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

            if(!InsertAuditPollsProduct(poll_id,0,is_recomieda,comentario)) return false;

            if(!InsertAuditPollsOtions(poll_id_2, 0, 0, ""))return false;
            if(is_recomieda==0){
                //Enviando por defecto estock segun el swich que marco
                if(!InsertAuditPollsProduct(poll_id_3, 0, stock, "")) return false;
            } else if(is_recomieda==1) {
                //Enviando por defecto estock 1
                if(!InsertAuditPollsProduct(poll_id_3,0,1,"")) return false;
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            hidepDialog();
            if (result){
                // loadLoginActivity();
                //new loadPoll72().execute();

                //////////////////////////////////////////////modificado///////////////////////////////////////////
                ProductScore ps = new ProductScore();
                if(tipo.equals("CADENA") || tipo.equals("MINI CADENAS") ) {
                    if (is_recomieda == 1 ) {
                        ps = db.getProductScoreForStore(store_id);
                        int total_products = 0;
                        total_products = 1 + ps.getTotalProducts();
                        db.updateProductScoreForTotalProducts(store_id, total_products);

                    }
                } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA")  || tipo.equals("SUB DISTRIBUIDOR")) {
                    if (is_recomieda == 1 & is_priority == 1) {
                        ps = db.getProductScoreForStore(store_id);
                        int total_products = 0;
                        total_products = 1 + ps.getTotalProducts();
                        db.updateProductScoreForTotalProducts(store_id, total_products);

                    }
                }
                //////////////////////////////////////////////end///////////////////////////////////////////

                finish();
            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
            }

        }
    }


    private boolean InsertAuditPollsProduct(int poll_id, int status , int result,String comentario) {
        int success;
        try {
            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id", String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", String.valueOf(product_id));
            params.put("sino", "1");
            params.put("coment", String.valueOf(comentario));
            params.put("result", String.valueOf(result));
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(rout_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("status", String.valueOf(status));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPollsProduct" ,"POST", params);
            // check your log for json response
            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro, registro duplicado");
                    // return json.getString("message");
                    // return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, " Error " + Log.getStackTraceString(e));
            return false;
        }

        return true;

    }



    private boolean InsertAuditPollsOtions(int poll_id, int status,int result,String comentario) {
        int success;
        try {


            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id", String.valueOf(poll_id));
            params.put("poll_id", String.valueOf(poll_id));
            params.put("store_id", String.valueOf(store_id));
            params.put("options", "1");
            params.put("limits", "0");
            params.put("media", "0");
            params.put("coment", "1");
            params.put("coment_options", "0");
            params.put("comentario_options", "");
            params.put("limite", "");
            params.put("opcion", totalOption);
            params.put("sino", "0");
            params.put("product", "1");
            params.put("comentario", String.valueOf(comentarioOtros));
            params.put("result", String.valueOf(result));
            params.put("idCompany", String.valueOf(GlobalConstant.company_id));
            params.put("idRuta", String.valueOf(rout_id));
            params.put("idAuditoria", String.valueOf(audit_id));
            params.put("product_id", String.valueOf(product_id));
            params.put("status", String.valueOf(status));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPolls" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditBayer" ,"POST", params);
            // check your log for json response
            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro, registro duplicado");
                    // return json.getString("message");
                    // return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, " Error " + Log.getStackTraceString(e));
            return false;
        }

        return true;
    }

    private void loadActionControl(){

        checkBoxArray[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[1].setText("1");
                    editTextArray[1].setEnabled(true);
                    editTextArray[1].requestFocus();
                } else{
                    editTextArray[1].setText("0");
                    editTextArray[1].setEnabled(false);
                }
            }
        });

        checkBoxArray[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[2].setText("1");
                    editTextArray[2].setEnabled(true);
                    editTextArray[2].requestFocus();
                } else{
                    editTextArray[2].setText("0");
                    editTextArray[2].setEnabled(false);
                }
            }
        });

        checkBoxArray[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[3].setText("1");
                    editTextArray[3].setEnabled(true);
                    editTextArray[3].requestFocus();
                } else{
                    editTextArray[3].setText("0");
                    editTextArray[3].setEnabled(false);
                }
            }
        });

        checkBoxArray[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[4].setText("1");
                    editTextArray[4].setEnabled(true);
                    editTextArray[4].requestFocus();
                } else{
                    editTextArray[4].setText("0");
                    editTextArray[4].setEnabled(false);
                }
            }
        });

        checkBoxArray[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[5].setText("1");
                    editTextArray[5].setEnabled(true);
                    editTextArray[5].requestFocus();
                } else{
                    editTextArray[5].setText("0");
                    editTextArray[5].setEnabled(false);
                }
            }
        });

        checkBoxArray[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[6].setText("1");
                    editTextArray[6].setEnabled(true);
                    editTextArray[6].requestFocus();
                } else{
                    editTextArray[6].setText("0");
                    editTextArray[6].setEnabled(false);
                }
            }
        });

        checkBoxArray[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[7].setText("1");
                    editTextArray[7].setEnabled(true);
                    editTextArray[7].requestFocus();
                } else{
                    editTextArray[7].setText("0");
                    editTextArray[7].setEnabled(false);
                }
            }
        });

        checkBoxArray[8].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[8].setText("1");
                    editTextArray[8].setEnabled(true);
                    editTextArray[8].requestFocus();
                } else{
                    editTextArray[8].setText("0");
                    editTextArray[8].setEnabled(false);
                }
            }
        });

        checkBoxArray[9].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[9].setText("1");
                    editTextArray[9].setEnabled(true);
                    editTextArray[9].requestFocus();
                } else{
                    editTextArray[9].setText("0");
                    editTextArray[9].setEnabled(false);
                }
            }
        });

        checkBoxArray[10].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[10].setText("1");
                    editTextArray[10].setEnabled(true);
                    editTextArray[10].requestFocus();
                } else{
                    editTextArray[10].setText("0");
                    editTextArray[10].setEnabled(false);
                }
            }
        });


        checkBoxArray[11].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[11].setText("1");
                    editTextArray[11].setEnabled(true);
                    editTextArray[11].requestFocus();
                } else{
                    editTextArray[11].setText("0");
                    editTextArray[11].setEnabled(false);
                }
            }
        });


        checkBoxArray[12].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[12].setText("1");
                    editTextArray[12].setEnabled(true);
                    editTextArray[12].requestFocus();
                } else{
                    editTextArray[12].setText("0");
                    editTextArray[12].setEnabled(false);
                }
            }
        });

        checkBoxArray[13].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[13].setText("1");
                    editTextArray[13].setEnabled(true);
                    editTextArray[13].requestFocus();
                } else{
                    editTextArray[13].setText("0");
                    editTextArray[13].setEnabled(false);
                }
            }
        });


        checkBoxArray[14].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[14].setText("1");
                    editTextArray[14].setEnabled(true);
                    editTextArray[14].requestFocus();
                } else{
                    editTextArray[14].setText("0");
                    editTextArray[14].setEnabled(false);
                }
            }
        });

        checkBoxArray[15].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[15].setText("1");
                    editTextArray[15].setEnabled(true);
                    editTextArray[15].requestFocus();
                } else{
                    editTextArray[15].setText("0");
                    editTextArray[15].setEnabled(false);
                }
            }
        });


        checkBoxArray[16].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[16].setText("1");
                    editTextArray[16].setEnabled(true);
                    editTextArray[16].requestFocus();
                } else{
                    editTextArray[16].setText("0");
                    editTextArray[16].setEnabled(false);
                }
            }
        });


        checkBoxArray[17].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[17].setText("1");
                    editTextArray[17].setEnabled(true);
                    editTextArray[17].requestFocus();
                } else{
                    editTextArray[17].setText("0");
                    editTextArray[17].setEnabled(false);
                }
            }
        });

        checkBoxArray[18].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[18].setText("1");
                    editTextArray[18].setEnabled(true);
                    editTextArray[18].requestFocus();
                } else{
                    editTextArray[18].setText("0");
                    editTextArray[18].setEnabled(false);
                }
            }
        });


        checkBoxArray[19].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[19].setText("1");
                    editTextArray[19].setEnabled(true);
                    editTextArray[19].requestFocus();
                } else{
                    editTextArray[19].setText("0");
                    editTextArray[19].setEnabled(false);
                }
            }
        });

        checkBoxArray[20].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[20].setText("1");
                    editTextArray[20].setEnabled(true);
                    editTextArray[20].requestFocus();
                } else{
                    editTextArray[20].setText("0");
                    editTextArray[20].setEnabled(false);
                }
            }
        });

        checkBoxArray[21].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[21].setText("1");
                    editTextArray[21].setEnabled(true);
                    editTextArray[21].requestFocus();
                } else{
                    editTextArray[21].setText("0");
                    editTextArray[21].setEnabled(false);
                }
            }
        });

        checkBoxArray[22].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[22].setText("1");
                    editTextArray[22].setEnabled(true);
                    editTextArray[22].requestFocus();
                } else{
                    editTextArray[22].setText("0");
                    editTextArray[22].setEnabled(false);
                }
            }
        });

        checkBoxArray[23].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    editTextArray[23].setText("1");
                    editTextArray[23].setEnabled(true);
                    editTextArray[23].requestFocus();
                } else{
                    editTextArray[23].setText("0");
                    editTextArray[23].setEnabled(false);
                }
            }
        });


    }
}

