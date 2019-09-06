package com.chen91apps.echos;

import android.net.Uri;
import android.os.Bundle;

import com.chen91apps.echos.fragments.ImageFragment;
import com.chen91apps.echos.fragments.VideoFragment;
import com.chen91apps.echos.utils.Configure;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.widget.TextView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity implements ImageFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener {

    private ArrayList<Fragment> listFrames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initToolBar();

        initContent();
    }

    private void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initContent()
    {
        TextView viewTitle = (TextView) findViewById(R.id.view_title);
        TextView viewContent = (TextView) findViewById(R.id.view_content);
        TextView viewSrc = (TextView) findViewById(R.id.view_src);
        TextView viewCategory = (TextView) findViewById(R.id.view_category);
        TextView viewAuthor = (TextView) findViewById(R.id.view_author);
        TextView viewTime = (TextView) findViewById(R.id.view_time);
        TextView viewUrl = (TextView) findViewById(R.id.view_url);

        String str = "2019年成都车展在中国西部国际博览城开幕，东风雷诺全球战略车型雷诺科雷缤全球首发，同时，首款欧系都市纯电SUV——雷诺 e诺也正式上市。\n" +
                "\n" +
                "从传统燃油车到最前沿新能源领域，东风雷诺将继续精准把握市场趋势和消费者需求，加速旗下产品产品扩军。\n" +
                "\n" +
                "成都车展上，东风雷诺全新车型科雷缤正式全球首发。在动力操控上，科雷缤搭载TCe 270 四缸涡轮增压发动机匹配7速湿式双离合变速箱，最大输出功率为115 kW，最大扭矩270 N·m，实现同级别最快的8.7秒百公里加速。\n" +
                "\n" +
                "该动力系统百公里综合油耗5.6L，以同级最低油耗实现了澎湃动力与高效节能之间的出色平衡。同时，科雷缤还为年轻消费者提供多感官驾驶模式。\n" +
                "\n" +
                "除了动力表现升级外，科雷缤还秉承雷诺“生命之花”设计语言，拥有LED C型灯组、8色最丰富颜色氛围灯和9.3英寸最大触控屏，并集多项智能科技、安全配置于一身。\n" +
                "\n" +
                "除了传统燃油车科雷缤外，本次东风雷诺还发布了旗下中国市场首款新能源产品——雷诺 e诺。雷诺 e诺共推出e趣型、e尚型、e智型和天猫精灵定制版、什马定制版五个版本，补贴后售价从6.18万元到7.18万元。\n" +
                "\n" +
                "为保证车辆安全，雷诺 e诺从研发、测试到生产都严格遵循雷诺全球统一质量标准，并历经多项严苛检测以保障产品品质。\n" +
                "\n" +
                "除了雷诺车型一贯的安全标准，在消费者最为关心的电池安全方面，雷诺 e诺采用雷诺独家电池保护强盾设计，以“双仓强盾”结合智能电池温控系统和国际最高电池过充标准，全方位保障电池安全。\n" +
                "\n" +
                "针对目前电动车充电难、充电慢的用户痛点，雷诺 e诺配置了多种充电模式，可兼容市场上超过90%的公用充电桩，同步支持家庭220V电源充电。\n" +
                "\n" +
                "7kW充电功率让雷诺 e诺快充30分钟可从30%电量充至80%，慢充4小时可从0电量充至100%电量。这不仅高效率满足了都市生活的快节奏，也实现了百公里低于9元的超低用车成本。\n" +
                "\n" +
                "此外，雷诺 e诺还搭载了同级领先的在线导航和娱乐、4G WIFI热点和语音识别等功能，支持以手机APP进行远程车载诊断和控制，让用户能实时互联，随心e生活。\n" +
                "\n" +
                "同时，为了进一步满足年轻消费者智能化需求，东风雷诺与天猫合作打造的雷诺 e诺天猫精灵定制版同步上市，并在车展现场为首位车主交付了钥匙。";

        viewContent.setText(str);

        listFrames = new ArrayList<>();
        listFrames.add(VideoFragment.newInstance("https://vjs.zencdn.net/v/oceans.mp4", ""));
        listFrames.add(ImageFragment.newInstance("", ""));
        listFrames.add(ImageFragment.newInstance("", ""));
        listFrames.add(ImageFragment.newInstance("", ""));

        ViewPager viewPager = (ViewPager) findViewById(R.id.image_viewpager);
        viewPager.setOffscreenPageLimit(listFrames.size());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return listFrames.get(position);
            }

            @Override
            public int getCount() {
                return listFrames.size();
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onImageFragmentInteraction(Uri uri) {

    }

    @Override
    public void onVideoFragmentInteraction(Uri uri) {

    }
}
