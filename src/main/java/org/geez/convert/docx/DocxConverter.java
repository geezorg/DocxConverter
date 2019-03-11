package org.geez.convert.docx;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.controlsfx.control.StatusBar;
 

public final class DocxConverter extends Application {
 
	private static final String VERSION = "v0.6.0";
    private Desktop desktop = Desktop.getDesktop();
    
    // Input Fonts
	private static final String brana = "Brana I/II";
	private static final String geezii = "Geez, GeezII";
	private static final String geezigna = "Geezigna";
	private static final String geezbasic = "GeezBasic";
	private static final String geeznewab = "GeezNewA/B";
	private static final String geeztypenet = "GeezTypeNet";
	private static final String powergeez = "Power Ge'ez";
	private static final String samawerfa = "Samawerfa";
	private static final String visualgeez = "Visual Ge'ez";
	private static final String visualgeez2000 = "VG2000 Main";
	// Output Fonts
	private static final String abyssinica = "Abyssinica SIL";
	private static final String nyala = "Nyala";
	private static final String kefa = "Kefa";
	private static final String brana_uni = "Brana";
	private static final String powergeez_uni = "Power Geez Unicode1";

	private String systemIn  = brana; // alphabetic based default
	private String systemOut = abyssinica;
	private boolean openOutput = true;
	private List<File> inputList = null;
	protected StatusBar statusBar = new StatusBar();
	private boolean converted = false;
	
	
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
        String osName = System.getProperty("os.name");
        if( osName.equals("Mac OS X") ) {
            com.apple.eawt.Application.getApplication().setDockIconImage( SwingFXUtils.fromFXImage(logoImage, null) );      
        }

        Menu inFontMenu = new Menu( "Font _In" );
        RadioMenuItem inMenuItem1 = new RadioMenuItem( "_" + brana );
        RadioMenuItem inMenuItem2 = new RadioMenuItem( "_" + geezii );
        RadioMenuItem inMenuItem3 = new RadioMenuItem( "Geezigna" );
        RadioMenuItem inMenuItem4 = new RadioMenuItem( "GeezBasic" );
        RadioMenuItem inMenuItem5 = new RadioMenuItem( "Geez_NewA/B" );
        RadioMenuItem inMenuItem6 = new RadioMenuItem( "Geez_TypeNet" );
        RadioMenuItem inMenuItem7 = new RadioMenuItem( "_" + powergeez );
        RadioMenuItem inMenuItem8 = new RadioMenuItem( "_" + samawerfa );
        RadioMenuItem inMenuItem9 = new RadioMenuItem( "_" + visualgeez );
        RadioMenuItem inMenuItem10 = new RadioMenuItem( "VG _2000" );
        ToggleGroup groupInMenu = new ToggleGroup();
        
        inMenuItem1.setOnAction( evt -> setSystemIn( brana ) );
        inMenuItem1.setSelected(true);
        inMenuItem1.setToggleGroup( groupInMenu );
        inMenuItem2.setOnAction( evt -> setSystemIn( geezii ) );
        inMenuItem2.setToggleGroup( groupInMenu );
        inMenuItem3.setOnAction( evt -> setSystemIn( geezigna ) );
        inMenuItem3.setToggleGroup( groupInMenu );
        inMenuItem4.setOnAction( evt -> setSystemIn( geezbasic ) );
        inMenuItem4.setToggleGroup( groupInMenu );
        inMenuItem5.setOnAction( evt -> setSystemIn( geeznewab ) );
        inMenuItem5.setToggleGroup( groupInMenu );
        inMenuItem6.setOnAction( evt -> setSystemIn( geeztypenet ) );
        inMenuItem6.setToggleGroup( groupInMenu );
        inMenuItem7.setOnAction( evt -> setSystemIn( powergeez ) );
        inMenuItem7.setToggleGroup( groupInMenu );
        inMenuItem8.setOnAction( evt -> setSystemIn( samawerfa ) );
        inMenuItem8.setToggleGroup( groupInMenu );
        inMenuItem9.setOnAction( evt -> setSystemIn( visualgeez ) );
        inMenuItem9.setToggleGroup( groupInMenu );
        inMenuItem10.setOnAction( evt -> setSystemIn( visualgeez2000 ) );
        inMenuItem10.setToggleGroup( groupInMenu );
        
