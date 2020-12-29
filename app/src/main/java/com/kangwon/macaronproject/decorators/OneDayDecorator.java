package com.kangwon.macaronproject.decorators;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;
<<<<<<< HEAD

=======
>>>>>>> 81446d2de00a20220cc8588b699ea6dade00c2cf
    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(2.0f));    // 현재 날짜의 크기
<<<<<<< HEAD
        view.addSpan(new ForegroundColorSpan(Color.BLACK));   // 현재 날짜의 색
=======
        view.addSpan(new ForegroundColorSpan(Color.MAGENTA));   // 현재 날짜의 색
>>>>>>> 81446d2de00a20220cc8588b699ea6dade00c2cf
    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
