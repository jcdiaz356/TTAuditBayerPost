package com.dataservicios.ttauditbayerpost.util;

/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {
   public static String dominio = "http://ttaudit.com";
    public static final String LOGIN_URL = dominio + "/loginUser" ;
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 96;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
           1618, //0	  // 1553, 	¿Se encuentra abierto el establecimiento?
           1619, //1	  // 1554, 	¿Tiene exhibición Bayer?
           1620, //2	  // 1555, 	¿Se recomendo el Producto?
           1621, //3	  // 1556, 	¿Qué Producto recomendo?
           1622, //4	  // 1557, 	¿Tiene Stock?
           1623, //5	  // 1558, 	¿Recibio Premio?
           1624, //6	  // 1598, 	¿Tienes Naproxeno sódico genérico de 550mg?
           1625, //7	  // 1599, 	¿Cuantas pastillas compra un consumidor por vez de Naproxeno sódico 550mg genérico?marca?
           1626, //8	  // 1600, 	¿Cuantas pastillas viene en un blíster?
           1627, //9	  // 1601, 	¿Cuál es el precio de la pastilla?
           1628, //10     // 1602, 	¿Cuál es el precio del blíster?
           1629, //11     // 1603, 	Ahora que no hay Dolocordralan en el mercado que producto similar recomienda?
           1630, //12     // 1604, 	Doloneurobion vienen realizando algún tipo de actividad promocional con ustedes?
           1633, //13               Qué tipo de actividades viene realizando Doloneurobion con ustedes?
           1634, //14               Cada cuánto tiempo lo visita un representante/promotor de Doloneurobion?
    } ;

    public static int[] audit_id = new int[]{
//            14,	// 0 "Bayer Productos"
    } ;

    public static final String JPEG_FILE_PREFIX = "_bayer_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}