/**
 * 
 */
package com.qingfengweb.client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013、7、26
 * 文件显示类
 *
 */
@SuppressLint("DefaultLocale")
public class FileListViewActivity extends Activity implements OnItemClickListener{
	public static String tag = "OpenFileDialog";
	static final public String sRoot = "/"; 
	static final public String sParent = "..";
	static final public String sFolder = ".";
	static final public String sEmpty = "";
	static final private String sOnErrorMsg = "No rights to access!";
	private String path = sRoot;
	private List<Map<String, Object>> list = null;
	private int dialogid = 0;
	
	public static String suffix = ".doc;.docx;.pdf;.xls;.xlsx;.ppt;.pptx;.jpg;.png;.rar;.zip;.gz;.psd;.ai;";//定义容许打开的文件格式
	
	private Map<String, Integer> imagemap = null;
	
	private ListView fileListView;
	private TextView currentRootTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_filelistview);
		fileListView =  (ListView) findViewById(R.id.fileListView);
		currentRootTv = (TextView) findViewById(R.id.currentRoot);
		fileListView.setOnItemClickListener(this);
		imagemap = new HashMap<String, Integer>();
		// 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
		imagemap.put(sRoot, R.drawable.filedialog_root);	// 根目录图标
		imagemap.put(sParent, R.drawable.filedialog_folder_up);	//返回上一层的图标
		imagemap.put(sFolder, R.drawable.filedialog_folder);	//文件夹图标
		imagemap.put("docx", R.drawable.qf_word);	//wav文件图标
		imagemap.put("doc", R.drawable.qf_doc);	//wav文件图标
		imagemap.put("pdf", R.drawable.qf_pdf);	//wav文件图标
		imagemap.put("xls", R.drawable.qf_xls);	//wav文件图标
		imagemap.put("xlsx", R.drawable.qf_excel);	//wav文件图标
		imagemap.put("ppt", R.drawable.qf_ppt1);	//wav文件图标
		imagemap.put("pptx", R.drawable.qf_ppt);	//wav文件图标
		imagemap.put("jpg", R.drawable.qf_jpg);	//wav文件图标
		imagemap.put("png", R.drawable.qf_png);	//wav文件图标
		imagemap.put("rar", R.drawable.qf_rar);	//wav文件图标
		imagemap.put("zip", R.drawable.qf_zip);	//wav文件图标
		imagemap.put("gz", R.drawable.qf_tar);	//wav文件图标
		imagemap.put("psd", R.drawable.qf_psd);	//wav文件图标
		imagemap.put("ai", R.drawable.qf_ai);	//wav文件图标
		suffix = suffix==null?"":suffix.toLowerCase();
		
		refreshFileList();
		
	}
	/**
	 * 获取后缀名称
	 * @param filename
	 * @return
	 */
	private String getSuffix(String filename){
		int dix = filename.lastIndexOf('.');
		if(dix<0){
			return "";
		}else{
			return filename.substring(dix+1);
		}
	}
	/**
	 * 根据文件类型获取列表前面的图片的种类
	 * @param s
	 * @return
	 */
	private int getImageId(String s){
		if(imagemap == null){
			return 0;
		}else if(imagemap.containsKey(s)){
			return imagemap.get(s);
		}else if(imagemap.containsKey(sEmpty)){
			return imagemap.get(sEmpty);
		}else {
			return 0;
		}
	}
	/**
	 * 刷新列表
	 * @return
	 */
	private int refreshFileList()
	{
		// 刷新文件列表
		File[] files = null;
		try{
			files = new File(path).listFiles();
		}catch(Exception e){
			files = null;
		}
		if(files==null){
			// 访问出错
			Toast.makeText(this, sOnErrorMsg,Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(list != null){
			list.clear();
		}else{
			list = new ArrayList<Map<String, Object>>(files.length);
		}
		
		// 用来先保存文件夹和文件夹的两个列表
		ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();
		
		if(!this.path.equals(sRoot)){
			// 添加根目录 和 上一层目录
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", sRoot);
			map.put("path", sRoot);
			map.put("img", getImageId(sRoot));
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", sParent);
			map.put("path", path);
			map.put("img", getImageId(sParent));
			list.add(map);
		}
		
		for(File file: files)
		{
			if(file.isDirectory() && file.listFiles()!=null){
				// 添加文件夹
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", file.getName());
				map.put("path", file.getPath());
				map.put("img", getImageId(sFolder));
				lfolders.add(map);
			}else if(file.isFile()){
				// 添加文件
				String sf = getSuffix(file.getName()).toLowerCase();
				if(suffix == null || suffix.length()==0 || (sf.length()>0 && suffix.indexOf("."+sf+";")>=0)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", file.getName());
					map.put("path", file.getPath());
					map.put("img", getImageId(sf));
					lfiles.add(map);
				}
			}  
		}
		
		list.addAll(lfolders); // 先添加文件夹，确保文件夹显示在上面
		list.addAll(lfiles);	//再添加文件
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.filelistitem, new String[]{"img", "name"}, new int[]{R.id.filedialogitem_img, R.id.filedialogitem_name});
		fileListView.setAdapter(adapter);
		return files.length;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// 条目选择
		String pt = (String) list.get(arg2).get("path");
		String fn = (String) list.get(arg2).get("name");
		int imageId = (Integer) list.get(arg2).get("img");
		if(fn.equals(sRoot) || fn.equals(sParent)){
			// 如果是更目录或者上一层
			File fl = new File(pt);
			String ppt = fl.getParent();
			if(ppt != null){
				// 返回上一层
				path = ppt;
				currentRootTv.setVisibility(View.VISIBLE);
				currentRootTv.setText(ppt);
			}else{
				// 返回更目录
				path = sRoot;
				currentRootTv.setVisibility(View.GONE);
			}
		}
		else{
			File fl = new File(pt);
			if(fl.isFile()){
				// 设置回调的返回值
				Bundle bundle = new Bundle();
				bundle.putString("path", pt);
				bundle.putString("name", fn);
				bundle.putInt("imageId", imageId);
				Intent intent = new Intent();
				intent.putExtra("file", bundle);
				setResult(2, intent);
				finish();
				return;
			}else if(fl.isDirectory()){
				// 如果是文件夹
				// 那么进入选中的文件夹
				path = pt;
				currentRootTv.setVisibility(View.VISIBLE);
				currentRootTv.setText(pt);
			}
		}
		this.refreshFileList();
	}
	
}
