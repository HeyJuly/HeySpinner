package com.hey.lib;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

public class HeySpinner extends AppCompatTextView {

    private static final int MAX_LEVEL = 10000;

    private Drawable arrowDrawable;

    private ObjectAnimator arrowAnimator = null;

    private ListView listView;

    private PopupWindow popupWindow;

    private View referenceView;

    private HeySpinnerBaseAdapter adapter;


    public int textColor;
    public int arrowColor;
    public int dividerColor;
    public int dividerHeight;

    public int itemGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

    private OnSelectListener mSelectListener;

    private int displayHeight;

    private int parentVerticalOffset;

    public static final int VERTICAL_OFFSET = 1;

    private int dropDownListPaddingBottom = 5;

    private boolean showArrow;

    public int arrowRes;

    public int selectIndex;

    public int backgroundRes;

    public int popupBackground;

    public float popupHeight;

    public int itemPadding[];

    private int popupWidth;

    private ClickListener listener;

    public boolean tintColor = true;

    public HeySpinner(Context context) {
        super(context);
        init(context, null);
    }

    public HeySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }


    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void init(Context context, AttributeSet attrs) {
        initAttr(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (listener.onClick()) {
                        return;
                    }
                }
                if (isEnabled()) {
                    if (popupWindow != null && !popupWindow.isShowing()) {
                        showDropDown();
                    } else {
                        dismissDropDown();
                    }
                }
            }
        });
        referenceView = this;
        listView = new ListView(context);
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setFooterDividersEnabled(true);
        listView.setDivider(new ColorDrawable(dividerColor));
        listView.setDividerHeight(dividerHeight);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                if (popupWindow != null && popupWindow.isShowing()) {
                    setText(adapter.getItem(position).toString());
                    popupWindow.dismiss();
                }
                if (mSelectListener != null) {
                    mSelectListener.onSelect(position);
                }
            }
        });

        popupWindow = new PopupWindow();
        popupWindow.setContentView(listView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        if (popupBackground != 0) {
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, popupBackground));
        } else {
            popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                animateArrow(false);
            }
        });
        measureDisplayHeight();

    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeySpinner);
        textColor = typedArray.getColor(R.styleable.HeySpinner_itemTextColor, Color.BLACK);
        arrowColor = typedArray.getColor(R.styleable.HeySpinner_arrowColor, Color.BLACK);
        dividerColor = typedArray.getColor(R.styleable.HeySpinner_dividerColor, Color.WHITE);
        dividerHeight = typedArray.getDimensionPixelSize(R.styleable.HeySpinner_dividerHeight, 2);
        showArrow = typedArray.getBoolean(R.styleable.HeySpinner_showArrow, true);
        arrowRes = typedArray.getResourceId(R.styleable.HeySpinner_arrowSrc, R.drawable.arrow);
        backgroundRes = typedArray.getResourceId(R.styleable.HeySpinner_itemBackground, 0);
        popupBackground = typedArray.getResourceId(R.styleable.HeySpinner_popupBackground, 0);
        itemGravity = typedArray.getInteger(R.styleable.HeySpinner_itemGravity, Gravity.CENTER);
        tintColor = typedArray.getBoolean(R.styleable.HeySpinner_tintColor, true);
        popupHeight = typedArray.getFloat(R.styleable.HeySpinner_popupHeight, 1f);
        typedArray.recycle();
        if (showArrow) {
            arrowDrawable = initArrowDrawable(arrowColor);
            setIncludeFontPadding(false);
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], arrowDrawable, getCompoundDrawables()[3]);
        }
        itemPadding = new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
    }

    private Drawable initArrowDrawable(int drawableTint) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), arrowRes);
        if (drawable != null && tintColor) {
            drawable = DrawableCompat.wrap(drawable);
            if (drawableTint != Integer.MAX_VALUE && drawableTint != 0) {
                DrawableCompat.setTint(drawable, drawableTint);
            }
        }
        return drawable;
    }


    public void animateArrow(boolean shouldRotateUp) {
        if (arrowDrawable != null) {
            int start = shouldRotateUp ? 0 : MAX_LEVEL;
            int end = shouldRotateUp ? MAX_LEVEL : 0;
            arrowAnimator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
            arrowAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            arrowAnimator.start();
        }
    }


    public void setItemGravity(int gravity) {
        itemGravity = gravity;
    }

    private void showDropDown() {
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        animateArrow(true);
        measurePopUpDimension();
        popupWindow.showAsDropDown(referenceView);
    }

    private void dismissDropDown() {
        popupWindow.dismiss();
    }


    private void measureDisplayHeight() {
        displayHeight = (int) (getContext().getResources().getDisplayMetrics().heightPixels * popupHeight);
    }


    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(referenceView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(getPopUpHeight(), MeasureSpec.AT_MOST);
        listView.measure(widthSpec, heightSpec);
        popupWindow.setWidth(referenceView.getMeasuredWidth());
        popupWindow.setHeight(listView.getMeasuredHeight() + dropDownListPaddingBottom);
    }


    private int getPopUpHeight() {
        return Math.max(verticalSpaceBelow(), verticalSpaceAbove());
    }

    private int verticalSpaceAbove() {
        return getParentVerticalOffset();
    }

    private int verticalSpaceBelow() {
        return displayHeight - getParentVerticalOffset() - getMeasuredHeight();
    }

    private int getParentVerticalOffset() {
        if (parentVerticalOffset > 0) {
            return parentVerticalOffset;
        }
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        return parentVerticalOffset = locationOnScreen[VERTICAL_OFFSET];
    }


    public void setAdapter(HeySpinnerBaseAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
        setSelectIndex(0);
    }

    public HeySpinnerBaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * dp
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setItemPadding(int left, int top, int right, int bottom) {
        this.itemPadding = new int[]{dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)};
    }

    public void attachData(List<String> data) {
        if (data != null && !data.isEmpty()) {
            if (adapter != null) {
                adapter.setData(data);
            } else {
                adapter = new HeySpinnerDefaultAdapter(getContext(), data, textColor, itemGravity, itemPadding, backgroundRes);
            }
        }
        listView.setAdapter(adapter);
        setSelectIndex(0);
    }

    public void setSelectIndex(int position) {
        if (adapter != null) {
            this.selectIndex = position;
            setText(adapter.getItem(position).toString());
            if (mSelectListener != null) {
                mSelectListener.onSelect(position);
            }
        }
    }

    public int dp2px(int dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public int getSelectIndex() {

        return selectIndex;
    }

    public interface OnSelectListener {
        void onSelect(int position);
    }

    public interface ClickListener {
        /**
         * @return if return true will not show items
         */
        boolean onClick();
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.mSelectListener = listener;
    }


    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }


    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        if (showArrow) {
            arrowDrawable = initArrowDrawable(arrowColor);
            setIncludeFontPadding(false);
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], arrowDrawable, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        }
    }


    public int getPopupWidth() {
        return popupWidth;
    }

    public void setPopupWidth(int popupWidth) {
        this.popupWidth = popupWidth;
    }


    public void setReferenceView(View view) {
        this.referenceView = view;
    }
}
