//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.14 at 06:55:25 PM GMT 
//


package pl.baczkowicz.mqttspy.configuration.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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


/**
 * <p>Java class for BaseMqttMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseMqttMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Topic" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Payload" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QoS" type="{http://baczkowicz.pl/mqtt-spy/common}qos"/>
 *         &lt;element name="Retained" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseMqttMessage", propOrder = {
    "topic",
    "payload",
    "qoS",
    "retained"
})
public class BaseMqttMessage
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElement(name = "Topic", required = true)
    protected String topic;
    @XmlElement(name = "Payload", required = true)
    protected String payload;
    @XmlElement(name = "QoS")
    protected int qoS;
    @XmlElement(name = "Retained")
    protected boolean retained;

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Gets the value of the payload property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayload(String value) {
        this.payload = value;
    }

    /**
     * Gets the value of the qoS property.
     * 
     */
    public int getQoS() {
        return qoS;
    }

    /**
     * Sets the value of the qoS property.
     * 
     */
    public void setQoS(int value) {
        this.qoS = value;
    }

    /**
     * Gets the value of the retained property.
     * 
     */
    public boolean isRetained() {
        return retained;
    }

    /**
     * Sets the value of the retained property.
     * 
     */
    public void setRetained(boolean value) {
        this.retained = value;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theTopic;
            theTopic = this.getTopic();
            toStringBuilder.append("topic", theTopic);
        }
        {
            String thePayload;
            thePayload = this.getPayload();
            toStringBuilder.append("payload", thePayload);
        }
        {
            int theQoS;
            theQoS = this.getQoS();
            toStringBuilder.append("qoS", theQoS);
        }
        {
            boolean theRetained;
            theRetained = this.isRetained();
            toStringBuilder.append("retained", theRetained);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof BaseMqttMessage)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final BaseMqttMessage that = ((BaseMqttMessage) object);
        equalsBuilder.append(this.getTopic(), that.getTopic());
        equalsBuilder.append(this.getPayload(), that.getPayload());
        equalsBuilder.append(this.getQoS(), that.getQoS());
        equalsBuilder.append(this.isRetained(), that.isRetained());
    }

    public boolean equals(Object object) {
        if (!(object instanceof BaseMqttMessage)) {
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
        hashCodeBuilder.append(this.getTopic());
        hashCodeBuilder.append(this.getPayload());
        hashCodeBuilder.append(this.getQoS());
        hashCodeBuilder.append(this.isRetained());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final BaseMqttMessage copy = ((target == null)?((BaseMqttMessage) createCopy()):((BaseMqttMessage) target));
        {
            String sourceTopic;
            sourceTopic = this.getTopic();
            String copyTopic = ((String) copyBuilder.copy(sourceTopic));
            copy.setTopic(copyTopic);
        }
        {
            String sourcePayload;
            sourcePayload = this.getPayload();
            String copyPayload = ((String) copyBuilder.copy(sourcePayload));
            copy.setPayload(copyPayload);
        }
        {
            int sourceQoS;
            sourceQoS = this.getQoS();
            int copyQoS = ((int) copyBuilder.copy(sourceQoS));
            copy.setQoS(copyQoS);
        }
        {
            boolean sourceRetained;
            sourceRetained = this.isRetained();
            boolean copyRetained = ((boolean) copyBuilder.copy(sourceRetained));
            copy.setRetained(copyRetained);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new BaseMqttMessage();
    }

}
