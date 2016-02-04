package com.sbpinvertor.modbus.slave;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.data.DataHolder;
import com.sbpinvertor.modbus.exception.ModbusSlaveException;
import com.sbpinvertor.modbus.msg.base.ModbusRequest;
import com.sbpinvertor.modbus.net.ModbusConnection;
import com.sbpinvertor.modbus.net.ModbusTransport;

import java.io.IOException;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class RequestHandlerSerial extends RequestHandler {

    public RequestHandlerSerial(ModbusSlave slave, ModbusConnection conn) {
        super(slave, conn);
    }

    @Override
    public void run() {
        setListening(true);
        do {
            try {
                DataHolder dataHolder = getSlave().getDataHolder();
                ModbusTransport transport = getConn().getTransport();
                if (dataHolder == null)
                    throw new ModbusSlaveException("DataHolder is null");
                ModbusRequest request = (ModbusRequest) transport.readRequest();
                transport.send(request.getResponse(dataHolder));
            } catch (Exception e) {
                Modbus.log().fine("Request timeout(no clients connected)");
            }
        } while (isListening());
        try {
            getConn().close();
        } catch (IOException e) {
            Modbus.log().warning(e.getMessage());
        }
    }
}