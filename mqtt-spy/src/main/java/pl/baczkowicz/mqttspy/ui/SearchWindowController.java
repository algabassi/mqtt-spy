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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.connectivity.MqttAsyncConnection;
import pl.baczkowicz.mqttspy.connectivity.MqttContent;
import pl.baczkowicz.mqttspy.connectivity.MqttSubscription;
import pl.baczkowicz.mqttspy.events.EventManager;
import pl.baczkowicz.mqttspy.events.observers.MessageAddedObserver;
import pl.baczkowicz.mqttspy.events.observers.MessageListChangedObserver;
import pl.baczkowicz.mqttspy.events.observers.MessageRemovedObserver;
import pl.baczkowicz.mqttspy.storage.ManagedMessageStoreWithFiltering;
import pl.baczkowicz.mqttspy.ui.utils.FxmlUtils;
import pl.baczkowicz.mqttspy.ui.utils.StylingUtils;

/**
 * Controller for the search window.
 */
public class SearchWindowController extends AnchorPane implements Initializable, MessageAddedObserver, MessageRemovedObserver, MessageListChangedObserver
{
	/** Initial and minimal scene/stage width. */	
	public final static int WIDTH = 780;
	
	/** Initial and minimal scene/stage height. */
	public final static int HEIGHT = 550;
	
	final static Logger logger = LoggerFactory.getLogger(SearchWindowController.class);
	
	@FXML
	private Button createNewSearchButton;

	@FXML
	private TabPane searchTabs;
	
	private int searchNumber = 1;
	
	private Map<Tab, SearchPaneController> searchPaneControllers = new HashMap<Tab, SearchPaneController>();

	private ManagedMessageStoreWithFiltering store;

	private MqttSubscription subscription;

	private String subscriptionName;

	private Stage stage;

	private EventManager eventManager;

	private MqttAsyncConnection connection;
	
	public void initialize(URL location, ResourceBundle resources)
	{
		searchTabs.getTabs().clear();				
	}
	
	public void createNewSearch()
	{
		final Tab tab = createSearchTab(this);
		searchTabs.getTabs().add(tab);
		
		Platform.runLater(new Runnable()
		{			
			@Override
			public void run()
			{
				if (searchPaneControllers.get(tab) != null)
				{
					searchPaneControllers.get(tab).requestSearchFocus();
				}
			}
		});		
	}
	
	public Tab createSearchTab(final Object parent)
	{
		// Load a new tab and message pane
		final FXMLLoader loader = FxmlUtils.createFXMLLoader(parent, FxmlUtils.FXML_LOCATION + "SearchPane.fxml");

		final AnchorPane searchPane = FxmlUtils.loadAnchorPane(loader);
		final SearchPaneController searchPaneController = ((SearchPaneController) loader.getController());
		
		final Tab tab = new Tab();
		tab.setText("New search " + searchNumber);
		searchNumber++;

		tab.setClosable(true);
		tab.setContent(searchPane);
		tab.setOnClosed(new EventHandler<Event>()
		{
			@Override
			public void handle(Event event)
			{
				searchPaneController.cleanup();				
				
				//TODO: should this be removed?
				//subscriptionPaneEventDispatcher.deleteObserver(searchPaneController);
			}
		});
		
		searchPaneController.setTab(tab);
		searchPaneController.setEventManager(eventManager);
		searchPaneController.setStore(store);
		searchPaneController.setConnection(connection);
		
		//TODO: should this be added?
		//subscriptionPaneEventDispatcher.addObserver(searchPaneController);
		
		searchPaneController.init();		
		
		searchPaneControllers.put(tab, searchPaneController);		

		return tab;
	}

	public void handleClose()
	{
		 for (final SearchPaneController controller : searchPaneControllers.values())
		 {
			 controller.disableAutoSearch();
		 }		
	}

	public void init()
	{
		stage = (Stage) searchTabs.getScene().getWindow();
		updateTitle();
		createNewSearchButton.setText("Create new search for \"" + subscriptionName + "\"");
		
		if (subscription != null)
		{
			createNewSearchButton.setStyle(StylingUtils.createBaseRGBString(subscription.getColor()));
		}
	}
	
	private void updateTitle()
	{
		final String messagesText = store.getMessages().size() == 1 ?  "message" : "messages";
		
		if (!store.browsingFiltersEnabled())
		{			
			stage.setTitle(subscriptionName + " - " + store.getMessages().size() + " " + messagesText + " available for searching");
		}
		else
		{
			stage.setTitle(subscriptionName + " - " + store.getMessages().size() + " " + messagesText 
					+ " available for searching (" + MessageNavigationController.getBrowsingTopicsInfo(store) + ")");		
		}		
	}
	
	@Override
	public void onMessageAdded(final MqttContent message)
	{
		updateTitle();		
	}
	
	@Override
	public void onMessageRemoved(final MqttContent message, final int messageIndex)
	{
		updateTitle();
	}
	
	@Override
	public void onMessageListChanged()
	{
		updateTitle();
	}
	
	// ===============================
	// === Setters and getters =======
	// ===============================

	public void setStore(ManagedMessageStoreWithFiltering store)
	{
		this.store = store;	
	}
	
	public void setSubscription(MqttSubscription subscription)
	{
		this.subscription = subscription;		
	}

	public void setSubscriptionName(final String name)
	{
		this.subscriptionName = name;		
	}
	
	public void setEventManager(final EventManager eventManager)
	{
		this.eventManager = eventManager;
	}

	public void setConnection(MqttAsyncConnection connection)
	{
		this.connection = connection;		
	}
}
