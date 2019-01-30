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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
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
	private static final String geezii = "Geez, GeezII";
	private static final String geeznewab = "GeezNewA/B";
	private static final String geeztypenet = "GeezTypeNet";
	private static final String powergeez = "Power Ge'ez";
	private static final String samawerfa = "Samawerfa";
	private static final String visualgeez = "Visual Ge'ez";
	private static final String abyssinica = "Abyssinica SIL";
	private static final String nyala = "Nyala";
	private static final String kefa = "Kefa";

	private String systemIn  = brana; // alphabetic based default
	private String systemOut = abyssinica;
	private boolean openOutput = true;
	private List<File> inputList = null;
	
	
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

        Menu inFontMenu = new Menu( "Font _In" );
        RadioMenuItem inMenuItem1 = new RadioMenuItem( "_" + brana );
        RadioMenuItem inMenuItem2 = new RadioMenuItem( "_" + geezii );
        RadioMenuItem inMenuItem3 = new RadioMenuItem( "Geez_NewA/B" );
        RadioMenuItem inMenuItem4 = new RadioMenuItem( "Geez_TypeNet" );
        RadioMenuItem inMenuItem5 = new RadioMenuItem( "_" + powergeez );
        RadioMenuItem inMenuItem6 = new RadioMenuItem( "_" + samawerfa );
        RadioMenuItem inMenuItem7 = new RadioMenuItem( "_" + visualgeez );
        ToggleGroup groupInMenu = new ToggleGroup();
        
        inMenuItem1.setOnAction(
        	new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    systemIn = brana;
                }
            }
        );
        inMenuItem1.setSelected(true);
        inMenuItem1.setToggleGroup( groupInMenu );
        
        inMenuItem2.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = geezii;
                    }
                }
         );
        inMenuItem2.setToggleGroup( groupInMenu );
        inMenuItem3.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = geeznewab;
                    }
                }
            );
        inMenuItem3.setToggleGroup( groupInMenu );
        inMenuItem4.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = geeztypenet;
                    }
                }
            );
        inMenuItem4.setToggleGroup( groupInMenu );
        inMenuItem5.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = powergeez;
                    }
                }
            );
        inMenuItem5.setToggleGroup( groupInMenu );
        inMenuItem6.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = samawerfa;
                    }
                }
            );
        inMenuItem6.setToggleGroup( groupInMenu );
        inMenuItem7.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemIn = visualgeez;
                    }
                }
            );
        inMenuItem7.setToggleGroup( groupInMenu );
        
        inFontMenu.getItems().addAll( inMenuItem1, inMenuItem2, inMenuItem3, inMenuItem4, inMenuItem5, inMenuItem6, inMenuItem7 );


        Menu outFontMenu = new Menu( "Font _Out" );
        RadioMenuItem outMenuItem1 = new RadioMenuItem( "_" + abyssinica );
        RadioMenuItem outMenuItem2 = new RadioMenuItem( "_Kefa" );
        RadioMenuItem outMenuItem3 = new RadioMenuItem( "_" + nyala );
        ToggleGroup groupOutMenu = new ToggleGroup();
        
        
        outMenuItem1.setOnAction(
        	new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    systemOut = abyssinica;
                }
            }
        );
        outMenuItem1.setSelected(true);
        outMenuItem1.setToggleGroup( groupOutMenu );
        
        outMenuItem2.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemOut = kefa;
                    }
                }
            );
        outMenuItem2.setToggleGroup( groupOutMenu );
        outMenuItem3.setOnAction(
            	new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        systemOut = nyala;
                    }
                }
            );
        outMenuItem3.setToggleGroup( groupOutMenu );
        
        outFontMenu.getItems().addAll( outMenuItem1, outMenuItem2, outMenuItem3 );
        

        ListView<Label> listView = new ListView<Label>();
        listView.setEditable(false);
        listView.setPrefHeight( 125 );
        listView.setPrefWidth( 310 );
        ObservableList<Label> data = FXCollections.observableArrayList();
        VBox listVBox = new VBox( listView );
        listView.autosize();
        
        
        final Button convertButton = new Button("Convert File(s)");
        convertButton.setDisable( true );
        convertButton.setOnAction(
        	new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    if ( inputList != null ) {
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


        final Menu fileMenu = new Menu("_File"); 
        final FileChooser fileChooser = new FileChooser();
        
        // create menuitems 
        final MenuItem fileMenuItem1 = new MenuItem("Select Files..."); 
        fileMenuItem1.setOnAction(
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
        fileMenu.getItems().add( fileMenuItem1 ); 
        
        // create a menubar 
        MenuBar menuBar = new MenuBar(); 
  
        // add menu to menubar 
        menuBar.getMenus().addAll( fileMenu, inFontMenu, outFontMenu );

        
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
        GridPane.setConstraints(listVBox, 0, 1, 3, 1);
        GridPane.setConstraints(openFilesCheckbox, 0, 2, 2, 1);  GridPane.setConstraints(convertButton, 2, 4);
        GridPane.setHalignment(openFilesCheckbox, HPos.LEFT);    GridPane.setHalignment(convertButton, HPos.RIGHT);
        GridPane.setValignment(openFilesCheckbox, VPos.TOP);
        
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll( listVBox, openFilesCheckbox, convertButton );
 
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().add( menuBar );
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding( new Insets(12, 12, 12, 12) );
 
        stage.setScene(new Scene(rootGroup, 420, 260) );
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

    		
    		switch( systemIn ) {
		   		case brana:
		   			converter = new ConvertDocxBrana();
		   			break;
	    			
			   	case geezii:
		    		converter = new ConvertDocxFeedelGeezII();
		    		break;
		    			
		   		case geeznewab:
	    			converter = new ConvertDocxFeedelGeezNewAB();
	    			break;

		    	case geeztypenet:
		    		converter = new ConvertDocxGeezTypeNet();
		   			break;

		    	case powergeez:
		    		converter = new ConvertDocxPowerGeez();
		   			break;

		    	case samawerfa:
		    		converter = new ConvertDocxSamawerfa();
		   			break;
		   			
		    	case visualgeez:
		    		converter = new ConvertDocxVisualGeez();
		    		break;
    			
		    	default:
		    		System.err.println( "Unrecognized input system: " + systemIn );
		    		return;
    		}
		
    		converter.setFont( systemOut );
    		converter.process( inputFile, outputFile );
    		if ( openOutput ) {
    			if ( outputFile.exists() ) {
    				desktop.open( outputFile );
    			}
    			else {
    				// add a popup dialog to indicate file not found
    			}
    		}
        }
        catch (Exception ex) {
        	Logger.getLogger( DocxConverter.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
