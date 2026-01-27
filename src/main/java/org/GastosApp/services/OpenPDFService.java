package org.GastosApp.services;



import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import org.GastosApp.model.Movement;
import org.GastosApp.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Color;
import java.util.List;

public class OpenPDFService {

    public static void generarReporteMovimientos(User usuario){
        String nombreArchivo = "Reporte de movimientos " + usuario.getNombre() + ".pdf";

        // creamos un nuevo documento
        try(Document document = new Document(PageSize.A4);){
            // utilizamos una instancia del documento junto con el outputStream para cuando se crea un nuevo archivo
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            // Abrimos el documento para escribir
            document.open();

            // 1. Encabezado del documento
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph titulo = new Paragraph("Estado de Cuenta: usuario " + usuario.getNombre(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            // 2. Creacion de la tabla
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.5f, 2.5f, 3.5f, 1.5f}); //ajuste de anchos proporcional

            // 3. Agregamos los encabezados
            agregarCeldaEncabezado(table, "Fecha");
            agregarCeldaEncabezado(table, "Categoría");
            agregarCeldaEncabezado(table, "Descripción");
            agregarCeldaEncabezado(table, "Monto");

            //4. Carga de datos desde la lista Movement
            List<Movement> movs = usuario.getCm().getRecord();
            Font fontCeldas = FontFactory.getFont(FontFactory.HELVETICA, 10);

            for (Movement mov: movs){
                table.addCell(new Phrase(mov.getDate(), fontCeldas));
                table.addCell(new Phrase(mov.getCategory(), fontCeldas));
                table.addCell(new Phrase(mov.getDescription(), fontCeldas));
                // Formato para el monto
                PdfPCell celdaMonto = new PdfPCell(new Phrase("$" + mov.getAmount(), fontCeldas));
                celdaMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(celdaMonto);
            }

            document.add(table);
            System.out.println("Reporte PDF generado con exito: " + nombreArchivo);

        } catch(DocumentException | IOException exc){
            System.out.println("Eroor al crear el PDF: "+ exc.getMessage());
        }
    }

    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        Font fontH = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell celda = new PdfPCell(new Phrase(texto, fontH));
        celda.setBackgroundColor(Color.DARK_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

}
