package com.example.leesnriud.myucrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PICTURE = 1001;
    private static final int REQUEST_SELECT_PICTURE = 1002;
    //
    public static final String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .getAbsolutePath() + File.separator + "Camera";
    //文件路径
    public static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String appPath = basePath + File.separator + "lee";
    public static final String imagePath = appPath + File.separator + "image";

    private String photoPath;
    private String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_camera, R.id.bt_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_choose:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "camera"), REQUEST_SELECT_PICTURE);
                break;
            case R.id.bt_camera:
                String destinationFileName = "lee_" + CommonUtils.getTimeStamp() + ".jpg";
                photoPath = new File(cameraPath, destinationFileName).getAbsolutePath();
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File(cameraPath, destinationFileName));
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(photoIntent, REQUEST_TAKE_PICTURE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(this, "无法裁剪", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Log.e("111","123");
            } else if (requestCode == REQUEST_TAKE_PICTURE) {
                startCropActivity(Uri.fromFile(new File(photoPath)));
            }
            if (resultCode == UCrop.RESULT_ERROR) {
                Log.e("111","456");
            }
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "sst_" + CommonUtils.getTimeStamp() + ".jpg";
        File imageDir = new File(imagePath);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        imagepath = new File(imagePath, destinationFileName).getAbsolutePath();
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(imagePath, destinationFileName)));
        //动态设置图片的宽高比 1:1
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);

    }


    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        //options.setCompressionFormat开始设置
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //设置裁剪图片的质量
        options.setCompressionQuality(90);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);
        //uCrop.withOptions结束设置
        return uCrop.withOptions(options);
    }

}
