/***********************************************************************************
 * 
 * Copyright (c) 2014 Kamil Baczkowicz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 *    Kamil Baczkowicz - initial API and implementation and/or initial documentation
 *    
 */
package pl.baczkowicz.mqttspy.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the converter window.
 */
public class ConverterController implements Initializable
{
	final static Logger logger = LoggerFactory.getLogger(ConverterController.class);

	@FXML
	private TextArea textToEncode;
	
	@FXML
	private TextArea textToDecode;
	
	@FXML
	private TextArea encodedText;
	
	@FXML
	private TextArea decodedText;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{		
		textToEncode.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				encodedText.setText(Base64.encodeBase64String(newValue.getBytes()));			
			}
		});		
		
		textToDecode.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				decodedText.setText(new String(Base64.decodeBase64(newValue)));			
			}
		});
	}	
}
