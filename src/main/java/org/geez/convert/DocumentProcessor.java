package org.geez.convert;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

public abstract class DocumentProcessor implements Callable<Void> {
	
    protected File inputFile = null, outputFile = null;
	protected List<File> inputFileList = null;
	protected boolean setProgress = true;

	
    protected final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    
    public void setFiles( final File inputFile, final File outputFile ) {
    	this.inputFile  = inputFile;
    	this.outputFile = outputFile;
    } 
	
	public void setFileList( List<File> inputFileList ) {
		this.inputFileList = inputFileList;
	}
    
    public ReadOnlyDoubleProperty progressProperty() {
        return progress.getReadOnlyProperty() ;
    }   
    
    public final double getProgress() {
        return progressProperty().get();
    }
    
	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    public DocumentProcessor() {
    }  

    public DocumentProcessor( List<File> inputFileList ) {
    	setFileList( inputFileList );
    }

    public DocumentProcessor( final File inputFile, final File outputFile ) {
    	setFiles( inputFile, outputFile );
    }
    
}
