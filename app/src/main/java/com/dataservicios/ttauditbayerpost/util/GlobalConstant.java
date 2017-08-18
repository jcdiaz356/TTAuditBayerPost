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
           1618, //0	  // 1553, // 1479 ,  // 1355 ,  // 1019, // 939,    //0	¿Se encuentra abierto el establecimiento?
           1619, //1	  // 1554, // 1480 ,  // 1356 ,  // 1020, // 940,    //1	¿Tiene exhibición Bayer?
           1620, //2	  // 1555, // 1481 ,  // 1357 ,  // 1021, // 941,    //2	¿Se recomendo el Producto?
           1621, //3	  // 1556, // 1482 ,  // 1358 ,  // 1022, // 942,    //3	¿Qué Producto recomendo?
           1622, //4	  // 1557, // 1483 ,  // 1359 ,  // 1023, // 943,    //4	¿Tiene Stock?
           1623, //5	  // 1558, // 1484 ,  // 1360 ,  // 1024, // 944,    //5	¿Recibio Premio?
           1624, //6	  // 1598, // 1485 ,  // 1361 ,  // 1025, // 945,    //6	¿Tienes Naproxeno sódico genérico de 550mg?
           1625, //7	  // 1599, // 1487 ,  // //1221 ,  // 1026, // 946,  //7	¿Cuantas pastillas compra un consumidor por vez de Naproxeno sódico 550mg genérico?marca?
           1626, //8	  // 1600, // 1489 ,  // //1222 ,  // 1027, // 947,  //8	¿Cuantas pastillas viene en un blíster?
           1627, //9	  // 1601, // 1492 ,  // //1223 ,  // 1028, // 948,  //9	¿Cuál es el precio de la pastilla?
           1628, //10     // 1602, // 1493 ,  // //1224 ,  // 1029, // 949,  //10	¿Cuál es el precio del blíster?
           1629, //11     // 1603, // 1493 ,  // //1224 ,  // 1029, // 949,  //11	¿Cuando algún consumidor pregunta sobre un medicamente siempre ofreces primero los productos incentivados?
           1630, //12     // 1604, // 1493 ,  // //1224 ,  // 1029, // 949,  //12	¿Qué tanto intentas cambiar el producto que te pide un consumidor con productos incentivados
           1633, //13
           1634, //14
    } ;

    public static int[] audit_id = new int[]{
//            14,	// 0 "Bayer Productos"
    } ;

    public static final String JPEG_FILE_PREFIX = "_bayer_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}