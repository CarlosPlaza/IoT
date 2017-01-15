/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parametros;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlo
 */
@XmlRootElement
public class Parametro {
    int codRequest=0;
    int linea=24;
    //=========================      Lineas
    //int codigo=1234567890; Referente a tag
    String[] lineas;
    
    //=========================      BUS
    
    int allLinea=0;// 0 un Bus | 1 todos los disponibles
    int sentido=1;
    String codigo_nfc="";  
    double latitud=0;
    double longitud=0;
    double tiempo=0;
    double latitudP=0;
    double longitudP=0;
    double[] latitudsB={};
    double[] longitudsB={};
    double[] tiemposB={};
    double[] distanciasB={};
    //=======================      USUARIO
    String correo="";
    String password="";
    String id="";
    //double latitud;
    //double longitud;
    //========================== InformacionBUS
    //double latitud;
    //double longitud;
    String origen="";
    String destino="";
    double distancia=0;
    double[] latituds={};
    double[] longituds={};
    double[] latitudsR={};
    double[] longitudsR={};
    //=============================== PARADAS
    //double[] latituds;
    //double[] longituds;
    String[] ubicaciones;
    public Parametro(){}

    public int getCodRequest() {
        return codRequest;
    }

    public void setCodRequest(int codRequest) {
        this.codRequest = codRequest;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSentido() {
        return sentido;
    }

    public void setSentido(int sentido) {
        this.sentido = sentido;
    }

    public String getCodigo_nfc() {
        return codigo_nfc;
    }

    public void setCodigo_nfc(String codigo_nfc) {
        this.codigo_nfc = codigo_nfc;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double[] getLatituds() {
        return latituds;
    }

    public void setLatituds(double[] latituds) {
        this.latituds = latituds;
    }

    public double[] getLongituds() {
        return longituds;
    }

    public void setLongituds(double[] longituds) {
        this.longituds = longituds;
    }

    public double getLatitudP() {
        return latitudP;
    }

    public void setLatitudP(double latitudP) {
        this.latitudP = latitudP;
    }

    public double getLongitudP() {
        return longitudP;
    }

    public void setLongitudP(double longitudP) {
        this.longitudP = longitudP;
    }

    public String[] getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(String[] ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public double[] getLatitudsR() {
        return latitudsR;
    }

    public void setLatitudsR(double[] latitudsR) {
        this.latitudsR = latitudsR;
    }

    public double[] getLongitudsR() {
        return longitudsR;
    }

    public void setLongitudsR(double[] longitudsR) {
        this.longitudsR = longitudsR;
    }

    public String[] getLineas() {
        return lineas;
    }

    public void setLineas(String[] lineas) {
        this.lineas = lineas;
    }

    public double[] getLatitudsB() {
        return latitudsB;
    }

    public void setLatitudsB(double[] latitudsB) {
        this.latitudsB = latitudsB;
    }

    public double[] getLongitudsB() {
        return longitudsB;
    }

    public void setLongitudsB(double[] longitudsB) {
        this.longitudsB = longitudsB;
    }

    public double[] getTiemposB() {
        return tiemposB;
    }

    public void setTiemposB(double[] tiemposB) {
        this.tiemposB = tiemposB;
    }

    public double[] getDistanciasB() {
        return distanciasB;
    }

    public void setDistanciasB(double[] distanciasB) {
        this.distanciasB = distanciasB;
    }

    public int getAllLinea() {
        return allLinea;
    }

    public void setAllLinea(int allLinea) {
        this.allLinea = allLinea;
    }

    
    
}
