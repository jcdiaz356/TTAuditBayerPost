package com.dataservicios.ttauditbayerpost;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.dataservicios.ttauditbayerpost.Model.Audit;
import com.dataservicios.ttauditbayerpost.Model.PollProductStore;
import com.dataservicios.ttauditbayerpost.Model.Product;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.util.ConexionInternet;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.JSONParser;

public class MainActivity extends Activity {
    // Logcat tag
    private static final String LOG_TAG = "Load Activity";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private int splashTime = 3000;
    private Thread thread;


    private ProgressBar mSpinner;
    private TextView tvCargando, tv_Version ;
    private ConexionInternet cnInternet ;
    private Activity MyActivity;
    //private JSONParser jsonParser;
    // Database Helper
   private DatabaseHelper db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivity = (Activity) this;
        mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
        mSpinner.setIndeterminate(true);
        tvCargando = (TextView) findViewById(R.id.tvCargando);
        tv_Version = (TextView) findViewById(R.id.tvVersion);

        PackageInfo pckInfo ;
        try {
            pckInfo= getPackageManager().getPackageInfo(getPackageName(),0);
            tv_Version.setText(pckInfo.versionName);


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



                cnInternet=new ConexionInternet(MyActivity);
        db = new DatabaseHelper(getApplicationContext());

        db.deleteAllPollProductStore();
        loadPollProductStore();

        if (cnInternet.isOnline()){
            if (db.checkDataBase(MyActivity)){

                mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
                mSpinner.setIndeterminate(true);
//                thread = new Thread(runable);
//                thread.start();

                //db.deleteAllUser();
                db.deleteAllProducts();

                //db.deleteAllAudits();
                db.deleteAllPresenseProduct();


                new loadProducts().execute();

            }else{
                db.deleteAllUser();

                db.deleteAllProducts();
                db.deleteAllAudits();
                db.deleteAllPresenseProduct();



                new loadProducts().execute();
            }


        }else  {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Conexion a internet?");
            alertDialogBuilder
                    .setMessage("No se encontro conexion a Internet!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }



    }


    private void loadLoginActivity()
    {
        Intent intent = new Intent(MyActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public Runnable runable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(splashTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                //Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                Intent intent = new Intent("com.dataservicios.systemauditor.LOGIN");
//
//                startActivity(intent);
//                finish();

                loadLoginActivity();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };





    class loadProducts extends AsyncTask<Void, Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            tvCargando.setText("Cargando Product...");
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //cargaTipoPedido();
                readJsonProducts();
//            Intent intent = new Intent("com.dataservicios.redagenteglobalapp.LOGIN");
//            startActivity(intent);
//            finish();
            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
               // loadLoginActivity();
                //new loadPublicity().execute();
                //loadLoginActivity();
                if(checkAndRequestPermissions()) loadLoginActivity();
            }
        }
    }



    private void readJsonProducts() {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("company_id", String.valueOf(GlobalConstant.company_id)));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonListProductsBayer" ,"POST", params);
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonListProducts" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("products");

                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Product product = new Product();
                            product.setId(Integer.valueOf(obj.getString("id")));
                            product.setName(obj.getString("fullname"));
                            product.setEam(obj.getString("eam"));
                            product.setCategory_id(Integer.valueOf(obj.getString("category_id")));
                            product.setCategory_name(obj.getString("categoria"));
                            product.setActive(0);
                            product.setImage(GlobalConstant.dominio + "/media/images/bayer/products/" + obj.getString("imagen"));
                            product.setCompany_id(Integer.valueOf(obj.getString("company_id")));
                            db.createProduct(product);
                            //pedido.setDescripcion(obj.getString("descripcion"));
                            // adding movie to movies array
                            // tipoPedidoList.add(pedido);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //poblandoSpinnerTipoPedido();
                    Log.d(LOG_TAG, String.valueOf(db.getAllProducts()));
                }
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reed publicity de json
     */

