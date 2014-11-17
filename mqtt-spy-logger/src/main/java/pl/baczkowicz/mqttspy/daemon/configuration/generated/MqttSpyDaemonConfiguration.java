//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.17 at 06:19:45 PM GMT 
//


package pl.baczkowicz.mqttspy.daemon.configuration.generated;

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
 * <p>Java class for MqttSpyDaemonConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MqttSpyDaemonConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Connection" type="{http://baczkowicz.pl/mqtt-spy/daemon/configuration}DaemonMqttConnectionDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MqttSpyDaemonConfiguration", propOrder = {
    "connection"
})
public class MqttSpyDaemonConfiguration
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElement(name = "Connection", required = true)
    protected DaemonMqttConnectionDetails connection;

    /**
     * Gets the value of the connection property.
     * 
     * @return
     *     possible object is
     *     {@link DaemonMqttConnectionDetails }
     *     
     */
    public DaemonMqttConnectionDetails getConnection() {
        return connection;
    }

    /**
     * Sets the value of the connection property.
     * 
     * @param value
     *     allowed object is
     *     {@link DaemonMqttConnectionDetails }
     *     
     */
    public void setConnection(DaemonMqttConnectionDetails value) {
        this.connection = value;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            DaemonMqttConnectionDetails theConnection;
            theConnection = this.getConnection();
            toStringBuilder.append("connection", theConnection);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MqttSpyDaemonConfiguration)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final MqttSpyDaemonConfiguration that = ((MqttSpyDaemonConfiguration) object);
        equalsBuilder.append(this.getConnection(), that.getConnection());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MqttSpyDaemonConfiguration)) {
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
        hashCodeBuilder.append(this.getConnection());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MqttSpyDaemonConfiguration copy = ((target == null)?((MqttSpyDaemonConfiguration) createCopy()):((MqttSpyDaemonConfiguration) target));
        {
            DaemonMqttConnectionDetails sourceConnection;
            sourceConnection = this.getConnection();
            DaemonMqttConnectionDetails copyConnection = ((DaemonMqttConnectionDetails) copyBuilder.copy(sourceConnection));
            copy.setConnection(copyConnection);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MqttSpyDaemonConfiguration();
    }

}
