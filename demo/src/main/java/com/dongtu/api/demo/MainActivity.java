package com.dongtu.api.demo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import com.dongtu.sdk.Dongtu;
import com.dongtu.sdk.callback.DTSendImageListener;
import com.dongtu.sdk.model.DTImage;
import com.dongtu.sdk.visible.DTOutcomeListener;
import com.dongtu.sdk.visible.config.DTDefaultImageDetailConfigProvider;
import com.dongtu.sdk.visible.config.DTDefaultImageViewConfigProvider;
import com.dongtu.sdk.widget.DTEditText;
import com.dongtu.sdk.widget.DTImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * 首先从AndroidManifest.xml中取得appId和appSecret，然后进行初始化
         */
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            Dongtu.configure(this, bundle.getString("dt_app_id"), bundle.getString("dt_app_secret"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Dongtu.setUserInfo("-1", "Android Tester", null, "da_address", "da_mail", "da_phone", null);
        setContentView(R.layout.main);
        Dongtu.setImageDetailConfigProvider(new DTDefaultImageDetailConfigProvider() {
            @Override
            public int heightDP() {
                return 55;
            }
        });
        final DTEditText searchBox = findViewById(R.id.input);
        Dongtu.setupSearchPopupAboveView(findViewById(R.id.input_bar), searchBox);
        Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dongtu.showDongtuPopup(MainActivity.this);
            }
        });
        final ListView conversationView = findViewById(R.id.conversation);
        final ConversationAdaptor conversationAdaptor = new ConversationAdaptor();
        conversationView.setAdapter(conversationAdaptor);
        Dongtu.setSendImageListener(new DTSendImageListener() {
            @Override
            public void onSendImage(DTImage image) {
                conversationAdaptor.addMessage(image);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Dongtu.unload();
        super.onDestroy();
    }

    private class ConversationAdaptor extends BaseAdapter {
        private List<Pair<Boolean, DTImage>> mMessageList = new ArrayList<>();

        private void addMessage(DTImage image) {
            mMessageList.add(new Pair<>(true, image));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final FrameLayout layout;
            final DTImageView imageView;
            final FrameLayout.LayoutParams imageViewParams;
            final Pair<Boolean, DTImage> message = mMessageList.get(position);
            if (convertView instanceof FrameLayout) {
                layout = (FrameLayout) convertView;
                imageView = (DTImageView) layout.getChildAt(0);
                imageViewParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            } else {
                layout = new FrameLayout(MainActivity.this);
                ViewGroup.LayoutParams params = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                imageView = new DTImageView(MainActivity.this);
                imageView.config(new DTDefaultImageViewConfigProvider() {
                    @Override
                    public int tipText1() {
                        return Color.GREEN;
                    }
                });
                imageViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(imageViewParams);
                layout.addView(imageView);
            }
            imageViewParams.gravity = message.first ? Gravity.END : Gravity.START;
            Dongtu.loadImageInto(message.second.getImage(), message.second.getId(), imageView, message.second.getWidth() * 2, message.second.getHeight() * 2, new DTOutcomeListener() {
                @Override
                public void onSuccess() {
                    Log.i("conversation", "img load success");
                }

                @Override
                public void onFailure(int errorCode, String reason) {
                    Log.i("conversation", "img load failure");
                }
            });
            return layout;
        }
    }
}
