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
        key=dateField.getText();
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
        System.out.println("TEST");
       Path inputPath=Paths.get("./base.txt");
       List<String>lines=Files.readAllLines(inputPath,Charset.forName("UTF-8"));
       Collections.sort(lines);
       for(int i=0;i<lines.size();i++){
           System.out.println(lines.get(i));
       }
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
                date+=tmp.charAt(3)+tmp.charAt(4);
                date+="-";
                date+=tmp.charAt(0)+tmp.charAt(1);
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
        for(int i=0;i<=1;i++){
            Label labelFor=new Label();
            labelFor=createLabelWithLesson(lines.get(i));
            labelFor.setLayoutX(50);
            labelFor.setLayoutY(100);
        }
        scene=new Scene(group,1000,500);
        scene.setFill(Color.valueOf("#2d3447"));
        stage.setScene(scene);
     }
     public Label createLabelWithLesson(String line){
        Label label=new Label();
        String day="",mounth="",year="";
        day+=line.charAt(8)+line.charAt(9);
        mounth+=line.charAt(5)+line.charAt(6);
        year+=line.charAt(0)+line.charAt(1)+line.charAt(2)+line.charAt(3);
        label.setText(day+"/"+mounth+"/"+year);
        return label;
     }

}
