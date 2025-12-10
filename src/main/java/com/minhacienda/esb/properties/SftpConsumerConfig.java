package com.minhacienda.esb.properties;

import io.smallrye.config.ConfigMapping;


@ConfigMapping(prefix = "sftp.consumer")
public interface SftpConsumerConfig {
    public String getHostName();
    public String getPort();
    public String getAuthKey();
    public String getAuthUser();
    public String getPathDownload();
    public String getJschLoggingLevel();
    public int getReconnectAttempts();
    public long getReconnectDelay();
    public long getCheckInterval();
    public String getDelay();
    public boolean getStreamDownload();
    public boolean getUseUserKnownHostsFile();
    public boolean getFastExistsCheck();
    public boolean getStepwise();
}