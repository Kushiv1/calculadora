package com.example.calculadorajava;

public class Calculo {
    private double numero1;
    private double numero2;
    private String operador;
    private double resultado;
    private String usuarioId;
    private long timestamp;

    // Constructor vacío requerido por Firebase
    public Calculo() {}

    // Constructor completo
    public Calculo(double numero1, double numero2, String operador, double resultado, String usuarioId, long timestamp) {
        this.numero1 = numero1;
        this.numero2 = numero2;
        this.operador = operador;
        this.resultado = resultado;
        this.usuarioId = usuarioId;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public double getNumero1() {
        return numero1;
    }

    public void setNumero1(double numero1) {
        this.numero1 = numero1;
    }

    public double getNumero2() {
        return numero2;
    }

    public void setNumero2(double numero2) {
        this.numero2 = numero2;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Método para obtener una representación de la operación
    public String getOperacionCompleta() {
        return numero1 + " " + operador + " " + numero2 + " = " + resultado;
    }
}
