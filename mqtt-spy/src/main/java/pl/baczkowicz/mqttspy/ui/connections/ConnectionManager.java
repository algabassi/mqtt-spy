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
package pl.baczkowicz.mqttspy.ui.connections;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.common.generated.MessageLog;
import pl.baczkowicz.mqttspy.common.generated.MessageLogEnum;
import pl.baczkowicz.mqttspy.common.generated.UserCredentials;
import pl.baczkowicz.mqttspy.configuration.ConfigurationManager;
import pl.baczkowicz.mqttspy.configuration.ConfiguredConnectionDetails;
import pl.baczkowicz.mqttspy.configuration.generated.UserInterfaceMqttConnectionDetails;
import pl.baczkowicz.mqttspy.connectivity.MqttAsyncConnection;
import pl.baczkowicz.mqttspy.connectivity.MqttAsyncConnectionRunnable;
import pl.baczkowicz.mqttspy.connectivity.MqttConnectionStatus;
import pl.baczkowicz.mqttspy.connectivity.MqttContent;
import pl.baczkowicz.mqttspy.connectivity.RuntimeConnectionProperties;
import pl.baczkowicz.mqttspy.connectivity.handlers.MqttCallbackHandler;
import pl.baczkowicz.mqttspy.connectivity.handlers.MqttDisconnectionResultHandler;
import pl.baczkowicz.mqttspy.connectivity.handlers.MqttEventHandler;
import pl.baczkowicz.mqttspy.connectivity.reconnection.ReconnectionManager;
import pl.baczkowicz.mqttspy.events.EventManager;
import pl.baczkowicz.mqttspy.events.queuable.UIEventHandler;
import pl.baczkowicz.mqttspy.events.queuable.connectivity.MqttConnectionAttemptFailureEvent;
import pl.baczkowicz.mqttspy.events.queuable.connectivity.MqttDisconnectionAttemptFailureEvent;
import pl.baczkowicz.mqttspy.events.queuable.ui.MqttSpyUIEvent;
import pl.baczkowicz.mqttspy.exceptions.ConfigurationException;
import pl.baczkowicz.mqttspy.exceptions.MqttSpyException;
import pl.baczkowicz.mqttspy.logger.MqttMessageLogger;
import pl.baczkowicz.mqttspy.messages.ReceivedMqttMessage;
import pl.baczkowicz.mqttspy.messages.ReceivedMqttMessageWithSubscriptions;
import pl.baczkowicz.mqttspy.scripts.InteractiveScriptManager;
import pl.baczkowicz.mqttspy.scripts.Script;
import pl.baczkowicz.mqttspy.stats.StatisticsManager;
import pl.baczkowicz.mqttspy.storage.ManagedMessageStoreWithFiltering;
import pl.baczkowicz.mqttspy.ui.ConnectionController;
import pl.baczkowicz.mqttspy.ui.MainController;
import pl.baczkowicz.mqttspy.ui.SubscriptionController;
import pl.baczkowicz.mqttspy.ui.panes.PaneVisibilityStatus;
import pl.baczkowicz.mqttspy.ui.panes.TabStatus;
import pl.baczkowicz.mqttspy.ui.utils.ConnectivityUtils;
import pl.baczkowicz.mqttspy.ui.utils.ContextMenuUtils;
import pl.baczkowicz.mqttspy.ui.utils.DialogUtils;
import pl.baczkowicz.mqttspy.ui.utils.FxmlUtils;
import pl.baczkowicz.mqttspy.ui.utils.TabUtils;

/**
 * Class for managing connection tabs.
 */
public class ConnectionManager
{
	/** Diagnostic logger. */
	private final static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	/** Global event manager.*/
	private final EventManager eventManager;
	
	/** Global statistics manager .*/
	private final StatisticsManager statisticsManager;
	
	/** Global configuration manager. */
	private final ConfigurationManager configurationManager;
	
	// TODO: not sure this is needed
	/** Map of connections and their IDs. */
	private Map<Integer, MqttAsyncConnection> connections = new HashMap<Integer, MqttAsyncConnection>();
	
	/** Map of connections and their connection controllers. */
	private final Map<MqttAsyncConnection, ConnectionController> connectionControllers = new HashMap<>();
	
	/** Map of connections and their tabs. */
	private final Map<MqttAsyncConnection, Tab> connectionTabs = new HashMap<>();
	
	/** Map of connections and their subscription managers. */
	private final Map<MqttAsyncConnection, SubscriptionManager> subscriptionManagers = new HashMap<>();
	
	/** UI event queue. */
	private final Queue<MqttSpyUIEvent> uiEventQueue;

	/** Reconnection manager. */
	private ReconnectionManager reconnectionManager;

