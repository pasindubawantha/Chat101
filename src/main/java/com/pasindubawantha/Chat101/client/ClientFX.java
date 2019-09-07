package com.pasindubawantha.Chat101.client;

import javafx.application.Application;  
import javafx.event.ActionEvent;  
import javafx.event.EventHandler;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.stage.Stage;  
import javafx.scene.layout.StackPane;   

import org.apache.commons.text.StringEscapeUtils;

import com.pasindubawantha.Chat101.client.ClientThread;
import com.pasindubawantha.Chat101.server.Server;

public class ClientFX extends Application{  
  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
        Button btn1=new Button("Say, Hello World");  
        btn1.setOnAction(new EventHandler<ActionEvent>() {  
              
            public void handle(ActionEvent arg0) {  
                // TODO Auto-generated method stub  
                System.out.println("hello world");  
            }  
        });  
        StackPane root=new StackPane();  
        root.getChildren().add(btn1);  
        Scene scene=new Scene(root,600,400);      
        primaryStage.setTitle("First JavaFX Application");  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  
    
    public static void main (String[] args)  
    {  
        launch(args);  
    }  

}
