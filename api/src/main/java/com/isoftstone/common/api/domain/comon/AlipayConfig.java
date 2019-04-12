package com.isoftstone.common.api.domain.comon;

public class AlipayConfig {
	   // 商户appid
		/*public static String APPID = "2018093061503950";
		// 私钥 pkcs8格式的
		public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9ZtwJ3gn7XRR3aFw4OJtIp9/LkjpVJFawg0RPk4xJ4cdKcKJGJ4cFE7P2f+FJyFHexc9/DLNU/jUrWFZdtAjZ+wXZ3I2GQ0R+371niOdZc0DHQtcHB1fcuZXQiw5xghgkzSAOqJr3tWqjMparoQRxchYFTYjYWawD1yyLgTdXpNSLmyf6kSQBdE4ypVm1ld5MulYMOhcP+0pxz27Uc6L0ymyotXStYFAFGPLM/87Ox/qLNrJCdX/iEbRiEgrkbSsFvHtpCIAbYaRHVPbFEdgwUUxsp5XawS5mpDpZWsmZqkLDfiIwW0Kv0WyiT72TTPUco7xZ7Xzwq3yx3eGe4hOtAgMBAAECggEARfs2fDLdb1ICGOingmJwBdV5XEdoZEMsAMZPvDNhV38j2r5pRxCd0+Nm0EKJ5HAux+hA9cmkjVfsH1rhdnfp+VjF3ayfYieCrHpRCmtMSAHMwpNQzhkJftXXpBltOcV+1EIygRgNQk922lv7zeiNsrkbp9PxJzdi1nxvyg/xeeEbgL1c5Yo0FiK7zYLX9py988utou3cgfu8RxwVEimO1IswNx5hyvLGAA/kEKNM6QX5yTxupEUUudo2fFvgVk6Q3TI3d2kvpEZlRsD2F18Av/vMY3UPuWyjctm3OOO0N21vVXVyUCfUrYH6DyiRQ51lo4JKpQijPIC0V75dUZeMoQKBgQDmTOJh+TZ7P25t3egkffPM/3MfIrxLH5hjBFWkXS8WxkFXERjdLOwhnIJShb9Xu5u20bY2Be/BC0uxlRCuYHFri1scQt3jHUODP0h+MJ0tSHGgARUUQT26Xss8kiB0aY5+kNLvhq5NCd8Sp86SCpVNg97Fix1OZMxLyJ3zU07gOQKBgQDSiZq4of53EOxjUKQSxOCofxDA9wH2cJ8j6VVAlXRiwoAcfhL3i2aAcCi3+Z442EyC0485Ua4LusIov4L8xiIXNOlBsxVymB542eOXRExS34mHfVl9YFYkprXxyzbafQXtYHgCyV5sF8dUA7d5Rz+z2cQ7piaFKTRRb7JJfUknFQKBgFgwSOBLlllLyPW5bBPNR3bQuzzJCGGcD7abClCOfmGyOYMlwOexNBsK27zOCp+F7/LqC2RUkaLSuIGgG2sq80PDcXg5As7dfeTcbt4jr4yPmTM0NuYytXiQpDLlohfx8+bzflLKuCQ4C3wRVkEGXHX6cWwkAWzrnG/IuvNai3HBAoGBAK9+C/4C6f2Nmb1fJqSOf0V0RR7TxksJrV1dzbY73fvEbM1TObQl+pbs5sQTyhNQGNakSipZL16PmfAlUqYo8ZmufRBKmoIe0qeh19frEkOyGQ1bKxEbEDMU02xtG5gbxqkLVH3eJFqtp3Ucc/8fqLXdu3iNEmbE5dtL46zmRefRAoGAWx14AYjgtRUpPZdqLRM7542zTnKaDx+jWT5ODtRSKSsLyM6LHNoK4qy+6qi/eB4Yqgw/7EmD3OpRtVpE7Y51C/g8T3DCg8OhcJlLrq6Aa3WeJ+CuTSvmjHt4/MoI+i1KRYfl9HqcnisRzX0wHZBofquqZJwGQP6HBl4kuvHlmxw=";
		// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
		//public static String notify_url = "http://tour.v3ws.com/payali/notify";
		
		public static String notify_url = "http://tour.v3ws.com:82/index.php/api/Alipay/notify_url";
		// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
		public static String return_url = "http://tour.v3ws.com/h5/member/default.html";
		// 请求网关地址
		public static String URL = "https://openapi.alipay.com/gateway.do";
		// 编码
		public static String CHARSET = "UTF-8";
		// 返回格式
		public static String FORMAT = "json";
		// 支付宝公钥                                                                               MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjq0jQmSuj+9VxOK6QqxBY7uXdURAsBTzT/HUB9p3lYR+jz8BlNJnOZNhCKOm+3N3n+LxEW7bdDZJof8BLQ5kV+Dlp+evYGdCPd2WRRSGlxMEWBVE/8hOBF509SLZcKESv9YuUGEPpC1I9I9Ip/liHLNsetA5ZS7B6URjR/hDL9m7lN3QqVJmYxbOejqqvyx7gnFaTnS3LbN2qH1vALVLsKqyjZs6xoXSd1BJgbN4hGtYzmIrG821pOer7dvE+3ECm3nqZ/l/cMKd7brlJzVStKSmXFFd++kbUWGsSx22TY5WNUxA+GtX+wAczu49NV7zIMHaMDYECrXgXptw89cE8QIDAQAB
		public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjq0jQmSuj+9VxOK6QqxBY7uXdURAsBTzT/HUB9p3lYR+jz8BlNJnOZNhCKOm+3N3n+LxEW7bdDZJof8BLQ5kV+Dlp+evYGdCPd2WRRSGlxMEWBVE/8hOBF509SLZcKESv9YuUGEPpC1I9I9Ip/liHLNsetA5ZS7B6URjR/hDL9m7lN3QqVJmYxbOejqqvyx7gnFaTnS3LbN2qH1vALVLsKqyjZs6xoXSd1BJgbN4hGtYzmIrG821pOer7dvE+3ECm3nqZ/l/cMKd7brlJzVStKSmXFFd++kbUWGsSx22TY5WNUxA+GtX+wAczu49NV7zIMHaMDYECrXgXptw89cE8QIDAQAB";
		// 日志记录目录
		public static String log_path = "/log";
		// RSA2
		public static String SIGNTYPE = "RSA2";*/
	// 商户appid
			public static String APPID = "2019011162882263";
			// 私钥 pkcs8格式的
			public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7LCmk+5yWnl9gDeSN3HQFVapybPTCjMO2DbCdowQWdO5xmpojo840vpEJXJ5YufR10pI1DHo9Y7cuxNsrKS/aMxlsK9dIBqva/r8iAkrFULZGoPgbxJwIIGZgqD6TFs/6UQixweTed//f9oCb5DLw3/d4oE0/x/GFPanGkstqoLdQa5levrj0MZkmP9gtApji/uJTAv1skTgbm3936/PpXtLKDURdJTZEt9zPWaMj6FfGjEROT1Hg3zcoba6Rkpp4KghcXbPVZEgccJFDKNTX6Qn+k4upfjjmWJnapGtOtpuF53Uj5tF16Ng+IuxNEDu1P4aBcMoEXMqv/F8WVJBtAgMBAAECggEBALXpayQVAxCEJfAGMJ8LpptUsg76BDxdcQ0sav4aZKuqw+AXwT1oQ86RdAbkRGW5fXVRzZXPZbLqiivjWZuuMPrh9RBj35z6FucF9h+ImMEC+9TTN7BNOtTPIJFKk16Dqmj/fvK8nsL1X/N88WwRsszW7/8F1W5q1jqIPsxm+VrL4sSZmHjIWCBCYpOKm/0ZdeU5Gmi1kjJZepK70TQOckLvWTPgnIPJTW33Pf+2tVFExrppcpQMMOoc7Wv5bUeotZpeTmW3xwRgN2Nw0WqK8w+lGtz3bURubCpPYWP8afxnKcBBBG1c3QSXAEFdv+0Q/NEPpZkbT66EQK32QTKoFQECgYEA89UdAIlOoV7nFKrF5ZpHtgoAoQmjJcpZ6mMLwTE0cdjwNgucze9IhZbwIRxmKeXzI08QrAN8scvVVXJa3ArJOmK/lFAzwXM9ewiOIkb7OH3/nnnA3AZKZUnQbMnZqLedfs6dhkrTtbPwSf4UwIXW/8igXr5JMmOLAhXXSORgbEECgYEAxIM8P9Dlnt6WGjVKBFrl7WELqhY5AndSJt/96Zx7ozFKTHXct4mv+egfodoecC/cvpBorDmsiCzO+pUHVai6eatVq9RJ89BIkiqSYW9fqbFCZ/IN7GdWoUz+pVRKXUWpUZxBxHHAymVp66hrsxZ5PVSwEsRqwuEGlRqV0b3DSS0CgYASIzksEAqsUE3qKKXd5XMOJIJVeE0Ng5baq5F1Rn67NULxEj4Qx4rifQ7Rt7YVycgjST9ZPE0rQJlW6NG/aoH7eNr9YlXnPvZEz6ca/6KyWhUIqc/hzgn8rNf3L7CkLzxk8WOgWLKU4pSuPFs40vviPP9I7Oiu+d6eQheokWxmwQKBgGQhwud5mCdVoAKGuMpzTsmgM4Ndg3sgNbodVJJe7BkBmqxPOkJDZGngv42HSQ89NCveBZgMYz+YJXdFzND3At3QUf0WVQBpVzEzhBcl2wbqjb3Ndw7IsTnLs82mn6XbQyWUi2Z/2A7JkilETmyYvPplVrqZXxUh+Gqtez2+he2ZAoGAZUWxcbAugLGZEeZ+wbXe9kZblYFI+1uuZBddR9M+WsJPPlg/FmeFRBeWybD3DPEw9SGBvKcJcs8NovxOXaZn6B6RqVkpjxZCnbm1kgcPnwv9DfUy5ox05/AKb+RXaixpqULKmLg2CdkGNKI7varsbAjoNmACElcNT77nCOn6zQo=";
			// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
			public static String notify_url = "http://tour.v3ws.com/index.php/api/Alipay/notify_url";
			// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
			public static String return_url = "http://tour.v3ws.com/h5/member/default.html";
			// 请求网关地址
			public static String URL = "https://openapi.alipay.com/gateway.do";
			// 编码
			public static String CHARSET = "UTF-8";
			// 返回格式
			public static String FORMAT = "json";
			// 支付宝公钥
			public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm00d5eYv8EzwlQkLlqesm5RvxkTXUNECjs7QyzhIE5V6rSckpsUbJaMriALl0rLWu+jPuCcHq/1YfaHYoqnkT78U0FgWOH6nAs3yfsTUTZ6aiC/lI2JO1pl88UDJGMi4UT238JbwLcwgcH5gw5NTtoC8AopS9FVANY3LwEDg+XUHPsxEOXFpc61sK0BR4knjGSf4Z/wBwQnvuL69/KsnooDziwtc4MF/BAP0IdZetmrCFe88txq6jlL9aZgttEMZYEfyMVtNp0D8Y7YEvhBNBGBvp5QVzap8B6ujPe0byFSIhzTLwhJOpLe+s9WonTUWXU64n2OL02Ze09w/EZW+zQIDAQAB";
			// 日志记录目录
			public static String log_path = "/log";
			// RSA2
			public static String SIGNTYPE = "RSA2";
}
