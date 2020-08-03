package com.mx.ai.sports.common.oss;

import java.io.Serializable;

/**
 * OSS文件配置
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
public class OssConfig implements Serializable {

    private static final long serialVersionUID = -1370526123866014849L;

    /**
     * 临时文件存放地方
     */
    private String tmpdir;
    /**
     * 文件在OSS存储的根目录名称,默认是upload
     */
    private String rootFolder = "upload";
    /**
     * 文件在OSS存储的路劲分隔符,/
     */
    public String delimiter = "/";

    public String getTmpdir() {
        return tmpdir;
    }

    public void setTmpdir(String tmpdir) {
        this.tmpdir = tmpdir;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
