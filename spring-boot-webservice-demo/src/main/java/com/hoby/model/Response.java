package com.hoby.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Response complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="Response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseHeader" type="{http://www.djhealthunion.com/}ResponseHeader" minOccurs="0"/>
 *         &lt;element name="responseBody" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response", propOrder = {
        "responseHeader",
        "responseBody"
})
public class Response {

    protected ResponseHeader responseHeader;
    protected String responseBody;

    /**
     * 获取responseHeader属性的值。
     *
     * @return possible object is
     * {@link ResponseHeader }
     */
    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    /**
     * 设置responseHeader属性的值。
     *
     * @param value allowed object is
     *              {@link ResponseHeader }
     */
    public void setResponseHeader(ResponseHeader value) {
        this.responseHeader = value;
    }

    /**
     * 获取responseBody属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * 设置responseBody属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseBody(String value) {
        this.responseBody = value;
    }

}
