package com.company.files;

import com.company.controller.MainController;
import com.company.entity.Conversion;
import com.company.entity.Currency;
import com.company.entity.Customer;
import com.company.db.Database;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public interface WorkWithFiles {

    Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    String BASE_FOLDER = "src/main/resources/documents";
    File CUSTOMER_FILE = new File(BASE_FOLDER, "customers.json");
    File CONVERSION_FILE = new File(BASE_FOLDER, "conversions.json");

    static void readCustomerList(){
        if(!CUSTOMER_FILE.exists()) return;

        try {
            Customer[] customers = GSON.fromJson(new BufferedReader(new FileReader(CUSTOMER_FILE)), Customer[].class);
//            List customers = GSON.fromJson(new BufferedReader(new FileReader(CUSTOMER_FILE)),
//                    new TypeToken<List<Customer>>(){}.getType());
            Database.customerList.clear();
            Database.customerList.addAll(List.of(customers));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void writeCustomerList(){
        try (PrintWriter writer = new PrintWriter(CUSTOMER_FILE)) {
            writer.write(GSON.toJson(Database.customerList));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void readConversionList(){
        if(!CONVERSION_FILE.exists()) return;

        try {
            Conversion[] conversions = GSON.fromJson(new BufferedReader(new FileReader(CONVERSION_FILE)), Conversion[].class);
//            List conversions = GSON.fromJson(new BufferedReader(new FileReader(CONVERSION_FILE)),
//                    new TypeToken<List<Conversion>>() {
//                    }.getType());
            Database.conversionList.clear();
            Database.conversionList.addAll(List.of(conversions));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void writeConversionList(){
        try (PrintWriter writer = new PrintWriter(CONVERSION_FILE)) {
            writer.write(GSON.toJson(Database.conversionList));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static File generateUsersExcelFile() {
        File file = new File(BASE_FOLDER, "users.xlsx");

        try (FileOutputStream out = new FileOutputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook();
        ) {

            XSSFSheet sheet = workbook.createSheet();

            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("Chat Id");
            header.createCell(1).setCellValue("First Name");
            header.createCell(2).setCellValue("Last Name");
            header.createCell(3).setCellValue("Phone Number");

            int i=0;
            for (Customer customer : Database.customerList) {
                i++;
                XSSFRow row = sheet.createRow(i);
                row.createCell(0).setCellValue(customer.getChatId());
                row.createCell(1).setCellValue(customer.getFirstName());
                row.createCell(2).setCellValue(customer.getLastName());
                row.createCell(3).setCellValue(customer.getPhoneNumber());
            }

            for (int j = 0; j < 4; j++) {
                sheet.autoSizeColumn(j);
            }

            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    static File generateConversionsExcelFile() {
        File file = new File(BASE_FOLDER, "conversions.xlsx");

        try (FileOutputStream out = new FileOutputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook();
        ) {

            XSSFSheet sheet = workbook.createSheet();

            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("Chat Id");
            header.createCell(1).setCellValue("First Name");
            header.createCell(2).setCellValue("Amount");
            header.createCell(3).setCellValue("Currency Name");
            header.createCell(4).setCellValue("Date and time");

            int i=0;
            for (Conversion conversion : Database.conversionList) {
                i++;
                XSSFRow row = sheet.createRow(i);
                row.createCell(0).setCellValue(conversion.getChatId());
                row.createCell(1).setCellValue(conversion.getFirstName());
                row.createCell(2).setCellValue(conversion.getAmount());
                row.createCell(3).setCellValue(conversion.getCurrencyName());
                row.createCell(4).setCellValue(conversion.getDate());
            }

            for (int j = 0; j < 5; j++) {
                sheet.autoSizeColumn(j);
            }

            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    static File generateExchangeRatePdfFile(){
        File file = new File(BASE_FOLDER, "exchangeRates.pdf");

        try(PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument)) {

            pdfDocument.addNewPage();
            Paragraph paragraph = new Paragraph("Exchange rates to Uzbekistan Sum").setBold();
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            float[] columns = {40f, 50f, 50f, 70f, 70f, 70f, 70f};
            Table table = new Table(columns);

            String[] header = {"ID", "FLAG", "CCY", "CcyNM UZ", "CcyNM EN", "RATE", "DIFFERENCE"};
            for (String s : header) {
                table.addCell(s);
            }

            for (int i = 0; i < Database.currencyList.size(); i++) {
                ImageData imageData = ImageDataFactory.create("src/main/resources/flags/" + i + ".jpg");
                table.addCell(String.valueOf(Database.currencyList.get(i).getId()));
                table.addCell(new Image(imageData).setAutoScale(true));
                table.addCell(Database.currencyList.get(i).getCcy());
                table.addCell(Database.currencyList.get(i).getCcyNmUZ());
                table.addCell(Database.currencyList.get(i).getCcyNmEN());
                table.addCell(Database.currencyList.get(i).getRate());
                table.addCell(Database.currencyList.get(i).getDiff());
            }

            for (int i = 1; i < table.getNumberOfRows(); i++) {
                for (int j = 0; j < table.getNumberOfColumns(); j++) {
                    table.getCell(i, j).setTextAlignment(TextAlignment.CENTER);
                }
            }

            for (int i = 0; i < table.getNumberOfColumns(); i++) {
                table.getCell(0, i).setTextAlignment(TextAlignment.CENTER).setBold();
            }
            Paragraph paragraph1 = new Paragraph(Database.currencyList.get(0).getDate() + " holatiga ko'ra");
            paragraph1.setTextAlignment(TextAlignment.RIGHT);
            paragraph1.setBold();


            document.add(table);
            document.add(paragraph1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
