package com.isoftstone.common.api.util;

import com.isoftstone.common.util.CommUtil;
import org.common.constant.CommonConstants;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

public class ArgsValidateUtils {
	public static String validateResult(String key, Object value, Object obj) {
		String messageStr = "";
		switch (key) {
		case CommonConstants.CHECK_TYPE_NOTNULL:
			if (StringUtils.isEmpty(obj)) {
				messageStr = "不能为空, ";
			}
			break;

		case CommonConstants.CHECK_TYPE_NULL:
			if (!StringUtils.isEmpty(obj)) {
				messageStr = "必须为空, ";
			}
			break;

		case CommonConstants.CHECK_TYPE_EMAIL:
			if (!StringUtils.isEmpty(obj)) {
				if (!CommUtil.checkEmail(obj.toString())) {
					messageStr = "邮件格式不正确, ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_PHONE:
			if (!StringUtils.isEmpty(obj)) {
				if (!CommUtil.isMobile(obj.toString())) {
					messageStr = "手机格式不正确, ";
				}
			}

			break;

		case CommonConstants.CHECK_TYPE_ID_CARD:
			if (!StringUtils.isEmpty(obj)) {
				if (!CommUtil.isIDCard(obj.toString())) {
					messageStr = "身份证格式不正确, ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_MAX:
			long maxValue = Long.parseLong(value.toString());
			if (obj == null) {
				messageStr = "不能超过最大值" + String.valueOf(maxValue) + ", ";
			} else {
				long curV = Long.parseLong(obj.toString());
				if (curV > maxValue) {
					messageStr = "不能超过最大值" + String.valueOf(maxValue) + ", ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_MIN:
			long minValue = Long.parseLong(value.toString());
			if (obj == null) {
				messageStr = "不能低于最小值" + String.valueOf(minValue) + ", ";
			} else {
				long curV = Long.parseLong(obj.toString());
				if (curV < minValue) {
					messageStr = "不能低于最小值" + String.valueOf(minValue) + ", ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_CORN:
			if (obj == null) {
				messageStr = "必须为corn表达式, ";
			} else if (!CronSequenceGenerator.isValidExpression(obj.toString())) {
				messageStr = "必须为corn表达式, ";
			}
			break;

		case CommonConstants.CHECK_TYPE_MIN_LENGTH:
			long minValue1 = Long.parseLong(value.toString());
			if (obj == null) {
				messageStr = "长度不能小于" + minValue1 + ", ";
			} else {
				long curV = obj.toString().length();
				if (curV < minValue1) {
					messageStr = "长度不能小于" + minValue1 + ", ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_MAX_LENGTH:
			long maxLeng = Long.parseLong(value.toString());
			if (obj == null) {
				messageStr = "长度不能大于" + maxLeng + ", ";
			} else {
				long curV = obj.toString().length();
				if (curV > maxLeng) {
					messageStr = "长度不能大于" + maxLeng + ", ";
				}
			}
			break;

		case CommonConstants.CHECK_TYPE_REGULAR: // 正则匹配
			// to-do
			break;
		default:
			break;
		}

		return messageStr;
	}
}
