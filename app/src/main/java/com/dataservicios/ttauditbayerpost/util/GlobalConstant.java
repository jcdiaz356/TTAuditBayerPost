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
    public static int company_id = 163;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
         2686, // 2431 , // 2152, //2041, //1881 , //1839, // 1618, //0	  // 1553, 	¿Se encuentra abierto el establecimiento?
         2687, // 2432 , // 2153, //2042, //1882 , //1840, // 1619, //1	  // 1554, 	¿Tiene exhibición Bayer?
         2688, // 2433 , // 2154, //2043, //1883 , //1841, // 1620, //2	  // 1555, 	¿Se recomendo el Producto?
         2689, // 2434 , // 2155, //2044, //1884 , //1842, // 1621, //3	  // 1556, 	¿Qué Producto recomendo?
         2690, // 2435 , // 2156, //2045, //1885 , //1843, // 1622, //4	  // 1557, 	¿Tiene Stock?
         2691, // 2436 , // 2157, //2046, //1886 , //1844, // 1623, //5	  // 1558, 	¿Recibio Premio?
                //1845, // 1624, //6	  // 1598, 	¿Tienes Naproxeno sódico genérico de 550mg?
                //1846, // 1625, //7	  // 1599, 	¿Cuantas pastillas compra un consumidor por vez de Naproxeno sódico 550mg genérico?marca?
                //1847, // 1626, //8	  // 1600, 	¿Cuantas pastillas viene en un blíster?
                //1848, // 1627, //9	  // 1601, 	¿Cuál es el precio de la pastilla?
                //1849, // 1628, //10     // 1602, 	¿Cuál es el precio del blíster?
                //1850, // 1629, //11     // 1603, 	Ahora que no hay Dolocordralan en el mercado que producto similar recomienda?
                //1851, // 1630, //12     // 1604, 	Doloneurobion vienen realizando algún tipo de actividad promocional con ustedes?
                //1852, // 1633, //13               Qué tipo de actividades viene realizando Doloneurobion con ustedes?
                //1853, // 1634, //14               Cada cuánto tiempo lo visita un representante/promotor de Doloneurobion?
    } ;

    public static int[] audit_id = new int[]{
//            14,	// 0 "Bayer Productos"
    } ;

    public static final String JPEG_FILE_PREFIX = "_bayer_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}