package com.chinaLife.claimAssistant.bean;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;

public class sc_GetText {
	private final static int CASE_STATUS_DELETED = 8; // 案件已删除
	private final static int CASE_STATUS_CANCEL = 4; // 案件已撤销
	private final static int CASE_STATUS_COMPLETE = 2; // 赔案已完成
	private final static int CASE_STATUS_DOING = 1; // 赔案正在进行
	private final static int CLAIM_STATUS_SERVICE_CHOICED = 1; // 客户已确认服务方式
	private final static int CLAIM_STATUS_SURVEYING = 2; // 客户正在进行自助查勘
	private final static int CLAIM_STATUS_SURVEIED = 4; // 客户自助查勘已完成，待后台审核
	private final static int CLAIM_STATUS_SURVEY_PASSED = 8; // 自助查勘审核通过
	private final static int CLAIM_STATUS_SURVEY_UNPASSED = 16; // 自助查勘审核不通过
	private final static int CLAIM_STATUS_CERT_REQUIRE = 32; // 缺少单证，需要客户补全单证
	private final static int CLAIM_STATUS_CERT_UPLOADED = 64; // 补全单证完成，待后台审核
	private final static int CLAIM_STATUS_CERT_PASSED = 128; // 单证审核通过
	private final static int CLAIM_STATUS_CERT_UNPASSED = 256; // 单证审核不通过
	private final static int CLAIM_STATUS_INDEMNITY_TO_CONFIRM = 512; // 后台核赔完成，待客户确认
	private final static int CLAIM_STATUS_INDEMNITY_REFUSE = 1024; // 客户不同意赔款金额
	private final static int CLAIM_STATUS_INDEMNITY_ACCEPT = 2048; // 客户同意赔款金额
	private final static int CLAIM_STATUS_PAYING = 4096; // 正在进行赔付
	private final static int CLAIM_STATUS_PAIED = 8192; // 赔付完成
	private final static int CLAIM_STATUS_COMPLETE = 16384; // 已结案
	private final static int CLAIM_STATUS_CANCEL = 32768; // 任务已撤销
	private final static int CLAIM_STATUS_DELETED = 65536; // 任务已删除
	private final static int CLAIM_STATUS_SUSPEND = 131072; // 任务已中止

	/*
	 * "理赔状态 1-客户已确认服务方式(客户可以进行自助查勘) 2-客户正在进行自助查勘 4-客户自助查勘已完成，待后台审核 8-自助查勘审核通过
	 * 16-自助查勘审核不通过 32-缺少单证，需要客户补全单证 64-补全单证完成，待后台审核 128-单证审核通过 256-单证审核不通过
	 * 512-后台核赔完成，待客户确认 1024-客户不同意赔款金额 2048-客户同意赔款金额 4096-正在进行赔付 8192-赔付完成
	 * 16384－已结案 32768-任务已撤销 65536-任务已删除"
	 */
	public static String getMsg1(int claimstatus, int casestatus) {
		String msg = "";
		if (claimstatus > 0) {
			if ((claimstatus & CLAIM_STATUS_SUSPEND) == CLAIM_STATUS_SUSPEND) {
				msg = "赔案任务已终止";
			} else if ((claimstatus & CLAIM_STATUS_DELETED) == CLAIM_STATUS_DELETED) {
				msg = "赔案任务已过期";
			} else if ((claimstatus & CLAIM_STATUS_COMPLETE) == CLAIM_STATUS_COMPLETE) {
				msg = "自助服务已完成，感谢您的使用。如有任何建议或意见，欢迎您拨打我司客服热线95519。有了您的支持，我们将做得更好!";
			} else if ((claimstatus & CLAIM_STATUS_CANCEL) == CLAIM_STATUS_CANCEL) {
				msg = "赔案已撤消";
			}
		}
		return msg;
	}