        inFontMenu.getItems().addAll( inMenuItem1, inMenuItem2, inMenuItem3, inMenuItem4, inMenuItem5, inMenuItem6, inMenuItem7, inMenuItem8, inMenuItem9, inMenuItem10 );


        Menu outFontMenu = new Menu( "Font _Out" );
        RadioMenuItem outMenuItem1 = new RadioMenuItem( "_" + abyssinica );
        RadioMenuItem outMenuItem2 = new RadioMenuItem( "_" + brana_uni );
        RadioMenuItem outMenuItem3 = new RadioMenuItem( "_Kefa" );
        RadioMenuItem outMenuItem4 = new RadioMenuItem( "_" + nyala );
        RadioMenuItem outMenuItem5 = new RadioMenuItem( "_" + powergeez_uni );
        ToggleGroup groupOutMenu = new ToggleGroup();
              
        outMenuItem1.setOnAction( event -> setSystemOut( abyssinica ) );
        outMenuItem1.setSelected(true);
        outMenuItem1.setToggleGroup( groupOutMenu );        
        outMenuItem2.setOnAction( event -> setSystemOut( brana_uni ) );
        outMenuItem2.setToggleGroup( groupOutMenu );
        outMenuItem3.setOnAction( event -> setSystemOut( kefa ) );
        outMenuItem3.setToggleGroup( groupOutMenu );
        outMenuItem4.setOnAction( event -> setSystemOut( nyala ) );
        outMenuItem4.setToggleGroup( groupOutMenu );
        outMenuItem5.setOnAction( event -> setSystemOut( powergeez_uni ) );
        outMenuItem5.setToggleGroup( groupOutMenu );

       
        outFontMenu.getItems().addAll( outMenuItem1, outMenuItem2, outMenuItem3, outMenuItem4, outMenuItem5 );
        

        ListView<Label> listView = new ListView<Label>();
        listView.setEditable(false);
        listView.setPrefHeight( 125 ); // 205 for screenshots
        listView.setPrefWidth( 310 );
        ObservableList<Label> data = FXCollections.observableArrayList();
        VBox listVBox = new VBox( listView );
        listView.autosize();
        
        
        final Button convertButton = new Button("Convert");
        convertButton.setDisable( true );
        convertButton.setOnAction( event -> {
        	convertButton.setDisable( true );
        	convertFiles( convertButton, listView ); 
        });

        final Menu fileMenu = new Menu("_File"); 
        final FileChooser fileChooser = new FileChooser();
        
        // create menu items 
        final MenuItem fileMenuItem1 = new MenuItem( "Select Files..." ); 
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
        fileMenu.getItems().add( new SeparatorMenuItem() );
        
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        fileMenu.getItems().add( exitMenuItem ); 
        
        
        final Menu helpMenu = new Menu( "Help" );
        final MenuItem aboutMenuItem = new MenuItem( "About" );
        helpMenu.getItems().add( aboutMenuItem );
        
        aboutMenuItem.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
			        Alert alert = new Alert(AlertType.INFORMATION);
			        alert.setTitle( "About Legacy Ethiopic Docx Converter" );
			        alert.setHeaderText( "Legacy Ethiopic Font Converter for Docx " + VERSION );
			        
			        FlowPane fp = new FlowPane();
			        Label label = new Label( "Visit the project homepage on" );
			        Hyperlink link = new Hyperlink("GitHub");
			        fp.getChildren().addAll( label, link);

			        link.setOnAction( (event) -> {
	                    alert.close();
	                    try {
		                    URI uri = new URI( "https://github.com/geezorg/DocxConverter/" );
		                    desktop.browse( uri );
	                    }
	                    catch(Exception ex) {
	                    	
	                    }
			        });