    private void readJsonAudits() {
        int success;
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("company_id", "7"));
            JSONParser jsonParser = new JSONParser();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonAuditsForStore" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                JSONArray ObjJson;
                ObjJson = json.getJSONArray("audits");
                if(ObjJson.length() > 0) {
                    for (int i = 0; i < ObjJson.length(); i++) {
                        try {
                            JSONObject obj = ObjJson.getJSONObject(i);

                            String idAuditoria = obj.getString("id");
                            String auditoria = obj.getString("fullname");

                            Audit audit = new Audit();
                            audit.setId(Integer.valueOf(obj.getString("id")));
                            audit.setName(obj.getString("fullname"));
                            audit.setStore_id(0);
                            audit.setScore(0);

                            db.createAudit(audit);
                            //pedido.setDescripcion(obj.getString("descripcion"));
                            // adding movie to movies array
                            // tipoPedidoList.add(pedido);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //poblandoSpinnerTipoPedido();
                    Log.d(LOG_TAG, String.valueOf(db.getAllAudits()));
                }
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadPollProductStore(){

        PollProductStore pd = new PollProductStore();


        //534 APRONAX
        pd.setIdProduct(534);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Tengo dolor en la espalda, me duele y siento que está inflamado ¿Qué me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(534);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Tengo dolor en la espalda, me duele y siento que está inflamado ¿Qué me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(534);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Tengo dolor en la espalda, me duele y siento que está inflamado ¿Qué me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(534);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Tengo dolor en la espalda, me duele y siento que está inflamado ¿Qué me recomiendas?");
        db.createPollProductStore(pd);


        pd.setIdProduct(534);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Tengo una lesión en la pierna por jugar futbol, que pastilla me recomiendas que me desinflame?");
        db.createPollProductStore(pd);

        pd.setIdProduct(534);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Tengo una lesión en la pierna por jugar futbol, que pastilla me recomiendas que me desinflame?");
        db.createPollProductStore(pd);

        //535 ASPIRINA 500

        pd.setIdProduct(535);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(535);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(535);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(535);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);


        pd.setIdProduct(535);
        pd.setTypeStore("CADENA");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        //537 Gynocanesten
        pd.setIdProduct(537);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);


        pd.setIdProduct(537);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);


