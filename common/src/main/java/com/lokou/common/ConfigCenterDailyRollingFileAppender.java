package com.lokou.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 日志自定义append，能按照日期和文件大小进行切换
 * 
 * @author
 * @create 2016年11月16日
 * 
 */
public class ConfigCenterDailyRollingFileAppender extends RollingFileAppender {
	private String datePattern = "'.'yyyy-MM-dd";
	private String dateStr = null;
	private static final int maxShortErrLine = 5;

	public String getDatePattern() {
		return datePattern;
	}

	public void rollOver() {
		File target;
		File file;
		if (qw != null) {
			LogLog.debug("rolling over count="
					+ ((CountingQuietWriter) qw).getCount());
		}
		LogLog.debug("maxBackupIndex=" + maxBackupIndex);

		// 如果maxBackups<=0则不需要重命名文件
		if (maxBackupIndex > 0) {
			// 删除旧的文件
			file = new File(fileName + dateStr + '.' + maxBackupIndex);
			if (file.exists())
				file.delete();
			// 将文件序号滚动重命名加1
			for (int i = maxBackupIndex - 1; i >= 1; i--) {
				file = new File(fileName + dateStr + "." + i);
				if (file.exists()) {
					target = new File(fileName + dateStr + '.' + (i + 1));
					LogLog.debug("Renaming file " + file + " to " + target);
					file.renameTo(target);
				}
			}

			// 将当前写的文件重命名为文件名.日期.1
			target = new File(fileName + dateStr + "." + 1);
			this.closeFile(); // 关闭文件，以确保重命名成功
			file = new File(fileName);
			LogLog.debug("Renaming file " + file + " to " + target);
			file.renameTo(target);
		}

		try {
			this.setFile(fileName, false, bufferedIO, bufferSize);
		} catch (IOException e) {
			LogLog.error("setFile(" + fileName + ", false) call failed.", e);
		}
	}

	/*
	 * protected void subAppend(LoggingEvent event) { if (dateStr == null) {
	 * dateStr = new SimpleDateFormat(getDatePattern()).format(new Date(
	 * System.currentTimeMillis())); }
	 * 
	 * if ((fileName != null) && (((CountingQuietWriter) qw).getCount() >=
	 * maxFileSize) || !(dateStr.equals(new SimpleDateFormat(getDatePattern())
	 * .format(new Date(System.currentTimeMillis()))))) { this.rollOver(); if
	 * (!dateStr.equals(new SimpleDateFormat(getDatePattern()) .format(new
	 * Date(System.currentTimeMillis())))) { dateStr = new
	 * SimpleDateFormat(getDatePattern()) .format(new
	 * Date(System.currentTimeMillis())); } } super.subAppend(event); }
	 */

	@Override
	protected void subAppend(LoggingEvent event) {
		if (dateStr == null) {
			this.layout = new PatternLayout("[%d] [%-p] [%c{2}] message:[%m]%n");
			dateStr = new SimpleDateFormat(getDatePattern()).format(new Date(
					System.currentTimeMillis()));
		}

		if ((fileName != null)
				&& (((CountingQuietWriter) qw).getCount() >= maxFileSize)
				|| !(dateStr.equals(new SimpleDateFormat(getDatePattern())
						.format(new Date(System.currentTimeMillis()))))) {
			this.rollOver();
			if (!dateStr.equals(new SimpleDateFormat(getDatePattern())
					.format(new Date(System.currentTimeMillis())))) {
				dateStr = new SimpleDateFormat(getDatePattern())
						.format(new Date(System.currentTimeMillis()));
			}
		}
		String logContent = this.layout.format(event);
		String header = logContent.substring(0, logContent.indexOf("message"));
		String body = logContent.substring(logContent.indexOf("message"));
		logContent = header
				+ body;
		if (layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null && s.length > 0) {
				logContent = logContent.replace("\n", "").replace("\r", "");
				logContent += " errinfo:[";
				int len = s.length >= maxShortErrLine ? maxShortErrLine
						: s.length;
				for (int i = 0; i < len; i++) {
					logContent += s[i];
				}
				logContent += "]\r\n";
			}

		}

		this.qw.write(logContent);

		if (layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				for (int i = 0; i < len; i++) {
					this.qw.write(s[i]);
					this.qw.write(Layout.LINE_SEP);
				}
			}
		}

		if (this.immediateFlush) {
			this.qw.flush();
		}

		if ((fileName != null)
				&& ((CountingQuietWriter) qw).getCount() >= maxFileSize)
			this.rollOver();
	}
}
