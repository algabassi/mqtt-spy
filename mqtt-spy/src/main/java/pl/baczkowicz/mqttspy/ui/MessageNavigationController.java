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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.connectivity.MqttContent;
import pl.baczkowicz.mqttspy.events.EventManager;
import pl.baczkowicz.mqttspy.events.observers.MessageAddedObserver;
import pl.baczkowicz.mqttspy.events.observers.MessageIndexIncrementObserver;
import pl.baczkowicz.mqttspy.events.observers.MessageIndexToFirstObserver;
import pl.baczkowicz.mqttspy.events.observers.MessageRemovedObserver;
import pl.baczkowicz.mqttspy.storage.BasicMessageStore;
import pl.baczkowicz.mqttspy.storage.ManagedMessageStoreWithFiltering;
import pl.baczkowicz.mqttspy.ui.messagelog.MessageLogUtils;
import pl.baczkowicz.mqttspy.ui.utils.TextUtils;
import pl.baczkowicz.mqttspy.ui.utils.UiUtils;
import pl.baczkowicz.mqttspy.utils.FileUtils;

/**
 * Controller for the message navigation buttons.
 */
public class MessageNavigationController implements Initializable, MessageIndexToFirstObserver, MessageIndexIncrementObserver, MessageAddedObserver, MessageRemovedObserver
{
	final static Logger logger = LoggerFactory.getLogger(MessageNavigationController.class);

	@FXML
	private Label messageLabel;

	@FXML
	private Label filterStatusLabel;
	
	@FXML
	private CheckBox showLatestBox;

	@FXML
	private ToggleGroup wholeMessageFormat;

	@FXML
	private MenuButton formattingMenuButton;

	@FXML
	private Menu formatterMenu;
	
	@FXML
	private Menu customFormatterMenu;
	
	@FXML
	private CheckMenuItem uniqueOnlyMenu;

	@FXML
	private ToggleGroup selectionFormat;

	@FXML
	private Button moreRecentButton;

	@FXML
	private Button lessRecentButton;

	@FXML
	private Button showFirstButton;

	@FXML
	private Button showLastButton;

	@FXML
	private HBox messageIndexBox; 
	
	@FXML
	private MenuButton filterButton;

	private int selectedMessage;

	private BasicMessageStore store; 
	
	private TextField messageIndexValueField;
	
	private Label totalMessagesValueLabel;
	
	private EventManager eventManager;

