package com.algaworks.pedidovenda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

	/**
	 * @param originalDate
	 * @return
	 */
	public String format(Date originalDate) {
		String formatted;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		formatted = sdf.format(originalDate);
		return formatted;
	}
}