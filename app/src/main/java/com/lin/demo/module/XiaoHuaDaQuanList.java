package com.lin.demo.module;

/**
 * Created by Administrator on 2016/12/28 0028.
 */

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */
public  class XiaoHuaDaQuanList implements Serializable {

    private int error_code=-5;
    private String reason;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            private String content;
            private String hashId;
            private int unixtime;
            private String updatetime;
            private String url="";

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "content='" + content + '\'' +
                        ", hashId='" + hashId + '\'' +
                        ", unixtime=" + unixtime +
                        ", updatetime='" + updatetime + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "data=" + data +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "XiaoHuaDaQuanList{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}