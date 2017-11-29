package com.tcoj.theconnotationofjokes;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.tcoj.baselibrary.ExceptionCrashHandler;
import com.tcoj.baselibrary.dialog.AlertDialog;
import com.tcoj.baselibrary.fixbug.FixDexManager;
import com.tcoj.baselibrary.ioc.ViewByIdUtil;
import com.tcoj.framelibrary.BaseSkinActivity;
import com.tcoj.framelibrary.DefaultNavigationBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 主页面
 */
public class MainActivity extends BaseSkinActivity implements View.OnClickListener{

    private ImageView ss_iv;
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {
        //,(ViewGroup) findViewById(R.id.activity_main)
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar
                .DefaultBuilder(this)
                .setTitle("损色儿")
                .setRightText("更多")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"哈哈",Toast.LENGTH_SHORT).show();
                    }
                })
                .builder();
    }

    @Override
    protected void initView() {
        ss_iv = (ImageView) findViewById(R.id.ss_iv);
        ss_iv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        //fixBugByAli();

        //fixBugByDex();
    }

    /**
     * ali
     */
    private void fixBugByAli() {
        //把上次收集的崩溃信息上传到服务器
        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()){
            //上传到服务器
            //读取文件内容打印
            try {
                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
                char[] buffer = new char[1024];
                int len = 0;
                while ((len = fileReader.read(buffer))!=-1){
                    String result = new String(buffer,0,len);
                    Log.e("ss", result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.apatch");

        if (fixFile.exists()){
            try{
               // BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 自己的修复
     */
    private void fixBugByDex() {
        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.dex");

        if (fixFile.exists()){
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(MainActivity.this,"修复成功啦啦啦",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"修复失败哎",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.dialog_lyt)
                .setText(R.id.dialog_tv,"损色儿分享")
                .addDefaultAnimation()
                .fromBottom(true)
                .fullWidth()
                .show();
        final EditText editText = dialog.getView(R.id.dialog_et);
        dialog.setOnClickListener(R.id.dialog_iv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"损色儿"+editText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}