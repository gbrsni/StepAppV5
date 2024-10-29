package com.example.stepappv4.ui.Day;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepappv4.StepAppOpenHelper;
import com.example.stepappv4.databinding.FragmentDayBinding;
import com.example.stepappv4.R;

public class DayFragment extends Fragment {
    AnyChartView anyChartView;

    LocalDate date;
    String dateString;
    Date cDate = new Date();

    private FragmentDayBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create column chart
        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Cartesian createColumnChart(){
        Map<String, Integer> graph_map = new TreeMap<>();
        int i;
        for (i = 0; i < 7; i ++) {
            date = LocalDate.now().minusDays(i);
            dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            graph_map.put(dateString, 0);
        }

        for (i = 0; i < 7; i ++) {
            date = LocalDate.now().minusDays(i);
            dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            graph_map.put(dateString, StepAppOpenHelper.loadSingleRecord(getContext(), dateString));
        }

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        Column column = cartesian.column(data);

        column.color("#1EB980");

        column.stroke("#1EB980");

        column.tooltip()
                .titleFormat("On day: {%X}")
                .format("{%Value} Steps")
                .anchor(Anchor.RIGHT_BOTTOM);

        column.tooltip()
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        cartesian.background().fill("#00000000");
        cartesian.xAxis(0).title("Day");
        cartesian.yAxis(0).title("Number of steps");
        cartesian.animation(true);


        return cartesian;
    }
}