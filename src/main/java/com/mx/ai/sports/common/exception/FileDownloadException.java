package com.mx.ai.sports.common.exception;

/**
 * 文件下载异常
 * @author Mengjiaxin
 * @date 2019-08-20 16:20
 */
public class FileDownloadException extends Exception {
    private static final long serialVersionUID = -4353976687870027960L;

    public FileDownloadException(String message) {
        super(message);
    }
}
