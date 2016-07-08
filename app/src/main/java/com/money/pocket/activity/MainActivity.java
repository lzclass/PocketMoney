package com.money.pocket.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.money.pocket.R;

import net.youmi.android.AdManager;
import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsEarnNotify;
import net.youmi.android.offers.PointsManager;

import org.xutils.view.annotation.ViewInject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PointsChangeNotify, PointsEarnNotify {
    @ViewInject(R.id.lv_task)
    private ListView lv_task;

    @ViewInject(R.id.tv_pointsBalance)
    /**
     * 显示积分
     */
    private TextView tv_pointsBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initUi();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始化ui<br>
     */
    private void initUi() {
        // (可选)关闭有米log输出，建议开发者在嵌入有米过程中不要关闭，以方便随时捕捉输出信息，出问题时可以快速定位问题
        // AdManager.getInstance(Context context).setEnableDebugLog(false);

        // 初始化接口，应用启动的时候调用，参数：appId, appSecret
        AdManager.getInstance(this).init("cfdbdd2786ea88ea", "d8edde7d10dd0073");

        // 如果开发者使用积分墙的服务器回调,
        // 1.需要告诉sdk，现在采用服务器回调
        // 2.建议开发者传入自己系统中用户id（如：邮箱账号之类的）（请限制在50个字符串以内）
        // 3.务必在下面的OffersManager.getInstance(this).onAppLaunch();代码之前声明使用服务器回调

        // OffersManager.getInstance(this).setUsingServerCallBack(true);
        // OffersManager.getInstance(this).setCustomUserId("user_id");

        // 如果使用积分广告，请务必调用积分广告的初始化接口:
        OffersManager.getInstance(this).onAppLaunch();

        // (可选)注册积分监听-随时随地获得积分的变动情况
        PointsManager.getInstance(this).registerNotify(this);

        // (可选)注册积分订单赚取监听（sdk v4.10版本新增功能）
        PointsManager.getInstance(this).registerPointsEarnNotify(this);
        // 查询积分余额
        // 从5.3.0版本起，客户端积分托管将由 int 转换为 float
        float pointsBalance = PointsManager.getInstance(this).queryPoints();
        tv_pointsBalance.setText("积分余额：" + pointsBalance);
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showYouMi();
                        break;
                }
            }
        });
    }

    private void showYouMi() {
        // 展示全屏的积分墙界面

        // 调用方式一：直接打开全屏积分墙
        // OffersManager.getInstance(this).showOffersWall();

        // 调用方式二：直接打开全屏积分墙，并且监听积分墙退出的事件onDestory
        OffersManager.getInstance(this).showOffersWall(new Interface_ActivityListener() {

            /**
             * 当积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
             */
            @Override
            public void onActivityDestroy(Context context) {
                Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();
            }
        });
//        PointsManager.getInstance(this).awardPoints(10.0f);
//        PointsManager.getInstance(this).spendPoints(20.0f);
    }

    /**
     * 退出时回收资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // （可选）注销积分监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerNotify(this)进行积分余额监听器注册，那这里必须得注销
        PointsManager.getInstance(this).unRegisterNotify(this);

        // （可选）注销积分订单赚取监听
        // 如果在onCreate调用了PointsManager.getInstance(this).registerPointsEarnNotify(this)
        // 进行积分订单赚取监听器注册，那这里必须得注销
        PointsManager.getInstance(this).unRegisterPointsEarnNotify(this);

        // 回收积分广告占用的资源
        OffersManager.getInstance(this).onAppExit();
    }

    /**
     * 检查广告配置
     */
    private void checkConfig() {
        StringBuilder sb = new StringBuilder();

        // 检查广告配置（如果没有使用文档中的<通过Receiver来获取积分订单>，那么就使用本方法来检查广告配置）
        boolean isCorrect = OffersManager.getInstance(this).checkOffersAdConfig();

        // 检查广告配置（如果使用了文档中的<通过Receiver来获取积分订单>，那么就使用本方法来检查广告配置）
        // boolean isCorrect = OffersManager.getInstance(this).checkOffersAdConfig(true);

        addTextToSb(sb, isCorrect ? "广告配置结果：正常" : "广告配置结果：异常，具体异常请查看Log，Log标签：YoumiSdk");
        addTextToSb(sb, "%s服务器回调", OffersManager.getInstance(this).isUsingServerCallBack() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏下载相关的通知", AdManager.isDownloadTipsDisplayOnNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏安装成功的通知",
                AdManager.isInstallationSuccessTipsDisplayOnNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s通知栏赚取积分的提示", PointsManager.isEnableEarnPointsNotification() ? "已经开启" : "没有开启");
        addTextToSb(sb, "%s积分赚取的Toast提示", PointsManager.isEnableEarnPointsToastTips() ? "已经开启" : "没有开启");

        new AlertDialog.Builder(this).setTitle("检查结果").setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }

                }).create().show();
    }

    /**
     * 格式化字符串
     */
    private void addTextToSb(StringBuilder sb, String format, Object... args) {
        sb.append(String.format(format, args));
        sb.append(System.getProperty("line.separator"));
    }

    /**
     * 积分余额发生变动时，就会回调本方法（本回调方法执行在UI线程中）
     * <p>
     * 从5.3.0版本起，客户端积分托管将由 int 转换为 float
     */
    @Override
    public void onPointBalanceChange(float pointsBalance) {
        tv_pointsBalance.setText("积分余额：" + pointsBalance);
    }

    /**
     * 积分订单赚取时会回调本方法（本回调方法执行在UI线程中）
     */
    @Override
    public void onPointEarn(Context arg0, EarnPointsOrderList list) {
        //		// 遍历订单并且toast提示
        //		for (int i = 0; i < list.size(); ++i) {
        //			EarnPointsOrderInfo info = list.get(i);
        //			Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
        //		}
    }
}
