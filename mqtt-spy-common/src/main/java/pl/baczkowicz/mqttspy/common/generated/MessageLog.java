//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.22 at 09:47:29 AM GMT 
//


package pl.baczkowicz.mqttspy.common.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageLog.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageLog">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISABLED"/>
 *     &lt;enumeration value="XML_WITH_ENCODED_PAYLOAD"/>
 *     &lt;enumeration value="XML_WITH_PLAIN_PAYLOAD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageLog")
@XmlEnum
public enum MessageLog {

    DISABLED,
    XML_WITH_ENCODED_PAYLOAD,
    XML_WITH_PLAIN_PAYLOAD;

    public String value() {
        return name();
    }

    public static MessageLog fromValue(String v) {
        return valueOf(v);
    }

}
