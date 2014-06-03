package pl.edu.pw.ii.pik01.seeknresolve.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Printer {

    public static final String COMPILED_REPORTS_DIR = "src/main/resources/compiledReports";

    private String jasperFilePath;
    private OutputType outputType;
    private OutputStream outputStream;
    private HttpServletResponse response;
    private String outputFilePath;
    private Map<String, Object> parameters;
    private Collection<?> dataSource;

    private Printer(){
    }

    public static PrinterBuilder getBuilder() {
        return new PrinterBuilder();
    }

    public void print() throws JRException, IOException {
        ObjectMapper objectMapper =  new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        String json = objectMapper.writeValueAsString(dataSource.toArray());

        JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(json.getBytes()));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, parameters, jsonDataSource);
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        switch(outputType){
            case FILE:
                Files.write(pdfBytes, new File(outputFilePath));
                break;
            case STREAM:
                outputStream.write(pdfBytes);
                break;
            case HTTP_RESPONSE:
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=" + "bugsReport.pdf");
                response.setContentLength(pdfBytes.length);

                response.getOutputStream().write(pdfBytes);
                response.flushBuffer();
                break;
        }
    }

    public enum OutputType{
        FILE,
        STREAM,
        HTTP_RESPONSE
    }

    public static class PrinterBuilder {

        private String jasperFilePath;
        private OutputType outputType = OutputType.HTTP_RESPONSE;
        private OutputStream outputStream;
        private HttpServletResponse response;
        private String outputFilePath;
        private Map<String, Object> parameters = new HashMap<>();
        private Collection<?> dataSource;

        public PrinterBuilder setJasperFile(String jasperFilePath){
            this.jasperFilePath = jasperFilePath;
            return this;
        }

        public PrinterBuilder setOutputType(OutputType outputType){
            checkNotNull(outputType);
            this.outputType = outputType;
            return this;
        }

        public PrinterBuilder setOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
            return this;
        }

        public PrinterBuilder setOutputResponse(HttpServletResponse response) {
            this.response = response;
            return this;
        }
        
        public PrinterBuilder setOutputFile(String outputFilePath) {
            this.outputFilePath = outputFilePath;
            return this;
        }

        public PrinterBuilder setParameters(Map<String, Object> parameters) {
            checkNotNull(parameters);
            this.parameters = parameters;
            return this;
        }

        public PrinterBuilder addParameter(String paramName, Object paramValue) {
            parameters.put(paramName, paramValue);
            return this;
        }

        public PrinterBuilder setDataSource(Collection<?> dataSource) {
            checkNotNull(dataSource);
            this.dataSource = dataSource;
            return this;
        }

        public Printer build(){
            Printer printer = new Printer();

            checkArgument(!Strings.isNullOrEmpty(jasperFilePath), "You have to provide report file");

            checkArgument(outputType != OutputType.FILE || outputFilePath != null, "You have to provide output file.");
            checkArgument(outputType != OutputType.STREAM || outputStream != null, "You have to provide output stream");
            checkArgument(outputType != OutputType.HTTP_RESPONSE || response != null, "You have to provide response");

            checkArgument(dataSource != null, "You have to provide data source");

            printer.jasperFilePath = this.jasperFilePath;
            printer.outputType = this.outputType;
            printer.outputStream = this.outputStream;
            printer.response = this.response;
            printer.outputFilePath = this.outputFilePath;
            printer.parameters = this.parameters;
            printer.dataSource = this.dataSource;

            return printer;
        }
    }
}