	public ConnectionManager(final EventManager eventManager, final StatisticsManager statisticsManager, final ConfigurationManager configurationManager)
	{
		this.uiEventQueue = new LinkedBlockingQueue<>();
		
		this.eventManager = eventManager;
		this.statisticsManager = statisticsManager;
		this.configurationManager = configurationManager;
		
		this.reconnectionManager = new ReconnectionManager();
		new Thread(reconnectionManager).start();
		
		new Thread(new UIEventHandler(uiEventQueue, eventManager)).start();
	}
	
	public void openConnection(final ConfiguredConnectionDetails configuredConnectionDetails, final MainController mainController) throws ConfigurationException
	{
		// Note: this is not a complete ConfiguredConnectionDetails copy but ConnectionDetails copy - any user credentials entered won't be stored in config
		final ConfiguredConnectionDetails connectionDetails = new ConfiguredConnectionDetails();
		configuredConnectionDetails.copyTo(connectionDetails);
		connectionDetails.setId(configuredConnectionDetails.getId());			
		
		final boolean cancelled = completeUserAuthenticationCredentials(connectionDetails, mainController.getStage());		
		
		if (!cancelled)
		{
			final String validationResult = ConnectivityUtils.validateConnectionDetails(connectionDetails, true);
			if (validationResult != null)
			{
				DialogUtils.showValidationWarning(validationResult);
			}
			else
			{
				final RuntimeConnectionProperties connectionProperties = new RuntimeConnectionProperties(connectionDetails);
				new Thread(new Runnable()
				{					
					@Override
					public void run()
					{
						loadConnectionTab(mainController, mainController, connectionProperties);					
					}
				}).start();											
			}
		}
	}	

	private boolean completeUserAuthenticationCredentials(final UserInterfaceMqttConnectionDetails connectionDetails, final Stage stage)
	{
		if (connectionDetails.getUserAuthentication() != null)
		{
			// Copy so that we don't store it in the connection and don't save those values
			final UserCredentials userCredentials = new UserCredentials();
			connectionDetails.getUserCredentials().copyTo(userCredentials);
			
			// Check if ask for username or password, and then override existing values if confirmed
			if (connectionDetails.getUserAuthentication().isAskForPassword() || connectionDetails.getUserAuthentication().isAskForUsername())
			{
				// Password is decoded and encoded in this utility method
				if (!DialogUtils.showUsernameAndPasswordDialog(stage, connectionDetails.getName(), userCredentials))
				{
					return true;
				}
			}
			
			// Settings user credentials so they can be validated and passed onto the MQTT client library			
			connectionDetails.setUserCredentials(userCredentials);
		}
		
		return false;
	}	
	
	/**
	 * Creates and loads a new connection tab.
	 * 
	 * @param mainController The main controller
	 * @param parent The UI parent node
	 * @param connectionProperties The connection properties from which to create the connection
	 */
	public void loadConnectionTab(final MainController mainController,
			final Object parent, final RuntimeConnectionProperties connectionProperties)
	{		
		// Create connection
		final MqttAsyncConnection connection = createConnection(connectionProperties, uiEventQueue);
		connection.setOpening(true);
		connection.setStatisticsManager(statisticsManager);

		// Load a new tab and connection pane
		final FXMLLoader loader = FxmlUtils.createFXMLLoader(parent, FxmlUtils.FXML_LOCATION + "ConnectionTab.fxml");
		AnchorPane connectionPane = FxmlUtils.loadAnchorPane(loader);
		
		final ConnectionController connectionController = (ConnectionController) loader.getController();
		connectionController.setConnection(connection);
		connectionController.setConnectionManager(this);
		connectionController.setEventManager(eventManager);
		connectionController.setStatisticsManager(statisticsManager);
		connectionController.setTabStatus(new TabStatus());
		connectionController.getTabStatus().setVisibility(PaneVisibilityStatus.NOT_VISIBLE);
				
		final Tab connectionTab = createConnectionTab(connection.getProperties().getName(), connectionPane, connectionController);
		
		final SubscriptionManager subscriptionManager = new SubscriptionManager(eventManager, configurationManager, uiEventQueue);			
		
		final SubscriptionController subscriptionController = subscriptionManager.createSubscriptionTab(
				true, parent, connection.getStore(), null, connection);
		subscriptionController.setConnectionController(connectionController);
		subscriptionController.setFormatting(configurationManager.getConfiguration().getFormatting());
		
		final ConnectionManager connectionManager = this;
		
		Platform.runLater(new Runnable()
		{			
			@Override
			public void run()
			{					
				connectionController.init();
				subscriptionController.init();				
								
				mainController.addConnectionTab(connectionTab);
				connectionController.getTabStatus().setVisibility(PaneVisibilityStatus.ATTACHED);
				connectionController.getTabStatus().setParent(connectionTab.getTabPane());
				
				// TODO: move creation of the context menus outside the FX thread?
				connectionTab.setContextMenu(ContextMenuUtils.createConnectionMenu(connection, connectionController, connectionManager));
				subscriptionController.getTab().setContextMenu(ContextMenuUtils.createAllSubscriptionsTabContextMenu(connection, eventManager, subscriptionManager, configurationManager));
				
				eventManager.registerConnectionStatusObserver(connectionController, connection);
				// connection.addObserver(connectionController);											
				connection.setOpening(false);
				connection.setOpened(true);
				
				// Connect
				if (connection.getProperties().isAutoConnect())
				{
					connectToBroker(connection);
				}
				else
				{
					connection.setConnectionStatus(MqttConnectionStatus.NOT_CONNECTED);
				}	
				
				// Add "All" subscription tab
				connectionController.getSubscriptionTabs().getTabs().clear();
				connectionController.getSubscriptionTabs().getTabs().add(subscriptionController.getTab());
				
				connectionControllers.put(connection, connectionController);
				connectionTabs.put(connection, connectionTab);
				subscriptionManagers.put(connection, subscriptionManager);
								
				// Populate panes
				mainController.populateConnectionPanes(connectionProperties.getConfiguredProperties(), connectionController);	
				
				// Apply perspective
				mainController.showPerspective(connectionController);
			}
		});		
	}
	
