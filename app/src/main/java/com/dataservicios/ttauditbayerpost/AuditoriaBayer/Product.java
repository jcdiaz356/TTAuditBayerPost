package com.dataservicios.ttauditbayerpost.AuditoriaBayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dataservicios.ttauditbayerpost.Model.ProductScore;
import com.dataservicios.ttauditbayerpost.R;
import com.dataservicios.ttauditbayerpost.SQLite.DatabaseHelper;
import com.dataservicios.ttauditbayerpost.adapter.ProductsAdapter;
import com.dataservicios.ttauditbayerpost.util.GlobalConstant;
import com.dataservicios.ttauditbayerpost.util.JSONParserX;
import com.dataservicios.ttauditbayerpost.util.SessionManager;

/**
 * Created by Jaime on 9/02/2016.
 */
public class Product extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = "Activity_Product";
    private SessionManager session;
    private ListView listView;
    private ProductsAdapter adapter;
    private DatabaseHelper db;
    private ProgressDialog pDialog;
    private List<com.dataservicios.ttauditbayerpost.Model.Product> productList = new ArrayList<com.dataservicios.ttauditbayerpost.Model.Product>();

    private String tipo,cadenaruc,fechaRuta;
    private Integer user_id, company_id,store_id,rout_id,audit_id;

    private TextView tv_contador;
    private Button bt_finalizar;

    private int awards = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Productos");

        tv_contador = (TextView) findViewById(R.id.tvContador);
        bt_finalizar = (Button) findViewById(R.id.btFinalizar);

        Bundle bundle = getIntent().getExtras();
        company_id = bundle.getInt("company_id");
        store_id = bundle.getInt("idPDV");
        tipo = bundle.getString("tipo");
        cadenaruc = bundle.getString("cadenaruc");
        rout_id = bundle.getInt("idRuta");
        fechaRuta = bundle.getString("fechaRuta");
        audit_id = bundle.getInt("idAuditoria");


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        db = new DatabaseHelper(getApplicationContext());
        int total_products = db.getProductCount();
        tv_contador.setText(String.valueOf(total_products));

        listView = (ListView) findViewById(R.id.listProducts);
        productList =  db.getAllProducts();
        // adapter = new PublicityAdapter(this, db.getAllPublicity());
        adapter = new ProductsAdapter(MyActivity,  productList);
        listView.setAdapter(adapter);
        Log.d(LOG_TAG, String.valueOf(db.getAllProducts()));
        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // selected item
                String product_id = ((TextView) view.findViewById(R.id.tvId)).getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(), product_id, Toast.LENGTH_SHORT);
                toast.show();

                Bundle argPDV = new Bundle();
                argPDV.putInt("store_id", Integer.valueOf(store_id));
                argPDV.putInt("company_id", Integer.valueOf(company_id));
                argPDV.putInt("rout_id", Integer.valueOf(rout_id));
                argPDV.putString("tipo", String.valueOf(tipo));
                argPDV.putString("cadenaruc", String.valueOf(cadenaruc));
                argPDV.putInt("product_id", Integer.valueOf(product_id));
                argPDV.putString("fechaRuta", fechaRuta);
                argPDV.putInt("audit_id", audit_id);
                Intent intent = null;
                //Apronax
                if (product_id.equals("534")){
                    intent = new Intent(MyActivity, Apronax.class);
                }

//                //Aspirina 500
//                if (product_id.equals("535")){
//                    intent = new Intent(MyActivity, AspirinaQuinientos.class);
//                }

                //Aspirina 100
                if (product_id.equals("538")){
                    intent = new Intent(MyActivity, Aspirina.class);
                }

                //Gynocanasten
//                if (product_id.equals("537")){
//                    intent = new Intent(MyActivity, Gynocanesten.class);
//                }
//                Supradyn
                if (product_id.equals("536")){
                    intent = new Intent(MyActivity, Supradyn.class);
                }

//                //Redoxon
                if (product_id.equals("539")){
                    intent = new Intent(MyActivity, Redoxon.class);
                }

//                //Berocca
//                if (product_id.equals("540")){
//                    intent = new Intent(MyActivity, Berocca.class);
//                }

                //BEPANTHEN CREMA X 30
                if (product_id.equals("640")){
                    intent = new Intent(MyActivity, Bepanthen.class);
                }

                //Supradyn Pro natal
