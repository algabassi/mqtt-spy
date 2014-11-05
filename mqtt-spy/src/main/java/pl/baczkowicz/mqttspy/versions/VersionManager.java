package pl.baczkowicz.mqttspy.versions;

import java.io.IOException;
import java.net.URL;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import pl.baczkowicz.mqttspy.common.exceptions.XMLException;
import pl.baczkowicz.mqttspy.common.xml.XMLParser;
import pl.baczkowicz.mqttspy.configuration.ConfigurationManager;
import pl.baczkowicz.mqttspy.ui.controlpanel.ItemStatus;
import pl.baczkowicz.mqttspy.versions.generated.MqttSpyVersions;
import pl.baczkowicz.mqttspy.versions.generated.ReleaseStatus;

/**
 * 
 * Manages loading of the version info.
 * 
 * @author Kamil Baczkowicz
 *
 */
public class VersionManager extends XMLParser
{
	private static final String PACKAGE = "pl.baczkowicz.mqttspy.versions.generated";
	
	private static final String SCHEMA = "/mqtt-spy-versions.xsd";
	
	private MqttSpyVersions versions;

	private ConfigurationManager configurationManager;

	public VersionManager(final ConfigurationManager configurationManager) throws XMLException
	{
		super(SCHEMA, PACKAGE);
		this.configurationManager = configurationManager;
					
		this.versions = new MqttSpyVersions();
	}
	
	public MqttSpyVersions loadVersions() throws XMLException
	{
		try
		{
			final URL url = new URL(configurationManager.getProperty(ConfigurationManager.VERSION_INFO_URL));

			versions = (MqttSpyVersions) loadFromInputStream(url.openStream());			
		}
		catch (IOException e)
		{
			throw new XMLException("Cannot read version info from " + configurationManager.getProperty(ConfigurationManager.VERSION_INFO_URL), e);
		}
				
		return versions;
	}
	
	public MqttSpyVersions getVersions()
	{
		return versions;
	}

	public static boolean isInRange(final String currentRelease, final ReleaseStatus release)
	{
		if ((new DefaultArtifactVersion(currentRelease).compareTo(new DefaultArtifactVersion(release.getFromVersion())) >= 0)
			&& (new DefaultArtifactVersion(currentRelease).compareTo(new DefaultArtifactVersion(release.getToVersion())) <= 0))
		{
			return true;		
		}
		
		return false;
	}
	
	public static ItemStatus convertVersionStatus(final ReleaseStatus release)
	{
		switch (release.getUpdateStatus())
		{
			case CRITICAL:
				return ItemStatus.ERROR;
			case UPDATE_RECOMMENDED:
				return ItemStatus.WARN;
			case NEW_AVAILABLE:
				return ItemStatus.INFO;
			case ON_LATEST:
				return ItemStatus.OK;
			default:
				return ItemStatus.ERROR;		
		}
	}
}
