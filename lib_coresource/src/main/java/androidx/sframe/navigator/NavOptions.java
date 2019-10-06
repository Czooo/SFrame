package androidx.sframe.navigator;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavOptions {

	private boolean mAddToBackStack = true;

	@AnimRes
	@AnimatorRes
	private int mEnterAnim = -1;
	@AnimRes
	@AnimatorRes
	private int mExitAnim = -1;
	@AnimRes
	@AnimatorRes
	private int mPopEnterAnim = -1;
	@AnimRes
	@AnimatorRes
	private int mPopExitAnim = -1;

	@NonNull
	public NavOptions setAddToBackStack(boolean addToBackStack) {
		this.mAddToBackStack = addToBackStack;
		return this;
	}

	@NonNull
	public NavOptions setEnterAnim(@AnimRes @AnimatorRes int enterAnim) {
		this.mEnterAnim = enterAnim;
		return this;
	}

	@NonNull
	public NavOptions setExitAnim(@AnimRes @AnimatorRes int exitAnim) {
		this.mExitAnim = exitAnim;
		return this;
	}

	@NonNull
	public NavOptions setPopEnterAnim(@AnimRes @AnimatorRes int popEnterAnim) {
		this.mPopEnterAnim = popEnterAnim;
		return this;
	}

	@NonNull
	public NavOptions setPopExitAnim(@AnimRes @AnimatorRes int popExitAnim) {
		this.mPopExitAnim = popExitAnim;
		return this;
	}

	public boolean isAddToBackStack() {
		return this.mAddToBackStack;
	}

	@AnimRes
	@AnimatorRes
	public int getEnterAnim() {
		return this.mEnterAnim;
	}

	@AnimRes
	@AnimatorRes
	public int getExitAnim() {
		return this.mExitAnim;
	}

	@AnimRes
	@AnimatorRes
	public int getPopEnterAnim() {
		return this.mPopEnterAnim;
	}

	@AnimRes
	@AnimatorRes
	public int getPopExitAnim() {
		return this.mPopExitAnim;
	}
}
