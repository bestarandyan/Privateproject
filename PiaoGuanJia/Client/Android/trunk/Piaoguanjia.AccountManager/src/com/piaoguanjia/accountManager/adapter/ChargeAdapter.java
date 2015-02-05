package com.piaoguanjia.accountManager.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.piaoguanjia.accountManager.CheckManagerActivity;
import com.piaoguanjia.accountManager.CheckManagerListActivity;
import com.piaoguanjia.accountManager.R;
import com.piaoguanjia.accountManager.bean.WayStatus;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 */
public class ChargeAdapter extends BaseAdapter {
	public ArrayList<HashMap<String, Object>> list;
	public Context context;
	public int type;

	public ChargeAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, int type) {
		this.context = context;
		this.list = list;
		this.type = type;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem2, null);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.tv_accounttype = (TextView) convertView
					.findViewById(R.id.tv_accounttype);
			holder.tv_accounttypetitle = (TextView) convertView
					.findViewById(R.id.tv_accounttypetitle);
			holder.tv_time1 = (TextView) convertView
					.findViewById(R.id.tv_time1);
			holder.tv_time2 = (TextView) convertView
					.findViewById(R.id.tv_time2);
			holder.tv_check01 = (TextView) convertView
					.findViewById(R.id.tv_check01);
			holder.tv_check02 = (TextView) convertView
					.findViewById(R.id.tv_check02);
			holder.tv_amount1 = (TextView) convertView
					.findViewById(R.id.tv_amount1);
			holder.tv_amount2 = (TextView) convertView
					.findViewById(R.id.tv_amount2);
			holder.linear = (LinearLayout) convertView
					.findViewById(R.id.linear);
			holder.linear_pro = (LinearLayout) convertView
					.findViewById(R.id.linear_pro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_username, tv_accounttype, tv_accounttypetitle,
				tv_time1, tv_time2, tv_check01, tv_check02, tv_amount1,
				tv_amount2;
		private LinearLayout linear, linear_pro;

		public void setContent(int position) {
			if (list.get(position).get("username") != null) {
				tv_username.setText(list.get(position).get("username")
						.toString());
			}

			if (type == CheckManagerActivity.TYPE_CHOGNZHI) {
				if (list.get(position).get("status") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("status").toString());
					linear.setVisibility(View.VISIBLE);
					tv_check01.setTextColor(Color.parseColor("#666666"));
					if (state == 1) {
						tv_check02.setText("审核通过");
					} else if (state == 2) {
						tv_check02.setText("审核不通过");
					} else if (state == 3) {
						tv_check02.setText("已取消");
					} else {
						linear.setVisibility(View.GONE);
					}
					if (list.get(position).get("audittime") != null) {
						tv_time2.setText(list.get(position).get("audittime")
								.toString());
					}
				}
				tv_amount2.setVisibility(View.VISIBLE);
				tv_amount1.setVisibility(View.GONE);
				// 充值方式
				if (list.get(position).get("type").toString() != null) {
					int id = Integer.parseInt(list.get(position).get("type")
							.toString());
					tv_amount1.setText(WayStatus.getName(id));
					tv_amount1.setTextColor(Color.BLUE);
					tv_amount1.setVisibility(View.VISIBLE);
				}
				tv_accounttypetitle.setText("账户类型:");
				if (list.get(position).get("accounttype") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("accounttype").toString());
					if (state == 2) {
						if (list.get(position).get("productname") != null) {
							tv_accounttype.setText("专用账户【"
									+ list.get(position).get("productname")
											.toString() + "】");
						} else {
							tv_accounttype.setText("专用账户");
						}
					} else {
						tv_accounttype.setText("总账户");
					}
				}
			} else if (type == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
				if (list.get(position).get("status") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("status").toString());
					linear.setVisibility(View.VISIBLE);
					tv_check01.setTextColor(Color.parseColor("#666666"));
					if ((state & 2) == 2) {
						tv_check02.setText("审核通过");
					} else if ((state & 4) == 4) {
						tv_check02.setText("审核不通过");
					} else {
						linear.setVisibility(View.GONE);
					}

					if (list.get(position).get("audittime") != null) {
						tv_time2.setText(list.get(position).get("audittime")
								.toString());
					}
				}
				tv_amount2.setVisibility(View.GONE);
				tv_amount1.setVisibility(View.GONE);
				tv_accounttypetitle.setText("产品名称:");
				String productname = "";
				String productid = "";
				if (list.get(position).get("productname") != null) {
					productname = list.get(position).get("productname")
							.toString();
				}
				if (list.get(position).get("productid") != null) {
					productid = list.get(position).get("productid").toString();
				}
				tv_accounttype.setText(productname + "【" + productid + "】");
				if (list.get(position).get("accounttype") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("accounttype").toString());
					if (state != 2) {
						linear_pro.setVisibility(View.GONE);
					}
				}
			} else {
				if (list.get(position).get("status") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("status").toString());
					linear.setVisibility(View.VISIBLE);
					tv_check01.setTextColor(Color.parseColor("#666666"));
					if ((state & 2) == 2) {
						tv_check02.setText("审核通过");
					} else if ((state & 4) == 4) {
						tv_check02.setText("审核不通过");
					} else {
						linear.setVisibility(View.GONE);
					}

					if (list.get(position).get("audittime") != null) {
						tv_time2.setText(list.get(position).get("audittime")
								.toString());
					}
				}
				tv_amount2.setVisibility(View.GONE);
				tv_amount1.setVisibility(View.VISIBLE);
				tv_accounttypetitle.setText("产品名称:");
				String productname = "";
				String productid = "";
				if (list.get(position).get("productname") != null) {
					productname = list.get(position).get("productname")
							.toString();
				}
				if (list.get(position).get("productid") != null) {
					productid = list.get(position).get("productid").toString();
				}
				tv_accounttype.setText(productname + "【" + productid + "】");
				if (list.get(position).get("accounttype") != null) {
					int state = Integer.parseInt(list.get(position)
							.get("accounttype").toString());
					if (state != 2) {
						linear_pro.setVisibility(View.GONE);
					}
				}
			}

			if (list.get(position).get("createtime") != null) {
				tv_time1.setText(list.get(position).get("createtime")
						.toString());
				tv_check01.setText("审核中");
			}

			if (list.get(position).get("amount") != null) {
				tv_amount2.setText("￥"
						+ list.get(position).get("amount").toString());
			}
			if (list.get(position).get("amount1") != null) {
				tv_amount1.setText("￥"
						+ list.get(position).get("amount1").toString());
			}

		}
	}
}
