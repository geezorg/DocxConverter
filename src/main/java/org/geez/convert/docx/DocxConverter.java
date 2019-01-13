package org.geez.convert.docx;


import java.awt.Desktop;

import java.io.File;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public final class DocxConverter extends Application {
 
    private Desktop desktop = Desktop.getDesktop();
	private static final String brana = "Brana I/II";
	private static final String geeznewab = "GeezNewA/B";
	private static final String geeztype = "GeezType";
	private static final String powergeez = "Power Ge'ez";
	private static final String abyssinica = "Abyssinica SIL";
	private static final String nyala = "Nyala";
	private static final String kefa = "Kefa";

	private String systemIn  = brana; // alphabetic based default
	private String systemOut = abyssinica;
	private boolean openOutput = true;
	private List<File>  inputList = null;
	
	
    private static void configureFileChooser( final FileChooser fileChooser ) {      
    	fileChooser.setTitle("View Word Files");
        fileChooser.setInitialDirectory(
        		new File( System.getProperty("user.home") )
        );                 
        fileChooser.getExtensionFilters().add(
        		new FileChooser.ExtensionFilter("*.docx", "*.docx")
        );
    }
    
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Ethiopic Docx Font Converter");
        Image logoImage = new Image( ClassLoader.getSystemResourceAsStream("images/geez-org-avatar.png") );
        stage.getIcons().add( logoImage );
        final Label label = new Label( "Ethiopic Legacy Font Converter" );
        String osName = System.getProperty("os.name");
        if( osName.equals("Mac OS X") ) {
            com.apple.eawt.Application.getApplication().setDockIconImage( SwingFXUtils.fromFXImage(logoImage, null) );      
        }

        ComboBox<String> fontMenu = new ComboBox<String>();
        fontMenu.getItems().addAll( brana, geeznewab, geeztype, powergeez );       
        fontMenu.setValue( brana );
        fontMenu.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldFont, String newFont) {
                systemIn = newFont;
            } 
        });
        

        ComboBox<String> uniFontMenu = new ComboBox<String>();
        uniFontMenu.getItems().addAll( abyssinica, kefa, nyala );       
        uniFontMenu.setValue( abyssinica );
        uniFontMenu.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldFont, String newFont) {
                systemOut = newFont;
            } 
        });
        

        ListView<Label> listView = new ListView<Label>();
        listView.setEditable(false);
        listView.setPrefHeight( 100 );
        listView.setPrefWidth( 280 );
        ObservableList<Label> data = FXCollections.observableArrayList();
        VBox listVBox = new VBox( listView );
        listView.autosize();
        
        
        final Button convertButton = new Button("Convert File(s)");
        convertButton.setDisable( true );
        convertButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if (inputList != null) {
                        	convertButton.setDisable( true );
                        	int i = 0;
                            ObservableList<Label> itemList = listView.getItems();
                            for (File file : inputList) {
                            	processFile( file );
                                Label label = itemList.get(i);
                                label.setText("\u2713 " + label.getText() );
                                label.setStyle( "-fx-font-style: italic;" );
                                // itemList.set(i, oldValue );
                                Platform.runLater(() -> listView.refresh() );
                        		// listView.fireEvent(new ListView.EditEvent<>(listView, ListView.editCommitEvent(), label, i));
                                i++;
                            }
                        } 
                        inputList = null;
                    }
                }
        );


        final Button openFilesButton  = new Button("Select Files...");
        final FileChooser fileChooser = new FileChooser();
        
        openFilesButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                	listView.getItems().clear();
                	configureFileChooser(fileChooser);    
                    inputList = fileChooser.showOpenMultipleDialog( stage );
                    
                    if ( inputList != null ) {
                    	for( File file: inputList) {
                    		Label rowLabel = new Label( file.getName() );
                    		data.add( rowLabel );
                    		Tooltip tooltip = new Tooltip( file.getPath() );
                    		rowLabel.setTooltip( tooltip );
                    	} 
                    	listView.setItems( data );
                    	convertButton.setDisable( false );
                    }
                }
            }
        );

        
        
        CheckBox openFilesCheckbox = new CheckBox( "Open file(s) after conversion?");
        openFilesCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    openOutput = new_val.booleanValue();
            }
        });
        openFilesCheckbox.setSelected(true);
 
        final GridPane inputGridPane = new GridPane();
 
        GridPane.setConstraints(label, 0, 0, 3, 1);
        Text fontIn = new Text( "Font In" );
        fontIn.setStyle( "-fx-font-style: italic;" );
        Text fontOut = new Text( "Font Out" );
        fontOut.setStyle( "-fx-font-style: italic;" );
        GridPane.setConstraints( fontIn, 0, 1);                  GridPane.setConstraints( fontOut, 1, 1);
        GridPane.setConstraints(fontMenu, 0, 2);                 GridPane.setConstraints(uniFontMenu, 1, 2);               GridPane.setConstraints(openFilesButton, 2, 2);
        GridPane.setHalignment(fontMenu, HPos.LEFT);             GridPane.setHalignment(uniFontMenu, HPos.CENTER);         GridPane.setHalignment(openFilesButton, HPos.RIGHT);
        
        GridPane.setConstraints(listVBox, 0, 3, 3, 1);
        GridPane.setConstraints(openFilesCheckbox, 0, 4, 2, 1);  GridPane.setConstraints(convertButton, 2, 4);
        GridPane.setHalignment(openFilesCheckbox, HPos.LEFT);    GridPane.setHalignment(convertButton, HPos.RIGHT);
        GridPane.setValignment(openFilesCheckbox, VPos.TOP);
        
        // ColumnConstraints col1 = new ColumnConstraints();
        // col1.setPercentWidth(40);
        // inputGridPane.getColumnConstraints().addAll(col1);
        
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(label, fontIn, fontOut, fontMenu, uniFontMenu, openFilesButton, listVBox, openFilesCheckbox, convertButton);
 
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding( new Insets(12, 12, 12, 12) );
 
        stage.setScene(new Scene(rootGroup, 400, 270) );
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    ConvertDocx converter = null;
    private void processFile(File inputFile) {
        try {
        	String inputFilePath = inputFile.getPath();
        	String outputFilePath = inputFilePath.replaceAll("\\.docx", "-" + systemOut.replace( " ", "-" ) + ".docx");
    		File outputFile = new File ( outputFilePath );


    		if( converter == null ) {
    			switch( systemIn ) {
		    		case brana:
		    			converter = new ConvertDocxBrana();
		    			break;
    			
		    		case geeznewab:
		    			converter = new ConvertDocxFeedelGeezNewAB();
		    			break;

		    		case geeztype:
		    			converter = new ConvertDocxGeezType();
		    			break;

		    		case powergeez:
		    			converter = new ConvertDocxPowerGeez();
		    			break;
    			
		    		default:
		    			System.err.println( "Unrecognized input system: " + systemIn );
		    			return;
    			}
    		}
		
    		converter.setFont( systemOut );
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
