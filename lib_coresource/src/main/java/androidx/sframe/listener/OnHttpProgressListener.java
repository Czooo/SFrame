package androidx.sframe.listener;

/**
 * Author create by ok on 2019/2/14
 * Email : ok@163.com.
 */
public interface OnHttpProgressListener {

	void onHttpProgress(String url, long contentLength, long readSoFar);
}
