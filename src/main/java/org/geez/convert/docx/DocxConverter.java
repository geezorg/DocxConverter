package org.geez.convert.docx;



import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.StatusBar;

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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
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
	protected StatusBar statusBar = new StatusBar();
	
	
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
        RadioMenuItem inMenuItem3 = new RadioMenuItem( "Geez_NewA/B" );
        RadioMenuItem inMenuItem4 = new RadioMenuItem( "Geez_TypeNet" );
        RadioMenuItem inMenuItem5 = new RadioMenuItem( "_" + powergeez );
        RadioMenuItem inMenuItem6 = new RadioMenuItem( "_" + samawerfa );
        RadioMenuItem inMenuItem7 = new RadioMenuItem( "_" + visualgeez );
        ToggleGroup groupInMenu = new ToggleGroup();
        
        inMenuItem1.setOnAction( evt -> setSystemIn( brana ) );
        inMenuItem1.setSelected(true);
        inMenuItem1.setToggleGroup( groupInMenu );
        inMenuItem2.setOnAction( evt -> setSystemIn( geezii ) );
        inMenuItem2.setToggleGroup( groupInMenu );
        inMenuItem3.setOnAction( evt -> setSystemIn( geeznewab ) );
        inMenuItem3.setToggleGroup( groupInMenu );
        inMenuItem4.setOnAction( evt -> setSystemIn( geeztypenet ) );
        inMenuItem4.setToggleGroup( groupInMenu );
        inMenuItem5.setOnAction( evt -> setSystemIn( powergeez ) );
        inMenuItem5.setToggleGroup( groupInMenu );
        inMenuItem6.setOnAction( evt -> setSystemIn( samawerfa ) );
        inMenuItem6.setToggleGroup( groupInMenu );
        inMenuItem7.setOnAction( evt -> setSystemIn( visualgeez ) );
        inMenuItem7.setToggleGroup( groupInMenu );
        
        inFontMenu.getItems().addAll( inMenuItem1, inMenuItem2, inMenuItem3, inMenuItem4, inMenuItem5, inMenuItem6, inMenuItem7 );


        Menu outFontMenu = new Menu( "Font _Out" );
        RadioMenuItem outMenuItem1 = new RadioMenuItem( "_" + abyssinica );
        RadioMenuItem outMenuItem2 = new RadioMenuItem( "_Kefa" );
        RadioMenuItem outMenuItem3 = new RadioMenuItem( "_" + nyala );
        ToggleGroup groupOutMenu = new ToggleGroup();
              
        outMenuItem1.setOnAction( event -> setSystemOut( abyssinica ) );
        outMenuItem1.setSelected(true);
        outMenuItem1.setToggleGroup( groupOutMenu );        
        outMenuItem2.setOnAction( event -> setSystemOut( kefa ) );
        outMenuItem2.setToggleGroup( groupOutMenu );
        outMenuItem3.setOnAction( event -> setSystemOut( nyala ) );
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
        convertButton.setOnAction( event -> convertFiles(listView) );

        final Menu fileMenu = new Menu("_File"); 
        final FileChooser fileChooser = new FileChooser();
        
        // create menuitems 
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
			        alert.setTitle( "About Docx Converter" );
			        alert.setHeaderText( "Information Alert" );
			        String s ="This is an example of JavaFX 8 Dialogs... ";
			        alert.setContentText(s);
			        alert.show();
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
        // bottomSpacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(bottomSpacer, Priority.SOMETIMES);
        HBox hbottomBox = new HBox( openFilesCheckbox, bottomSpacer, convertButton );
        hbottomBox.setPadding(new Insets(4, 0, 4, 0));
        hbottomBox.setAlignment( Pos.CENTER_LEFT );
        VBox vbottomBox = new VBox( hbottomBox, statusBar );
        // vbottomBox.setPadding(new Insets(4, 0, 2, 4));
        // vbottomBox.setSpacing(4);
        //statusBar.setText( "In: " + systemIn + " Out: " + systemOut  );
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
        // rootGroup.setPadding( new Insets(12, 12, 12, 12) );
        rootGroup.setPadding( new Insets(8, 8, 8, 8) );
 
        stage.setScene(new Scene(rootGroup, 420, 220) );
        stage.show();
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    private void convertFiles(ListView<Label> listView) {
        if ( inputList != null ) {
            int i = 0;
            ObservableList<Label> itemList = listView.getItems();
            for (File file : inputList) {
                 processFile( file );
                 Label label = itemList.get(i);
                 Platform.runLater(() -> label.setText("\u2713 " + label.getText() ));
                 label.setStyle( "-fx-font-style: italic;" );
                 listView.refresh();
                 // Platform.runLater(() -> listView.refresh() );
                 i++;
             }
         } 
    }
    
    
    ConvertDocx converter = null;
    private void processFile(File inputFile) {
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
                	
                	converter.progressProperty().addListener( 
                		(obs, oldProgress, newProgress) -> updateProgress( newProgress.doubleValue(), 1.0 )
                	);
                	converter.call();

    				done();
            		return null;
                } 
            };
            
            statusBar.progressProperty().bind( task.progressProperty() );
            
           	// remove bindings again
            task.setOnSucceeded( event -> { 
            	statusBar.progressProperty().unbind();
            	if ( openOutput ) {
            		if ( outputFile.exists() ) { try { 
            			desktop.open( outputFile ); } catch(IOException ex) {}
            		}
            		else {
            			// add a popup dialog to indicate file not found
            		}
            	}
            
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
        
    	TextFlow flowIn = new TextFlow();
        TextFlow flowOut = new TextFlow();

        Text in  = new Text("In: ");
        in.setStyle("-fx-font-weight: bold; position:absolute; top: 0;");
       
        Text out = new Text("Out: ");
        out.setStyle("-fx-font-weight: bold; position:absolute; bottom: 0;");
        
        
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
