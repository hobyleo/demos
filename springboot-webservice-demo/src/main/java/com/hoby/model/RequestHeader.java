package com.hoby.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RequestHeader complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="RequestHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiver" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="msgType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="msgId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="msgPriority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="msgVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestHeader", propOrder = {
        "sender",
        "receiver",
        "requestTime",
        "msgType",
        "msgId",
        "msgPriority",
        "msgVersion"
})
public class RequestHeader {

    protected String sender;
    protected String receiver;
    protected String requestTime;
    protected String msgType;
    protected String msgId;
    protected String msgPriority;
    protected String msgVersion;

    /**
     * 获取sender属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getSender() {
        return sender;
    }

    /**
     * 设置sender属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSender(String value) {
        this.sender = value;
    }

    /**
     * 获取receiver属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 设置receiver属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReceiver(String value) {
        this.receiver = value;
    }

    /**
     * 获取requestTime属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getRequestTime() {
        return requestTime;
    }

    /**
     * 设置requestTime属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestTime(String value) {
        this.requestTime = value;
    }

    /**
     * 获取msgType属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * 设置msgType属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMsgType(String value) {
        this.msgType = value;
    }

    /**
     * 获取msgId属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * 设置msgId属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    /**
     * 获取msgPriority属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getMsgPriority() {
        return msgPriority;
    }

    /**
     * 设置msgPriority属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMsgPriority(String value) {
        this.msgPriority = value;
    }

    /**
     * 获取msgVersion属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getMsgVersion() {
        return msgVersion;
    }

    /**
     * 设置msgVersion属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMsgVersion(String value) {
        this.msgVersion = value;
    }

}
