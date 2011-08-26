package com.aiiiizu.tionz.sns;

import android.app.Service;

/**
 * <pre>
 * SNS�֏������ݏ��������{���钊�ۃN���X
 * </pre>
 * 
 * @author maguhiro
 */
public abstract class AbstractSNSWriter {
	// ==================================================
	// Fields
	/** �Ăяo������Service�I�u�W�F�N�g */
	protected Service service = null;

	// ==================================================
	// Constructors
	public AbstractSNSWriter(Service service) {
		if (service == null) throw new IllegalArgumentException("service is null");
		this.service = service;
	}

	// ==================================================
	// Methods
	/**
	 * �Ώۂ�SNS�ɏ����ݏ��������{���܂��B
	 * 
	 * @param content �����ޓ��e
	 */
	public abstract void write(String content);
}