	public static String getMsg(int claimstatus, int casestatus,int servertype) {
		String msg = "";
		if (claimstatus > 0) {
			if ((claimstatus & CLAIM_STATUS_SUSPEND) == CLAIM_STATUS_SUSPEND) {
				msg = "赔案任务已终止";
			} else if ((claimstatus & CLAIM_STATUS_DELETED) == CLAIM_STATUS_DELETED) {
				msg = "赔案任务已过期";
			} else if ((claimstatus & CLAIM_STATUS_COMPLETE) == CLAIM_STATUS_COMPLETE) {
				if(servertype == 1){
					msg = "自助查勘已完成";
				}else{
					msg = "赔案处理完成";
				}
			} else if ((claimstatus & CLAIM_STATUS_CANCEL) == CLAIM_STATUS_CANCEL) {
				msg = "赔案已撤消";
			} else if ((claimstatus & CLAIM_STATUS_PAIED) == CLAIM_STATUS_PAIED) {
				msg = "赔付已完成,请注意查收";
			} else if ((casestatus & CLAIM_STATUS_PAYING) == CLAIM_STATUS_PAYING) {
				msg = "正在进行赔付";
			} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_TO_CONFIRM) == CLAIM_STATUS_INDEMNITY_TO_CONFIRM) {
				if ((claimstatus & CLAIM_STATUS_CERT_UNPASSED) == CLAIM_STATUS_CERT_UNPASSED) {
					msg = "单证审核不通过,请重新上传";
				} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_REFUSE) == CLAIM_STATUS_INDEMNITY_REFUSE) {
					msg = "不同意赔款金额，待保险公司处理";
				} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_ACCEPT) == CLAIM_STATUS_INDEMNITY_ACCEPT) {
					msg = "已接受赔款金额，待保险公司处理";
				} else {
					msg = "后台核赔完成，请确认赔款金额";
				}
			} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_TO_CONFIRM) != CLAIM_STATUS_INDEMNITY_TO_CONFIRM
					&& ((claimstatus & CLAIM_STATUS_INDEMNITY_REFUSE) == CLAIM_STATUS_INDEMNITY_REFUSE || (claimstatus & CLAIM_STATUS_INDEMNITY_ACCEPT) == CLAIM_STATUS_INDEMNITY_ACCEPT)) {
				if ((claimstatus & CLAIM_STATUS_CERT_UNPASSED) == CLAIM_STATUS_CERT_UNPASSED) {
					msg = "单证审核不通过,请重新上传";
				} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_REFUSE) == CLAIM_STATUS_INDEMNITY_REFUSE) {
					msg = "不同意赔款金额，待保险公司处理";
				} else if ((claimstatus & CLAIM_STATUS_INDEMNITY_ACCEPT) == CLAIM_STATUS_INDEMNITY_ACCEPT) {
					msg = "已接受赔款金额，待保险公司处理";
				} else {
					msg = "后台核赔完成，请确认赔款金额";
				}
			} else if ((claimstatus & CLAIM_STATUS_CERT_REQUIRE) == CLAIM_STATUS_CERT_REQUIRE) {
				if ((claimstatus & CLAIM_STATUS_CERT_PASSED) == CLAIM_STATUS_CERT_PASSED) {
					msg = "单证审核通过";
				} else if ((claimstatus & CLAIM_STATUS_CERT_UNPASSED) == CLAIM_STATUS_CERT_UNPASSED) {
					msg = "单证审核不通过,请重新上传";
				} else if ((claimstatus & CLAIM_STATUS_CERT_UPLOADED) == CLAIM_STATUS_CERT_UPLOADED) {
					msg = "单证上传完成，待保险公司审核";
				} else {
					msg = "缺少单证，请上传指定单证";
				}
			} else if ((claimstatus & CLAIM_STATUS_SURVEYING) == CLAIM_STATUS_SURVEYING) {
				if ((claimstatus & CLAIM_STATUS_SURVEY_UNPASSED) == CLAIM_STATUS_SURVEY_UNPASSED) {
					msg = "照片未审核通过，请重拍上传";
				} else if ((claimstatus & CLAIM_STATUS_SURVEY_PASSED) == CLAIM_STATUS_SURVEY_PASSED) {
					msg = "照片审核通过";
				} else if ((claimstatus & CLAIM_STATUS_SURVEIED) == CLAIM_STATUS_SURVEIED) {
					msg = "已完成现场拍照，待保险公司审核";
				} else {
					if(servertype == 1){
						msg = "正在进行自助查勘";
					}else{
						msg = "正在进行查勘定损";
					}
				}
			} else if ((claimstatus & CLAIM_STATUS_SERVICE_CHOICED) == CLAIM_STATUS_SERVICE_CHOICED) {
				msg = "已选择服务方式";
			} else {
				msg = "请点击［赔案处理］选择自助服务方式";
			}
		} else {
			/*
			 * 1－处理中 2－已结案 4－已撤销 8 - 已过期"
			 */
			if ((casestatus & CASE_STATUS_DELETED) == CASE_STATUS_DELETED) {
				msg = "赔案已过期";
			} else if ((casestatus & CASE_STATUS_CANCEL) == CASE_STATUS_CANCEL) {
				msg = "赔案已撤消";
			} else if ((casestatus & CASE_STATUS_COMPLETE) == CASE_STATUS_COMPLETE) {
				msg = "赔案已处理完成";
			} else if ((casestatus & CASE_STATUS_DOING) == CASE_STATUS_DOING) {
				msg = "赔案正在处理中";
			} else {
				msg = "请点击［赔案处理］选择自助服务方式";
			}
		}
		return msg;
	}

	// 判断是否活动的赔案
	public static boolean isCancle(int claimstatus, int casestatus) {
		boolean b = false;
		if (claimstatus > 0) {
			if ((claimstatus & CLAIM_STATUS_CANCEL) == CLAIM_STATUS_CANCEL)
				b = true;
		} else if ((casestatus & CASE_STATUS_CANCEL) == CASE_STATUS_CANCEL)
			b = true;
		return b;
	}

	// 判断是否活动的赔案
	public static boolean isActive(int claimstatus, int casestatus) {
		boolean b = true;
		if (casestatus == 0) {
			return true;
		}
		if (claimstatus > 0) {
			if ((claimstatus & CLAIM_STATUS_DELETED) == CLAIM_STATUS_DELETED
					|| (claimstatus & CLAIM_STATUS_COMPLETE) == CLAIM_STATUS_COMPLETE
					|| (claimstatus & CLAIM_STATUS_CANCEL) == CLAIM_STATUS_CANCEL
					|| (claimstatus & CLAIM_STATUS_SUSPEND) == CLAIM_STATUS_SUSPEND)
				b = false;
		} else if ((casestatus & CASE_STATUS_DELETED) == CASE_STATUS_DELETED
				|| (casestatus & CASE_STATUS_CANCEL) == CASE_STATUS_CANCEL
				|| (casestatus & CASE_STATUS_COMPLETE) == CASE_STATUS_COMPLETE)
			b = false;
		return b;
	}

	/**
	 * 判断按钮是否可点
	 * 
	 * @return
	 */
	public static boolean isOnclickBtn(long status, int step) {
		long[] statuses1 = new long[] { 4, 8 };
		long[] statuses2 = new long[] { 64, 128 };
		long[] statuses3 = new long[] { 1024, 2048 };
		long[] statuses4 = new long[] { 8192, 16384, 32768, 65536 };
		switch (step) {
		case 0:
			for (long s : statuses1) {
				if ((s & status) == s)
					return true;
			}
			return false;
		case 1:

			for (long s : statuses2) {
				if ((s & status) == s)
					return true;
			}
			return false;
		case 2:

			for (long s : statuses3) {
				if ((s & status) == s)
					return true;
			}
		case 3:

			for (long s : statuses4) {
				if ((s & status) == s)
					return true;
			}

		}
		return false;
	}

	public static int pareStatus(int state) {
		long status = state;
		if (status == 0) {
			return 0;
		}
		if (step4(status)) {
			return 3;
		} else if (step3(status)) {
			return 2;
		} else if (step2(status)) {
			return 1;
		} else if (step1(status)) {
			return 0;
		} else {
			return 0;
		}
	}

	/**
	 * 步骤1
	 * 
	 * @return
	 */
	public static boolean step1(long status) {
		long[] statuses = new long[] { 1, 2, 4, 8, 16 };
		for (long s : statuses) {
			if ((s & status) == s)
				return true;
		}
		return false;
	}

	/**
	 * 步骤2
	 * 
	 * @return
	 */
	public static boolean step2(long status) {
		long[] statuses = new long[] { 32, 64, 256 };
		for (long s : statuses) {
			if ((s & status) == s)
				return true;
		}
		return false;

	}

	/**
	 * 步骤3
	 * 
	 * @return
	 */
	public static boolean step3(long status) {
		long[] statuses = new long[] { 512, 1024, 2048, 4096 };
		for (long s : statuses) {
			if ((s & status) == s)
				return true;
		}
		return false;
	}

	/**
	 * 步骤4
	 * 
	 * @return
	 */
	public static boolean step4(long status) {
		long[] statuses = new long[] { 8192, 16384, 32768, 65536 };
		for (long s : statuses) {
			if ((s & status) == s)
				return true;
		}
		return false;

	}

}
