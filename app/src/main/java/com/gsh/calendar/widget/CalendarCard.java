package com.gsh.calendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.gsh.calendar.R;
import com.gsh.calendar.data.CalendarOneScreenDataMonth;
import com.gsh.calendar.data.CalendarOneScreenDataWeek;
import com.gsh.calendar.data.DayData;
import com.gsh.calendar.util.DateUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * @author Damon Salvatore
 * @ClassName: CalendarCard
 * @Description: 日历的card 周视图和月视图两种模式
 * @email shaohua.guo@raiing.com
 */
public class CalendarCard extends View {

    private final String TAG = "CalendarCard";

    /**
     * 选中的sell
     */
    private Cell selectedSell;

    /**
     * 日历中 "日" 数字的大小(动态生成)
     */
    private float dayTextSize = 0;

    /**
     * 点击事件的阀值
     */
    private int touchSlop;

    /**
     * 该View所需要的数据集 月数据
     */
    public CalendarOneScreenDataMonth dataMonth;


    /**
     * 该View所需要的数据集 周数据
     */
    public CalendarOneScreenDataWeek dataWeek;

    /**
     * 日历的高度
     */
    private int height = 0;

    /**
     * 日历的宽度
     */
    private int width = 0;

    /**
     * 每个表格的边长(表格为正方形)
     */
    private float cellLength = 0;

    /**
     * 行数组
     */
    private Row[] rows;


    //******************* paint ***********************

    /**
     * "日" 黑 白 灰 画笔
     */
    private Paint paintDayText;


    /**
     * "事件标记圆点"的画笔
     */
    private Paint paintEventSign;

    /**
     * "日" 选中的空心圆的画笔
     */
    private Paint paintDaySelected;

    /**
     * 排卵日 实心圆的画笔
     */
    private Paint paintOvulation;

    /**
     * 月经期和易孕期 背景色块儿 画笔
     */
    private Paint paintPeriod;

    /**
     * "日" 上方文字的画笔
     */
    private Paint paintTopText;

    /**
     * action_down:X轴的位置
     */
    private float mDownX;
    /**
     * action_down:Y轴的位置
     */
    private float mDownY;

    /**
     * cell的点击事件监听器
     */
    private OnCellClickListener onCellClickListener;

    /**
     * true 月视图模式
     * false 轴视图模式
     */
    private boolean monthType = true;


    public CalendarCard(Context context, CalendarOneScreenDataMonth dataMonth) {
        super(context);
        if (dataMonth == null) {
            throw new RuntimeException("参数异常,dataMonth为null");
        }
        init(dataMonth, null);
    }

    public CalendarCard(Context context, CalendarOneScreenDataWeek dataWeek) {
        super(context);
        if (dataWeek == null) {
            throw new RuntimeException("参数异常,dataWeek为null");
        }
        init(null, dataWeek);
    }


    private void init(CalendarOneScreenDataMonth dataMonth, CalendarOneScreenDataWeek dataWeek) {
        monthType = dataMonth != null;
        this.dataMonth = dataMonth;
        this.dataWeek = dataWeek;
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setBackgroundColor(getResources().getColor(R.color.color_c3));
        initSomeSize();
        initCell();
    }

    private void initCell() {
        rows = new Row[monthType ? CalendarConstant.TOTAL_ROW_MONTH : CalendarConstant.TOTAL_ROW_WEEK];
        for (int j = 0; j < rows.length; j++) {
            Row mRow = new Row(j);
            rows[j] = mRow;
            for (int i = 0; i < Calendar.SATURDAY; i++) {
                Cell mCell = new Cell(i + 1, j);
                rows[j].cells[i] = mCell;
            }
        }
    }

