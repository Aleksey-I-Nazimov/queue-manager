package org.numamo.lib.test.queue.manager.model;

import java.util.Map;


/**
 * The basic model of the configuration parameters
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputConnectionParams {


    /**
     * JMS message type of the input or output queues
     */
    public enum JmsMessageType {
        JMS_TEXT_MESSAGE, JMS_BYTE_MESSAGE
    }


    // Variables and constructors:-------------------------------------------------------
    private String brokerUrl;
    private String brokerHost;
    private String brokerPort;

    private String user;
    private String password;

    private String connectedInputQueue;
    private String connectedOutputQueue;
    private Map<String,String> additionalParams;

    private JmsMessageType outputQueueMessageType;


    // Public API:-----------------------------------------------------------------------
    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public String getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectedInputQueue() {
        return connectedInputQueue;
    }

    public void setConnectedInputQueue(String connectedInputQueue) {
        this.connectedInputQueue = connectedInputQueue;
    }

    public String getConnectedOutputQueue() {
        return connectedOutputQueue;
    }

    public void setConnectedOutputQueue(String connectedOutputQueue) {
        this.connectedOutputQueue = connectedOutputQueue;
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }

    public JmsMessageType getOutputQueueMessageType() {
        return outputQueueMessageType;
    }

    public void setOutputQueueMessageType(JmsMessageType outputQueueMessageType) {
        this.outputQueueMessageType = outputQueueMessageType;
    }

    @Override
    public String toString() {
        return "InputOutputConnectionParams{" +
                "brokerUrl='" + brokerUrl + '\'' +
                ", brokerHost='" + brokerHost + '\'' +
                ", brokerPort='" + brokerPort + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", connectedInputQueue='" + connectedInputQueue + '\'' +
                ", connectedOutputQueue='" + connectedOutputQueue + '\'' +
                ", additionalParams=" + additionalParams +
                ", outputQueueMessageType=" + outputQueueMessageType +
                '}';
    }

}
