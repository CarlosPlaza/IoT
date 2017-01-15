/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Parametros.Parametro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author carlo
 */

public class MySQL {
 
    Connection conexion = null;
    Statement comando = null;
    ResultSet registro;
    public MySQL(){
        MySQLConnect();
    }
    
    private Connection MySQLConnect() {
 
        try {
            //Driver JDBC
            Class.forName("com.mysql.jdbc.Driver");
            //Nombre del servidor. localhost:3306 es la ruta y el puerto de la conexión MySQL
            //panamahitek_text es el nombre que le dimos a la base de datos
            String servidor = "jdbc:mysql://localhost:3306/movilidad";
            //El root es el nombre de usuario por default. No hay contraseña
            String usuario = "root";
            String pass = "";
            //Se inicia la conexión
            conexion = DriverManager.getConnection(servidor, usuario, pass);
 
        } catch (ClassNotFoundException ex) {
          //  JOptionPane.showMessageDialog(null, ex, "1Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } catch (SQLException ex) {
           // JOptionPane.showMessageDialog(null, ex, "2Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } catch (Exception ex) {
          //  JOptionPane.showMessageDialog(null, ex, "3Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } finally {
          //  JOptionPane.showMessageDialog(null, "Conexión Exitosa");
            return conexion;
        }
    }
    
    
    public Parametro getLineas(Parametro p){
        
        try {
            print("Codigo NFC:"+p.getCodigo_nfc());
            String query="SELECT distinct CONCAT(linea, '-', sentido) linSen FROM estaciones where codigo_nfc=CAST("+p.getCodigo_nfc()+" AS SIGNED)";        
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            /* Se imprimen los registros que estén guardados en la base de datos*/
            ArrayList<String> lineas = new ArrayList<String>();
            while (this.registro.next()) {
                lineas.add(this.registro.getString(1));      
                
            }
            
            print("Lineas: "+lineas.toString());
            lineas.add("noVal");
            lineas.add("noVal");
            p.setLineas(to_arrayS(lineas));
            return p;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(MySQL_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Parametro();
    }
    public Parametro getPosicion(Parametro p){
        
        try {
            String query="select * "
                    + "from actividad_lineas "
                    + "where id_linea="+p.getLinea()+" and id_sentido="+p.getSentido();        
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            /* Se imprimen los registros que estén guardados en la base de datos*/
            double min=1000000;
            double d=0;
            while (this.registro.next()) {
                d=distanciaCoord(this.registro.getDouble(5),this.registro.getDouble(4),p.getLatitudP(),p.getLongitudP());
                /*val=Math.pow(this.registro.getDouble(4)-p.getLatitudP(), 2)+Math.pow(this.registro.getDouble(5)-p.getLongitudP(), 2);
                d=Math.pow(val,0.5);//Se calcula la distancia de cada bus con esa linea y sentido, respecto al usuario
                p.setLatitud(this.registro.getDouble(4));
                p.setLongitud(this.registro.getDouble(5));*/
                print(d);
                if(d<min){//Se lecciona las coordenadas del bus mas proximo al usuario
                    min=d;
                    
                    p.setLatitud(this.registro.getDouble(5));
                    p.setLongitud(this.registro.getDouble(4));
                    print("Lat: " +this.registro.getDouble(4)+", Lon:"+this.registro.getDouble(5));
                }
               //print("Nombre: " + this.registro.getTimestamp(6).getMinutes()); 
            }
            min=min*1000;
            p.setDistancia(min);
            p.setTiempo((9*min)/50);//v=e/t - t=e/v  //Se considera una velocidad de 20km/h
            return p;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(MySQL_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Parametro();
    }
    
    
    public Parametro getInfo(Parametro p){
        
        try {
            String condicion=" linea="+p.getLinea()+" and sentido="+p.getSentido();
            // ------------------------- SESLECCIONAR ORIGEN Y DESTINO DE LINEA
            String query="select origen,destino "
                    + "from linea where "+condicion+" limit 1";
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            while (this.registro.next()) {
                p.setOrigen(this.registro.getString(1));
                p.setDestino(this.registro.getString(2));
            }
            // -------------------------- SELECCIONAR ESTACIONES DE LINEA
            query="select latitud,longitud,ubicacion "
                    + "from estaciones_hora "
                    + "where "+condicion;        
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            /* Se imprimen los registros que estén guardados en la base de datos*/
            ArrayList<Double> latituds = new ArrayList<Double>();
            ArrayList<Double> longituds = new ArrayList<Double>();
            ArrayList<String> ubicaciones = new ArrayList<String>();
            //print("=======================");
            while (this.registro.next()) {
                latituds.add(this.registro.getDouble(1));
                longituds.add(this.registro.getDouble(2));
                ubicaciones.add(this.registro.getString(3));
                print(this.registro.getString(1));
               //print("Nombre: " + this.registro.getTimestamp(6).getMinutes()); 
            }
            latituds.add(0.0);
            longituds.add(0.0);
            latituds.add(0.0);
            longituds.add(0.0);
            ubicaciones.add("noVal");
            ubicaciones.add("noVal");
            p.setLatituds(to_arrayD(latituds));
            p.setLongituds(to_arrayD(longituds));
            p.setUbicaciones(to_arrayS(ubicaciones));
            // ------------------------------- SELECCION DE RUTA DE BUS
            query="SELECT distinct latitud,longitud FROM movilidad.rutas_hora \n" +
            "where "+condicion+"";
            latituds= new ArrayList<Double>();
            longituds = new ArrayList<Double>();
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            while (this.registro.next()) {
                latituds.add(this.registro.getDouble(1));
                longituds.add(this.registro.getDouble(2));
                //print(this.registro.getDouble(1)+" "+ this.registro.getDouble(2));
               //print("Nombre: " + this.registro.getTimestamp(6).getMinutes()); 
            }
            p.setLatitudsR(to_arrayD(latituds));
            p.setLongitudsR(to_arrayD(longituds));
            return p;
            //ArrayUtils.toPrimitive(latituds.toArray(new Double[latituds.size()]));
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(MySQL_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Parametro();
    }
    
    private static double[] to_arrayD(ArrayList<Double> doubles)
    {
        double[] ret = new double[doubles.size()];
        Iterator<Double> iterator = doubles.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            ret[i] = iterator.next();
            i++;
        }
        return ret;
    }
    private static String[] to_arrayS(ArrayList<String> strings)
    {
        String[] ret = new String[strings.size()];
        Iterator<String> iterator = strings.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            ret[i] = iterator.next();
            i++;
        }
        return ret;
    }
    
    public static void print(Object o){
         System.out.println(o);
    }
    
    public Parametro getBusProximo(Parametro p){
        
        try {
            print("================== Nueva Lectura ================");
            print("Tipo request lineas: "+p.getAllLinea());
            String query="SELECT latitud,longitud FROM estaciones_hora WHERE ";
            String condicion=" linea="+p.getLinea()+" and sentido="+p.getSentido()+" ";
            query=query+condicion;
            //--------------------- SE SELECCIONA LA PARADA MAS PROXIMA AL USUARIO
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            double min=1000000;
            double d=0;
            double[] p1=new double[2],p2=new double[2],hk=new double[2];
            while (this.registro.next()) {
                d=distanciaCoord(this.registro.getDouble(1),this.registro.getDouble(2),p.getLatitudP(),p.getLongitudP());
                print(d);
                if(d<min){//Se lecciona las coordenadas del bus mas proximo al usuario
                    min=d;
                    
                    hk[0]=this.registro.getDouble(1);
                    hk[1]=this.registro.getDouble(2);//Coordenada de parada
                    //print("Estaciones>>> Lat: "+hk[0]+", lon: "+hk[1]);
                } 
            }
            
            //-------------------- SE SELECCIONA LA RUTA DE LA LINEA Y SENTIDO
            query="SELECT distinct latitud,longitud FROM movilidad.rutas_hora WHERE ";
            condicion=" linea="+p.getLinea()+" and sentido="+p.getSentido()+" ";
            query=query+condicion;
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            ArrayList<double[]> coordenadas=new ArrayList<double[]>();
            int c=0;
            double[] A;
            double radioBus,radioParada=0.010792830504874174;
            //radioParada=0.015;
            while (this.registro.next()) {
                p2[0]=this.registro.getDouble(1);
                p2[1]=this.registro.getDouble(2);
                coordenadas.add(new double[]{p2[0],p2[1]});
                //print("Rutas>>> Lat: "+p2[0]+", lon: "+p2[1]);
                if(c>0){//Se lecciona las coordenadas del bus mas proximo al usuario
                   A=puntoOrtogonal(p1, p2, hk,radioParada);
                   //print("Punto ortogonal ParadaRuta: A: "+A);
                   if(A!=null){
                       print("Ruta elegida>>> Lat: "+p2[0]+", lon: "+p2[1]);
                       break;
                   }
                } 
                p1=p2.clone();
                c++;
            }
            coordenadas.add(hk);
            coordenadas.remove(coordenadas.size()-2);
            //----------------------------SE SELECCIONA EL BUS MAS CERCANO A LA PARADA DEL USUARIO
            radioBus=0.008340023497561063;
            //radioBus=0.01;
            query="select latitud,longitud " //del bus
                    + "from actividad_lineas "
                    + "where id_linea="+p.getLinea()+" and id_sentido="+p.getSentido();        
            this.comando = this.conexion.createStatement();
            this.registro= this.comando.executeQuery(query);
            /* Se imprimen los registros que estén guardados en la base de datos*/
            min=10000000;
            d=0;
            double[] puntoBus={0,0},pb=new double[2];
            boolean calcularDistancia,valido=false;
            int auxi=0;
            ArrayList<Double> latitudsB = new ArrayList<Double>();
            ArrayList<Double> longitudsB = new ArrayList<Double>();
            ArrayList<Double> tiemposB = new ArrayList<Double>();
            ArrayList<Double> distanciasB = new ArrayList<Double>();
            double velocidad=25;
            while (this.registro.next()) {
                pb[0]=this.registro.getDouble(1);
                pb[1]=this.registro.getDouble(2);
                print("Linea Leida>>> Lat: "+pb[0]+", lon: "+pb[1]);
                calcularDistancia=false;
                for(int i=1;i<coordenadas.size();i++){
                    A=puntoOrtogonal(coordenadas.get(i-1), coordenadas.get(i), pb,radioBus);
                    if(A!=null){
                        print("Punto BusRuta: lat:"+A[0]+", lon: "+A[1]);
                        auxi=i;
                        calcularDistancia=true;
                        break;
                    }
                }
                d=-1;
                if(calcularDistancia){
                   p2=coordenadas.get(auxi);
                   
                   d=distanciaCoord(pb[0],pb[1], p2[0],p2[1] );
                   for(int c2=auxi+1;c2<coordenadas.size();c2++){
                        p1=coordenadas.get(c2-1);
                        p2=coordenadas.get(c2);
                        d=d+distanciaCoord(p1[0],p1[1], p2[0],p2[1] );
                    } 
                }
                if(p.getAllLinea()==1 && d>=0){
                    latitudsB.add(pb[0]);
                    longitudsB.add(pb[1]);
                    tiemposB.add(redondear(calcularTiempo(d,velocidad),2));
                    distanciasB.add(redondear(d*1000,2));
                }else{
                //print("Distancia Bus: "+d);
                    if(0<=d && d<min){//Se lecciona las coordenadas del bus mas proximo al usuario
                        min=d;
                        puntoBus=pb;
                    }
                }
               //print("Nombre: " + this.registro.getTimestamp(6).getMinutes()); 
            }
            if(p.getAllLinea()==1){
                latitudsB.add(0.0);latitudsB.add(0.0);
                longitudsB.add(0.0);longitudsB.add(0.0);
                tiemposB.add(0.0);tiemposB.add(0.0);
                distanciasB.add(0.0);distanciasB.add(0.0);
                p.setLatitudsB(to_arrayD(latitudsB));
                p.setLongitudsB(to_arrayD(longitudsB));
                p.setTiemposB(to_arrayD(tiemposB));
                p.setDistanciasB(to_arrayD(distanciasB));
            }else{
                print("Bus>>> Lat: "+puntoBus[0]+", lon: "+puntoBus[1]+", distancia: "+min);
                p.setLatitud(puntoBus[0]);
                p.setLongitud(puntoBus[1]);
                p.setDistancia(redondear(min*1000,2));
                p.setTiempo(redondear(calcularTiempo(min,velocidad),2));
            }
            return p;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //Logger.getLogger(MySQL_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Parametro();
    }
    
    public double calcularTiempo(double espacio,double velocidad){//En metros, km/h
        espacio=espacio*1000;// a metros
        return espacio/((10*velocidad)/36);  //v=e/t - t=e/v 
    }
    
    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {  //Distancia en Km
        //double radioTierra = 3958.75;//en millas  
        double radioTierra = 6371;//en kilómetros  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));  
        double distancia = radioTierra * va2;  
        //print(distancia);
        return distancia;  
    }  
    
    public static double[] puntoOrtogonal(double p0[], double p1[], double hk[], double radio) {  //Distancia en Km
        //double radioTierra = 3958.75;//en millas 
        double vx=p1[0]-p0[0];
        double vy=p1[1]-p0[1];
        double denominador=Math.pow(vx, 2)+Math.pow(vy, 2);
        if(denominador==0){denominador=1;}
        //if(denominador!=0){
        double c=((hk[0]-p0[0])*vx+(hk[1]-p0[1])*vy)/denominador;
        double Ax=p0[0]+c*vx;
        double Ay=p0[1]+c*vy;
        double[] rx=ordenar(new double[]{p0[0],p1[0]});
        double[] ry=ordenar(new double[]{p0[1],p1[1]});
        double r=distanciaCoord(hk[0],hk[1],Ax,Ay);
        print("P>>> h:"+hk[0]+", k:"+hk[1]+", x0: "+p0[0]+", y0: "+p0[1]);
        print("Distancia calculada: "+r);
        if((r<=radio) &&(rx[0]<=Ax && Ax<=rx[1]) && ry[0]<=Ay && Ay<=ry[1]){
            return new double[]{Ax,Ay};
        }
       // }
        return null;  
    }
    
    public boolean verificarInterseccion(){
        double x0=0,y0=0,x1=0,y1=0,h=0,k=0,r=0;
        double m=(y0-y1)/(x0-x1);
        double A=(1+Math.pow(m, 2));  // 1+m^2
        double B=2*(-h+m*(-m*x0+y0-k));//2(-h+m(-mx0+yo-k))
        double C=Math.pow(h, 2)+x0*m*(m-2*(y0-k))+y0*(y0-2*k)+Math.pow(k, 2)-Math.pow(r, 2);//h^2+xom(m-2yo+2k)+y0(y0-2k)+k^2-r^2=0
        double lamda=Math.pow(B, 2)-4*A*C;
        if(lamda>=0){
            return true;
        }
        return false;
    }
    public static double[] ordenar(double num[]){
        if(num[0]>num[1]){
            return new double[]{num[1],num[0]};
        }
        return num;
    }
    public static double redondear(double numero, int digitos) {
        double resultado;
        resultado = numero * Math.pow(10, digitos);
        resultado = Math.round(resultado);
        resultado = resultado/Math.pow(10, digitos);
        return resultado;
    }
    
    // ==========================================BUS
    public void insertData(Parametro p){
        try{
            String query;    
            //query="INSERT INTO movilidad.historico_lineas VALUES (null,"+p.getLinea()+","+p.getSentido()+","+p.getLongitud()+","+p.getLatitud()+","+p.getCodigo()+",NOW())";
            query="INSERT INTO movilidad.historico_lineas VALUES (null,24,1,-79.010050,-2.899256,2000000,NOW())";
            this.comando = this.conexion.createStatement();
            int executeUpdate = this.comando.executeUpdate(query);
            print("Resultado INSERT: "+executeUpdate);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
// Ax^2+Bx+C=0
//lamda=b^2-4ac
//lamda>0 Recta y circunferencia secantes
//lamda=0 Recta y circunferencia tangentes
//lamda<0 No se juntan en ningun punto
//https://donnierock.com/2015/03/16/calculando-la-distancia-entre-doos-coordenadas-en-java/