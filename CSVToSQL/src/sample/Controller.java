package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Controller {
    @FXML
    private Button button1;

    @FXML
    private TextField textField1;

    @FXML
    private TextArea textArea;

    private static final String NUMERIC_TYPE = "NUMERIC";

    private static final String STRING_TYPE = "STRING";

    private static final String NEW_LINE = "%n";

    private FileChooser fileChooser = new FileChooser();

    public void initialize(URL location, ResourceBundle resources) {

        // TODO (don't really need to do anything here).
        // TODO (Thực sự cũng không cần thiết viết gì ở đây).

    }

    public void onActionHandler(ActionEvent event) throws IOException {
        System.out.println("Button Clicked!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if(textField1.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill out Table Name");
            alert.show();
        } else {
            String tableName = textField1.getText();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                String path = file.getAbsolutePath();
                textField1.setText(path);
                Reader reader = new FileReader(path);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord : csvRecords) {
                    for (int i = 67; i <= 74; i++) {
                        System.out.println("INSERT INTO " + tableName +" (created_time, last_updated_time, creator_id, last_updated_id, is_actived, is_deleted, agency_id, catalog_type, catalog_name, catalog_code, note) VALUES('2021-03-17 16:53:30.294', '2021-03-17 16:53:30.294', NULL, NULL, true, false," + i + ", 'AQUA', '" + csvRecord.get(5) + "', '" + csvRecord.get(3) + "', NULL);");
//                cnt++;
                    }
                }
            }
        }
    }

    public void onDefaultActionHandler(ActionEvent event) throws IOException{
        textArea.setEditable(false);
        if(textField1.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill out Table Name");
            alert.show();
        } else {
            String tableName = textField1.getText();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null){
                System.out.println("on default clicked");
                String path = file.getAbsolutePath();
                Reader reader = new FileReader(path);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                List<String> headerNames = csvParser.getHeaderNames();
                long recordNumber = csvParser.getRecordNumber();
                int columnNumber = headerNames.size();
                int currentLine = 1;
                int currentColumn =1;
                //mapping datatype
                Map<Integer, String> dataTypeMapping = new HashMap<>();
                for(int i=0;i<columnNumber;i++){
                    boolean isNumeric = true;
                    for(CSVRecord csvRecord : csvRecords){
                        if (Pattern.matches("[0-9]+", csvRecord.get(i) ) == false ) {
                            isNumeric= false;
                            break;
                        }
//                        if(csvRecord.is)
                    }
                    if(isNumeric){
                        dataTypeMapping.put(i+1,NUMERIC_TYPE);
                    } else {
                        dataTypeMapping.put(i+1,STRING_TYPE);
                    }

                }

//                StringBuffer createStatement = new StringBuffer("CREATE TABLE "+tableName+" (");
                    textArea.appendText("CREATE TABLE "+tableName+" (");
                // mapping data type

                //pre handle header name
                List<String> handledHeaderNames = new ArrayList<>();
                for(String headerName : headerNames){
                    handledHeaderNames.add(preHandleString(headerName));
                }
                //pre handle header name

                //create statement
                for(String handleHeaderName : handledHeaderNames){
//                    String preHandledString = preHandleString(headerName);
//                    createStatement.append(handleHeaderName);
                        textArea.appendText(handleHeaderName);
                    String dataType = dataTypeMapping.get(currentColumn);
                    if(currentColumn==columnNumber) {
//                        createStatement.append(" " + dataType + " ");
                        textArea.appendText(" " + dataType + " ");
                    } else {
//                        createStatement.append(" " + dataType + ", ");
                        textArea.appendText(" " + dataType + ", ");
                    }
                    currentColumn++;
                }
//                createStatement.append(");");
                textArea.appendText(");"+System.lineSeparator());
//                System.out.println(createStatement);

                //end create statement

                //insert
                StringBuffer insertQuery = new StringBuffer();

                for(CSVRecord csvRecord : csvRecords){
//                    if(currentLine == 1){
//                        StringBuffer create = new StringBuffer("CREATE TABLE ");
//                        for(int i=0;i<columnNumber;i++){
//                            create.append(csvRecord.get(i));
//                        }
//                        System.out.println(create);
//                    }
//                    StringBuffer insertQuery = new StringBuffer("INSERT INTO "+tableName+" (");
//                    insertQuery.append("INSERT INTO "+tableName+" (");
                    textArea.appendText("INSERT INTO "+tableName+" (");
                    String prefix =" ";
                    for(String handleHeaderName : handledHeaderNames){
//                        insertQuery.append(prefix);
                        textArea.appendText(prefix);
                        prefix = ",";
                        insertQuery.append(handleHeaderName);
                        textArea.appendText(handleHeaderName);
                    }
//                    insertQuery.append(") VALUES(");
                    textArea.appendText(") VALUES(");
                    String prefixNd ="";
                    for(int i=0;i<columnNumber;i++){
                        String dataTypeColumn = dataTypeMapping.get(i+1);
//                        insertQuery.append(prefixNd);
                        textArea.appendText(prefixNd);
                        prefixNd=",";
                        if(dataTypeColumn.equals(STRING_TYPE)){
//                            insertQuery.append("'"+csvRecord.get(i)+"'");
                            textArea.appendText("'"+csvRecord.get(i)+"'");
                        } else {
//                            insertQuery.append(csvRecord.get(i));
                            textArea.appendText(csvRecord.get(i));
                        }
                    }
//                    insertQuery.append(");"+"\\r\\n");
                    textArea.appendText(");"+System.lineSeparator());
//                    currentLine++;
//                    System.out.println("INSERT INTO"+tableName+"(csv");
                    System.out.println(insertQuery);
                }
                // end insert
//                textArea.setDisable(false);
//                textArea.setText(insertQuery.toString());

            }

        }


    }
    public String preHandleString(String string){
        String response = string.trim();
        response=response.toLowerCase();
        response=response.replaceAll("\\s","_");
        return response;
    }

//    tao file


}
