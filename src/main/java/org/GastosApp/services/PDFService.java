//package org.GastosApp.services;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.util.stream.Collectors;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//
//// importamos la libreria de apachePDFBox
//
//import org.GastosApp.model.Movement;
//import org.GastosApp.model.User;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
//
//import java.io.IOException;
//
//// TODO falta agregar alguna verificacion si no hay contenido para el reporte
//
//public abstract class PDFService {
//
//    public static void generarReporte(User usuario){
//        String nombreArchivo = "Reporte de movimientos_" + usuario.getNombre() + ".pdf";
//        List<Movement> movimientos = usuario.getCm().getRecord();
//        if(movimientos.isEmpty()){
//            System.out.println("No se han realizados movimientos");
//            return;
//        }
//
//        // 1 crear el documento
//        try(PDDocument document = new PDDocument()){
//            // 2 crear una pagina (por defecto carta/A4)
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            // creamos el flujo del documento
//            try(PDPageContentStream contentStream = new PDPageContentStream(document, page)){
//                // Definir fuente (En PDFBox 3.x se usa Standard14Fonts)
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
//
//                // iniciamos la escritura
//                contentStream.beginText();
//                contentStream.newLineAtOffset(50,750);
//                contentStream.showText("Reporte de Movimientos: " + usuario.getNombre());
//
//                // Ajustamos fuente para el listado
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
//                contentStream.newLineAtOffset(0, -40); // Bajamos un poco para empezar la lista
//
//                // recorremos los movimientos
//                for(String mov: movimientos){
//                    // escribimos una linea
//                    contentStream.showText("- " + mov);
//
//                    // IMPORTANTE: tenemos que ir bajando a la proxima linea
//                    // En PDFBox, los saltos de línea son manuales
//                    contentStream.newLineAtOffset(0, -15);
//
//                }
//                // finalizamos la escritura
//                contentStream.endText();
//
//            }
//            // se guarda el archivo
//            document.save(nombreArchivo);
//            System.out.println("Archivo guardado exitosamente como: " + nombreArchivo);
//
//        } catch(IOException exc){
//            System.out.println( "Error al generar el reporte: " + exc.getMessage());
//        }
//    }
//
//    public static void reporteDiario(User usuario){
//        String nombreArchivo = "Reporte de movimientos diarios_" + usuario.getNombre() + ".pdf";
//        // 1. Obtenemos la fecha de hoy como String (el formato que usas en tu record)
//        String fechaHoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//        List<String> movimientosHoy = usuario.getCm().getRecord().stream()
//                .filter(mov -> mov.contains(fechaHoy)) // "Filtra los que contengan la fecha de hoy"
//                .collect(Collectors.toList()); // "Convierte el resultado de vuelta a una Lista" puedo usar directamente toList() pero viene de Collector
//        if(movimientosHoy.isEmpty()){
//            System.out.println("No se han realizados movimientos");
//            return;
//        }
//
//        try(PDDocument document = new PDDocument()){
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//
//
//            try(PDPageContentStream contentStream = new PDPageContentStream(document,page)){
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
//                // iniciamos la escritura
//                contentStream.beginText();
//                contentStream.newLineAtOffset(50,750);
//                contentStream.showText("Reporte de Movimientos Diarios: " + usuario.getNombre());
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
//                contentStream.newLineAtOffset(0, -40);
//
//                // iniciamos el ciclo
//                for(String mov: movimientosHoy){
//                    // escribimos una linea
//                    contentStream.showText("- " + mov);
//
//                    // IMPORTANTE: tenemos que ir bajando a la proxima linea
//                    // En PDFBox, los saltos de línea son manuales
//                    contentStream.newLineAtOffset(0, -15);
//                }
//
//
//
//
//                // cerrramos el archivo
//                contentStream.endText();
//            }
//
//            // lo guardamos!
//            document.save(nombreArchivo);
//            System.out.println("Archivo guardado exitosamente como: " + nombreArchivo);
//
//        } catch(IOException exc){
//            System.out.println("No es posible crear el reporte " + exc.getMessage());
//        }
//    }
//}