	/**
	 * Creates and loads a message log tab.
	 * 
	 * @param mainController The main controller
	 * @param parent The parent UI node
	 * @param name Name of the tab
	 * @param list List of messages to display
	 */
	public void loadMessageLogTab(final MainController mainController, final Object parent, final String name, final List<ReceivedMqttMessage> list)
	{		
		// Load a new tab and connection pane
		final FXMLLoader loader = FxmlUtils.createFXMLLoader(parent, FxmlUtils.FXML_LOCATION + "ConnectionTab.fxml");
		AnchorPane connectionPane = FxmlUtils.loadAnchorPane(loader);
		
		final ConnectionController connectionController = (ConnectionController) loader.getController();
		
		//connectionController.setConnection(connection);
		connectionController.setConnectionManager(this);
		connectionController.setEventManager(eventManager);
		connectionController.setStatisticsManager(statisticsManager);
		connectionController.setReplayMode(true);
		connectionController.setTabStatus(new TabStatus());
		connectionController.getTabStatus().setVisibility(PaneVisibilityStatus.NOT_VISIBLE);
		
		final Tab replayTab = createConnectionTab(name, connectionPane, connectionController);
		final SubscriptionManager subscriptionManager = new SubscriptionManager(eventManager, configurationManager, uiEventQueue);			
		
        final ManagedMessageStoreWithFiltering store = new ManagedMessageStoreWithFiltering(
        		name, 0, list.size(), list.size(), uiEventQueue, eventManager);               
        
		final SubscriptionController subscriptionController = subscriptionManager.createSubscriptionTab(
				true, parent, store, null, null);
		subscriptionController.setConnectionController(connectionController);
		subscriptionController.setFormatting(configurationManager.getConfiguration().getFormatting());
		subscriptionController.setReplayMode(true);
		
		Platform.runLater(new Runnable()
		{			
			@Override
			public void run()
			{					
				connectionController.init();
				subscriptionController.init();				
								
				mainController.addConnectionTab(replayTab);
				
				replayTab.setContextMenu(ContextMenuUtils.createMessageLogMenu(replayTab, connectionController));
								
				// Add "All" subscription tab
				connectionController.getSubscriptionTabs().getTabs().clear();
				connectionController.getSubscriptionTabs().getTabs().add(subscriptionController.getTab());
				connectionController.getTabStatus().setVisibility(PaneVisibilityStatus.ATTACHED);
				connectionController.getTabStatus().setParent(replayTab.getTabPane());
				// TODO: pane status
				
				// Apply perspective
				connectionController.showReplayMode();
				
				// Process the messages
		        for (final ReceivedMqttMessage mqttMessage : list)
		        {		        	
		        	store.messageReceived(new MqttContent(mqttMessage));
		        }
			}
		});		
	}
	
	/**
	 * Gets a collection of all connections.
	 * 
	 * @return Collection of MqttAsyncConnection instances
	 */
	public Collection<MqttAsyncConnection> getConnections()
	{
		// TODO: needs to use the connections list, as the controllers are populated later,
		// so opening doesn't work properly
		return connections.values();
		//return connectionControllers.keySet();
	}
	
	/**
	 * Disconnects all connections.
	 */
	public void disconnectAll()
	{
		for (final MqttAsyncConnection connection : getConnections())
		{
			disconnectFromBroker(connection);
		}
	}
	
