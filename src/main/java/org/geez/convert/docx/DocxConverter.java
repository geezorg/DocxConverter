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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
        
		//Tooltip tooltip6 = new Tooltip( "Addis98, Blknwt98" );
		//Tooltip.install( inMenuItem6, tooltip6 );
		
		
        inMenuItem7.setOnAction( evt -> setSystemIn( visualgeez ) );
        inMenuItem7.setToggleGroup( groupInMenu );
        
        inFontMenu.getItems().addAll( inMenuItem1, inMenuItem2, inMenuItem3, inMenuItem4, inMenuItem5, inMenuItem6, inMenuItem7 );


        Menu outFontMenu = new Menu( "Font _Out" );
        RadioMenuItem outMenuItem1 = new RadioMenuItem( "_" + abyssinica );
        RadioMenuItem outMenuItem2 = new RadioMenuItem( "_Kefa" );
        RadioMenuItem outMenuItem3 = new RadioMenuItem( "_" + nyala );
        ToggleGroup groupOutMenu = new ToggleGroup();
        
        
        outMenuItem1.setOnAction( evt -> setSystemOut( abyssinica ) );

        outMenuItem1.setSelected(true);
        outMenuItem1.setToggleGroup( groupOutMenu );
        
        outMenuItem2.setOnAction( evt -> setSystemOut( kefa ) );

        outMenuItem2.setToggleGroup( groupOutMenu );
        outMenuItem3.setOnAction( evt -> setSystemOut( nyala ) );

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
        convertButton.setOnAction(evt -> convertFiles(listView));
        /*
        	new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    if ( inputList != null ) {
                       // convertButton.setDisable( true );
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
        */


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
                });
        
        
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
 
        /*
        final GridPane inputGridPane = new GridPane();
 
       // GridPane.setConstraints(label, 0, 0, 3, 1); 
        GridPane.setConstraints(listVBox, 0, 1, 3, 1);
        GridPane.setConstraints(openFilesCheckbox, 0, 2, 2, 1);  GridPane.setConstraints(convertButton, 2, 4);
        GridPane.setHalignment(openFilesCheckbox, HPos.LEFT);    GridPane.setHalignment(convertButton, HPos.RIGHT);
        GridPane.setValignment(openFilesCheckbox, VPos.TOP);
        
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll( listVBox, openFilesCheckbox, convertButton );
        */
        
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

    		
    		// try again with: https://stackoverflow.com/questions/49222017/javafx-make-threads-wait-and-threadsave-gui-update
    		
    		// https://stackoverflow.com/questions/47419949/propagate-progress-information-from-callable-to-task
            Task<Void> task = new Task<Void>() {
                @Override protected Void call() throws Exception {
                	
                	updateProgress(0, 0);
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
    private void updateStatusMessage() {
        TextFlow flowIn = new TextFlow();
        TextFlow flowOut = new TextFlow();

        Text in  = new Text("In: ");
        in.setStyle("-fx-font-weight: bold");


        Text out = new Text(" Out: ");
        out.setStyle("-fx-font-weight: bold");

        flowIn.getChildren().addAll(in, systemInText );
        flowOut.getChildren().addAll(out, systemOutText );
        //statusBar.setStyle( "" );
        
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        HBox hbox = new HBox();
        //hbox.getChildren().addAll( flowIn, separator1, flowOut, separator2 );
        hbox.getChildren().addAll( flowIn, flowOut );
        hbox.setAlignment(Pos.CENTER_LEFT);
        
        // statusBar.getLeftItems().add( hbox ); //.addAll( flowIn, separator1, flowOut, separator2 );
        statusBar.getLeftItems().add( new Text("OK") );
       // statusBar.setStyleText( );
        statusBar.setText( "OK" );
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