    private void initPaint() {
        paintDayText = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paintDayText.setColor(Color.parseColor(CalendarConstant.DAY_TEXT_COLOR_BLACK));
        paintDayText.setColor(getResources().getColor(R.color.color_f2));
        paintDayText.setTextSize(dayTextSize);

        paintTopText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTopText.setColor(getResources().getColor(R.color.color_f7));
//        paintTopText.setColor(Color.parseColor(CalendarConstant.DAY_TEXT_COLOR_GRAY));
        paintTopText.setTextSize(cellLength * 0.2f);

        paintPeriod = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPeriod.setStyle(Paint.Style.FILL);

        paintOvulation = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOvulation.setStyle(Paint.Style.FILL);
//        paintOvulation.setColor(Color.parseColor(CalendarConstant.OVULATION_BACKGROUND_COLOR));
        paintOvulation.setColor(getResources().getColor(R.color.color_ovulation));

        paintEventSign = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintEventSign.setStyle(Paint.Style.FILL);
//        paintEventSign.setColor(Color.parseColor(CalendarConstant.EVENT_SIGN_COLOR));
        paintEventSign.setColor(getResources().getColor(R.color.color_event_sign));

        paintDaySelected = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    /**
     * 初始化一些尺寸
     */
    private void initSomeSize() {
        width = getResources().getDisplayMetrics().widthPixels;
        int row = monthType ? CalendarConstant.TOTAL_ROW_MONTH : CalendarConstant.TOTAL_ROW_WEEK;
        height = (width / CalendarConstant.TOTAL_COL + 1) * row;
        initSomeSize0(height, width);
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                height = getHeight();
//                width = getWidth();
//                initSomeSize0(height, width);
//            }
//        });
    }


    private void initSomeSize0(int height, int width) {
//        Log.d(TAG, "height-->>" + height + ", width-->>" + width);
        float t = monthType ? CalendarConstant.TOTAL_ROW_MONTH : 1;
        cellLength = height / t;
        dayTextSize = cellLength * 0.3f;
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int j = 0; j < rows.length; j++) {
            if (rows[j] != null) {
                rows[j].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / cellLength);
                    int row = (int) (mDownY / cellLength);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的单元格
     *
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= CalendarConstant.TOTAL_COL || row >= CalendarConstant.TOTAL_ROW_MONTH)
            return;
        int week = col + 1;
        selectedSell = new Cell(
                week, row);
        rows[row].cells[col] = selectedSell;
        DayData dayData;
        int index;
        if (monthType) {
            Map<Integer, DayData> map = dataMonth.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        } else {
            Map<Integer, DayData> map = dataWeek.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        }
        Log.d(TAG, "点击的单元格为-->>" + dayData.toString() + ", index-->>" + index + ", row-->>" + row);
        if (dayData.isClickable()) {
            // 刷新界面
            update(row, week, dayData);
            if (onCellClickListener != null) {
                onCellClickListener.clickDate(dayData, index, row);
            }
        }
    }

    /**
     * 更新界面
     */
    private void update(int row, int week, DayData dayData) {
        int position = getPosition(row, week);


        if (monthType) {
            dataMonth.setSelectedPosition(position);
        } else {
            dataWeek.setSelectedPosition(position);
        }
        if (monthType) {
            //更新月视图
            updateMonth(dayData);
        } else {
            //更新周视图
            updateWeek(week);
        }

    }

    private void updateMonth(DayData dayData) {

        CollapseScrollView collapseScrollView = (CollapseScrollView) getParent().getParent().getParent();

        int itemPositionCurr = collapseScrollView.calendarViewPagerMonth.getCurrentItem();

        int realPositionPre = (itemPositionCurr - 1);
        int realPositionNext = (itemPositionCurr + 1);
        Log.d(TAG, "unix-->>月份当前Index-->>" + itemPositionCurr);

        if (realPositionPre >= 0) {
            CalendarCard calendarCardPre = collapseScrollView.viewsMonth[realPositionPre];
            CalendarOneScreenDataMonth dataPre = calendarCardPre.dataMonth;
            Calendar calendarPre = Calendar.getInstance();
            calendarPre.setTimeInMillis(dayData.getUnix() * 1000L);
            calendarPre.add(Calendar.MONTH, -1);
            int unixPre = (int) (calendarPre.getTimeInMillis() / 1000L);
            int positionPre = DateUtil.getArrFromUnix(unixPre, dataPre.getMapDay());
            Log.d(TAG, "unix-->>unixPre-->>" + unixPre + ", positionPre-->>" + positionPre);
            if (positionPre != -1) {
                dataPre.setSelectedPosition(positionPre);
                calendarCardPre.invalidate();
            }
        } else {
            Log.e(TAG, "updateMonth, pre越界了-->>" + realPositionPre);
        }
        if (realPositionNext < collapseScrollView.childCountMonth) {
            CalendarCard calendarCardNext = collapseScrollView.viewsMonth[realPositionNext];
            CalendarOneScreenDataMonth dataNext = calendarCardNext.dataMonth;
            Calendar calendarNext = Calendar.getInstance();
            calendarNext.setTimeInMillis(dayData.getUnix() * 1000L);
            calendarNext.add(Calendar.MONTH, 1);
            int unixNext = (int) (calendarNext.getTimeInMillis() / 1000L);
            int positionNext = DateUtil.getArrFromUnix(unixNext, dataNext.getMapDay());
            Log.d(TAG, "unix-->>unixNext-->>" + unixNext + ", positionNext-->>" + positionNext);
            if (positionNext != -1) {
                dataNext.setSelectedPosition(positionNext);
                calendarCardNext.invalidate();
            }
        } else {
            Log.e(TAG, "updateMonth, next越界了-->>" + realPositionNext);
        }

        invalidate();
    }