//                if (product_id.equals("642")){
//                    intent = new Intent(MyActivity, SupradynPronatal.class);
//                }

                //Canesten
                if (product_id.equals("643")){
                    intent = new Intent(MyActivity, Canesten.class);
                }

                ////ASPIRINA FORTE 644
                if (product_id.equals("644")){
                    intent = new Intent(MyActivity, AspirinaForte.class);
                }

                //BEROCCA + SUPRADYN
                if (product_id.equals("645")){
                    intent = new Intent(MyActivity, BeroccaSupradyn.class);
                }

                //Intent intent = new Intent(MyActivity, BayerPoll.class);
                intent.putExtras(argPDV);
                startActivity(intent);
            }
        });

        bt_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int totalItems=listView.getAdapter().getCount();
                String[] items = new String[totalItems];
                for(int i = 0; i < totalItems; i++){
                    items[i] = listView.getAdapter().getItem(i).toString();
                    com.dataservicios.ttauditbayerpost.Model.Product pd  =  new com.dataservicios.ttauditbayerpost.Model.Product();
                    pd = (com.dataservicios.ttauditbayerpost.Model.Product) adapter.getItem(i);

                    if(pd.getActive()==0) {

                        String nombre = pd.getName();
                        Toast.makeText(MyActivity,"Está pendiente para auditar " + nombre , Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Finalizar");
                builder.setMessage("Está seguro que desea finalizar: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ProductScore ps = new ProductScore();
                        ps = db.getProductScoreForStore(store_id);

                        int totalProductsScore = ps.getTotalProducts();
                        int totalExhibicionesScore = ps.getTotalExhibitions();


                        //--------------------------Logica de Premiacion ------------------------------
                        if(tipo.equals("CADENA")  || tipo.equals("MINI CADENAS")) {
                            if (totalProductsScore >= 1  && totalExhibicionesScore >= 1){
                                awards = 1;
                            }  else if (totalProductsScore >= 2 ){
                                awards = 1;
                            }

//                            if (totalProductsScore >= 1  && totalExhibicionesScore >= 1){
//                                awards = 1;
//                            }

                        } else if(tipo.equals("HORIZONTAL") || tipo.equals("DETALLISTA")   || tipo.equals("SUB DISTRIBUIDOR")) {

                            if (totalProductsScore >= 1  && totalExhibicionesScore >= 1){
                                awards = 1;
                            }  else if (totalProductsScore >= 2 ){
                                awards = 1;
                            }

//                            if (totalProductsScore >= 1 ){
//                                awards = 1;
//                            }
                        }

//                        if (totalProductsScore >= 3 ){
//                            awards = 1;
//                        }  else if (totalProductsScore ==2 && totalExhibicionesScore > 0){
//                            awards = 1;
//                        }


                        db.updateProductScoreForAwards(store_id, awards);
                        new closeAuditProducts().execute();
                        db.updateAllProductActive(0);

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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class closeAuditProducts extends AsyncTask<Void, Integer , Boolean> {
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
            InsertAuditPollsProduct("0","1","0","");

            if (awards == 1) {
                //new saveScore().execute();
                saveScoreStoreBayer(company_id, audit_id, store_id, user_id);
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
//                if (awards == 1) {
//                    new saveScore().execute();
//
//                } else {
                    hidepDialog();

                    Bundle argPDV = new Bundle();
                    argPDV.putInt("store_id", Integer.valueOf(store_id));
                    argPDV.putInt("company_id", Integer.valueOf(company_id));
                    argPDV.putInt("rout_id", Integer.valueOf(rout_id));
                    argPDV.putString("tipo", String.valueOf(tipo));
                    argPDV.putString("cadenaruc", String.valueOf(cadenaruc));
                    //argPDV.putInt("product_id", Integer.valueOf(product_id));
                    argPDV.putString("fechaRuta", fechaRuta);
                    argPDV.putInt("audit_id", audit_id);
                    //Intent intent = new Intent(MyActivity, Apronax275.class);
                    //Intent intent = new Intent(MyActivity, VariableImportante.class);
                    //Intent intent = new Intent(MyActivity, TieneApronaxActivity.class);
                    Intent intent = null;
                    if(tipo.equals("CADENA")  || tipo.equals("MINI CADENAS")) {
                        intent = new Intent(MyActivity, ConsumidorProductosIncentivadosActivity.class);
                        intent.putExtras(argPDV);
                        startActivity(intent);
                    } else if(tipo.equals("DETALLISTA")) {
                         intent = new Intent(MyActivity, TieneNaproxenoActivity.class);
                        intent.putExtras(argPDV);
                        startActivity(intent);
                    } else  {


                    }
                    finish();
//                }

            }
        }
    }


    private void InsertAuditPollsProduct(String poll_id, String status , String result, String comentario) {
        int success;
        try {


            HashMap<String, String> params = new HashMap<>();
            params.put("poll_id", "0");
            params.put("store_id", String.valueOf(store_id));
            params.put("product_id", "0");
            params.put("sino", "1");
            params.put("coment", String.valueOf(comentario));
            params.put("result", result);
            params.put("company_id", String.valueOf(GlobalConstant.company_id));
            params.put("idroute", String.valueOf(rout_id));
            params.put("idaudit", String.valueOf(audit_id));
            params.put("status", status);


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonInsertAuditPollsProduct" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                Log.d(LOG_TAG, json.getString("Ingresado correctamente"));
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void saveScoreStoreBayer( int company_id , int audit_id ,int store_id , int user_id) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("store_id", String.valueOf(store_id));
            params.put("company_id", String.valueOf(company_id));
            params.put("audit_id", String.valueOf(audit_id));
            params.put("user_id", String.valueOf(user_id));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/saveScoreStoreBayer" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            success = json.getInt("success");
            if (success == 1) {
                Log.d(LOG_TAG, json.getString("Ingresado correctamente"));
            }else{
                Log.d(LOG_TAG, json.getString("message"));
                // return json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }





    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }
}
