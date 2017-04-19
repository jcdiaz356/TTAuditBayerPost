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
    public static int company_id = 73;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";
    public static int[] poll_id = new int[]{
            1214 ,  // 1019, // 939, //	0	¿Se encuentra abierto el establecimiento?
            1215 ,  // 1020, // 940, //	1	¿Tiene exhibición Bayer?
            1216 ,  // 1021, // 941, //	2	¿Se recomendo el Producto?
            1217 ,  // 1022, // 942, //	3	¿Qué Producto recomendo?
            1218 ,  // 1023, // 943, //	4	¿Tiene Stock?
            1219 ,  // 1024, // 944, //	5	¿Recibio Premio?
            1220 ,  // 1025, // 945, //	6	¿Qué variable es importante para recomendar un producto o marca OTC (venta sin receta) por encima de otro?
            1221 ,  // 1026, // 946, //	7	Por favor seleccionar
            1222 ,  // 1027, // 947, //	8	 Que dias de la semana prefiere recibir una orientación y/o capacitación para cumplir con las exigencias de la autoridad de salud (marcar maximo 2 días de su preferencia)
            1223 ,  // 1028, // 948, //	9	Según el día que eligio en que horario podria recibir la orientación y/o capacitación
            1224 ,  // 1029, // 949, //	10	Desea recibir orientación y/o capacitación en algún otro tema de su interes, escribir el tema
            1225 ,  // 1030, // 950, //	11	Sugerencias

} ;

    public static int[] audit_id = new int[]{
//            14,	// 0 "Bayer Productos"
    } ;

    public static final String JPEG_FILE_PREFIX = "_bayer_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}