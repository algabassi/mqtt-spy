//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.09 at 11:37:09 PM GMT 
//


package pl.baczkowicz.mqttspy.common.generated;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.baczkowicz.mqttspy.common.generated package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.baczkowicz.mqttspy.common.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BaseConnectionDetails }
     * 
     */
    public BaseConnectionDetails createBaseConnectionDetails() {
        return new BaseConnectionDetails();
    }

    /**
     * Create an instance of {@link PublicationDetails }
     * 
     */
    public PublicationDetails createPublicationDetails() {
        return new PublicationDetails();
    }

    /**
     * Create an instance of {@link BaseConnectionDetailsWithSubscriptionsAndScripts }
     * 
     */
    public BaseConnectionDetailsWithSubscriptionsAndScripts createBaseConnectionDetailsWithSubscriptionsAndScripts() {
        return new BaseConnectionDetailsWithSubscriptionsAndScripts();
    }

    /**
     * Create an instance of {@link NamedBaseConnectionDetailsWithPubSub }
     * 
     */
    public NamedBaseConnectionDetailsWithPubSub createNamedBaseConnectionDetailsWithPubSub() {
        return new NamedBaseConnectionDetailsWithPubSub();
    }

    /**
     * Create an instance of {@link UserCredentials }
     * 
     */
    public UserCredentials createUserCredentials() {
        return new UserCredentials();
    }

    /**
     * Create an instance of {@link BaseMqttMessage }
     * 
     */
    public BaseMqttMessage createBaseMqttMessage() {
        return new BaseMqttMessage();
    }

    /**
     * Create an instance of {@link SubscriptionDetails }
     * 
     */
    public SubscriptionDetails createSubscriptionDetails() {
        return new SubscriptionDetails();
    }

}