package com.redpig.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.redpig.common.utils.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeDeserialize extends JsonDeserializer<Date> {
	private static final Logger LOG = Logger.getLogger(JsonDateTimeDeserialize.class);
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String text = jp.getText();
		if (Utils.isNotBlank(text)) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Utils.trim(text));
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}
}
