//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.14 at 06:55:25 PM GMT 
//


package pl.baczkowicz.mqttspy.configuration.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import pl.baczkowicz.mqttspy.common.generated.MqttConnectionDetails;
import pl.baczkowicz.mqttspy.common.generated.PublicationDetails;
import pl.baczkowicz.mqttspy.common.generated.Task;


/**
 * <p>Java class for UserInterfaceMqttConnectionDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserInterfaceMqttConnectionDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://baczkowicz.pl/mqtt-spy/common}MqttConnectionDetails">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Publication" type="{http://baczkowicz.pl/mqtt-spy/common}PublicationDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Subscription" type="{http://baczkowicz.pl/mqtt-spy-configuration}TabbedSubscriptionDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="UserAuthentication" type="{http://baczkowicz.pl/mqtt-spy-configuration}UserAuthenticationOptions" minOccurs="0"/>
 *         &lt;element name="AutoOpen" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AutoConnect" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Formatter" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/>
 *         &lt;element name="MinMessagesStoredPerTopic" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaxMessagesStored" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PublicationScripts" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BackgroundScript" type="{http://baczkowicz.pl/mqtt-spy/common}Task" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserInterfaceMqttConnectionDetails", propOrder = {
    "name",
    "publication",
    "subscription",
    "userAuthentication",
    "autoOpen",
    "autoConnect",
    "formatter",
    "minMessagesStoredPerTopic",
    "maxMessagesStored",
    "publicationScripts",
    "backgroundScript"
})
public class UserInterfaceMqttConnectionDetails
    extends MqttConnectionDetails
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Publication")
    protected List<PublicationDetails> publication;
    @XmlElement(name = "Subscription")
    protected List<TabbedSubscriptionDetails> subscription;
    @XmlElement(name = "UserAuthentication")
    protected UserAuthenticationOptions userAuthentication;
    @XmlElement(name = "AutoOpen", defaultValue = "true")
    protected Boolean autoOpen;
    @XmlElement(name = "AutoConnect", defaultValue = "false")
    protected Boolean autoConnect;
    @XmlElement(name = "Formatter")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object formatter;
    @XmlElement(name = "MinMessagesStoredPerTopic")
    protected Integer minMessagesStoredPerTopic;
    @XmlElement(name = "MaxMessagesStored")
    protected Integer maxMessagesStored;
    @XmlElement(name = "PublicationScripts")
    protected String publicationScripts;
    @XmlElement(name = "BackgroundScript")
    protected List<Task> backgroundScript;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the publication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PublicationDetails }
     * 
     * 
     */
    public List<PublicationDetails> getPublication() {
        if (publication == null) {
            publication = new ArrayList<PublicationDetails>();
        }
        return this.publication;
    }

    /**
     * Gets the value of the subscription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TabbedSubscriptionDetails }
     * 
     * 
     */
    public List<TabbedSubscriptionDetails> getSubscription() {
        if (subscription == null) {
            subscription = new ArrayList<TabbedSubscriptionDetails>();
        }
        return this.subscription;
    }

    /**
     * Gets the value of the userAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link UserAuthenticationOptions }
     *     
     */
    public UserAuthenticationOptions getUserAuthentication() {
        return userAuthentication;
    }

    /**
     * Sets the value of the userAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserAuthenticationOptions }
     *     
     */
    public void setUserAuthentication(UserAuthenticationOptions value) {
        this.userAuthentication = value;
    }

    /**
     * Gets the value of the autoOpen property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoOpen() {
        return autoOpen;
    }

    /**
     * Sets the value of the autoOpen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoOpen(Boolean value) {
        this.autoOpen = value;
    }

    /**
     * Gets the value of the autoConnect property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoConnect() {
        return autoConnect;
    }

    /**
     * Sets the value of the autoConnect property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoConnect(Boolean value) {
        this.autoConnect = value;
    }

    /**
     * Gets the value of the formatter property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFormatter() {
        return formatter;
    }

    /**
     * Sets the value of the formatter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFormatter(Object value) {
        this.formatter = value;
    }

    /**
     * Gets the value of the minMessagesStoredPerTopic property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinMessagesStoredPerTopic() {
        return minMessagesStoredPerTopic;
    }

    /**
     * Sets the value of the minMessagesStoredPerTopic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinMessagesStoredPerTopic(Integer value) {
        this.minMessagesStoredPerTopic = value;
    }

    /**
     * Gets the value of the maxMessagesStored property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxMessagesStored() {
        return maxMessagesStored;
    }

    /**
     * Sets the value of the maxMessagesStored property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxMessagesStored(Integer value) {
        this.maxMessagesStored = value;
    }

    /**
     * Gets the value of the publicationScripts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicationScripts() {
        return publicationScripts;
    }

    /**
     * Sets the value of the publicationScripts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicationScripts(String value) {
        this.publicationScripts = value;
    }

    /**
     * Gets the value of the backgroundScript property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the backgroundScript property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBackgroundScript().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Task }
     * 
     * 
     */
    public List<Task> getBackgroundScript() {
        if (backgroundScript == null) {
            backgroundScript = new ArrayList<Task>();
        }
        return this.backgroundScript;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            String theName;
            theName = this.getName();
            toStringBuilder.append("name", theName);
        }
        {
            List<PublicationDetails> thePublication;
            thePublication = this.getPublication();
            toStringBuilder.append("publication", thePublication);
        }
        {
            List<TabbedSubscriptionDetails> theSubscription;
            theSubscription = this.getSubscription();
            toStringBuilder.append("subscription", theSubscription);
        }
        {
            UserAuthenticationOptions theUserAuthentication;
            theUserAuthentication = this.getUserAuthentication();
            toStringBuilder.append("userAuthentication", theUserAuthentication);
        }
        {
            Boolean theAutoOpen;
            theAutoOpen = this.isAutoOpen();
            toStringBuilder.append("autoOpen", theAutoOpen);
        }
        {
            Boolean theAutoConnect;
            theAutoConnect = this.isAutoConnect();
            toStringBuilder.append("autoConnect", theAutoConnect);
        }
        {
            Object theFormatter;
            theFormatter = this.getFormatter();
            toStringBuilder.append("formatter", theFormatter);
        }
        {
            Integer theMinMessagesStoredPerTopic;
            theMinMessagesStoredPerTopic = this.getMinMessagesStoredPerTopic();
            toStringBuilder.append("minMessagesStoredPerTopic", theMinMessagesStoredPerTopic);
        }
        {
            Integer theMaxMessagesStored;
            theMaxMessagesStored = this.getMaxMessagesStored();
            toStringBuilder.append("maxMessagesStored", theMaxMessagesStored);
        }
        {
            String thePublicationScripts;
            thePublicationScripts = this.getPublicationScripts();
            toStringBuilder.append("publicationScripts", thePublicationScripts);
        }
        {
            List<Task> theBackgroundScript;
            theBackgroundScript = this.getBackgroundScript();
            toStringBuilder.append("backgroundScript", theBackgroundScript);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof UserInterfaceMqttConnectionDetails)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final UserInterfaceMqttConnectionDetails that = ((UserInterfaceMqttConnectionDetails) object);
        equalsBuilder.append(this.getName(), that.getName());
        equalsBuilder.append(this.getPublication(), that.getPublication());
        equalsBuilder.append(this.getSubscription(), that.getSubscription());
        equalsBuilder.append(this.getUserAuthentication(), that.getUserAuthentication());
        equalsBuilder.append(this.isAutoOpen(), that.isAutoOpen());
        equalsBuilder.append(this.isAutoConnect(), that.isAutoConnect());
        equalsBuilder.append(this.getFormatter(), that.getFormatter());
        equalsBuilder.append(this.getMinMessagesStoredPerTopic(), that.getMinMessagesStoredPerTopic());
        equalsBuilder.append(this.getMaxMessagesStored(), that.getMaxMessagesStored());
        equalsBuilder.append(this.getPublicationScripts(), that.getPublicationScripts());
        equalsBuilder.append(this.getBackgroundScript(), that.getBackgroundScript());
    }

    public boolean equals(Object object) {
        if (!(object instanceof UserInterfaceMqttConnectionDetails)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getName());
        hashCodeBuilder.append(this.getPublication());
        hashCodeBuilder.append(this.getSubscription());
        hashCodeBuilder.append(this.getUserAuthentication());
        hashCodeBuilder.append(this.isAutoOpen());
        hashCodeBuilder.append(this.isAutoConnect());
        hashCodeBuilder.append(this.getFormatter());
        hashCodeBuilder.append(this.getMinMessagesStoredPerTopic());
        hashCodeBuilder.append(this.getMaxMessagesStored());
        hashCodeBuilder.append(this.getPublicationScripts());
        hashCodeBuilder.append(this.getBackgroundScript());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final UserInterfaceMqttConnectionDetails copy = ((target == null)?((UserInterfaceMqttConnectionDetails) createCopy()):((UserInterfaceMqttConnectionDetails) target));
        super.copyTo(copy, copyBuilder);
        {
            String sourceName;
            sourceName = this.getName();
            String copyName = ((String) copyBuilder.copy(sourceName));
            copy.setName(copyName);
        }
        {
            List<PublicationDetails> sourcePublication;
            sourcePublication = this.getPublication();
            List<PublicationDetails> copyPublication = ((List<PublicationDetails> ) copyBuilder.copy(sourcePublication));
            copy.publication = null;
            List<PublicationDetails> uniquePublicationl = copy.getPublication();
            uniquePublicationl.addAll(copyPublication);
        }
        {
            List<TabbedSubscriptionDetails> sourceSubscription;
            sourceSubscription = this.getSubscription();
            List<TabbedSubscriptionDetails> copySubscription = ((List<TabbedSubscriptionDetails> ) copyBuilder.copy(sourceSubscription));
            copy.subscription = null;
            List<TabbedSubscriptionDetails> uniqueSubscriptionl = copy.getSubscription();
            uniqueSubscriptionl.addAll(copySubscription);
        }
        {
            UserAuthenticationOptions sourceUserAuthentication;
            sourceUserAuthentication = this.getUserAuthentication();
            UserAuthenticationOptions copyUserAuthentication = ((UserAuthenticationOptions) copyBuilder.copy(sourceUserAuthentication));
            copy.setUserAuthentication(copyUserAuthentication);
        }
        {
            Boolean sourceAutoOpen;
            sourceAutoOpen = this.isAutoOpen();
            Boolean copyAutoOpen = ((Boolean) copyBuilder.copy(sourceAutoOpen));
            copy.setAutoOpen(copyAutoOpen);
        }
        {
            Boolean sourceAutoConnect;
            sourceAutoConnect = this.isAutoConnect();
            Boolean copyAutoConnect = ((Boolean) copyBuilder.copy(sourceAutoConnect));
            copy.setAutoConnect(copyAutoConnect);
        }
        {
            Object sourceFormatter;
            sourceFormatter = this.getFormatter();
            Object copyFormatter = ((Object) copyBuilder.copy(sourceFormatter));
            copy.setFormatter(copyFormatter);
        }
        {
            Integer sourceMinMessagesStoredPerTopic;
            sourceMinMessagesStoredPerTopic = this.getMinMessagesStoredPerTopic();
            Integer copyMinMessagesStoredPerTopic = ((Integer) copyBuilder.copy(sourceMinMessagesStoredPerTopic));
            copy.setMinMessagesStoredPerTopic(copyMinMessagesStoredPerTopic);
        }
        {
            Integer sourceMaxMessagesStored;
            sourceMaxMessagesStored = this.getMaxMessagesStored();
            Integer copyMaxMessagesStored = ((Integer) copyBuilder.copy(sourceMaxMessagesStored));
            copy.setMaxMessagesStored(copyMaxMessagesStored);
        }
        {
            String sourcePublicationScripts;
            sourcePublicationScripts = this.getPublicationScripts();
            String copyPublicationScripts = ((String) copyBuilder.copy(sourcePublicationScripts));
            copy.setPublicationScripts(copyPublicationScripts);
        }
        {
            List<Task> sourceBackgroundScript;
            sourceBackgroundScript = this.getBackgroundScript();
            List<Task> copyBackgroundScript = ((List<Task> ) copyBuilder.copy(sourceBackgroundScript));
            copy.backgroundScript = null;
            List<Task> uniqueBackgroundScriptl = copy.getBackgroundScript();
            uniqueBackgroundScriptl.addAll(copyBackgroundScript);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new UserInterfaceMqttConnectionDetails();
    }

}
