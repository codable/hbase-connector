/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.hbase.api;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.UnhandledException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.Result;

/**
 * TODO: Description of the class, Comments in english by default
 * 
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public class RPCHBaseService implements HBaseService {

    private static final Charset UTF8 = Charset.forName("utf-8");
    private HTableInterfaceFactory hTableInterfaceFactory;
    private Configuration configuration;

    public RPCHBaseService() {
        hTableInterfaceFactory = new HTableFactory();
        configuration = HBaseConfiguration.create();
    }

    /** @see org.mule.module.hbase.api.HBaseService#createTable(java.lang.String) */
    public void createTable(String name) {
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration);
            HTableDescriptor descriptor = new HTableDescriptor(name);
            hBaseAdmin.createTable(descriptor);
        } catch (MasterNotRunningException e) {
             // TODO: Define what to do!
            throw new UnhandledException(e);
        } catch (ZooKeeperConnectionException e) {
             // TODO: Define what to do!
            throw new UnhandledException(e);
        } catch (IOException e) {
            // TODO: Define what to do!
            throw new UnhandledException(e);
        }
    }
    
    private Result doGet(final HTableInterface hTableInterface, final String rowKey) {
        try {
            return hTableInterface.get(createGet(rowKey));
        } catch (IOException e) {
             // TODO: Define what to do!
            throw new UnhandledException(e);
        }
    }

    private Get createGet(String rowKey) {
        Get get = new Get(rowKey.getBytes(UTF8));
        return get;
    }

    private HTableInterface getHTable(String tableName) {
        return hTableInterfaceFactory.createHTableInterface(configuration, tableName.getBytes(UTF8));
    }

    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String) */
    public Result get(String tableName, String row) {
        return doGet(getHTable(tableName), row);
    }

    /** @see org.mule.module.hbase.api.HBaseService#addProperties(java.util.Map) */
    public void addProperties(Map<String, String> properties) {
        for (Entry<String, String> entry : properties.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
    }

}
