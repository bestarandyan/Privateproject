/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.qingfengweb.lottery.payment;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	public static final String DEFAULT_PARTNER = "2088601193198721";

	public static final String DEFAULT_SELLER = "mail@qingfengweb.com";

	public static final String PRIVATE = "MIICXgIBAAKBgQC8wNRm43ncITtlQfsa2XnXDfDhidfuO15Go35kIa/QCwE5l14fxskjazV5la71p0aor11qSbPTOSjoC7dOEiFcf2gMr5nZ6V4ad8K1O81arFFdQfvz3zmoAK2qo8dbC6HhAkor2s9In5BaVsdmtV4ugcluaLLb72TXJBA6zKXTHwIDAQABAoGARqGx2b6Nvz4AR+fk2ys+WKcy2HMIhnFY2f4cMcQZUjSMBM0Lv8BXTBW2MQU+L1alLkJRJpDl5cih8hNQSLqoGJw88FmvImK2XDzRnWXxyJxESmDhbUfE4hcm2Z1K/P+/WAbQ8/MJhA5hMZumxhnhZuC/EoyMtShf+KkKPODsXoECQQDlBsdSrXCAobpPdXxAiFaBvtCca5kTCH8GejqM92Qx5/NjYm+w1qwkmUzPovuz2zPg5Eg8L4mV6PFSI+QmCsiRAkEA0vvM1KP3UK1a0kRY7b/f5stvEmvWlrkYURVTEWC/KZmct/1ggwTwPfKrL56oi8SGxxShxc2rZtVzkVb19Hc4rwJBAKqPxMue3QT+7MiWP0W0A3C20ZdcTnCtSCRjzM0ExLqh+lGOko2JIkRTYq/Tuk30tosKl30zDez1wRqiHhvytfECQQCLNLpkPr9KSucWvyPR0ny/wdBKXj6a+gtyTFifqa7XmtQNja9lIxgEN539cHiSp5uptRI7+cSyt/lllvP3VcZfAkEAtUDeDc1VOu1x55rBkgHH/YTz4gwC8QsqCHKOr5P07yxOLqa93dbTbQM7YczrgPsLLvue1zqD+hX//V7VZ7IC1g==";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8wNRm43ncITtlQfsa2XnXDfDhidfuO15Go35kIa/QCwE5l14fxskjazV5la71p0aor11qSbPTOSjoC7dOEiFcf2gMr5nZ6V4ad8K1O81arFFdQfvz3zmoAK2qo8dbC6HhAkor2s9In5BaVsdmtV4ugcluaLLb72TXJBA6zKXTHwIDAQAB";

}
