//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.14 at 06:43:55 PM GMT 
//


package pl.baczkowicz.mqttspy.daemon.configuration.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.baczkowicz.mqttspy.daemon.configuration.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MqttSpyDaemonConfiguration_QNAME = new QName("http://baczkowicz.pl/mqtt-spy/daemon/configuration", "MqttSpyDaemonConfiguration");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.baczkowicz.mqttspy.daemon.configuration.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MqttSpyDaemonConfiguration }
     * 
     */
    public MqttSpyDaemonConfiguration createMqttSpyDaemonConfiguration() {
        return new MqttSpyDaemonConfiguration();
    }

    /**
     * Create an instance of {@link DaemonMqttConnectionDetails }
     * 
     */
    public DaemonMqttConnectionDetails createDaemonMqttConnectionDetails() {
        return new DaemonMqttConnectionDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MqttSpyDaemonConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://baczkowicz.pl/mqtt-spy/daemon/configuration", name = "MqttSpyDaemonConfiguration")
    public JAXBElement<MqttSpyDaemonConfiguration> createMqttSpyDaemonConfiguration(MqttSpyDaemonConfiguration value) {
        return new JAXBElement<MqttSpyDaemonConfiguration>(_MqttSpyDaemonConfiguration_QNAME, MqttSpyDaemonConfiguration.class, null, value);
    }

}