    private void updateWeek(int week) {
        CollapseScrollView collapseScrollView = (CollapseScrollView) getParent().getParent();
        int itemPositionCurr = collapseScrollView.calendarViewPagerWeek.getCurrentItem();
        int weekChildCount = collapseScrollView.childCountWeek;
        Log.e(TAG, "updateWeek, itemPositionCurr-->>" + itemPositionCurr);
        int realPositionPre = (itemPositionCurr - 1);
        int realPositionNext = (itemPositionCurr + 1);
        if (realPositionPre >= 0) {
            CalendarCard calendarCard = collapseScrollView.viewsWeek[realPositionPre];
                int weekReal = week;
            if (realPositionPre == 0) {
                CalendarOneScreenDataWeek dataWeek = calendarCard.dataWeek;
                long unixRespected = dataWeek.getMapDay().get(week).getUnix();
                if (unixRespected < collapseScrollView.viewPagerRealLeft) {
                    weekReal = DateUtil.getWeek(collapseScrollView.viewPagerRealLeft);
                }
                Log.w(TAG, "updateWeek, pre,第一周,选中星期-->>" + weekReal);
            }

            calendarCard.dataWeek.setSelectedPosition(weekReal);
            calendarCard.invalidate();
        } else {
            Log.e(TAG, "updateWeek, pre越界了-->>" + realPositionPre);
        }
        if (realPositionNext < collapseScrollView.childCountWeek) {
            CalendarCard calendarCard = collapseScrollView.viewsWeek[realPositionNext];
            int weekReal = week;
            if (realPositionNext == (weekChildCount - 1)) {
                CalendarOneScreenDataWeek dataWeek = calendarCard.dataWeek;
                long unixRespected = dataWeek.getMapDay().get(week).getUnix();
                if (unixRespected > collapseScrollView.viewPagerRealRight) {
                    weekReal = DateUtil.getWeek(collapseScrollView.viewPagerRealRight);
                }
                Log.w(TAG, "updateWeek, next,最后一周,选中星期-->>" + weekReal);
            }
            calendarCard.dataWeek.setSelectedPosition(weekReal);
            calendarCard.invalidate();
        } else {
            Log.e(TAG, "updateWeek, next越界了-->>" + realPositionNext);
        }
        invalidate();
//        for (int i = 0; i < CollapseScrollView.VIEWPAGER_SIZE; i++) {
//            CalendarCard calendarCard = collapseScrollView.viewsWeek[i];
//            calendarCard.dataWeek.setSelectedPosition(week);
//            calendarCard.invalidate();
//        }
    }

    public interface OnCellClickListener {
        /**
         * @param data  被选中的 CustomDate数据
         * @param index
         */
        void clickDate(DayData data, int index, int row);

    }

    /**
     * 绘制行
     */
    private class Row {
        /**
         * j: 行
         **/
        public int j;

        public Cell[] cells;

