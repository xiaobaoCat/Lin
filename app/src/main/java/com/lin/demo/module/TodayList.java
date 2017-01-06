package com.lin.demo.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class TodayList implements Serializable {

    /**
     * reason : success
     * result : [{"day":"12/12","date":"1642年12月12日","title":"荷兰航海家亚伯·塔斯曼发现新西兰","e_id":"13787"},{"day":"12/12","date":"1765年12月12日","title":"中国清代画家郑燮逝世","e_id":"13788"},{"day":"12/12","date":"1821年12月12日","title":"法国作家福楼拜诞辰","e_id":"13789"},{"day":"12/12","date":"1832年12月12日","title":"挪威数学家西罗出生","e_id":"13790"},{"day":"12/12","date":"1915年12月12日","title":"美国著名歌唱家弗兰克诞辰","e_id":"13791"},{"day":"12/12","date":"1915年12月12日","title":"首架金属飞机首次试飞","e_id":"13792"},{"day":"12/12","date":"1921年12月12日","title":"美国女天文学家亨丽爱塔·勒维特逝世","e_id":"13793"},{"day":"12/12","date":"1925年12月12日","title":"伊朗巴列维王朝建立","e_id":"13794"},{"day":"12/12","date":"1927年12月12日","title":"张太雷在广州起义中牺牲","e_id":"13795"},{"day":"12/12","date":"1931年12月12日","title":"印度神秘学家奥修出生","e_id":"13796"},{"day":"12/12","date":"1936年12月12日","title":"西安事变","e_id":"13797"},{"day":"12/12","date":"1952年12月12日","title":"毕加索把新作和平鸽献给世界和平大会","e_id":"13798"},{"day":"12/12","date":"1954年12月12日","title":"中央军委召开扩大会议划分军区","e_id":"13799"},{"day":"12/12","date":"1963年12月12日","title":"肯尼亚独立","e_id":"13800"},{"day":"12/12","date":"1966年12月12日","title":"航海家吉切斯特创只身远航纪录","e_id":"13801"},{"day":"12/12","date":"1966年12月12日","title":"教育家吴玉章逝世","e_id":"13802"},{"day":"12/12","date":"1971年12月12日","title":"我国自行研制的第一艘导弹驱逐舰启用","e_id":"13803"},{"day":"12/12","date":"1973年12月12日","title":"八大军区司令员对调","e_id":"13804"},{"day":"12/12","date":"1973年12月12日","title":"黄帅事件掀起\u201c破师道尊严\u201d浪潮","e_id":"13805"},{"day":"12/12","date":"1979年12月12日","title":"全斗焕发动军事政变","e_id":"13806"},{"day":"12/12","date":"1979年12月12日","title":"鲁迅研究学会在京成立","e_id":"13807"},{"day":"12/12","date":"1982年12月12日","title":"美国发生历史上最大的现金盗窃案","e_id":"13808"},{"day":"12/12","date":"1984年12月12日","title":"美天文学家观测到太阳系外第一颗行星","e_id":"13809"},{"day":"12/12","date":"1985年12月12日","title":"美租用专机坠毁二百余军人遇难","e_id":"13810"},{"day":"12/12","date":"1992年12月12日","title":"中国电影《留守女士》获\u201c金金字塔\u201d奖","e_id":"13811"},{"day":"12/12","date":"1992年12月12日","title":"印尼地震千人死亡","e_id":"13812"},{"day":"12/12","date":"1994年12月12日","title":"上海地铁一号线开通","e_id":"13813"},{"day":"12/12","date":"1996年12月12日","title":"印孟签署分享恒河水条约","e_id":"13814"},{"day":"12/12","date":"1997年12月12日","title":"欧盟开始东扩进程","e_id":"13815"},{"day":"12/12","date":"1997年12月12日","title":"审判全球通缉的头号国际恐怖分子\u201c卡洛斯\u201d","e_id":"13816"},{"day":"12/12","date":"1998年12月12日","title":"欧盟首脑会议闭幕　通过《维也纳欧洲战略》","e_id":"13817"},{"day":"12/12","date":"2002年12月12日","title":"我完成世界首张水稻基因组\u201c精细图\u201d谱","e_id":"13818"},{"day":"12/12","date":"2010年12月12日","title":"广州亚残运会开幕","e_id":"13819"},{"day":"12/12","date":"2010年12月12日","title":"中国首个极深地下\u201c暗物质\u201d实验室投入使用","e_id":"13820"}]
     * error_code : 0
     */

    private String reason;
    private int error_code=-5;
    private List<ResultBean> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        /**
         * day : 12/12
         * date : 1642年12月12日
         * title : 荷兰航海家亚伯·塔斯曼发现新西兰
         * e_id : 13787
         */

        private String day;
        private String date;
        private String title;
        private String e_id;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }
    }
}
