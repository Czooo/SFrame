package androidx.demon.widget.cache;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public interface IObjectCache<Result, Target> {

	void prepare(Target target, int position);

	Result create(Target target, int position);

	Result obtain(Target target, int position);

	void recycle(Result result);

	void clear();
}
