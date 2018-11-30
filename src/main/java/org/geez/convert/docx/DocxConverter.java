package org.geez.convert.docx;


import java.awt.Desktop;

import java.io.File;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public final class DocxConverter extends Application {
 
    private Desktop desktop = Desktop.getDesktop();
	private static final String brana = "Brana I/II";
	private static final String geeznewab = "GeezNewA/B";
	private static final String geeztype = "GeezType";

	private String system = brana; // alphabetic based default
	private boolean openOutput = true;
	private List<File>  inputList = null;
	
    private static void configureFileChooser(
    		
            final FileChooser fileChooser) {      
                fileChooser.setTitle("View Pictures");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );                 
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("*.docx", "*.docx")
                );
    }
    
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Ethiopic Docx Converter");
        Image logoImage = new Image( ClassLoader.getSystemResourceAsStream("images/geez-org-avatar.png") );
        stage.getIcons().add( logoImage );


        ComboBox<String> fontMenu = new ComboBox<String>();
        fontMenu.getItems().addAll( brana, geeznewab );       
        fontMenu.setValue( "Brana I/II" );
        fontMenu.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldFont, String newFont) {
                system = newFont;
            } 
        });
        
        
        final Button convertButton = new Button("Convert File(s)");
        convertButton.setDisable( true );
        convertButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if (inputList != null) {
                            for (File file : inputList) {
                                openFile( file );
                            }
                        }
                        inputList = null;
                        convertButton.setDisable( true );
                    }
                }
        );


        final Button openFilesButton  = new Button("Select Files...");
        final FileChooser fileChooser = new FileChooser();
        openFilesButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                	configureFileChooser(fileChooser);    
                    inputList = fileChooser.showOpenMultipleDialog( stage );
                    
                    convertButton.setDisable( false );
                }
            }
        );

        
        
        CheckBox openFilesCheckbox = new CheckBox( "Open file(s)\nafter conversion?");
        openFilesCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    openOutput = new_val.booleanValue();
            }
        });
        openFilesCheckbox.setSelected(true);
 
        final GridPane inputGridPane = new GridPane();
 
        GridPane.setConstraints(fontMenu, 0, 0);
        GridPane.setConstraints(openFilesButton, 1, 0);
        GridPane.setConstraints(openFilesCheckbox, 0, 1);
        GridPane.setConstraints(convertButton, 1, 1);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(fontMenu, openFilesButton, openFilesCheckbox, convertButton);
 
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding( new Insets(12, 12, 12, 12) );
 
        stage.setScene(new Scene(rootGroup));
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    ConvertDocx converter = null;
    private void openFile(File inputFile) {
        try {
        	String inputFilePath = inputFile.getPath();
        	String outputFilePath = inputFilePath.replaceAll("\\.docx", "-Abyssinica.docx");
    		File outputFile = new File ( outputFilePath );


		if( converter == null ) {
    			switch( system ) {
		    		case brana:
    				converter = new ConvertDocxBrana();
    				break;
    			
    				case geeznewab:
    				converter = new ConvertDocxFeedelGeezNewAB();
    				break;

    				case geeztype:
    				converter = new ConvertDocxGeezType();
    				break;
    			
    				default:
    				System.err.println( "Unrecognized input system: " + system );
    				return;
    			}
		}

		converter.process( inputFile, outputFile );
    		
		if ( openOutput ) {
	  	  desktop.open( outputFile );
		}
	}
	catch (Exception ex) {
		Logger.getLogger( DocxConverter.class.getName() ).log( Level.SEVERE, null, ex );
	}
    }
}