        pd.setIdProduct(537);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);


        pd.setIdProduct(537);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);


        pd.setIdProduct(537);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);

        pd.setIdProduct(537);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Tengo ardor, picazón,  en la zona vaginal, que me recomiendas para eso?");
        db.createPollProductStore(pd);


        //536 Supradyn
        pd.setIdProduct(536);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(536);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(536);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(536);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(536);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(536);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Qué multivitaminico que me de energía me recomiendas?");
        db.createPollProductStore(pd);



        //539 Redoxon
        pd.setIdProduct(539);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        pd.setIdProduct(539);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        pd.setIdProduct(539);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        pd.setIdProduct(539);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        pd.setIdProduct(539);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        pd.setIdProduct(539);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Que vitamina recomiendas para mejorar mis defensas y no resfriarme?");
        db.createPollProductStore(pd);

        // 540 Berocca

        pd.setIdProduct(540);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);


        pd.setIdProduct(540);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);

        pd.setIdProduct(540);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);

        pd.setIdProduct(540);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);

        pd.setIdProduct(540);
        pd.setTypeStore("CADENA");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);

        pd.setIdProduct(540);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("Y vitaminas para estar más concentrado en el trabajo...");
        db.createPollProductStore(pd);

        //538 ASPIRINA 100

        pd.setIdProduct(538);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);

        pd.setIdProduct(538);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);

        pd.setIdProduct(538);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);

        pd.setIdProduct(538);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);

        pd.setIdProduct(538);
        pd.setTypeStore("CADENA");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);

        pd.setIdProduct(538);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("Doctor/a estoy teniendo fuertes punzadas en el pecho, tengo un ritmo de vida desordenada, y he oído que puedo tener riesgo de infarto, ¿Que podría hacer para evitarlo? ");
        db.createPollProductStore(pd);


        //640 BEPANTHEN CREMA X 30

        pd.setIdProduct(640);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion(" ¿Qué crema me recomienda para regenerar la piel dañada?");
        db.createPollProductStore(pd);


        pd.setIdProduct(640);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion(" ¿Qué crema me recomienda para regenerar la piel dañada?");
        db.createPollProductStore(pd);


        pd.setIdProduct(640);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion(" ¿Qué crema me recomienda para regenerar la piel dañada?");
        db.createPollProductStore(pd);


        pd.setIdProduct(640);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion(" ¿Qué crema me recomienda para regenerar la piel dañada?");
        db.createPollProductStore(pd);

        pd.setIdProduct(640);
        pd.setTypeStore("CADENA");
        pd.setQuestion(" Necesito una crema que sea hidratante y además que me regenere la piel. ¿Qué me recomienda?");
        db.createPollProductStore(pd);

        pd.setIdProduct(640);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion(" Necesito una crema que sea hidratante y además que me regenere la piel. ¿Qué me recomienda?");
        db.createPollProductStore(pd);

        //642 Supradyn Pro natal

        pd.setIdProduct(642);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy dando de lactar?");
        db.createPollProductStore(pd);


        pd.setIdProduct(642);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy dando de lactar?");
        db.createPollProductStore(pd);


        pd.setIdProduct(642);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy dando de lactar?");
        db.createPollProductStore(pd);


        pd.setIdProduct(642);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy dando de lactar?");
        db.createPollProductStore(pd);



        pd.setIdProduct(642);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy embarazada?");
        db.createPollProductStore(pd);

        pd.setIdProduct(642);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Qué multivitamínico me recomienda para tomar si estoy embarazada?");
        db.createPollProductStore(pd);



        //643 CANASTEN

        pd.setIdProduct(643);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Tengo pie de atleta ¿ Que me recomienda?");
        db.createPollProductStore(pd);


        pd.setIdProduct(643);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Tengo picazón, mal olor y enrojecimiento en los dedos del pie ¿ Que me recomienda?");
        db.createPollProductStore(pd);


        pd.setIdProduct(643);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Tengo picazón, mal olor y enrojecimiento en los dedos del pie ¿ Que me recomienda?");
        db.createPollProductStore(pd);


        pd.setIdProduct(643);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Tengo picazón, mal olor y enrojecimiento en los dedos del pie ¿ Que me recomienda?");
        db.createPollProductStore(pd);



        pd.setIdProduct(643);
        pd.setTypeStore("CADENA");
        pd.setQuestion("Tengo pie de atleta ¿ Que me recomienda?");
        db.createPollProductStore(pd);

        pd.setIdProduct(643);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("Tengo pie de atleta ¿ Que me recomienda?");
        db.createPollProductStore(pd);

        //644 ASPIRINA FORTE
        pd.setIdProduct(644);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(644);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(644);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(644);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);


        pd.setIdProduct(644);
        pd.setTypeStore("CADENA");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);

        pd.setIdProduct(644);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("Tengo un dolor muy fuerte de cabeza, ¿Qué pastilla me puedes recomendar?");
        db.createPollProductStore(pd);


        //645 Berocca + Supradyn


        pd.setIdProduct(645);
        pd.setTypeStore("HORIZONTAL");
        pd.setQuestion("¿ Qué producto para multivitamínico, me recomiendas que me otorgue mayor ganancia y buena rotación?");
        db.createPollProductStore(pd);

        pd.setIdProduct(645);
        pd.setTypeStore("SUB DISTRIBUIDOR");
        pd.setQuestion("¿ Qué producto multivitamínico, me recomiendas para  que me otorgue mayor ganancia y buena rotación ?");
        db.createPollProductStore(pd);

        pd.setIdProduct(645);
        pd.setTypeStore("DETALLISTA");
        pd.setQuestion("¿Qué multivitamínico que me de energía y mejore mi concentración me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(645);
        pd.setTypeStore("MINI CADENAS");
        pd.setQuestion("¿Qué multivitamínico que me de energía y mejore mi concentración me recomiendas?");
        db.createPollProductStore(pd);

        pd.setIdProduct(645);
        pd.setTypeStore("CADENA");
        pd.setQuestion("¿Qué multivitamínico que me de energía y mejore mi concentración me recomiendas?");
        db.createPollProductStore(pd);


        pd.setIdProduct(645);
        pd.setTypeStore("Mayoristas");
        pd.setQuestion("¿Qué multivitamínico que me de energía y mejore mi concentración me recomiendas?");
        db.createPollProductStore(pd);


        List<PollProductStore> lispollProductStore =  new ArrayList<PollProductStore>();
        lispollProductStore = db.getAllPollProductStore();

        Log.e(LOG_TAG, "final lista productos");

    }

    //  Chequeando permisos de usuario Runtime
    private boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.CAMERA);
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int callPhonePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.CALL_PHONE);
        int readPhoneStatePermission = ContextCompat.checkSelfPermission(MyActivity, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (callPhonePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MyActivity,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean respuestas = false ;
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {


            if (grantResults.length > 0) {

//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED ) {
//                    loadLoginActivity();
//
//                }  else {
//                    alertDialogBasico();
//                }
                boolean permissionsApp = true ;
                for(int i=0; i < grantResults.length; i++) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //alertDialogBasico();
                        permissionsApp = false;
                        break;
                    }
                }

                if (permissionsApp==true) {
                    loadLoginActivity();

                }  else {
                    alertDialogBasico();
                }

            }
        }

    }

    public void alertDialogBasico() {

        // 1. Instancia de AlertDialog.Builder con este constructor
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage(R.string.dialog_message_permission);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });

        builder.show();

    }



}
