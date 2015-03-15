package com.odious.modbus;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ModbusReader {
	private ModbusMaster master;
	private Object res;
	private static float displayedResult = 0;
	// 999999 = 0xF423F
	// 0xFFFFFFFF = 4294967295
	public static final long MAX_HEX_TO_DEC_VALUE = 4_294_967_296L;
	public static final int MAX_DISPLAYED_VALUE = 999_999;
	private int baudRate, dataBits, stopBits;
	private String portId;
	private float tempResult = 0;
	private Direction direction;

	public ModbusReader(String portId, int baudRate, int dataBits, int stopBits) {
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.portId = portId;
	}

	public void sendRequest() {
		ModbusFactory factory = new ModbusFactory();
		SerialParameters params = new SerialParameters();
		params.setBaudRate(baudRate);
		params.setDataBits(dataBits);
		params.setStopBits(stopBits);
		params.setParity(0);
		params.setCommPortId(portId);
		master = factory.createRtuMaster(params);
		master.setTimeout(300);
		master.setRetries(0);
		try {
			master.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Serial connection problem.");
			return;
		}

		try {
			ModbusLocator locator = new ModbusLocator(1,
					RegisterRange.HOLDING_REGISTER, 1, DataType.FOUR_BYTE_BCD);
			res = master.getValue(locator);
			if (res != null) {
				String transformed = transformResult(res.toString());
				if (transformed != null) {
					long parsedResult = Long.parseLong(transformed, 16);
					if (parsedResult > MAX_DISPLAYED_VALUE) {
						displayedResult = (float) -(MAX_HEX_TO_DEC_VALUE - parsedResult) / 1000;
					} else {
						displayedResult = (float) (Long.parseLong(transformed, 16)) / 1000;
					}
					direction = (tempResult < displayedResult) ? Direction.DOWN : Direction.UP;
					if (tempResult == displayedResult) {
						direction = Direction.REST;
					}
					tempResult = displayedResult;
				}
				System.out.println("" + displayedResult);
			}
		
		} catch (ModbusTransportException mte) {
			mte.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		} finally {
			master.destroy();
		}
	}

	public String transformResult(String result) {
		StringBuffer value = new StringBuffer();
		value.append(result.substring(6, 8));
		value.append(result.substring(4, 6));
		value.append(result.substring(2, 4));
		value.append(result.substring(0, 2));

		return value.toString();
	}

	public Object getResponse() {
		return res;
	}

	public static float getValue() {
		return displayedResult;
	}

	public ModbusMaster getMaster() {
		return master;
	}

	public float getTempResult() {
		return tempResult;
	}

	public Direction getDirection() {
		return direction;
	}

}
