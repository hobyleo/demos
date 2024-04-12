package com.hoby.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Request complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestHeader" type="{http://www.djhealthunion.com/}RequestHeader" minOccurs="0"/>
 *         &lt;element name="requestBody" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Request", propOrder = {
        "requestHeader",
        "requestBody"
})
public class Request {

    protected RequestHeader requestHeader;
    protected String requestBody;

    /**
     * 获取requestHeader属性的值。
     *
     * @return possible object is
     * {@link RequestHeader }
     */
    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    /**
     * 设置requestHeader属性的值。
     *
     * @param value allowed object is
     *              {@link RequestHeader }
     */
    public void setRequestHeader(RequestHeader value) {
        this.requestHeader = value;
    }

    /**
     * 获取requestBody属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getRequestBody() {
        return requestBody;
    }

    /**
     * 设置requestBody属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestBody(String value) {
        this.requestBody = value;
    }

}