	public void initialize(URL location, ResourceBundle resources)
	{				
		messageIndexValueField = new TextField();
		messageIndexValueField.setEditable(false);
		messageIndexValueField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> ob, String o, String n)
			{
				// expand the textfield
				messageIndexValueField.setPrefWidth(TextUtils.computeTextWidth(
						messageIndexValueField.getFont(), messageIndexValueField.getText()) + 12);
			}
		});
		
		messageLabel.getStyleClass().add("messageIndex");
		messageLabel.setPadding(new Insets(2, 2, 2, 2));
		
		totalMessagesValueLabel = new Label();
		totalMessagesValueLabel.getStyleClass().add("messageIndex");
		totalMessagesValueLabel.setPadding(new Insets(2, 2, 2, 2));
				
		filterStatusLabel = new Label();
		filterStatusLabel.getStyleClass().add("filterOn");
		filterStatusLabel.setPadding(new Insets(2, 2, 2, 2));
		
		messageIndexValueField.setPadding(new Insets(2, 5, 2, 5));
		messageIndexValueField.getStyleClass().add("messageIndex");
		messageIndexValueField.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent event)
			{
				
				switch(event.getTextDeltaYUnits()) 
				{
			        case LINES:
			        	updateMessageIndexFromScroll((int) event.getTextDeltaY());
			            break;
			        case PAGES:
			        	updateMessageIndexFromScroll((int) event.getTextDeltaY());
			            break;
			        case NONE:
			        	updateMessageIndexFromScroll((int) event.getDeltaY());			        	
			            break;
				}
			}
		});
		messageIndexValueField.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() 
		{
	        @Override
	        public void handle(KeyEvent keyEvent) 
	        {
	        	switch (keyEvent.getCode())
	        	{
		        	case SPACE:
		        	{
		        		showLatestBox.setSelected(!showLatestBox.isSelected());
		        		break;
		        	}
		        	case HOME:
		        	{
		        		showFirst();
		        		break;
		        	}
		        	case END:
		        	{
		        		showLast();
		        		break;
		        	}
		        	case PAGE_UP:
		        	{
		        		changeSelectedMessageIndex(5);
		        		break;
		        	}
		        	case PAGE_DOWN:
		        	{
		        		changeSelectedMessageIndex(-5);
		        		break;
		        	}
		        	case UP:
		        	{
		        		changeSelectedMessageIndex(1);
		        		break;
		        	}
		        	case DOWN:
		        	{
		        		changeSelectedMessageIndex(-1);
		        		break;
		        	}
		        	default:
		        		break;
	        	}
	        }
	    });		
	}

	public void init()
	{
		moreRecentButton.setTooltip(new Tooltip("Show more recent message"));
		lessRecentButton.setTooltip(new Tooltip("Show less recent message"));
		showFirstButton.setTooltip(new Tooltip("Show the latest message"));
		showLastButton.setTooltip(new Tooltip("Show the oldest message"));	
	}
	
	// ===================
	// === FXML methods ==
	// ===================
	
	@FXML
	private void showFirst()
	{
		showFirstMessage();
	}

	@FXML
	private void showLast()
	{
		showLastMessage();
	}	

	@FXML
	private void showMoreRecent()
	{
		changeSelectedMessageIndex(-1);
	}	
	
	@FXML
	private void showLessRecent()
	{
		changeSelectedMessageIndex(1);
	}

	// ====================
	// === Other methods ==
	// ====================
		
	public void onMessageAdded(final MqttContent message)
	{
		// This is registered for filtered messages only
		if (showLatest())
		{
			onNavigateToFirst();
		}
		else
		{
			onMessageIndexIncrement();
		}
	}
		
	@Override
	public void onMessageIndexChange(final int newSelectedMessage)
	{
		// logger.info("{} Index change = " + newSelectedMessage, store.getName()); 
		if (selectedMessage != newSelectedMessage)
		{
			selectedMessage = newSelectedMessage;
			updateIndex();
		}		
	}
	
	@Override
	public void onNavigateToFirst()
	{
		// logger.info("{} Index change to first", store.getName());
		showFirstMessage();				
	}
	
	@Override
	public void onMessageIndexIncrement()
	{
		// logger.info("{} Index increment", store.getName());
		
		selectedMessage++;
		
		// Because this is an event saying a new message is available, but we don't want to display it,
		// so by not refreshing the content of the old one we allow uninterrupted interaction with UI fields (e.g. selection, etc.)
		updateIndex(false);			
	}
	
	public void onMessageRemoved(final MqttContent message, final int messageIndex)
	{
		if (messageIndex < selectedMessage)
		{
			selectedMessage--;					
		}		
			
		updateIndex(false);		
	}
	
	private void showFirstMessage()
	{
		if (store.getMessages().size() > 0)
		{
			selectedMessage = 1;
			updateIndex();
		}
		else
		{
			selectedMessage = 0;
			updateIndex();
		}
	}

	private void showLastMessage()
	{
		if (store.getMessages().size() > 0)
		{
			selectedMessage = store.getMessages().size();
			updateIndex();
		}
	}
	
	private void changeSelectedMessageIndex(final int count)
	{
		if (store.getMessages().size() > 0)
		{
			if (selectedMessage + count <= 1)
			{
				showFirstMessage();
			}
			else if (selectedMessage + count >= store.getMessages().size())
			{
				showLastMessage();
			}
			else
			{
				selectedMessage = selectedMessage + count;
				updateIndex();
			}
		}		
	}

	private void updateIndex()
	{
		updateIndex(true);
	}
	
	private void updateIndex(final boolean refreshMessageDetails)
	{
		final String selectedIndexValue = selectedMessage > 0 ? String.valueOf(selectedMessage) : "-";
		final String totalMessagesValue = "/ " + store.getMessages().size(); 		
		
		if (messageIndexBox.getChildren().size() == 1)
		{
			messageLabel.setText("Message ");	
			messageIndexBox.getChildren().add(messageIndexValueField);
			messageIndexBox.getChildren().add(totalMessagesValueLabel);		
			messageIndexBox.getChildren().add(filterStatusLabel);
		}
		
		messageIndexValueField.setText(selectedIndexValue);		
		totalMessagesValueLabel.setText(totalMessagesValue);

		updateFilterStatus();
		
		if (refreshMessageDetails)
		{
			eventManager.changeMessageIndex(store, this, selectedMessage);
		}
	}
	
	private void updateFilterStatus()
	{
		if (!store.browsingFiltersEnabled())
		{			
			if (!store.messageFiltersEnabled())
			{
				filterStatusLabel.setText("");
			}
			else
			{
				filterStatusLabel.setText("(filter is active)");	
			}
		}
		else if (store instanceof ManagedMessageStoreWithFiltering)
		{	
			if (!store.messageFiltersEnabled())
			{
				filterStatusLabel.setText("(" + getBrowsingTopicsInfo((ManagedMessageStoreWithFiltering) store) + ")");
			}
			else
			{
				filterStatusLabel.setText("(" + getBrowsingTopicsInfo((ManagedMessageStoreWithFiltering) store) + "; filter is active)");
			}
		}
	}

	public static String getBrowsingTopicsInfo(final ManagedMessageStoreWithFiltering store)
	{
		final int selectedTopics = store.getFilteredMessageStore().getShownTopics().size();
		final int totalTopics = store.getAllTopics().size();
		
		return "browsing " + selectedTopics + "/" + totalTopics + " " + (totalTopics == 1? "topic" : "topics");		
	}
	
	private void updateMessageIndexFromScroll(final int scroll)
	{
		if (scroll > 0)
    	{
    		changeSelectedMessageIndex(1);
    	}
    	else
    	{
    		changeSelectedMessageIndex(-1);
    	}
	}	

	public void clear()
	{
		messageLabel.setText("No messages");
		messageIndexBox.getChildren().clear();
		messageIndexBox.getChildren().add(messageLabel);
	}
	
	public boolean showLatest()
	{
		return showLatestBox.isSelected();
	}
	
	public void hideShowLatest()
	{
		showLatestBox.setVisible(false);
	}
	
	public void copyMessageToClipboard()
	{
		if (getSelectedMessageIndex() > 0)
		{
			UiUtils.copyToClipboard(MessageLogUtils.getCurrentMessageAsMessageLog(store, getSelectedMessageIndex() - 1));
		}
	}
	
	public void copyMessagesToClipboard()	
	{
		UiUtils.copyToClipboard(MessageLogUtils.getAllMessagesAsMessageLog(store));
	}
	
	public void copyMessageToFile()
	{
		if (getSelectedMessageIndex() > 0)
		{
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select message log file to save to");
			String extensions = "messages";
			fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Message log file", extensions));
	
			final File selectedFile = fileChooser.showSaveDialog(getParentWindow());
	
			if (selectedFile != null)
			{
				FileUtils.writeToFile(selectedFile, MessageLogUtils.getCurrentMessageAsMessageLog(store, getSelectedMessageIndex() - 1));
			}
		}
	}
	
	public void copyMessagesToFile()
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select message log file to save to");
		String extensions = "messages";
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Message log file", extensions));

		final File selectedFile = fileChooser.showSaveDialog(getParentWindow());

		if (selectedFile != null)
		{
			FileUtils.writeToFile(selectedFile, MessageLogUtils.getAllMessagesAsMessageLog(store));
		}
	}
	
	private Window getParentWindow()
	{
		return messageLabel.getScene().getWindow();
	}

//	public void setFilterActive(final boolean active)
//	{
//		filterActive = active;
//		updateFilterStatus();
//	}
	
	// ===============================
	// === Setters and getters =======
	// ===============================
	
	public void setStore(final BasicMessageStore store)
	{
		this.store = store;
	}
	
	public int getSelectedMessageIndex()
	{
		return selectedMessage;
	}
	
	public void setEventManager(final EventManager eventManager)
	{
		this.eventManager = eventManager;
	}
	
	/**
	 * Get the 'unique only' menu item.
	 * 
	 * @return the uniqueOnlyMenu
	 */
	public CheckMenuItem getUniqueOnlyMenu()
	{
		return uniqueOnlyMenu;
	}

	public MenuButton getFilterButton()
	{
		return filterButton;		
	}
}
