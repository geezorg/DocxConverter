package org.geez.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
// import java.util.logging.Level;
// import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.controlsfx.control.StatusBar;
import org.geez.convert.docx.DocxProcessor;
import org.geez.convert.docx.DocxProcessorAutodetect;
import org.geez.convert.fontsystem.ConvertFontSystem;
import org.geez.convert.fontsystem.ConvertFontSystemBrana;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezII;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezNewAB;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezigna;
import org.geez.convert.fontsystem.ConvertFontSystemGeezFont;
import org.geez.convert.fontsystem.ConvertFontSystemGeezTypeNet;
import org.geez.convert.fontsystem.ConvertFontSystemNCIC;
import org.geez.convert.fontsystem.ConvertFontSystemPowerGeez;
import org.geez.convert.fontsystem.ConvertFontSystemSamawerfa;
import org.geez.convert.fontsystem.ConvertFontSystemVisualGeez;
import org.geez.convert.fontsystem.ConvertFontSystemVisualGeez2000;

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
 

public final class DocxConverter extends Application {
 
	private static final String VERSION = "v0.8.0";
	private final int APP_HEIGHT = 220, APP_WIDTH = 420; //  420, 420) ); // 305 for screenshots 220 normal
    private Desktop desktop = Desktop.getDesktop();
    
    // Input Fonts
    private static final String autodetect = "Autodetect";
	private static final String brana = "Brana I/II";
	private static final String geezii = "Geez, GeezII";
	private static final String geezigna = "Geezigna";
	private static final String geezfont = "GeezFont";
	private static final String geeznewab = "GeezNewA/B";
	private static final String geeztypenet = "GeezTypeNet";
	private static final String ncic = "Agafari (AGF)";
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
	private static final String bembino = "Bembino";
	private static final String gsmahtem = "GS GeezMahtemUnicode";
	
	private String systemIn  = autodetect;
	private String systemOut = abyssinica;
	private boolean openOutput = true;
	private List<File> inputFileList = null;
	protected StatusBar statusBar = new StatusBar();
	private boolean converted = false;
	private static Logger LOGGER = LoggerFactory.getLogger( DocxConverter.class );
			
