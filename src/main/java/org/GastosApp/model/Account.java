package org.GastosApp.model;

// esta clase,se encarga de mantener el estado individual de la cuenta

public class Account {
    private static int contadorId = 0;
    // Atributos, monto de la cuenta, nombre, identificador
    private double monto;
    private String nombre;
    private int accountId = 0;
    private int userId = 0;

    public Account(double montoInicial, String nombre, int userId){
        this.monto = montoInicial;
        this.nombre = nombre;
        contadorId++;
        this.accountId = contadorId;
        this.userId = userId;

    }
    // sobrecarga de metodos
    public Account(int id, double montoInicial, String nombre, int userId){
        this.monto = montoInicial;
        this.nombre = nombre;
        this.accountId = id;
        this.userId = userId;
        // nos aseguramos de mantener los ids sincronizados
        if (id >= contadorId) {
            contadorId = id;
        }
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    // el id no se cambiará por este lado, solamente lo devolveremos
    public int getAccountId() {
        return accountId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUserId(){
        return this.userId;
    }

    public void deposito(double din){
        double sum = this.getMonto() + din;
        this.setMonto(sum);
    }

    public void extraccion(double din){
        double rest = this.getMonto() - din;
        this.setMonto(rest);
    }

    public String showInfo(){
        return "Cuenta: " + getNombre() + ", Monto: $" + getMonto();
    }
}
