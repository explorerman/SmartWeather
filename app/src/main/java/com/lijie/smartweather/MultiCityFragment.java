package com.lijie.smartweather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lijie on 17/5/19.
 */

public class MultiCityFragment extends Fragment {


    @Bind(R.id.swiprefresh)
    SwipeRefreshLayout swiprefresh;
    @Bind(R.id.empty)
    LinearLayout emptyLayout;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;

    //private List<Weather> weather;
    //private MultiCityAdapter mAdapter;
    private View view;

    private Context mContext;
    //private SwipeMenuRecyclerView mMenuRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_multicity, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    /**
     * 没有使用rxfragment
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("我是第" + i + "个。");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));// 布局管理器。
        //recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        recyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        MenuAdapter menuAdapter = new MenuAdapter(dataList);
        menuAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(menuAdapter);
    }

//    private void initView() {
//        weather = new ArrayList<>();
//        mAdapter = new MultiCityAdapter(weather);
//        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerview.setAdapter(mAdapter);
//    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        int width = getResources().getDimensionPixelSize(R.dimen.item_height);

        // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        // 添加左侧的，如果不添加，则左侧不会出现菜单。
        {
            SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                    .setImage(R.mipmap.ic_action_add) // 图标。
                    .setWidth(width) // 宽度。
                    .setHeight(height); // 高度。
            //swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。
            swipeRightMenu.addMenuItem(addItem);

            SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                    .setBackgroundDrawable(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_close)
                    .setWidth(width)
                    .setHeight(height);

            //swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
            swipeRightMenu.addMenuItem(closeItem);
        }
    }
};
    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. item在Adapter中position。
         * @param menuPosition    menuPosition. 菜单的position。
         * @param direction       如果是左侧菜单，值是：{@link SwipeMenuRecyclerView#LEFT_DIRECTION}，
         *                        如果是右侧菜单，值是：{@link SwipeMenuRecyclerView#RIGHT_DIRECTION}.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