			        alert.getDialogPane().contentProperty().set( fp );
			        alert.showAndWait();
                }
            }
        );
        
        
        // create a menubar 
        MenuBar leftBar = new MenuBar(); 
  
        // add menu to menubar 
        leftBar.getMenus().addAll( fileMenu, inFontMenu, outFontMenu );

        
        CheckBox openFilesCheckbox = new CheckBox( "Open file(s) after conversion?");
        openFilesCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    openOutput = new_val.booleanValue();
            }
        });
        openFilesCheckbox.setSelected(true);
        
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.SOMETIMES);
        HBox hbottomBox = new HBox( openFilesCheckbox, bottomSpacer, convertButton );
        hbottomBox.setPadding(new Insets(4, 0, 4, 0));
        hbottomBox.setAlignment( Pos.CENTER_LEFT );
        VBox vbottomBox = new VBox( hbottomBox, statusBar );

        statusBar.setText( "" );
        updateStatusMessage();

        
        MenuBar rightBar = new MenuBar();
        rightBar.getMenus().addAll( helpMenu );
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        
 
        final BorderPane rootGroup =new BorderPane();
        rootGroup.setTop( menubars );
        rootGroup.setCenter( listVBox );
        rootGroup.setBottom( vbottomBox );
        rootGroup.setPadding( new Insets(8, 8, 8, 8) );
 
        stage.setScene(new Scene(rootGroup, 420, 220) ); // 305 for screenshots
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    private void convertFiles(Button convertButton, ListView<Label> listView) {
    	
        if ( inputList != null ) {
        	if( converted ) {
        		// this is a re-run, reset file names;
        		for(Label label: listView.getItems()) {
        			label.setStyle( "" );
        			label.setText( label.getText().replaceFirst( "\u2713 ", "" ) );
        		}
        		listView.refresh();
        		converted = false;
        	}
            int i = 0;
            for (File file : inputList) {
                processFile( file, convertButton, listView, i );
                i++;
                
                // this sleep seems to help slower CPUs
                // when a list of files is processed, and
                // avoids an exception from wordMLPackage.save( outputFile );
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
             }
            converted = true;
         } 
    }
    
    
    ConvertDocx converter = null;
    private void processFile(File inputFile, Button convertButton, ListView<Label> listView, int listIndex) {
        try {
        	String inputFilePath = inputFile.getPath();
        	String outputFilePath = inputFilePath.replaceAll("\\.docx", "-" + systemOut.replace( " ", "-" ) + ".docx");
    		File outputFile = new File ( outputFilePath );

    		
    		switch( systemIn ) {
		   		case brana:
		   			converter = new ConvertDocxBrana( inputFile, outputFile );
		   			break;
	    			
			   	case geezii:
		    		converter = new ConvertDocxFeedelGeezII( inputFile, outputFile );
		    		break;
		    		
	    			
			   	case geezigna:
		    		converter = new ConvertDocxFeedelGeezigna( inputFile, outputFile );
		    		break;
		
			   	case geezbasic:
		    		converter = new ConvertDocxGeezBasic( inputFile, outputFile );
		    		break;    			
		    			
		   		case geeznewab:
	    			converter = new ConvertDocxFeedelGeezNewAB( inputFile, outputFile );
	    			break;

		    	case geeztypenet:
		    		converter = new ConvertDocxGeezTypeNet( inputFile, outputFile );
		   			break;

		    	case powergeez:
		    		converter = new ConvertDocxPowerGeez( inputFile, outputFile );
		   			break;

		    	case samawerfa:
		    		converter = new ConvertDocxSamawerfa( inputFile, outputFile );
		   			break;
		   			
		    	case visualgeez:
		    		converter = new ConvertDocxVisualGeez( inputFile, outputFile );
		    		break;
		   			
		    	case visualgeez2000:
		    		converter = new ConvertDocxVisualGeez2000( inputFile, outputFile );
		    		break;
    			
		    	default:
		    		System.err.println( "Unrecognized input system: " + systemIn );
		    		return;
    		}
		
    		converter.setFont( systemOut );

    		
    		// references:
    		// https://stackoverflow.com/questions/49222017/javafx-make-threads-wait-and-threadsave-gui-update
    		// https://stackoverflow.com/questions/47419949/propagate-progress-information-from-callable-to-task
            Task<Void> task = new Task<Void>() {
                @Override protected Void call() throws Exception {
                	
                	updateProgress(0.0,1.0);
                	converter.progressProperty().addListener( 
                		(obs, oldProgress, newProgress) -> updateProgress( newProgress.doubleValue(), 1.0 )
                	);
                	updateMessage("[" +  (listIndex+1) + "/" + listView.getItems().size() + "]" );
                	converter.call();
                	updateProgress(1.0, 1.0);

    				done();
            		return null;
                } 
            };
            
            statusBar.progressProperty().bind( task.progressProperty() );
            statusBar.textProperty().bind( task.messageProperty() );
            
           	// remove bindings again
            task.setOnSucceeded( event -> { 
            	statusBar.progressProperty().unbind();
            	if ( openOutput ) {
            		if ( outputFile.exists() ) {
            			try { 
            				desktop.open( outputFile ); 
            			}
            			catch(IOException ex) {
                			Alert errorAlert = new Alert(AlertType.ERROR);
                			errorAlert.setHeaderText( "File IO Exception" );
                			errorAlert.setContentText( "An error has occured opening the file:\n" + ex );
                			errorAlert.showAndWait();
            			}
            		}
            		else {
            			Alert errorAlert = new Alert(AlertType.ERROR);
            			errorAlert.setHeaderText( "Output file not found" );
            			errorAlert.setContentText( "The output file \"" + outputFile.getPath() + "\" could not be found.  Conversation has likely failed." );
            			errorAlert.showAndWait();
            		}
            	}
            	Label label = listView.getItems().get( listIndex );
                label.setText( "\u2713 " + label.getText() );
                label.setStyle( "-fx-font-style: italic;" );
                listView.refresh();
                convertButton.setDisable( false );
            
            });
            Thread convertThread = new Thread(task);
            convertThread.start();
            
            //convertThread.join();

        }
        catch (Exception ex) {
        	Logger.getLogger( DocxConverter.class.getName() ).log( Level.SEVERE, null, ex );
        }
        
    }
    

    Text systemInText = new Text( systemIn );
    Text systemOutText = new Text( systemOut );
    // status bar reference:
    // https://jar-download.com/artifacts/org.controlsfx/controlsfx-samples/8.40.14/source-code/org/controlsfx/samples/HelloStatusBar.java
    private void updateStatusMessage() {
    	systemInText.setStyle( "-fx-font-weight: bold;" );
    	systemOutText.setStyle( "-fx-font-weight: bold;" );
    	systemInText.setFill( Color.RED );
    	systemOutText.setFill( Color.GREEN );
        
    	TextFlow flowIn = new TextFlow();
        TextFlow flowOut = new TextFlow();

        Text in  = new Text("In: ");
        in.setStyle("-fx-font-weight: bold;");
       
        Text out = new Text("Out: ");
        out.setStyle("-fx-font-weight: bold;");
        
        
        flowIn.getChildren().addAll(in, systemInText );
        flowOut.getChildren().addAll(out, systemOutText );
       
        
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        separator1.setPadding( new Insets(0,0,0,6) );
        
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.setPadding( new Insets(0,0,0,6) );
        
        
        HBox hbox = new HBox();
        hbox.getChildren().addAll( flowIn, separator1, flowOut, separator2 );
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding( new Insets(2,0,0,0) );
        hbox.setSpacing(0.0);
        
        // working with a single flow leads to bad visual effects when the app size changes when the
        // font name changes, so we use an hbox instead
        // flow.getChildren().addAll( in, systemInText, separator1, out, systemOutText, separator2 );
        
        statusBar.getLeftItems().add( hbox );
    }
    private void setSystemIn(String systemIn) {
    	this.systemIn = systemIn;
    	systemInText.setText( systemIn );
    }
    private void setSystemOut(String systemOut) {
    	this.systemOut = systemOut;
    	systemOutText.setText( systemOut );
    }

}
