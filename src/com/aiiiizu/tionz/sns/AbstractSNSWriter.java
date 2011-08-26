package com.aiiiizu.tionz.sns;

import android.app.Service;

/**
 * <pre>
 * SNSへ書き込み処理を実施する抽象クラス
 * </pre>
 * 
 * @author maguhiro
 */
public abstract class AbstractSNSWriter {
	// ==================================================
	// Fields
	/** 呼び出し元のServiceオブジェクト */
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
	 * 対象のSNSに書込み処理を実施します。
	 * 
	 * @param content 書込む内容
	 */
	public abstract void write(String content);
}
