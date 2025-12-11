package com.minhacienda.esb.routes;

import org.apache.camel.builder.RouteBuilder;

import com.minhacienda.esb.properties.SftpConsumerConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SFTPConsumerRoute extends RouteBuilder {

	@Inject
	SftpConsumerConfig sftpConfig;

    @Override
    public void configure() throws Exception {
        StringBuilder path = new StringBuilder();
		path.append(sftpConfig.getHostName()
			+ ":"
			+ sftpConfig.getPort()
			+ "/"
			+ sftpConfig.getPathDownload()
			+ "?privateKeyFile="
			+ sftpConfig.getAuthKey()
			+ "&username="
			+ sftpConfig.getAuthUser()
			+ "&jschLoggingLevel="
			+ sftpConfig.getJschLoggingLevel()
			+ "&maximumReconnectAttempts="
			+ sftpConfig.getReconnectAttempts()
			+"&reconnectDelay="+sftpConfig.getReconnectDelay()
			+ "&readLock=none&readLockCheckInterval="
			+ sftpConfig.getCheckInterval()
			+ "&delay="+sftpConfig.getDelay()
			+"&streamDownload="+sftpConfig.getStreamDownload()
			+"&useUserKnownHostsFile="+sftpConfig.getUseUserKnownHostsFile()
			+"&fastExistsCheck="+sftpConfig.getFastExistsCheck()
			+"&stepwise="+sftpConfig.getStepwise()
			+"&delete=true"
		);

        from("sftp://" + path.toString()).routeId("sftp_consumer")
			.setProperty("sftpSourceEndpoint",constant(path.toString()))
			.log("sftp://"+path.toString())
			.log("Inicia ruta wk-pbs-sftp2blob  ${date:now:yyyy-MM-dd HH:mm:ss}")
			.log("Count: '${headers.CamelBatchSize}'")
			.log("Index: '${headers.CamelBatchIndex}'")
			.log("Archivo recibido ${headers.CamelFileNameOnly}")
			.to("direct:transformationRoute")
		.end();
    }
}
