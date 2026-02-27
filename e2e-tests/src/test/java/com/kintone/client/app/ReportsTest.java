package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.model.Order;
import com.kintone.client.model.app.report.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** AppClientのreports.jsonのテスト */
public class ReportsTest extends ApiTestBase {

    private static final String NUMBER_FIELD_CODE = "数値";

    private KintoneClient client;
    private App app;
    private Map<String, Report> originalReports;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
        // 元のレポート設定を保存
        originalReports = app.getReports(false);
    }

    @AfterEach
    public void cleanupReports() {
        if (app != null) {
            try {
                // 元のレポート設定に戻す
                client.app().updateReports(app.id(), originalReports);
                client.app().deployApp(app.id());
                app.waitDeploy();
            } catch (Exception e) {
                // ignore cleanup errors
            }
        }
    }

    @Test
    public void getReports_getReportsPreview() {
        Report report = barGraph(0, "棒グラフ", NUMBER_FIELD_CODE + " >= 1");
        app.updateReports(report).deploy();
        long revision = app.getAppRevision(false);

        GetReportsRequest req1 = new GetReportsRequest();
        req1.setApp(app.id());
        GetReportsResponseBody resp1 = client.app().getReports(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        Map<String, Report> reports = resp1.getReports();
        assertThat(reports).containsOnlyKeys("棒グラフ");
        assertThat(reports.get("棒グラフ"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(report);

        Report report2 =
                new Report()
                        .setName("グラフ2")
                        .setChartType(ChartType.BAR)
                        .setChartMode(ChartMode.NORMAL)
                        .setIndex(0L);
        app.updateReports(Collections.singletonMap("棒グラフ", report2));

        GetReportsPreviewRequest req2 = new GetReportsPreviewRequest();
        req2.setApp(app.id());
        GetReportsPreviewResponseBody resp2 = client.app().getReportsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        reports = resp2.getReports();
        report.setName("グラフ2");
        assertThat(reports).containsOnlyKeys("グラフ2");
        assertThat(reports.get("グラフ2"))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(report);
    }

    @Test
    public void updateReports() {
        long revision = app.getAppRevision(true);

        // グラフ0件
        UpdateReportsRequest req1 = new UpdateReportsRequest();
        req1.setApp(app.id()).setReports(Collections.emptyMap()).setRevision(revision);
        UpdateReportsResponseBody resp1 = client.app().updateReports(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision + 1);
        assertThat(resp1.getReports()).isEmpty();

        revision += 1;
        String query = NUMBER_FIELD_CODE + " >= 1";
        Map<String, Report> reports = new HashMap<>();
        reports.put("棒グラフ", barGraph(0, "棒グラフ", query));
        reports.put("毎年", barGraph(1, "毎年", query).setPeriodicReport(everyYear(12, 31, 23, 59)));
        PeriodicReport quarterly = everyQuarter(QuarterlyPattern.FEB_MAY_AUG_NOV, 1, 23);
        reports.put("毎四半期", barGraph(2, "毎四半期", query).setPeriodicReport(quarterly));
        reports.put("毎月", barGraph(3, "毎月", query).setPeriodicReport(everyMonth(15, 12, 00)));
        PeriodicReport weekly = everyWeek(DayOfWeek.SUNDAY, 9, 30);
        reports.put("毎週", barGraph(4, "毎週", query).setPeriodicReport(weekly));
        reports.put("毎日", barGraph(5, "毎日", query).setPeriodicReport(everyDay(15, 45)));
        reports.put("毎時", barGraph(6, "毎時", query).setPeriodicReport(everyHour(50)));

        UpdateReportsRequest req2 = new UpdateReportsRequest();
        req2.setApp(app.id()).setReports(reports).setRevision(revision);
        UpdateReportsResponseBody resp2 = client.app().updateReports(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getReports()).hasSize(7);
        for (String name : reports.keySet()) {
            assertThat(resp2.getReports().get(name).getId()).isGreaterThan(0);
        }

        updateReportIds(reports, resp2.getReports());
        Map<String, Report> settings = app.getReports(true);
        for (String name : reports.keySet()) {
            assertThat(settings.get(name)).usingRecursiveComparison().isEqualTo(reports.get(name));
        }
    }

    private void updateReportIds(Map<String, Report> reports, Map<String, ReportId> ids) {
        for (Report report : reports.values()) {
            report.setId(ids.get(report.getName()).getId());
        }
    }

    private Report barGraph(long index, String name, String query) {
        Report report = new Report();
        report.setName(name);
        report.setIndex(index);
        report.setChartType(ChartType.BAR);
        report.setChartMode(ChartMode.NORMAL);
        report.setFilterCond(query);
        report.setGroups(Arrays.asList(group("作成者")));
        report.setAggregations(Arrays.asList(aggCount()));
        report.setSorts(Arrays.asList(sort(0, Order.ASC), sort(1, Order.DESC)));
        return report;
    }

    private AggregationGroup group(String code) {
        AggregationGroup group = new AggregationGroup();
        group.setCode(code);
        return group;
    }

    private AggregationSetting aggCount() {
        AggregationSetting agg = new AggregationSetting();
        agg.setType(AggregationFunction.COUNT);
        return agg;
    }

    private AggregationSetting aggAvg(String code) {
        AggregationSetting agg = new AggregationSetting();
        agg.setType(AggregationFunction.AVERAGE);
        agg.setCode(code);
        return agg;
    }

    private AggregationSort sort(int i, Order order) {
        AggregationSort sort = new AggregationSort();
        if (i == 1) {
            sort.setBy(AggregationSortTarget.GROUP1);
        } else if (i == 2) {
            sort.setBy(AggregationSortTarget.GROUP2);
        } else if (i == 3) {
            sort.setBy(AggregationSortTarget.GROUP3);
        } else {
            sort.setBy(AggregationSortTarget.TOTAL);
        }
        sort.setOrder(order);
        return sort;
    }

    private PeriodicReport everyYear(int month, int day, int hour, int minute) {
        EveryYearPeriod p = new EveryYearPeriod();
        p.setMonth(month);
        p.setDayOfMonth(day);
        p.setTime(LocalTime.of(hour, minute));
        return new PeriodicReport().setActive(true).setPeriod(p);
    }

    private PeriodicReport everyQuarter(QuarterlyPattern pattern, int hour, int minute) {
        EveryQuarterPeriod p = new EveryQuarterPeriod();
        p.setPattern(pattern);
        p.setEndOfMonth();
        p.setTime(LocalTime.of(hour, minute));
        return new PeriodicReport().setActive(true).setPeriod(p);
    }

    private PeriodicReport everyMonth(int day, int hour, int minute) {
        EveryMonthPeriod p = new EveryMonthPeriod();
        p.setDayOfMonth(day);
        p.setTime(LocalTime.of(hour, minute));
        return new PeriodicReport().setActive(true).setPeriod(p);
    }

    private PeriodicReport everyWeek(DayOfWeek dayOfWeek, int hour, int minute) {
        EveryWeekPeriod p = new EveryWeekPeriod();
        p.setDayOfWeek(dayOfWeek);
        p.setTime(LocalTime.of(hour, minute));
        return new PeriodicReport().setActive(true).setPeriod(p);
    }

    private PeriodicReport everyDay(int hour, int minute) {
        EveryDayPeriod p = new EveryDayPeriod();
        p.setTime(LocalTime.of(hour, minute));
        return new PeriodicReport().setActive(true).setPeriod(p);
    }

    private PeriodicReport everyHour(int minute) {
        EveryHourPeriod p = new EveryHourPeriod();
        p.setMinute(minute);
        return new PeriodicReport().setActive(true).setPeriod(p);
    }
}
