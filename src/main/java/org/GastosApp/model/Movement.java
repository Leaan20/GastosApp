package org.GastosApp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Movement {

    private final String description;
    private final String category;
    private final BigDecimal amount;
    private final String date;
    // Fecha formateada
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    // para cuando utilicemos la base de datos

    public Movement( String category, String desc , BigDecimal amount){
        this.category = category;
        this.description = desc;
        this.amount = amount;
        this.date = LocalDateTime.now().format(fmt);

    }


    public String  showMov(){
        return "Categoria: " + category + "\n Descripcion: " + description + "\n monto: $" + amount + "\n Fecha: " + date ;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount(){ return amount; }

    public String getDate(){ return date; }




}
