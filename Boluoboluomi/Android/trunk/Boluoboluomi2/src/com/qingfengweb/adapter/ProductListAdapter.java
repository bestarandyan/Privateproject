/**
 * 
 */
package com.qingfengweb.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.id.biluomiV2.DetailProductActivity;
import com.qingfengweb.id.biluomiV2.ImagePreViewActivity;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.imagehandle.PicHandler;

/**
 * @author 刘星星 武国庆
 * @createDate 2013、8、27
 * 产品列表适配器
 *
 */
public class ProductListAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String,Object>> list;
	int width = MyApplication.getInstant().getWidthPixels()/5;
	int height = width;
	public ImageView imageView;
	public TextView couponTv;
	String couponid =  "";
	public ProductListAdapter(Context context, List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
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
		LayoutInflater layout = null;
		if(convertView == null){
			holder = new ViewHolder();
			layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.item_productlist, null);
			holder.rightLayout = (LinearLayout) convertView.findViewById(R.id.rightLayout);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.rightImg =  (ImageView) convertView.findViewById(R.id.rightImageView);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.couponTv = (TextView) convertView.findViewById(R.id.couponTv);
			RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(width, height);
			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map = list.get(position);
		String couponId = map.get("couponid").toString();
		if(couponId==null || couponId.equals("") || couponId.equals("0")){
			holder.rightLayout.setVisibility(View.GONE);
		}else{
			String couponUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL+couponId+".png";
			File file = new File(couponUrl);
			if(file.exists()){
				holder.rightImg.setImageResource(R.drawable.customize_xuanze);
				holder.couponTv.setText("查看优惠券");
				holder.couponTv.setTextColor(Color.WHITE);
			}
		}
		 String logo = map.get("imageid").toString();
			if(logo==null || logo.equals("")){//判断id是否存在 如果不存在  则直接设置默认图片
				holder.image.setImageResource(R.drawable.photolist_defimg);
			}else{//如果id存在
				String firstPhotoUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_PRODUCT_IMG_URL+logo+".png";
				if(new File(firstPhotoUrl).exists()){//判断此id的图片在本地是否存在  如果存在 则直接设置 
					holder.image.setImageBitmap(BitmapFactory.decodeFile(firstPhotoUrl));
				}else{//如果不存在 则去服务器下载
					holder.image.setImageResource(R.drawable.photolist_defimg);
					RequestServerFromHttp.downImage(context,holder.image,logo,ImageType.MerchanProductImg.getValue(),ImgDownType.ThumbBitmap.getValue(),
							 width+"",height+"",false,ConstantsValues.TREASURE_MERCHAN_PRODUCT_IMG_URL,R.drawable.photolist_defimg);
				}
			}
		holder.title.setText(map.get("name").toString());
		holder.content.setText(map.get("summary").toString());
		holder.rightLayout.setTag(position);
		holder.rightLayout.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				couponid = list.get((Integer) v.getTag()).get("couponid").toString();
				if(couponid!=null && !couponid.equals("")&& !couponid.equals("0")){
					imageView = (ImageView) v.findViewById(R.id.rightImageView);
					couponTv = (TextView) v.findViewById(R.id.couponTv);
					if(couponTv.getText().toString().trim().equals("查看优惠券")){
						String couponUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL+couponid+".png";
						Intent intent = new Intent(context,ImagePreViewActivity.class);
						intent.putExtra("url", couponUrl);
						context.startActivity(intent);
					}else{
						couponTv.setText("正在下载... ");
						couponTv.setTextColor(Color.BLACK);
						new Thread(runnable).start();
					}
					
				}
				
				
			}
		});
		convertView.setId(position);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,DetailProductActivity.class);
				Map<String,Object> map = list.get(v.getId());
				intent.putExtra("productInfo", (Serializable)map);
				context.startActivity(intent);
			}
		});
		
		
		return convertView;
	}
	
	public void downImage(Context context,ImageView imageView,String imgid,String image_type,
			String download_type,String width,String height,boolean isBackground,String imgUrl,int defId){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", RequestServerFromHttp.APPID));
		list.add(new BasicNameValuePair("appkey", RequestServerFromHttp.APPKEY));
		list.add(new BasicNameValuePair("action", RequestServerFromHttp.ACTION_DOWNLOAD));
		list.add(new BasicNameValuePair("imageid", imgid));
		list.add(new BasicNameValuePair("image_type",image_type));
		list.add(new BasicNameValuePair("download_type", download_type));
		list.add(new BasicNameValuePair("width", width));
		list.add(new BasicNameValuePair("height", height));
		Bitmap bmp = loadImageFromId(context, RequestServerFromHttp.INTERFACE_SYSTEM,list, imgUrl,200,200);
		if(bmp!=null){
			Message msg = new Message();
			msg.obj = imageView;
			msg.what = 0;
			handler.sendMessage(msg);
		}
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			downImage(context,imageView,couponid,ImageType.couponImg.getValue(),ImgDownType.BigBitmap.getValue(),
					 (width*5)+"",(height*5)+"",false,ConstantsValues.TREASURE_MERCHAN_COUPON_IMG_URL,R.drawable.photolist_defimg);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				((ImageView)(msg.obj)).setImageResource(R.drawable.customize_xuanze);
				couponTv.setText("查看优惠券");
				couponTv.setTextColor(Color.WHITE);
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 通过图片id下载图片
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	private  Bitmap loadImageFromId(Context context,String interfaceStr,List<NameValuePair> list,String toSD,final int imgW,final int imgH){
		Bitmap bitmap = null;
		String nameString = list.get(3).getValue()+".png";
		System.out.println("imageID--------------------------"+list.get(3).getValue());
		try {
			HttpPost httpPost = new HttpPost(interfaceStr);
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream isInputStream = entity.getContent();
//			long length =  entity.getContentLength();
//			if (length!=-1) {
//				byte[] imgData = new byte[(int) length];
//				byte[] temp = new byte[512];
//				int readLen = 0;
//				int destPos = 0;
//				while ((readLen = isInputStream.read(temp))>0) {
//					System.arraycopy(temp, 0, imgData, destPos, readLen);
//					destPos += readLen;
//				}
			bitmap = BitmapFactory.decodeStream(isInputStream);
//				bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
				File file = new File(FileUtils.SDPATH+toSD+nameString);
				if(bitmap!=null && !file.exists()){
					bitmap = PicHandler.scaleImg(bitmap, imgW,imgH);
					boolean b = PicHandler.OutPutImage(file,bitmap);
					System.out.println("图片存储========================"+(b?"成功":"失败"));
				}else if(bitmap == null && file.exists()){
					bitmap = PicHandler.getDrawable(file.getAbsolutePath(), imgW, imgH);
				}/*else if(bitmap ==null && !file.exists()){
					bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
				}*/
				isInputStream.close();
//			}
		} catch (IOException e) {
			/*if(defId!=0){
				bitmap = BitmapFactory.decodeResource(context.getResources(), defId);
			}*/
			return bitmap;
		}
		return bitmap;
	}
	class ViewHolder{
		ImageView image;
		TextView title;
		TextView content;
		ImageView rightImg;
		LinearLayout rightLayout;
		TextView couponTv;
	}
}
