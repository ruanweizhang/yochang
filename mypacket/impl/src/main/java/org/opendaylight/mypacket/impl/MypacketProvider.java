/*
 * Copyright © 2016 mypacket CopyRight and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mypacket.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.NotificationListener;

import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
//import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.mypacket.rev150105.MypacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MypacketProvider {

    private static final Logger LOG = LoggerFactory.getLogger(MypacketProvider.class);

    private final DataBroker dataBroker;
    private final RpcProviderRegistry rpcRegistry;
    private final NotificationPublishService notificationPublishService;
    private final NotificationService notificationService;

    //private RpcRegistration<MypacketService> mypacketService = null;

    // registration for PacketProcessingListener
    private ListenerRegistration<NotificationListener> registration = null;

    public MypacketProvider(final DataBroker dataBroker,
                final RpcProviderRegistry rpcRegistry,
                final NotificationPublishService notificationPublishService,
                final NotificationService notificationService) {
	    this.rpcRegistry = rpcRegistry;
        this.notificationPublishService = notificationPublishService;
        this.notificationService = notificationService;
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("MypacketProvider Session Initiated");

        // for packetout RPC handle
        PacketProcessingService packetProcessingService = rpcRegistry.getRpcService(PacketProcessingService.class);
        //mypacketService = rpcRegistry.addRpcImplementation(MypacketService.class, new MypacketImpl(PacketProcessingService));
        
        // catch notification
        if (notificationService != null) {
        LOG.info("NotificationService is: " + notificationService.toString());
        MypacketImpl mypacketHandler = new MypacketImpl(dataBroker,packetProcessingService);
        registration = notificationService.registerNotificationListener(mypacketHandler);

        }
    }
    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        LOG.info("MypacketProvider Closed");
        /*
        if (mypacketService != null) {
            mypacketService.close();
        }
        */
        if( registration != null){
            registration.close();           
        }
    }
}
