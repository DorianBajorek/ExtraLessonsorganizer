package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.LabelSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML TextField dateField;
    @FXML TextField hourField;
    @FXML TextField keyField;
    @FXML TextField infoField;
    String date;
    String hour;
    String key;
    String info;
    public void switchToScene1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/lessons.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        createCalendar();
    }
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/addLessons.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void addLesson(ActionEvent event) throws IOException{
        date=dateField.getText();
        hour=hourField.getText();
        key=keyField.getText();
        info=infoField.getText();
        //TODO: Repair if and add better check functions
        if(true || checkStandard(date,hour,key)){
            //format prawidłowy
            saveToFile(date,hour,key,info);
            sortBase();
            switchToScene1(event);
        }else{
            System.out.println("Nieprawidłowe dane");
        }
     }
     public void sortBase() throws IOException {
       Path inputPath=Paths.get("./base.txt");
       List<String>lines=Files.readAllLines(inputPath,Charset.forName("UTF-8"));
       Collections.sort(lines);
     }
     public boolean checkToSwap(int day1,int mounth1, int year1,int day2, int mounth2, int year2){
        if(year1>=year2){
            if(mounth1>=mounth2){
                if(day1>=day1){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
     }
     public boolean checkStandard(String date,String hour,String key){
        if(checkDate(date) && checkHour(hour) && checkKey(key)){
            return true;
        }else{
            return false;
        }
     }
     public boolean checkDate(String date){
        if(date.charAt(2)=='-' && date.charAt(5)=='-'){
            return true;
        }else{
            return false;
        }
     }
     public boolean checkHour(String hour){
        if(hour.charAt(2)==':'){
            return true;
        }else{
            return false;
         }
     }
     public boolean checkKey(String key){
        return true;
     }
     public void saveToFile(String date, String hour, String key, String info){
        File file=new File("base.txt");
        if(!file.exists()){
            try{
                file.createNewFile();
                //plik został utworzony
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        if(file.canWrite()){
            try{
                FileWriter fileWriter=new FileWriter(file,true);
                //change date to RRRR-MM-DD
                String tmp=date;
                date="";
                date=date+tmp.charAt(6)+tmp.charAt(7)+tmp.charAt(8)+tmp.charAt(9);
                date+="-";
                date=date+tmp.charAt(3)+tmp.charAt(4);
                date+="-";
                date=date+tmp.charAt(0)+tmp.charAt(1);
                String lessonString=date+"/"+hour+"/"+key+"/"+info+"\n";
                fileWriter.append(lessonString);
                fileWriter.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
     }
}
    public void createCalendar() throws IOException {
        Path inputPath=Paths.get("./base.txt");
        List<String>lines=Files.readAllLines(inputPath,Charset.forName("UTF-8"));
        int sizeOfFile=3;
        Rectangle[] rectangle=new Rectangle[sizeOfFile];
        for(int i=0;i<3;i++){
            Rectangle r=new Rectangle();
            r.setWidth(300);
            r.setHeight(400);
            r.setX(20+i*300+20*i);
            r.setY(50);
            r.setFill(Color.valueOf("#3E5081"));
            rectangle[i]=r;
        }
        AnchorPane group=new AnchorPane(rectangle);
        Label mainLabel= new Label();
        mainLabel.setText("Kalendarz");
        mainLabel.setLayoutX(430);
        mainLabel.setLayoutY(10);
        mainLabel.setFont(new Font("Arial", 30));
        mainLabel.setTextFill((Color.valueOf("#e68e8e")));
        group.getChildren().add(mainLabel);
        group.setStyle("-fx-background-color:#2d3447");
        for(int i=0;(i<lines.size())&&(i<3);i++){//TODO: <- version BETA
            //date:
            Label labelOnLesson=new Label();
            labelOnLesson=createLabelWithLessonDate(lines.get(i));
            labelOnLesson.setLayoutX(50+320*i);
            labelOnLesson.setLayoutY(60);
            group.getChildren().add(labelOnLesson);
            //hour
            Label labelOnLessonWithhour=new Label();
            labelOnLessonWithhour=createLabelWithLessonHour(lines.get(i));
            labelOnLessonWithhour.setLayoutX(50+320*i);
            labelOnLessonWithhour.setLayoutY(100);
            group.getChildren().add(labelOnLessonWithhour);
            //key:
            Label labelOnLessonWithKey=new Label();
            labelOnLessonWithKey=createLabelWithLessonKey(lines.get(i));
            labelOnLessonWithKey.setLayoutX(50+320*i);
            labelOnLessonWithKey.setLayoutY(140);
            group.getChildren().add(labelOnLessonWithKey);
            //PhoneNumber:
            Label labelOnLessonsWithNumber=new Label();
            labelOnLessonsWithNumber=createLabelWithLessonNumber(lines.get(i));
            labelOnLessonsWithNumber.setLayoutX(50+320*i);
            labelOnLessonsWithNumber.setLayoutY(180);
            group.getChildren().add(labelOnLessonsWithNumber);
        }
        scene=new Scene(group,1000,500);
        scene.setFill(Color.valueOf("#2d3447"));
        stage.setScene(scene);
     }
     public Label createLabelWithLessonDate(String line){
        Label label=new Label();
        String date="";
        date=date+line.charAt(8)+line.charAt(9);
        date+="-";
        date=date+line.charAt(5)+line.charAt(6);
        date+="-";
        date=date+line.charAt(0)+line.charAt(1)+line.charAt(2)+line.charAt(3);
        label.setText("Data: "+date);
        label.setTextFill(Color.valueOf("#e68e8e"));
        label.setStyle("-fx-font: 20 arial;");
        return label;
     }
     public Label createLabelWithLessonHour(String line){
        Label label=new Label();
        String hour="";
        hour=hour+line.charAt(11)+line.charAt(12);
        hour+=":";
        hour=hour+line.charAt(14)+line.charAt(15);
        label.setText("Hour: "+hour);
        label.setTextFill(Color.valueOf("#e68e8e"));
        label.setStyle("-fx-font: 20 arial;");
        return label;
     }
     public Label createLabelWithLessonNumber(String lineFromBase) throws IOException {
         String line="";
        //findInKeys:
        String key="";
        int numbersOfPoins=0;
        int indexFirstLetterOfKey=0;
        for(int i=0;i<lineFromBase.length();i++){
            if(lineFromBase.charAt(i)=='/'){
                numbersOfPoins++;
            }
            if(numbersOfPoins==2){
                indexFirstLetterOfKey=i;
                break;
            }
        }
        indexFirstLetterOfKey++;
        for(int i=indexFirstLetterOfKey;i<lineFromBase.length();i++){
            if(lineFromBase.charAt(i)=='/'){
                break;
            }else{
                key+=lineFromBase.charAt(i);
            }
        }
        System.out.println("KEY: "+key);
        //findKeysinBase
         Path inputPath=Paths.get("./keys.txt");
         List<String>lines=Files.readAllLines(inputPath,Charset.forName("UTF-8"));
         System.out.println(lines.get(0));
         for(int i=0;i<lines.size();i++){
             String actualKey="";
             actualKey=cutActualKey(lines.get(i));
             System.out.println("Actual key: "+actualKey);
             if(key.equals(actualKey)){
                 line=lines.get(i);
                 break;
             }
         }
         System.out.println("LINE: "+line);
        Label label=new Label();
        String number="";
        int firstPlace=0;
        for(int i=0;i<line.length();i++){
            if (line.charAt(i)==':') {
                firstPlace=i;
                break;
            }
        }
        firstPlace++;
        for(int i=firstPlace;i<line.length();i++){
            if(line.charAt(i)=='/'){
                break;
            }else{
                number+=line.charAt(i);
            }
        }
         label.setText("Phone Number: "+number);
         label.setTextFill(Color.valueOf("#e68e8e"));
         label.setStyle("-fx-font: 20 arial;");
         return label;
     }
     public String cutActualKey(String line){
        String cut="";
        for(int i=0;i<line.length();i++){
            if(line.charAt(i)==':'){
                break;
            }else{
                cut+=line.charAt(i);
            }
        }
        return cut;
     }
     public Label createLabelWithLessonKey(String lines){
        Label label=new Label();
         String key="";
         int numbersOfPoins=0;
         int indexFirstLetterOfKey=0;
         for(int i=0;i<lines.length();i++){
             if(lines.charAt(i)=='/'){
                 numbersOfPoins++;
             }
             if(numbersOfPoins==2){
                 indexFirstLetterOfKey=i;
                 break;
             }
         }
         indexFirstLetterOfKey++;
         for(int i=indexFirstLetterOfKey;i<lines.length();i++){
             if(lines.charAt(i)=='/'){
                 break;
             }else{
                 key+=lines.charAt(i);
             }
         }
         label.setText("Key: "+key);
         label.setTextFill(Color.valueOf("#e68e8e"));
         label.setStyle("-fx-font: 20 arial;");
         return label;
     }
}
