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
 * @author ������
 * @createDate 2013��7��26
 * �ļ���ʾ��
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
	
	public static String suffix = ".doc;.docx;.pdf;.xls;.xlsx;.ppt;.pptx;.jpg;.png;.rar;.zip;.gz;.psd;.ai;";//��������򿪵��ļ���ʽ
	
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
		// ���漸�����ø��ļ����͵�ͼ�꣬ ��Ҫ���Ȱ�ͼ����ӵ���Դ�ļ���
		imagemap.put(sRoot, R.drawable.filedialog_root);	// ��Ŀ¼ͼ��
		imagemap.put(sParent, R.drawable.filedialog_folder_up);	//������һ���ͼ��
		imagemap.put(sFolder, R.drawable.filedialog_folder);	//�ļ���ͼ��
		imagemap.put("docx", R.drawable.qf_word);	//wav�ļ�ͼ��
		imagemap.put("doc", R.drawable.qf_doc);	//wav�ļ�ͼ��
		imagemap.put("pdf", R.drawable.qf_pdf);	//wav�ļ�ͼ��
		imagemap.put("xls", R.drawable.qf_xls);	//wav�ļ�ͼ��
		imagemap.put("xlsx", R.drawable.qf_excel);	//wav�ļ�ͼ��
		imagemap.put("ppt", R.drawable.qf_ppt1);	//wav�ļ�ͼ��
		imagemap.put("pptx", R.drawable.qf_ppt);	//wav�ļ�ͼ��
		imagemap.put("jpg", R.drawable.qf_jpg);	//wav�ļ�ͼ��
		imagemap.put("png", R.drawable.qf_png);	//wav�ļ�ͼ��
		imagemap.put("rar", R.drawable.qf_rar);	//wav�ļ�ͼ��
		imagemap.put("zip", R.drawable.qf_zip);	//wav�ļ�ͼ��
		imagemap.put("gz", R.drawable.qf_tar);	//wav�ļ�ͼ��
		imagemap.put("psd", R.drawable.qf_psd);	//wav�ļ�ͼ��
		imagemap.put("ai", R.drawable.qf_ai);	//wav�ļ�ͼ��
		suffix = suffix==null?"":suffix.toLowerCase();
		
		refreshFileList();
		
	}
	/**
	 * ��ȡ��׺����
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
	 * �����ļ����ͻ�ȡ�б�ǰ���ͼƬ������
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
	 * ˢ���б�
	 * @return
	 */
	private int refreshFileList()
	{
		// ˢ���ļ��б�
		File[] files = null;
		try{
			files = new File(path).listFiles();
		}catch(Exception e){
			files = null;
		}
		if(files==null){
			// ���ʳ���
			Toast.makeText(this, sOnErrorMsg,Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(list != null){
			list.clear();
		}else{
			list = new ArrayList<Map<String, Object>>(files.length);
		}
		
		// �����ȱ����ļ��к��ļ��е������б�
		ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();
		
		if(!this.path.equals(sRoot)){
			// ��Ӹ�Ŀ¼ �� ��һ��Ŀ¼
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
				// ����ļ���
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", file.getName());
				map.put("path", file.getPath());
				map.put("img", getImageId(sFolder));
				lfolders.add(map);
			}else if(file.isFile()){
				// ����ļ�
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
		
		list.addAll(lfolders); // ������ļ��У�ȷ���ļ�����ʾ������
		list.addAll(lfiles);	//������ļ�
		
		
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
		// ��Ŀѡ��
		String pt = (String) list.get(arg2).get("path");
		String fn = (String) list.get(arg2).get("name");
		int imageId = (Integer) list.get(arg2).get("img");
		if(fn.equals(sRoot) || fn.equals(sParent)){
			// ����Ǹ�Ŀ¼������һ��
			File fl = new File(pt);
			String ppt = fl.getParent();
			if(ppt != null){
				// ������һ��
				path = ppt;
				currentRootTv.setVisibility(View.VISIBLE);
				currentRootTv.setText(ppt);
			}else{
				// ���ظ�Ŀ¼
				path = sRoot;
				currentRootTv.setVisibility(View.GONE);
			}
		}
		else{
			File fl = new File(pt);
			if(fl.isFile()){
				// ���ûص��ķ���ֵ
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
				// ������ļ���
				// ��ô����ѡ�е��ļ���
				path = pt;
				currentRootTv.setVisibility(View.VISIBLE);
				currentRootTv.setText(pt);
			}
		}
		this.refreshFileList();
	}
	
}