        Row(int j) {
            this.j = j;
            cells = new Cell[CalendarConstant.TOTAL_COL];
        }

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < Calendar.SATURDAY; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }

    }

    /**
     * 绘制每个单元格
     */
    private class Cell {

        /**
         * week: 星期
         **/
        public int week;

        /**
         * j: 行
         **/
        public int j;

        public Cell(int week, int j) {
            this.week = week;
            this.j = j;
        }

        /**
         * 绘制顺序
         * 1.cell的行经期和易孕期的色块儿背景
         * 2.排卵日色块儿
         * 3."日"上方文字
         * 4."日"下方圆点
         * 5.选中天的圆圈
         * 6."日"字符
         *
         * @param canvas canvas
         */
        public void drawSelf(Canvas canvas) {

            DayData dayData;
            if (monthType) {
                dayData = dataMonth.getMapDay().get(getPosition(j, week));
            } else {
                dayData = dataWeek.getMapDay().get(getPosition(j, week));

            }
            //********1.绘制cell的行经期和易孕期的色块儿背景
            int dayType = dayData.getDayType();

            switch (dayType) {
                case CalendarConstant.DAY_TYPE_MENS_ING:
                    paintPeriod.setColor(getResources().getColor(R.color.color_mens));
                    drawCellCenter(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_MENS_START:
                    paintPeriod.setColor(getResources().getColor(R.color.color_mens));
                    drawCellLeft(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_MENS_END:
                    paintPeriod.setColor(getResources().getColor(R.color.color_mens));
                    drawCellRight(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_EASY_ING:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    drawCellCenter(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_EASY_START:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    drawCellLeft(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_EASY_END:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    drawCellRight(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_MENS_ONLYONE:
                    paintPeriod.setColor(getResources().getColor(R.color.color_mens));
                    drawCellOnlyOne(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_EASY_ONLYONE0:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    drawCellOnlyOne(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_EASY_ONLYONE1:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    //********2.排卵日色块儿
                    drawCellOnlyOneOvulate(canvas);
                    break;
                case CalendarConstant.DAY_TYPE_OVULATE:
                    paintPeriod.setColor(getResources().getColor(R.color.color_c1));
                    //********2.排卵日色块儿
                    drawOvulate(canvas);
                    break;
            }
            //********3."日"上方文字
            drawTopText(canvas, dayData);
            //********4."日"下方圆点
            drawBottomCircle(canvas, dayData);

            //********5.选中天的圆圈
            drawSelectedCircle(canvas, dayData);
            //********6."日"字符
            drawText(canvas, dayData);
        }

        /**
         * 绘制选中的空心圆环
         *
         * @param canvas
         */
        private void drawSelectedCircle(Canvas canvas, DayData dayData) {
            int currentKey = getPosition(j, week);
            int selectedPosition = monthType ? dataMonth.getSelectedPosition() : dataWeek.getSelectedPosition();
            if (currentKey == selectedPosition) {
                paintDaySelected.setStyle(Paint.Style.FILL);
                paintDaySelected.setColor(getResources().getColor(R.color.color_day_selected_solid));
                canvas.drawCircle((week - 1 + 1 / 2f) * cellLength, (j + 1 / 2f) * cellLength, (1 / 2f - CalendarConstant.BACKGROUND_DELTA0) * cellLength, paintDaySelected);
                if (dayData.getDayType() == CalendarConstant.DAY_TYPE_SAFE) {
                    //只有安全期才绘制外层的圆环.
                    paintDaySelected.setStyle(Paint.Style.STROKE);
                    paintDaySelected.setStrokeWidth(4);
                    paintDaySelected.setColor(getResources().getColor(R.color.color_day_selected_border));
                    canvas.drawCircle((week - 1 + 1 / 2f) * cellLength, (j + 1 / 2f) * cellLength, (1 / 2f - CalendarConstant.BACKGROUND_DELTA0) * cellLength, paintDaySelected);
                }
            }
        }

        /**
         * 绘制下方事件标记的圆点
         *
         * @param canvas
         * @param dayData
         */
        private void drawBottomCircle(Canvas canvas, DayData dayData) {
            if (dayData.isHasCircleBottom()) {
                canvas.drawCircle((week - 1 + 0.5f) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0 / 2) * cellLength, CalendarConstant.EVENT_SIGN_RADIUS, paintEventSign);
            }
        }


        /**
         * 绘制上方的字符
         *
         * @param canvas
         * @param dayData
         */
        private void drawTopText(Canvas canvas, DayData dayData) {
            //绘制上方的字符
            long unix = dayData.getUnix();
            int topType = dayData.getTopType();
            String content;
            float textWidth;
            float textX;
            float deltaBaseLine = 0.24f;
            switch (topType) {
                case CalendarConstant.TOP_TYPE_TODAY:
                    content = getResources().getString(R.string.today);
                    textWidth = paintTopText.measureText(content);
                    textX = (week - 1 + 0.5f) * cellLength - textWidth / 2f;
                    canvas.drawText(content, textX, (j + deltaBaseLine) * cellLength - paintTopText.measureText(
                            content, 0, 1) / 2, paintTopText);
                    break;
                case CalendarConstant.TOP_TYPE_FIRST_DAY:
                    content = DateUtil.unix2Month(unix,getContext());
                    textWidth = paintTopText.measureText(content);
                    textX = (week - 1 + 0.5f) * cellLength - textWidth / 2f;
                    canvas.drawText(content, textX, (j + deltaBaseLine) * cellLength - paintTopText.measureText(
                            content, 0, 1) / 2, paintTopText);
                    break;

                default:
                    //don't forget default
                    break;
            }

        }

        private void drawText(Canvas canvas, DayData dayData) {
            int dayType = dayData.getDayType();
            if (!dayData.isClickable()) {
                paintDayText.setColor(getResources().getColor(R.color.color_f7));
            } else {
                if (dayType == CalendarConstant.DAY_TYPE_SAFE) {
                    paintDayText.setColor(getResources().getColor(R.color.color_f2));
                } else {
                    paintDayText.setColor(getResources().getColor(R.color.color_c3));
                }
            }

            int currentKey = getPosition(j, week);
            int selectedPosition = monthType ? dataMonth.getSelectedPosition() : dataWeek.getSelectedPosition();
            if (currentKey == selectedPosition) {
                paintDayText.setColor(getResources().getColor(R.color.color_f2));
            }

            int day = dayData.getDay();
            String content = String.valueOf(day);
            float textWidth = paintDayText.measureText(content);
            float textX = (week - 1 + 0.5f) * cellLength - textWidth / 2f;
            canvas.drawText(content, textX,
                    (j + 0.7f) * cellLength - paintDayText.measureText(
                            content, 0, 1) / 2, paintDayText);
        }

        private void drawCellOnlyOneOvulate(Canvas canvas) {
            drawCellOnlyOne(canvas);
            drawOvulateCircle(canvas);
        }


        /**
         * 绘制排卵日
         *
         * @param canvas
         */
        private void drawOvulate(Canvas canvas) {
            RectF rectF0;
            rectF0 = new RectF((week - 1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, week * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawRect(rectF0, paintPeriod);
            drawOvulateCircle(canvas);
        }

        private void drawOvulateCircle(Canvas canvas) {
            //绘制实心圆
            canvas.drawCircle((week - 1 + 1 / 2f) * cellLength, (j + 1 / 2f) * cellLength, (1 / 2f - CalendarConstant.BACKGROUND_DELTA0) * cellLength, paintOvulation);
        }

        /**
         * 只有一天的绘制
         *
         * @param canvas
         */
        private void drawCellOnlyOne(Canvas canvas) {
            RectF rectF0;
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA0 * 2) * cellLength - 1, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA0 * 2) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawRect(rectF0, paintPeriod);
            //绘制 左侧圆弧
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA0 - CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawArc(rectF0, CalendarConstant.START_ANGLE_LEFT, CalendarConstant.SWEEP_ANGLE, false, paintPeriod);
            //绘制 右侧圆弧
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA0 + CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawArc(rectF0, CalendarConstant.START_ANGLE_RIGHT, CalendarConstant.SWEEP_ANGLE, false, paintPeriod);
//            Log.d(TAG, "绘制一天的数据");
        }

        private void drawCellCenter(Canvas canvas) {
            RectF rectF0;
            rectF0 = new RectF((week - 1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, week * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawRect(rectF0, paintPeriod);
        }

        /**
         * 绘制左侧圆弧和背景颜色
         *
         * @param canvas canvas
         */
        private void drawCellLeft(Canvas canvas) {
            RectF rectF0;
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA0 * 2) * cellLength - 1, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, week * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawRect(rectF0, paintPeriod);
            //绘制 左侧圆弧
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA0 - CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawArc(rectF0, CalendarConstant.START_ANGLE_LEFT, CalendarConstant.SWEEP_ANGLE, false, paintPeriod);
        }

        /**
         * 绘制右侧圆弧和背景颜色
         *
         * @param canvas canvas
         */
        private void drawCellRight(Canvas canvas) {
            RectF rectF0;
            rectF0 = new RectF((week - 1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA0 * 2) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawRect(rectF0, paintPeriod);
            //绘制 右侧圆弧
            rectF0 = new RectF((week - 1 + CalendarConstant.BACKGROUND_DELTA0 + CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + CalendarConstant.BACKGROUND_DELTA0) * cellLength, (week - CalendarConstant.BACKGROUND_DELTA1) * cellLength, (j + 1 - CalendarConstant.BACKGROUND_DELTA0) * cellLength);
            canvas.drawArc(rectF0, CalendarConstant.START_ANGLE_RIGHT, CalendarConstant.SWEEP_ANGLE, false, paintPeriod);
        }
    }

    public OnCellClickListener getOnCellClickListener() {
        return onCellClickListener;
    }

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }



    private int getPosition(int row, int week) {
        return monthType ? CalendarConstant.CELL_ARRAY_MONTH[row][week] : CalendarConstant.CELL_ARRAY_WEEK[row][week];

    }
}