	/**
	 * Disconnects and closes the tab of the given connection.
	 * 
	 * @param connection The connection to close
	 */
	public void disconnectAndCloseTab(final MqttAsyncConnection connection)
	{		
		disconnectFromBroker(connection);
		connection.closeConnection();
		if (connection.getMessageLogger() != null && connection.getMessageLogger().isRunning())
		{
			connection.getMessageLogger().stop();
		}
		
		TabUtils.requestClose(connectionControllers.get(connection).getTab());
		connectionControllers.remove(connection);
		connectionTabs.remove(connection);
		subscriptionManagers.remove(connection);
		
		// Stop all scripts
		for (final Script script : connection.getScriptManager().getScripts().values())
		{
			connection.getScriptManager().stopScriptFile(script.getScriptFile());
		}		
	}
	
	/**
	 * Creates a new connection tab.
	 * 
	 * @param name Name of the tab
	 * @param content The content of the tab
	 * @param connectionController The connection controller
	 * 
	 * @return Created tab
	 */
	private Tab createConnectionTab(final String name, final Node content, final ConnectionController connectionController)
	{
		final Tab tab = new Tab();
		connectionController.setTab(tab);
		tab.setText(name);
		tab.setContent(content);		

		return tab;
	}	

	public MqttAsyncConnection createConnection(final RuntimeConnectionProperties connectionProperties, final Queue<MqttSpyUIEvent> uiEventQueue)
	{
		final MqttAsyncConnection connection = new MqttAsyncConnection(reconnectionManager,
				connectionProperties, MqttConnectionStatus.DISCONNECTED, eventManager, uiEventQueue);

		// Set up message logger		
		final MessageLog messageLog = connectionProperties.getConfiguredProperties().getMessageLog();		
		if (messageLog != null && !messageLog.getValue().equals(MessageLogEnum.DISABLED) 
				&& messageLog.getLogFile() != null && !messageLog.getLogFile().isEmpty())
		{
			final Queue<ReceivedMqttMessageWithSubscriptions> messageQueue= new LinkedBlockingQueue<ReceivedMqttMessageWithSubscriptions>();
			
			if (connection.getMessageLogger() == null)
			{
				final MqttMessageLogger messageLogger = new MqttMessageLogger(
						connection.getId(), messageQueue, messageLog, true, 50);
				connection.setMessageLogger(messageLogger);
			}
			
			if (!connection.getMessageLogger().isRunning())
			{
				new Thread(connection.getMessageLogger()).start();
			}
		}		
				
		final InteractiveScriptManager scriptManager = new InteractiveScriptManager(eventManager, connection);
		connection.setScriptManager(scriptManager);
	
		// Store the created connection
		connections.put(connectionProperties.getConfiguredProperties().getId(), connection);

		return connection;
	}

	/**
	 * Connects the specified connection to a broker.
	 * 
	 * @param connection The connection to connect
	 * 
	 * @return True if successfully connected
	 */
	public boolean connectToBroker(final MqttAsyncConnection connection)
	{
		try
		{
			connection.connect(new MqttCallbackHandler(connection), new MqttAsyncConnectionRunnable(connection));			
			return true;
		}
		catch (MqttSpyException e)
		{
			// TODO: simplify this
			Platform.runLater(new MqttEventHandler(new MqttConnectionAttemptFailureEvent(connection, e)));
			logger.error(e.getMessage(), e);
		}

		return false;
	}

	/**
	 * Disconnects the specified connection from the broker.
	 * 
	 * @param connection The connection to disconnect
	 */
	public void disconnectFromBroker(final MqttAsyncConnection connection)
	{				
		try
		{
			connection.disconnect(new MqttDisconnectionResultHandler());
		}
		catch (MqttSpyException e)
		{
			// TODO: simplify this
			Platform.runLater(new MqttEventHandler(new MqttDisconnectionAttemptFailureEvent(connection, e)));
			logger.error(e.getMessage(), e);
		}		
	}
	
	/**
	 * Disconnects and closes all connections.
	 */
	public void disconnectAndCloseAll()
	{
		for (final MqttAsyncConnection connection : getConnections())
		{
			disconnectAndCloseTab(connection);
		}
	}

	/**
	 * Gets the connection controllers.
	 * 
	 * @return Collection of ConnectionController instances
	 */
	public Collection<ConnectionController> getConnectionControllers()
	{
		return connectionControllers.values();
	}
	
	/**
	 * Gets subscription manager for the given connection.
	 * 
	 * @param connection The connection for which to retrieve the subscription manager
	 * 
	 * @return SubscriptionManager instance
	 */
	public SubscriptionManager getSubscriptionManager(final MqttAsyncConnection connection)
	{
		return subscriptionManagers.get(connection);
	}
}
