/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.msf4j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.msf4j.MicroservicesRunner;
import org.wso2.msf4j.util.SystemVariableUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final Path CONF_PATH = Paths.get(SystemVariableUtil.getValue("user.dir", "/home/wso2") + "/conf/transports/");
    private static final String NETTY_TRANSPORT_CONF = "netty-transports.yml";

    public static void main(String[] args) {

        Path confFilePath = CONF_PATH.resolve(Paths.get(NETTY_TRANSPORT_CONF));
        InputStream inputStream = Application.class.getResourceAsStream(File.separator + NETTY_TRANSPORT_CONF);
        try {
            Files.createDirectories(CONF_PATH);
            Files.copy(inputStream, confFilePath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Copied resource {} into {}", NETTY_TRANSPORT_CONF, confFilePath);
        } catch (IOException e) {
            logger.error("Failed to create and copy resource to {}", confFilePath);
        }

        System.setProperty("transports.netty.conf", CONF_PATH.resolve(NETTY_TRANSPORT_CONF).toString());
        logger.info("Set system properties before start microservice");

        new MicroservicesRunner()
                .deploy(new RestHandler())
                .start();
    }
}