    private DocxProcessor processor = new DocxProcessor();
	
    
    private static void configureFileChooser( final FileChooser fileChooser ) {      
    	fileChooser.setTitle( "View Word Files" );
        fileChooser.setInitialDirectory(
        		new File( System.getProperty( "user.home" ) )
        );                 
        fileChooser.getExtensionFilters().add(
        		new FileChooser.ExtensionFilter( "*.docx", "*.docx" )
        );
    }
    
    
    @Override
    public void start(final Stage stage) {
        stage.setTitle( "Ethiopic Docx Font Converter" );
        ClassLoader geezLibClassLoader = org.geez.convert.Converter.class.getClassLoader();
        Image logoImage = new Image( geezLibClassLoader.getResourceAsStream("images/geez-org-avatar.png") );
        stage.getIcons().add( logoImage );
        String osName = System.getProperty( "os.name" );
        if( osName.equals( "Mac OS X" ) ) {
            com.apple.eawt.Application.getApplication().setDockIconImage( SwingFXUtils.fromFXImage( logoImage, null ) );      
        }

        Menu inFontMenu = new Menu( "Font _In" );
        RadioMenuItem inMenuItem0  = new RadioMenuItem( autodetect );
        RadioMenuItem inMenuItem1  = new RadioMenuItem( "_" + ncic );
        RadioMenuItem inMenuItem2  = new RadioMenuItem( "_" + brana );
        RadioMenuItem inMenuItem3  = new RadioMenuItem( "_" + geezii );
        RadioMenuItem inMenuItem4  = new RadioMenuItem( "Geezigna" );
        RadioMenuItem inMenuItem5  = new RadioMenuItem( "Geez_Font" );
        RadioMenuItem inMenuItem6  = new RadioMenuItem( "Geez_NewA/B" );
        RadioMenuItem inMenuItem7  = new RadioMenuItem( "Geez_TypeNet" );
        RadioMenuItem inMenuItem8  = new RadioMenuItem( "_" + powergeez );
        RadioMenuItem inMenuItem9  = new RadioMenuItem( "_" + samawerfa );
        RadioMenuItem inMenuItem10 = new RadioMenuItem( "_" + visualgeez );
        RadioMenuItem inMenuItem11 = new RadioMenuItem( "VG _2000" );
        ToggleGroup groupInMenu = new ToggleGroup();
        
        inMenuItem0.setSelected(true);
        inMenuItem0.setOnAction( evt -> setSystemIn( autodetect ) );
        inMenuItem0.setToggleGroup( groupInMenu );
        inMenuItem1.setOnAction( evt -> setSystemIn( ncic ) );
        inMenuItem1.setToggleGroup( groupInMenu );
        inMenuItem2.setOnAction( evt -> setSystemIn( brana ) );
        inMenuItem2.setToggleGroup( groupInMenu );
        inMenuItem3.setOnAction( evt -> setSystemIn( geezii ) );
        inMenuItem3.setToggleGroup( groupInMenu );
        inMenuItem4.setOnAction( evt -> setSystemIn( geezigna ) );
        inMenuItem4.setToggleGroup( groupInMenu );
        inMenuItem5.setOnAction( evt -> setSystemIn( geezfont ) );
        inMenuItem5.setToggleGroup( groupInMenu );
        inMenuItem6.setOnAction( evt -> setSystemIn( geeznewab ) );
        inMenuItem6.setToggleGroup( groupInMenu );
        inMenuItem7.setOnAction( evt -> setSystemIn( geeztypenet ) );
        inMenuItem7.setToggleGroup( groupInMenu );
        inMenuItem8.setOnAction( evt -> setSystemIn( powergeez ) );
        inMenuItem8.setToggleGroup( groupInMenu );
        inMenuItem9.setOnAction( evt -> setSystemIn( samawerfa ) );
        inMenuItem9.setToggleGroup( groupInMenu );
        inMenuItem10.setOnAction( evt -> setSystemIn( visualgeez ) );
        inMenuItem10.setToggleGroup( groupInMenu );
        inMenuItem11.setOnAction( evt -> setSystemIn( visualgeez2000 ) );
        inMenuItem11.setToggleGroup( groupInMenu );
        
        inFontMenu.getItems().addAll( inMenuItem0, new SeparatorMenuItem(), inMenuItem1, inMenuItem2, inMenuItem3, inMenuItem4, inMenuItem5, inMenuItem6, inMenuItem7, inMenuItem8, inMenuItem9, inMenuItem10,  inMenuItem11 );


        Menu outFontMenu = new Menu( "Font _Out" );
        RadioMenuItem outMenuItem1 = new RadioMenuItem( "_" + abyssinica );
        RadioMenuItem outMenuItem2 = new RadioMenuItem( "Bembin_o" );
        RadioMenuItem outMenuItem3 = new RadioMenuItem( "_" + brana_uni );
        RadioMenuItem outMenuItem4 = new RadioMenuItem( "_" + gsmahtem );
        RadioMenuItem outMenuItem5 = new RadioMenuItem( "_Kefa" );
        RadioMenuItem outMenuItem6 = new RadioMenuItem( "_" + nyala );
        RadioMenuItem outMenuItem7 = new RadioMenuItem( "_" + powergeez_uni );
        ToggleGroup groupOutMenu = new ToggleGroup();
              
        outMenuItem1.setOnAction( event -> setSystemOut( abyssinica ) );
        outMenuItem1.setSelected(true);
        outMenuItem1.setToggleGroup( groupOutMenu );   
        outMenuItem2.setOnAction( event -> setSystemOut( bembino ) );
        outMenuItem2.setToggleGroup( groupOutMenu );
        outMenuItem3.setOnAction( event -> setSystemOut( brana_uni ) );
        outMenuItem3.setToggleGroup( groupOutMenu );
        outMenuItem4.setOnAction( event -> setSystemOut( gsmahtem ) );
        outMenuItem4.setToggleGroup( groupOutMenu );
        outMenuItem5.setOnAction( event -> setSystemOut( kefa ) );
        outMenuItem5.setToggleGroup( groupOutMenu );
        outMenuItem6.setOnAction( event -> setSystemOut( nyala ) );
        outMenuItem6.setToggleGroup( groupOutMenu );
        outMenuItem7.setOnAction( event -> setSystemOut( powergeez_uni ) );
        outMenuItem7.setToggleGroup( groupOutMenu );

       
        outFontMenu.getItems().addAll( outMenuItem1, outMenuItem2, outMenuItem3, outMenuItem4, outMenuItem5, outMenuItem6, outMenuItem7 );
        

        ListView<Label> listView = new ListView<Label>();
        listView.setEditable( false );
        listView.setPrefHeight(  APP_HEIGHT - 95 ); // 420, 220 => 330, 125
        listView.setPrefWidth( APP_WIDTH - 90 );
        ObservableList<Label> data = FXCollections.observableArrayList();
        VBox listVBox = new VBox( listView );
        listView.autosize();
        
        
        final Button convertButton = new Button("Convert");
        convertButton.setDisable( true );
        convertButton.setOnAction( event -> {
        	convertButton.setDisable( true );
        	convertFiles( convertButton, listView ); 
        });
        
        CheckBox openFilesCheckbox = new CheckBox( "Open file(s) after conversion?" );

        final Menu fileMenu = new Menu("_File"); 
        final FileChooser fileChooser = new FileChooser();
        
        // create menu items 
        final MenuItem fileMenuItem1 = new MenuItem( "Select Files..." ); 
        fileMenuItem1.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent evt) {
                	listView.getItems().clear();
                	configureFileChooser(fileChooser);    
                	List<File> selectedFiles = fileChooser.showOpenMultipleDialog( stage );
                    
                    if ( selectedFiles != null ) {
                    	inputFileList = new ArrayList<File>( selectedFiles );
	                    if ( inputFileList.size() == 1 ) {
	                    	openFilesCheckbox.setText( "Open file after conversion?" );
	                    } else {
	                    	openFilesCheckbox.setText( "Open files after conversion?" );                    	
	                    }
	                    
	                    Collections.sort( inputFileList, new Comparator<File>() {
	                        @Override
	                        public int compare(File o1, File o2) {
	                            String n1 = o1.getName();
	                            String n2 = o2.getName();
	                            return n1.compareTo(n2);
	                        }
	
	                    });
                    
                    	for( File file: inputFileList ) {
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
        
        MenuItem exitMenuItem = new MenuItem( "Exit" );
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        fileMenu.getItems().add( exitMenuItem ); 
        
        
        final Menu helpMenu = new Menu( "Help" );
        final MenuItem aboutMenuItem = new MenuItem( "About" );
        helpMenu.getItems().add( aboutMenuItem );
        
        aboutMenuItem.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle( final ActionEvent evt ) {
			        Alert alert = new Alert( AlertType.INFORMATION );
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
	    					Alert errorAlert = new Alert( AlertType.ERROR );
	    					errorAlert.setHeaderText( "An error opening a web browser." );
	    					errorAlert.setContentText( ex.getMessage() );
	    					errorAlert.showAndWait();
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

        
        openFilesCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    openOutput = new_val.booleanValue();
            }
        });
        openFilesCheckbox.setSelected(true);
        
        Region bottomSpacer = new Region();
        HBox.setHgrow( bottomSpacer, Priority.SOMETIMES );
        HBox hbottomBox = new HBox( openFilesCheckbox, bottomSpacer, convertButton );
        hbottomBox.setPadding(new Insets(4, 0, 4, 0));
        hbottomBox.setAlignment( Pos.CENTER_LEFT );
        VBox vbottomBox = new VBox( hbottomBox, statusBar );

        statusBar.setText( "" );
        updateStatusMessage();

        
        MenuBar rightBar = new MenuBar();
        rightBar.getMenus().addAll( helpMenu );
        Region spacer = new Region();
        spacer.getStyleClass().add( "menu-bar" );
        HBox.setHgrow( spacer, Priority.SOMETIMES );
        HBox menubars = new HBox( leftBar, spacer, rightBar );
        
 
        final BorderPane rootGroup = new BorderPane();
        rootGroup.setTop( menubars );
        rootGroup.setCenter( listVBox );
        rootGroup.setBottom( vbottomBox );
        rootGroup.setPadding( new Insets(8, 8, 8, 8) );
 
        Scene scene = new Scene(rootGroup, APP_WIDTH, APP_HEIGHT );
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                listView.setPrefHeight( Integer.parseInt(newSceneHeight.toString().split("\\.")[0] ) - 95);
            }
        });
        stage.setScene( scene ); // 305 for screenshots 220 normal
        stage.show();
    }
    
 
    private void convertFiles(Button convertButton, ListView<Label> listView) {
        if ( inputFileList != null ) {
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
            // if "Autodetect" is the system out, create the converter instance here and
            // pass it the inputList to iterate over to setup its font list and map
            if( systemIn.equals( "Autodetect") ) {
            	processor = new DocxProcessorAutodetect( inputFileList );
            	((DocxProcessorAutodetect)processor).readFonts();
            }
            else {
            	setFileConverter();
            }
            
    		processor.setFontOut( systemOut );
    		
            for( File file : inputFileList ) {
                processFile( file, convertButton, listView, i );
                i++;
                
                // this sleep seems to help slower CPUs
                // when a list of files is processed, and
                // avoids an exception from wordMLPackage.save( outputFile );
                try {
					Thread.sleep(1000);
				}
                catch (InterruptedException ex) {
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText( "An error occurred processing a file." );
					errorAlert.setContentText( ex.getMessage() );
					errorAlert.showAndWait();
				}
             }
           
            converted = true;
         } 
    }
    
    
    private void setFileConverter() {
        try {
    		// when working on a list, see if the previous instance can be used, where the inputFile and outputFile are just reset.
    		if( (processor.getTargetTypefaces().isEmpty()) || (! processor.getTargetTypefaces().contains( systemIn ) ) ) {
        		ConvertFontSystem converter = null;
	    		switch( systemIn ) {
			   		case brana:
			   			converter = new ConvertFontSystemBrana();
			   			break;
		    			
				   	case geezii:
			    		converter = new ConvertFontSystemFeedelGeezII();
			    		break;	
		    			
				   	case geezigna:
			    		converter = new ConvertFontSystemFeedelGeezigna();
			    		break;
			
				   	case geezfont:
			    		converter = new ConvertFontSystemGeezFont();
			    		break;    			
			    			
			   		case geeznewab:
		    			converter = new ConvertFontSystemFeedelGeezNewAB();
		    			break;
	
			    	case geeztypenet:
			    		converter = new ConvertFontSystemGeezTypeNet();
			   			break;
	
			    	case ncic:
			    		converter = new ConvertFontSystemNCIC();
			   			break;
			   			
			    	case powergeez:
			    		converter = new ConvertFontSystemPowerGeez();
			   			break;
	
			    	case samawerfa:
			    		converter = new ConvertFontSystemSamawerfa();
			   			break;
			   			
			    	case visualgeez:
			    		converter = new ConvertFontSystemVisualGeez();
			    		break;
			   			
			    	case visualgeez2000:
			    		converter = new ConvertFontSystemVisualGeez2000();
			    		break;
	    			
			    	default:
			    		System.err.println( "Unrecognized input system: " + systemIn );
			    		return;
	    		}
	    		
	    		processor.addConverter( converter );
    		}
		}
        catch (Exception ex) {
        	LOGGER.error ( ex.getStackTrace().toString() );
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText( "An error occurred instantiating a converter." );
			errorAlert.setContentText( ex.getMessage() );
			errorAlert.showAndWait();
        }
            
    }
    

    private void processFile(File inputFile, Button convertButton, ListView<Label> listView, int listIndex) {
    	
    	String inputFilePath = inputFile.getPath();
    	String outputFilePath = inputFilePath.replaceAll("\\.docx", "-" + systemOut.replace( " ", "-" ) + ".docx");
		File outputFile = new File ( outputFilePath );
		
		processor.setFiles( inputFile, outputFile );
        try {
    		// references:
    		// https://stackoverflow.com/questions/49222017/javafx-make-threads-wait-and-threadsave-gui-update
    		// https://stackoverflow.com/questions/47419949/propagate-progress-information-from-callable-to-task
            Task<Void> task = new Task<Void>() {
                @Override protected Void call() throws Exception {
                	
                	updateProgress(0.0,1.0);
                	processor.progressProperty().addListener( 
                		(obs, oldProgress, newProgress) -> updateProgress( newProgress.doubleValue(), 1.0 )
                	);
                	updateMessage("[" +  (listIndex+1) + "/" + listView.getItems().size() + "]" );
                	processor.call();
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
                //File outputFile = processor.getOutputFile( listIndex );
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
        	LOGGER.error ( ex.getStackTrace().toString() );
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText( "An error occurred processing a file." );
			errorAlert.setContentText( ex.getMessage() );
			errorAlert.showAndWait();
        }
        
    }
    

    Text systemInText = new Text( systemIn );
    Text systemOutText = new Text( systemOut );
    private void setSystemIn(String systemIn) {
    	this.systemIn = systemIn;
    	systemInText.setText( systemIn );
    }
    private void setSystemOut(String systemOut) {
    	this.systemOut = systemOut;
    	systemOutText.setText( systemOut );
    }
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
    
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
