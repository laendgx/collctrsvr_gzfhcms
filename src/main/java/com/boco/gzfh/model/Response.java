package com.boco.gzfh.model;

import java.io.ByteArrayOutputStream;

import com.boco.gzfh.utils.CoderUtils;

public class Response {
	/**
	 * 设备地址
	 */
	private int addr;

	/**
	 * 帧类型
	 */
	private int frameType;

	/**
	 * 数据
	 */
	private byte[] data;

	/**
	 * 检验码
	 */
	private byte[] checkCrc;

	/**
	 * 构造函数
	 * @param addr
	 * @param data
	 * @param checkCrc
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public Response(byte[] addr, byte[] frameType, byte[] data, byte[] checkCrc) throws NumberFormatException, Exception {
		super();
		this.addr = Integer.parseInt(new String(addr,"GBK"));
		this.frameType = Integer.parseInt(new String(frameType,"GBK"));;
		this.data = data;
		this.checkCrc = checkCrc;
	}

	/**
	 * @return the 设备地址
	 */
	public int getAddr() {
		return addr;
	}

	/**
	 * @return the 帧类型
	 */
	public int getFrameType() {
		return frameType;
	}

	/**
	 * 将设备返回的数据转换为字符串，并返回
	 * @return
	 */
	public String getData(){
		if (this.data == null){
			return null;
		}
		String result = new String(this.data);
		return result;
	}

	/**
	 * 以字节数组形式，返回接收到的数据
	 * @return
	 */
	public byte[] getDataArray(){
		return this.data;
	}

	/**
	 * 判断是否符合校验
	 * @return
	 */
	public boolean isChecked(){
		if (this.data == null || this.checkCrc == null || this.checkCrc.length < 2){
			return false;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			String myAddr = String.format("%02d", this.addr);
			String frameType = String.format("%02d", this.frameType);

			baos.write(myAddr.getBytes("GBK"));
			baos.write(frameType.getBytes("GBK"));
			baos.write(this.data);
			byte[] buffer = baos.toByteArray();

			short newCheckValue = CoderUtils.gen_crc(buffer, buffer.length);
			short oldCheckValue = (short)(this.checkCrc[0] << 8 + this.checkCrc[1]);

			if (newCheckValue == oldCheckValue){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}
}
